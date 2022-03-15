package com.solutional.homework.product.service;

import com.solutional.homework.product.service.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();
    Product getProduct(Long id);
}
