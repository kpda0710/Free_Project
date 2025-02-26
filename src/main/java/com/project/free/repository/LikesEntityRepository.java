package com.project.free.repository;

import com.project.free.entity.LikesEntity;
import com.project.free.entity.LikesStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesEntityRepository extends JpaRepository<LikesEntity, Long> {

    Optional<LikesEntity> findByUserIdAndTargetIdAndStatus(Long userId, Long targetId, LikesStatus status);
}