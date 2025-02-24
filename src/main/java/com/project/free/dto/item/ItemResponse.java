package com.project.free.dto.item;

import com.project.free.entity.ItemCategory;
import com.project.free.entity.PhotoEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {

    private Long itemId;

    private Long sellerId;

    private String itemName;

    private BigDecimal itemPrice;

    private String itemDescription;

    private ItemCategory itemCategory;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
