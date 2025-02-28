package com.project.free.repository;

import com.project.free.entity.SellerEntity;
import com.project.free.entity.ShoppingEntity;
import com.project.free.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingEntityRepository extends JpaRepository<ShoppingEntity, Long> {

    Optional<ShoppingEntity> findByUserId(Long userId);
}