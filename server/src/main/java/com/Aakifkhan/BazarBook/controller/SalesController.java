package com.Aakifkhan.BazarBook.controller;

import java.util.List;
import java.time.LocalDate;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

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
    public ResponseEntity<List<SalesResponse>> listSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(salesService.listSales(startDate, endDate));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSale(id);
        return ResponseEntity.ok().build();
    }
}
