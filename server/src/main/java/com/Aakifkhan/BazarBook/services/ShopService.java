package com.Aakifkhan.BazarBook.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.shop.ShopCreateRequest;
import com.Aakifkhan.BazarBook.dto.shop.ShopResponse;
import com.Aakifkhan.BazarBook.dto.shop.ShopUpdateRequest;
import com.Aakifkhan.BazarBook.model.Shop.ShopModel;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.ShopRepository;

import org.modelmapper.ModelMapper;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ModelMapper modelMapper;



    @Transactional
    public ShopResponse createShop(ShopCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();
        // Here we can verify if current user has Head role, skipped for brevity.

        ShopModel shop = new ShopModel();
        shop.setShopName(request.getShopName());
        shop.setShopAddress(request.getShopAddress());
        shop.setShopPhone(request.getShopPhone());
        shop.setUser(currentUser);
        shop.setCreatedBy(currentUser);
        shop.setUpdatedBy(currentUser);

        ShopModel saved = shopRepository.save(shop);
        return modelMapper.map(saved, ShopResponse.class);
    }

    @Transactional
    public ShopResponse updateShop(Long id, ShopUpdateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        // Optional: ensure currentUser is owner/Head
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        shop.setShopName(request.getShopName());
        shop.setShopAddress(request.getShopAddress());
        shop.setShopPhone(request.getShopPhone());
        shop.setUpdatedBy(currentUser);

        ShopModel updated = shopRepository.save(shop);
        return modelMapper.map(updated, ShopResponse.class);
    }

    @Transactional
    public void deleteShop(Long id) {
        UserModel currentUser = currentUserService.getCurrentUser();
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        shop.setDeleted(true);
        shop.setUpdatedBy(currentUser);
        shopRepository.save(shop);
    }

    public List<ShopResponse> listShops() {
        UserModel currentUser = currentUserService.getCurrentUser();
        List<ShopModel> shops = shopRepository.findByUser_IdAndIsDeletedFalse(currentUser.getId());
        List<ShopResponse> responses = new ArrayList<>();
        for (ShopModel s : shops) {
            responses.add(modelMapper.map(s, ShopResponse.class));
        }
        return responses;
    }

    public ShopResponse getShop(Long id) {
        UserModel currentUser = currentUserService.getCurrentUser();
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        return modelMapper.map(shop, ShopResponse.class);
    }
}
