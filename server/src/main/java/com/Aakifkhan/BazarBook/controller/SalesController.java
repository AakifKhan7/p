package com.Aakifkhan.BazarBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Aakifkhan.BazarBook.dto.sales.SalesCreateRequest;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;
import com.Aakifkhan.BazarBook.services.SalesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales")
@Validated
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/create")
    public ResponseEntity<SalesResponse> createSale(@Valid @RequestBody SalesCreateRequest request) {
        return ResponseEntity.ok(salesService.createSale(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesResponse>> listSales() {
        return ResponseEntity.ok(salesService.listSales());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSale(id);
        return ResponseEntity.ok().build();
    }
}
