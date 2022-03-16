package com.solutional.homework.order.model;

import com.solutional.homework.product.service.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderProduct {

    private String id;
    private String name;
    private String price;
    private Long productId;
    private Long quantity;
    private Product replacedWith;


    public OrderProduct() {

    }
}
