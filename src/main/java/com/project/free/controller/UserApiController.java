package com.project.free.controller;

import com.project.free.dto.user.*;
import com.project.free.exception.ResponseCode;
import com.project.free.service.UserService;
import com.project.free.util.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
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

    // 유저 가입 API
    @PostMapping("/signup")
    public CustomResponse<UserResponse> signupUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.signupUser(userRequest);
        return CustomResponse.success(ResponseCode.SUCCESS, userResponse);
    }

    // 유저 로그인 API
    @PostMapping("/login")
    public CustomResponse<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.loginUser(userLoginRequest);
        return CustomResponse.success(ResponseCode.SUCCESS, token);
    }

    // 유저 정보 가져오는 API
    @GetMapping()
    public CustomResponse<UserResponse> getUser(Authentication authentication) {
        UserResponse userResponse = userService.getUser(authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, userResponse);
    }

    // 유저 정보 수정하는 API
    @PutMapping()
    public CustomResponse<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, Authentication authentication) {
        UserResponse userResponse = userService.updateUser(userRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, userResponse);
    }

    // 유저 삭제하는 API
    @DeleteMapping()
    public CustomResponse<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
