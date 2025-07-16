package com.Aakifkhan.BazarBook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.Shop.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByUser_IdAndIsDeletedFalse(Long userId);
    Optional<Shop> findByIdAndIsDeletedFalse(Long id);
}
