package com.project.free.controller;

import com.project.free.entity.BaseEntity;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.UserEntity;
import com.project.free.entity.UserStatus;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/init")
public class InitApiController {

    private final UserEntityRepository userEntityRepository;
    private final BoardEntityRepository boardEntityRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<Void> initData(Authentication authentication) {
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .name("Jung" + i)
                    .password("1234")
                    .email("jung" + i + "@gmail.com")
                    .status(UserStatus.USER)
                    .isDeleted(false)
                    .build();
            userEntityRepository.save(userEntity);
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 10; i++) {
                BoardEntity boardEntity = BoardEntity.builder()
                        .userId(Long.valueOf(j))
                        .title("title" + i)
                        .content("content" + i)
                        .writer("Jung" + j)
                        .views(0L)
                        .isDeleted(false)
                        .build();
                boardEntityRepository.save(boardEntity);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
