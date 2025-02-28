package com.project.free.dto.item;

import com.project.free.entity.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    @NotBlank
    private String itemName;

    @NotNull
    private Long itemPrice;

    @NotBlank
    private String itemDescription;

    @NotNull
    private Long quantity;

    @NotNull
    private ItemCategory itemCategory;
}
