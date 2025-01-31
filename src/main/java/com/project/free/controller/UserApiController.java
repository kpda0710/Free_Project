package com.project.free.controller;

import com.project.free.dto.user.UserRequest;
import com.project.free.dto.user.UserResponse;
import com.project.free.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable(name = "userId") Long userId) {
        UserResponse userResponse = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "userId") Long userId, @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(userId, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
