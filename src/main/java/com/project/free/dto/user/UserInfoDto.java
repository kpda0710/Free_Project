package com.project.free.dto.user;

import com.project.free.entity.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long userId;

    private String email;

    private String name;

    private String password;

    private UserStatus status;
}
