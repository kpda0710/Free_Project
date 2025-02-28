package com.project.free.repository;

import com.project.free.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerEntityRepository extends JpaRepository<SellerEntity, Long> {

    Optional<SellerEntity> findByUserId(Long userId);
    List<SellerEntity> findByIsDeletedAndDeletedAtBefore(Boolean isDeleted, LocalDateTime deletedAt);
}