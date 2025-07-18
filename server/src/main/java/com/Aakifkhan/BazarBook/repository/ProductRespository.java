package com.Aakifkhan.BazarBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;

@Repository
public interface ProductRespository extends JpaRepository<ProductModel, Long> {
    
    List<ProductModel> findByProductNameIgnoreCaseContainingAndIsDeletedFalse(String productName);
    
    Optional<ProductModel> findByProductNameIgnoreCaseAndIsDeletedFalse(String productName);
    
    boolean existsByProductNameIgnoreCaseAndIsDeletedFalse(String productName);
}
