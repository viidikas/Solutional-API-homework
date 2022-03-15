package com.solutional.homework.order.util;

import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.product.service.model.Product;

import java.util.UUID;

public class OrderProductFactory {

    public static OrderProduct fromProduct(Product product) {

        OrderProduct orderProducts = new OrderProduct();
        orderProducts.setProductId(product.getId());
        orderProducts.setName(product.getName());
        orderProducts.setPrice(product.getPrice());
        orderProducts.setId(UUID.randomUUID().toString());
        orderProducts.setQuantity(1L);
        orderProducts.setReplacedWith(null);

        return orderProducts;

    }
}
