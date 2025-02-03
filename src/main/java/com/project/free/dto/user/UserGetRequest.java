package com.project.free.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserGetRequest {

    private Long userId;

    private String email;

    private String password;
}
