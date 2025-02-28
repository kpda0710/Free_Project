package com.project.free.repository;

import com.project.free.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PhotoEntityRepository extends JpaRepository<PhotoEntity, Long> {
    List<PhotoEntity> findByIsDeletedAndDeletedAtBefore(Boolean isDeleted, LocalDateTime deletedAt);
}