package com.project.free.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
    SOLD("판매중"),
    WAITING("상품 준비중"),
    SOLD_OUT("품절");

    private final String description;
}
