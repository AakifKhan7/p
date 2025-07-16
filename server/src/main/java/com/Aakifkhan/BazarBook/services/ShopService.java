package com.Aakifkhan.BazarBook.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.shop.ShopCreateRequest;
import com.Aakifkhan.BazarBook.dto.shop.ShopResponse;
import com.Aakifkhan.BazarBook.dto.shop.ShopUpdateRequest;
import com.Aakifkhan.BazarBook.model.Shop.Shop;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.ShopRepository;

import org.modelmapper.ModelMapper;
import com.Aakifkhan.BazarBook.services.CurrentUserService;

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

        Shop shop = new Shop();
        shop.setShopName(request.getShopName());
        shop.setShopAddress(request.getShopAddress());
        shop.setShopPhone(request.getShopPhone());
        shop.setUser(currentUser);
        shop.setCreatedBy(currentUser);
        shop.setUpdatedBy(currentUser);

        Shop saved = shopRepository.save(shop);
        return modelMapper.map(saved, ShopResponse.class);
    }

    @Transactional
    public ShopResponse updateShop(Long id, ShopUpdateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();
        Shop shop = shopRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        // Optional: ensure currentUser is owner/Head
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        shop.setShopName(request.getShopName());
        shop.setShopAddress(request.getShopAddress());
        shop.setShopPhone(request.getShopPhone());
        shop.setUpdatedBy(currentUser);

        Shop updated = shopRepository.save(shop);
        return modelMapper.map(updated, ShopResponse.class);
    }

    @Transactional
    public void deleteShop(Long id) {
        UserModel currentUser = currentUserService.getCurrentUser();
        Shop shop = shopRepository.findByIdAndIsDeletedFalse(id)
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
        return shopRepository.findByUser_IdAndIsDeletedFalse(currentUser.getId())
                .stream()
                .map(s -> modelMapper.map(s, ShopResponse.class))
                .collect(Collectors.toList());
    }

    public ShopResponse getShop(Long id) {
        UserModel currentUser = currentUserService.getCurrentUser();
        Shop shop = shopRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        return modelMapper.map(shop, ShopResponse.class);
    }
}
