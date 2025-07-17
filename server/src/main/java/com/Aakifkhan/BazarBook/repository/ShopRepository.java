package com.Aakifkhan.BazarBook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.Shop.ShopModel;

@Repository
public interface ShopRepository extends JpaRepository<ShopModel, Long> {
    List<ShopModel> findByUser_IdAndIsDeletedFalse(Long userId);
    Optional<ShopModel> findByIdAndIsDeletedFalse(Long id);
}
