package com.project.free.service;

import com.project.free.dto.UserRequest;
import com.project.free.dto.UserResponse;
import com.project.free.entity.UserEntity;
import com.project.free.entity.UserStatus;
import com.project.free.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void createUser() throws Exception {
        UserRequest userRequest = getUserRequest();

        UserEntity userEntity = getUserEntity();

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponse result = userService.createUser(userRequest);

        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(result.getStatus()).isEqualTo(UserStatus.USER);
    }

    @Test
    public void getUser() throws Exception {
        UserEntity userEntity = getUserEntity();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));

        UserResponse result = userService.getUser(1L);

        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    public void updateUser() throws Exception {
        UserEntity userEntity = getUserEntity();
        UserRequest userRequest = getUserRequest();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponse result = userService.updateUser(1L, userRequest);

        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(result.getStatus()).isEqualTo(UserStatus.USER);
    }

    @Test
    public void deleteUser() throws Exception {
        UserEntity userEntity = getUserEntity();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userService.deleteUser(1L);

        assertThat(userEntity.getIsDeleted()).isTrue();
    }

    private static UserEntity getUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .name("Jung")
                .password("1234")
                .email("Jung@gmail.com")
                .status(UserStatus.USER)
                .build();
        return userEntity;
    }

    private static UserRequest getUserRequest() {
        UserRequest userRequest = UserRequest.builder()
                .name("Jung")
                .password("1234")
                .email("Jung@gmail.com")
                .build();
        return userRequest;
    }
}