package com.project.free.dto.seller;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponse {

    private Long sellerId;

    private Long userId;

    private String sellerName;

    private String sellerEmail;

    private String sellerPhone;

    private String sellerAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
