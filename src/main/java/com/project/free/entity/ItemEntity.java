package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "item")
@Entity
@Getter
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long itemId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private BigDecimal itemPrice;

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @ToString.Exclude
    @OneToMany
    private List<PhotoEntity> photos = new ArrayList<>();

    public void updatePrice(BigDecimal price) {
        this.itemPrice = price;
    }

    public void updateDescription(String description) {
        this.itemDescription = description;
    }

    public void updateCategory(ItemCategory category) {
        this.itemCategory = category;
    }

    public void uploadImage(PhotoEntity photoEntity) {
        this.photos.add(photoEntity);
    }
}
