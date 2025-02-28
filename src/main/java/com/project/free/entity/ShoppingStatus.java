package com.project.free.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShoppingStatus {

    COMMON(1, 0),
    SILVER(3, 100000),
    GOLD(5, 500000),
    DIAMOND(8, 1000000),
    VIP(10, 5000000);

    private final int pointRate;
    private final int gradeMax;
}
