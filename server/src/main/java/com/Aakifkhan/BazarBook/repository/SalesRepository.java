package com.Aakifkhan.BazarBook.repository;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.Sales.SalesModel;

@Repository
public interface SalesRepository extends JpaRepository<SalesModel, Long> {

    @Query(value = "SELECT s.* FROM sales s " +
            "INNER JOIN shop sh ON s.shop_id = sh.id " +
            "WHERE sh.user_id = :userId " +
            "AND s.is_deleted = false " +
            "AND sh.is_deleted = false " +
            "ORDER BY s.created_at DESC", 
        nativeQuery = true)
    List<SalesModel> findActiveSalesByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT s.* FROM sales s " +
            "INNER JOIN shop sh ON s.shop_id = sh.id " +
            "WHERE sh.user_id = :userId " +
            "AND s.is_deleted = false " +
            "AND sh.is_deleted = false " +
            "AND (:start IS NULL OR s.created_at >= :start) " +
            "AND (:end IS NULL OR s.created_at <= :end) " +
            "ORDER BY s.created_at DESC", 
        nativeQuery = true)
    List<SalesModel> findActiveSalesByUserIdAndDateRange(@Param("userId") Long userId,
                                                         @Param("start") Timestamp start,
                                                         @Param("end") Timestamp end);

    Optional<SalesModel> findByIdAndIsDeletedFalse(Long id);
}
