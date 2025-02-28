package com.project.free.repository;

import com.project.free.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByUserId(Long userId);
    List<CommentEntity> findByIsDeletedAndDeletedAtBefore(Boolean isDeleted, LocalDateTime deletedAt);
}