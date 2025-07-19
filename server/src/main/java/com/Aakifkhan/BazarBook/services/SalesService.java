package com.Aakifkhan.BazarBook.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.sales.SalesCreateRequest;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;
import com.Aakifkhan.BazarBook.model.Sales.SalesModel;
import com.Aakifkhan.BazarBook.model.Shop.ShopModel;
import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;
import com.Aakifkhan.BazarBook.model.Inventory.InventoryModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.SalesRepository;
import com.Aakifkhan.BazarBook.repository.ShopRepository;
import com.Aakifkhan.BazarBook.repository.InventoryRepository;
import com.Aakifkhan.BazarBook.repository.ProductRespository;

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

    @Autowired
    private ProductRespository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create a new sale record and update inventory
     * @param request Sales creation request
     * @return Created sale details
     */
    @Transactional
    public SalesResponse createSale(SalesCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();

        // Validate shop exists and belongs to current user
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Shop does not belong to user");
        }

        // Get product by name (case-insensitive)
        ProductModel product = productRepository.findByProductNameIgnoreCaseAndIsDeletedFalse(request.getProductName())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check inventory
        InventoryModel inventory = inventoryRepository.findByShopAndProductAndIsDeletedFalse(shop, product);
        if (inventory == null) {
            throw new RuntimeException("Product not available in this shop");
        }

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Create sale record
        SalesModel sale = new SalesModel();
        sale.setProduct(product);
        sale.setQuantity(request.getQuantity());
        double unitPrice = inventory.getPrice();
        sale.setPrice(unitPrice * request.getQuantity());
        sale.setShop(shop);
        sale.setCreatedBy(currentUser);
        sale.setUpdatedBy(currentUser);

        SalesModel saved = salesRepository.save(sale);

        // Update inventory
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventory.setUpdatedBy(currentUser);
        inventoryRepository.save(inventory);

        return modelMapper.map(saved, SalesResponse.class);
    }

    public List<SalesResponse> listSales(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        UserModel currentUser = currentUserService.getCurrentUser();
        java.sql.Timestamp startTs = startDate != null ? java.sql.Timestamp.valueOf(startDate.atStartOfDay()) : null;
        java.sql.Timestamp endTs = endDate != null ? java.sql.Timestamp.valueOf(endDate.plusDays(1).atStartOfDay().minusSeconds(1)) : null;

        List<SalesModel> sales;
        if (startTs == null && endTs == null) {
            sales = salesRepository.findActiveSalesByUserId(currentUser.getId());
        } else {
            sales = salesRepository.findActiveSalesByUserIdAndDateRange(currentUser.getId(), startTs, endTs);
        }
        List<SalesResponse> responses = new ArrayList<>();
        for (SalesModel s : sales) {
            SalesResponse resp = modelMapper.map(s, SalesResponse.class);
            // Map nested product details manually
            resp.setName(s.getProduct().getProductName());
            resp.setCategory(s.getProduct().getCategory());
            resp.setDescription(s.getProduct().getDescription());
            resp.setImage(s.getProduct().getImage());
            resp.setShopId(s.getShop().getId());
            responses.add(resp);
        }
        return responses;
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
