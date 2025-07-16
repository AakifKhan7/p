package com.Aakifkhan.BazarBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Aakifkhan.BazarBook.dto.shop.ShopCreateRequest;
import com.Aakifkhan.BazarBook.dto.shop.ShopResponse;
import com.Aakifkhan.BazarBook.dto.shop.ShopUpdateRequest;
import com.Aakifkhan.BazarBook.services.ShopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shop")
@Validated
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/create")
    public ResponseEntity<ShopResponse> createShop(@Valid @RequestBody ShopCreateRequest request) {
        return ResponseEntity.ok(shopService.createShop(request));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ShopResponse> updateShop(@PathVariable Long id, @Valid @RequestBody ShopUpdateRequest request) {
        return ResponseEntity.ok(shopService.updateShop(id, request));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<ShopResponse>> listShops() {
        return ResponseEntity.ok(shopService.listShops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShop(id));
    }
}
