package com.solutional.homework.product.service;

import com.solutional.homework.error.ClientError;
import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.product.repository.ProductRepository;
import com.solutional.homework.product.repository.entity.ProductDbo;
import com.solutional.homework.product.service.model.Product;
import com.solutional.homework.product.service.util.ProductFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll().stream().map(ProductFactory::fromProductDbo).collect(Collectors.toList());
    }


    public Product getProduct(Long id) {
        Optional<ProductDbo> productDbo = productRepository.findById(id);
        if (productRepository.findById(id).isPresent()) {
            return ProductFactory.fromProductDbo(productDbo.get());
        } else {
            throw new ClientError("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }
}
