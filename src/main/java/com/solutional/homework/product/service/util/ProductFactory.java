package com.solutional.homework.product.service.util;

import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.product.repository.entity.ProductDbo;
import com.solutional.homework.product.service.model.Product;

import java.util.UUID;


public final class ProductFactory {

    public static Product fromProductDbo(ProductDbo productDbo){

        Product product = new Product();

        product.setId(productDbo.getId());
        product.setName(productDbo.getName());
        product.setPrice(productDbo.getPrice());
        return product;
    }

}
