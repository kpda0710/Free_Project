package com.project.free.dto.item;

import com.project.free.entity.ItemCategory;
import com.project.free.entity.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateRequest {

    private String itemName;

    private Long itemPrice;

    private String itemDescription;

    private Long quantity;

    private ItemCategory itemCategory;

    private ItemStatus status;
}
