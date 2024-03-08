package com.social.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.social.api.dto.request.User.LoginRequest;
import com.social.api.dto.request.User.RegisterRequest;
import com.social.api.dto.response.token.TokenResponse;
import com.social.api.entity.Role;
import com.social.api.service.iService.IJwtService;
import com.social.api.service.iService.IUserService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IJwtService service;

    @PostMapping("/register")
    public ResponseEntity<?> postMethodName(@RequestBody RegisterRequest request) throws Exception {
        userService.register(request, Role.User);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        System.out.println(request);
        var reponse = userService.login(request);
        return ResponseEntity.ok(reponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        userService.refreshToken(request, response);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
