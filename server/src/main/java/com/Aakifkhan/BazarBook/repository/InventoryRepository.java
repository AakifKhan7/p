package com.Aakifkhan.BazarBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.Inventory.InventoryModel;;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {
    
}
