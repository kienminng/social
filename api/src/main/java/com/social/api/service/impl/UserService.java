package com.social.api.service.impl;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.api.dto.request.User.LoginRequest;
import com.social.api.dto.request.User.PaginationUserRequest;
import com.social.api.dto.request.User.RegisterRequest;
import com.social.api.dto.response.BasePaginationResponse;
import com.social.api.dto.response.token.TokenResponse;
import com.social.api.dto.response.user.UserInfoResponse;
import com.social.api.entity.Role;
import com.social.api.entity.Status;
import com.social.api.entity.Token;
import com.social.api.entity.TokenType;
import com.social.api.entity.User;
import com.social.api.exception.ResourceNotFoundException;
import com.social.api.exception.ResourceServeInvalidException;
import com.social.api.repository.ITokenRepository;
import com.social.api.repository.IUserRepository;
import com.social.api.service.iService.IJwtService;
import com.social.api.service.iService.IUserService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private ITokenRepository tokenRepository;

    @Override
    public TokenResponse login(LoginRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());
        User user = userRepository.findByUsernameAndPassword(request.getUsernameOrEmail(), hashPassword).orElseThrow(
                () -> new ResourceNotFoundException("User name and passowrd not found"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.GenerateRefreshToken(user);

        TokenResponse token = new TokenResponse().builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return token;
    }

    @Override
    public BasePaginationResponse<UserInfoResponse> searchUsers(PaginationUserRequest input) {
        Pageable pageable = PageRequest.of(input.PageNo, input.PageSize);
        Slice<User> slice = userRepository.filter(input.getUsername(), input.getPhoneNumber(), input.getAddress(),
                input.getEmail(), pageable);

        long totalItems = slice.getNumberOfElements();
        List<User> content = slice.getContent();
        List<UserInfoResponse> dtoList = content.stream()
                .map(this::convertToUserInfoDTO)
                .collect(Collectors.toList());

        BasePaginationResponse<UserInfoResponse> page = new BasePaginationResponse<UserInfoResponse>(input.PageNo,
                input.PageSize, totalItems, dtoList);

        return page;

    }

    private UserInfoResponse convertToUserInfoDTO(User user) {
        return modelMapper.map(user, UserInfoResponse.class);
    }

    @Override
    public void register(RegisterRequest registerRequest, Role role) throws Exception {
        validate(registerRequest);
        try {

            String hashPassword = passwordEncoder.encode(registerRequest.getPassword());
            User user = new User().builder()
                    .id(0)
                    .email(registerRequest.getEmail())
                    .hashPassword(hashPassword)
                    .address(registerRequest.getAddress())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .username(registerRequest.getUsername())
                    .status(Status.active)
                    .role(role)
                    .build();
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public UserInfoResponse findById(int id) {
        User u = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Id: " + id + "not found"));
        @SuppressWarnings("static-access")
        UserInfoResponse userInfo = new UserInfoResponse().builder()
                .eamil(u.getEmail())
                .username(u.getUsername())
                .Address(u.getAddress())
                .phoneNumber(u.getPhoneNumber())
                .build();
        return userInfo;
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void ChangeStatus(int id, Status status) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id :" + id + " not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        final String authHeader = request.getHeader("Authori");
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.ExtractUsername(refreshToken);
        if (username != null) {
            User user = userRepository.loadByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException(username));
            if (jwtService.IsTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void validate(RegisterRequest user) {
        User u = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (u != null) {
            throw new ResourceServeInvalidException(
                    "Username " + user.getUsername() + " or Email " + user.getEmail() + " is exited");
        }
    }
}
