package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Table(name = "shopping")
@Entity
@Getter
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class ShoppingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long shoppingId;

    @Column(unique = true, nullable = false, updatable = false)
    private Long userId;

    private Long money;

    private Long totalUsedMoney;

    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShoppingStatus status = ShoppingStatus.COMMON;

    public void minusMoney(Long money) {
        this.money -= money;
    }

    public void plusMoney(Long money) {
        this.money += money;
    }

    public void plusTotalUsedMoney(Long money) {
        this.totalUsedMoney += money;
    }

    public void plusPoint(Long point) {
        this.point += point;
    }

    public void updateStatus(ShoppingStatus status) {
        this.status = status;
    }
}
