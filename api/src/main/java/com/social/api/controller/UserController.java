package com.social.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.api.dto.request.User.PaginationUserRequest;
import com.social.api.dto.response.BasePaginationResponse;
import com.social.api.dto.response.user.UserInfoResponse;
import com.social.api.service.iService.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/getList")
    public ResponseEntity<BasePaginationResponse<UserInfoResponse>> search(@RequestParam PaginationUserRequest request){
        var response = userService.searchUsers(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getInfo() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    
    @GetMapping
    public ResponseEntity<UserInfoResponse> findById(@RequestParam int id){
        return ResponseEntity.ok(userService.findById(id));
    }
    
}
