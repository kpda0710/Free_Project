package com.project.free.repository;

import com.project.free.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByTitle(String title);
}