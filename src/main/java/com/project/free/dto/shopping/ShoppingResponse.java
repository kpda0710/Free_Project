package com.project.free.dto.shopping;

import com.project.free.entity.ShoppingStatus;
import com.project.free.entity.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingResponse {

    private Long shoppingId;

    private Long userId;

    private Long money;

    private Long totalUsedMoney;

    private Long point;

    private ShoppingStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
