package com.solutional.homework.product.controller;

import com.solutional.homework.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDtoMapper productDtoMapper;

    @GetMapping("products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts().stream().map(productDtoMapper::map).collect(Collectors.toList()));
    }
}
