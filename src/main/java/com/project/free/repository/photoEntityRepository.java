package com.project.free.repository;

import com.project.free.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface photoEntityRepository extends JpaRepository<PhotoEntity, Long> {
}