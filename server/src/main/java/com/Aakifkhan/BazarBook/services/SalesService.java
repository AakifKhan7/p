package com.Aakifkhan.BazarBook.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.sales.SalesCreateRequest;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;
import com.Aakifkhan.BazarBook.model.Sales.SalesModel;
import com.Aakifkhan.BazarBook.model.Shop.ShopModel;
import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.SalesRepository;
import com.Aakifkhan.BazarBook.repository.ShopRepository;

import org.modelmapper.ModelMapper;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SalesResponse createSale(SalesCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();

        // Validate shop exists and belongs to current user
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Shop does not belong to user");
        }

        ProductModel product = entityManager.find(ProductModel.class, request.getProductId());
        if (product == null || product.isDeleted()) {
            throw new RuntimeException("Product not found");
        }

        SalesModel sale = new SalesModel();
        sale.setProduct(product);
        sale.setQuantity(request.getQuantity());
        sale.setPrice(request.getPrice());
        sale.setShop(shop);
        sale.setCreatedBy(currentUser);
        sale.setUpdatedBy(currentUser);

        SalesModel saved = salesRepository.save(sale);
        return modelMapper.map(saved, SalesResponse.class);
    }

    public List<SalesResponse> listSales() {
        UserModel currentUser = currentUserService.getCurrentUser();
        return salesRepository.findActiveSalesByUserId(currentUser.getId())
                .stream()
                .map(s -> {
                    SalesResponse resp = modelMapper.map(s, SalesResponse.class);
                    // Map nested product details manually
                    resp.setName(s.getProduct().getName());
                    resp.setCategory(s.getProduct().getCategory());
                    resp.setDescription(s.getProduct().getDescription());
                    resp.setImage(s.getProduct().getImage());
                    resp.setShopId(s.getShop().getId());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSale(Long id) {
        UserModel currentUser = currentUserService.getCurrentUser();
        SalesModel sale = salesRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        if (!sale.getShop().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        sale.setDeleted(true);
        sale.setUpdatedBy(currentUser);
        salesRepository.save(sale);
    }
}
