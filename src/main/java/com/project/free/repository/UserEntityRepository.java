package com.project.free.repository;

import com.project.free.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByIsDeletedAndDeletedAtBefore(Boolean isDeleted, LocalDateTime deletedAt);
}