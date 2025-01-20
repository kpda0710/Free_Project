package com.project.free.service;

import com.project.free.dto.user.UserRequest;
import com.project.free.dto.user.UserResponse;
import com.project.free.entity.UserEntity;
import com.project.free.entity.UserStatus;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public UserResponse createUser(UserRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .status(UserStatus.USER)
                .build();

        UserEntity saved = userEntityRepository.save(userEntity);

        return UserResponse.builder()
                .userId(saved.getUserId())
                .name(saved.getName())
                .password(saved.getPassword())
                .email(saved.getEmail())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public UserResponse getUser(Long userId) {
        UserEntity userEntity = getUserEntity(userId);

        return UserResponse.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public UserResponse updateUser(Long userId, UserRequest request) {
        UserEntity userEntity = getUserEntity(userId);

        userEntity.setName(request.getName());
        userEntity.setPassword(request.getPassword());
        userEntity.setEmail(request.getEmail());

        UserEntity saved = userEntityRepository.save(userEntity);

        return UserResponse.builder()
                .userId(saved.getUserId())
                .name(saved.getName())
                .password(saved.getPassword())
                .email(saved.getEmail())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public void deleteUser(Long userId) {
        UserEntity userEntity = getUserEntity(userId);

        userEntity.setIsDeleted(true);
        userEntity.setDeletedAt(LocalDateTime.now());
        userEntityRepository.save(userEntity);
    }

    private UserEntity getUserEntity(Long userId) {
        return userEntityRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));
    }
}
