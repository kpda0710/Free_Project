package com.project.free.repository;

import com.project.free.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByUserId(Long userId);
    Page<BoardEntity> findAllByTitleContaining(String title, Pageable pageable);
}