package com.solutional.homework.product.controller;

import com.solutional.homework.product.service.ProductService;
import com.solutional.homework.product.service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts() {

        return ResponseEntity.ok(productService.getProducts());
    }
}
