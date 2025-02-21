package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Table(name = "seller")
@Entity
@Getter
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class SellerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String sellerName;

    @Column(nullable = false)
    private String sellerEmail;

    @Column(nullable = false)
    private String sellerPhone;

    @Column(nullable = false)
    private String sellerAddress;
}
