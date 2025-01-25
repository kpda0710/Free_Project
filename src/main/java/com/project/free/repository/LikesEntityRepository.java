package com.project.free.repository;

import com.project.free.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesEntityRepository extends JpaRepository<LikesEntity, Long> {

    Optional<LikesEntity> findByUserIdAndBoardId(Long userId, Long boardId);
}