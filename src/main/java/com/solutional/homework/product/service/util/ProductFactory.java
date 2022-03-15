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

    public static OrderProduct fromProduct(Product product) {

        OrderProduct orderProducts = new OrderProduct();

        orderProducts.setProductId(product.getId());
        orderProducts.setName(product.getName());
        orderProducts.setPrice(product.getPrice());
        orderProducts.setId(UUID.randomUUID().toString());
        orderProducts.setQuantity(1L);
        orderProducts.setReplacedWith(product);

        return orderProducts;

    }
}
