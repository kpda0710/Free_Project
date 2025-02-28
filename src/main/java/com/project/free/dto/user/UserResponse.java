package com.project.free.dto.user;

import com.project.free.dto.shopping.ShoppingResponse;
import com.project.free.entity.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long userId;

    private String name;

    private String password;

    private String email;

    private UserStatus status;

    private Long shoppingId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
