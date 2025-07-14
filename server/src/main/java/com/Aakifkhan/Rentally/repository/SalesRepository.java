package com.Aakifkhan.Rentally.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.Rentally.model.Sales.SalesMode;

@Repository
public interface SalesRepository extends JpaRepository<SalesMode, Long> {

    List<SalesMode> findByShop_User_IdAndIsDeletedFalse(Long userId);

    Optional<SalesMode> findByIdAndIsDeletedFalse(Long id);
}
