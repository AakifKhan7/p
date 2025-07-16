package com.Aakifkhan.BazarBook.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.sales.SalesCreateRequest;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;
import com.Aakifkhan.BazarBook.model.Sales.SalesModel;
import com.Aakifkhan.BazarBook.model.Shop.Shop;
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

    @Transactional
    public SalesResponse createSale(SalesCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();

        // Validate shop exists and belongs to current user
        Shop shop = shopRepository.findByIdAndIsDeletedFalse(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Shop does not belong to user");
        }

        SalesModel sale = new SalesModel();
        sale.setName(request.getName());
        sale.setCategory(request.getCategory());
        sale.setQuantity(request.getQuantity());
        sale.setDescription(request.getDescription());
        sale.setPrice(request.getPrice());
        sale.setImage(request.getImage());
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
