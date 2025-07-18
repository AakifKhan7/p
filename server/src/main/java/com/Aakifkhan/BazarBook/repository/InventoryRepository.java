package com.Aakifkhan.BazarBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.Inventory.InventoryModel;
import com.Aakifkhan.BazarBook.model.Shop.ShopModel;
import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {
    
    List<InventoryModel> findByShopAndIsDeletedFalse(ShopModel shop);
    
    boolean existsByShopAndProductAndIsDeletedFalse(ShopModel shop, ProductModel product);
    
    InventoryModel findByShopAndProductAndIsDeletedFalse(ShopModel shop, ProductModel product);
}
