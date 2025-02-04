package com.project.free.controller;

import com.project.free.dto.user.*;
import com.project.free.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signupUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.signupUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.loginUser(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        UserResponse userResponse = userService.getUser(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, Authentication authentication) {
        UserResponse userResponse = userService.updateUser(userRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
