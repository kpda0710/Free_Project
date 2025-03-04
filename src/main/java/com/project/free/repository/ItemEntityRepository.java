package com.project.free.repository;

import com.project.free.entity.ItemCategory;
import com.project.free.entity.ItemEntity;
import com.project.free.entity.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findBySellerId(Long sellerId);
    Page<ItemEntity> findByItemNameContaining(String itemName, Pageable pageable);
    Page<ItemEntity> findByItemCategory(ItemCategory category, Pageable pageable);
    List<ItemEntity> findByIsDeletedAndDeletedAtBefore(Boolean isDeleted, LocalDateTime deletedAt);
    Optional<ItemEntity> findByItemIdAndStatus(Long itemId, ItemStatus status);
}