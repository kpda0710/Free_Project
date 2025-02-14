package com.project.free.controller;

import com.project.free.dto.user.*;
import com.project.free.exception.ResponseCode;
import com.project.free.service.UserService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public CustomResponse<UserResponse> signupUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.signupUser(userRequest);
        return CustomResponse.success(ResponseCode.USER_CREATE_SUCCESS, userResponse);
    }

    @PostMapping("/login")
    public CustomResponse<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.loginUser(userLoginRequest);
        return CustomResponse.success(ResponseCode.USER_LOGIN_SUCCESS, token);
    }

    @GetMapping()
    public CustomResponse<UserResponse> getUser(Authentication authentication) {
        UserResponse userResponse = userService.getUser(authentication);
        return CustomResponse.success(ResponseCode.USER_GET_SUCCESS, userResponse);
    }

    @PutMapping()
    public CustomResponse<UserResponse> updateUser(@RequestBody UserRequest userRequest, Authentication authentication) {
        UserResponse userResponse = userService.updateUser(userRequest, authentication);
        return CustomResponse.success(ResponseCode.USER_UPDATE_SUCCESS, userResponse);
    }

    @DeleteMapping()
    public CustomResponse<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return CustomResponse.success(ResponseCode.USER_DELETE_SUCCESS, null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public CustomResponse<List<UserResponse>> getAllUsers(Authentication authentication) {
        List<UserResponse> userResponseList = userService.getUserAll(authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, userResponseList);
    }
}
