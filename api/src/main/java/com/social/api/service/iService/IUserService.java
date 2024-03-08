package com.social.api.service.iService;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.social.api.dto.request.User.LoginRequest;
import com.social.api.dto.request.User.PaginationUserRequest;
import com.social.api.dto.request.User.RegisterRequest;
import com.social.api.dto.response.BasePaginationResponse;
import com.social.api.dto.response.token.TokenResponse;
import com.social.api.dto.response.user.UserInfoResponse;
import com.social.api.entity.Role;
import com.social.api.entity.Status;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IUserService {
    TokenResponse login(LoginRequest request);

    BasePaginationResponse<UserInfoResponse> searchUsers(PaginationUserRequest request);

    void register(RegisterRequest registerRequest, Role role) throws Exception;

    UserInfoResponse findById(int id);

    void delete(int id);

    void ChangeStatus(int id, Status status);

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException;
}
