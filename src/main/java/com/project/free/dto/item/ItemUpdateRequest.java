package com.project.free.dto.item;

import com.project.free.entity.ItemCategory;
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

    private BigDecimal itemPrice;

    private String itemDescription;

    private ItemCategory itemCategory;
}
