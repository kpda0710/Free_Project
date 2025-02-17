package com.project.free.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{2,}$", message = "이름 형식에 맞지 않습니다. 2글자 이상, 영어 숫자가 들어갈 수 있습니다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$])[A-Za-z0-9!@#$]{6,}$", message = "비밀번호 양식에 맞지 않습니다. 6글자 이상, 영어 숫자 특수문자(!@#$)가 1개이상 들어가야 합니다.")
    private String password;

    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
