package com.project.free.dto.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerRequest {

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식이 아닙니다.")
    private String sellerPhone;

    @NotBlank
    private String sellerAddress;
}
