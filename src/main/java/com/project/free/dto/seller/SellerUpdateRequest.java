package com.project.free.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{2,}$", message = "이름 형식에 맞지 않습니다. 2글자 이상, 영어 숫자가 들어갈 수 있습니다.")
    private String sellerName;

    @NotBlank
    @Email
    private String sellerEmail;

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식이 아닙니다.")
    private String sellerPhone;

    @NotBlank
    private String sellerAddress;
}
