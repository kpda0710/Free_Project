package com.project.free.repository;

import com.project.free.entity.ItemCategory;
import com.project.free.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findByItemNameContaining(String itemName, Pageable pageable);
    Page<ItemEntity> findByItemCategory(ItemCategory category, Pageable pageable);
}