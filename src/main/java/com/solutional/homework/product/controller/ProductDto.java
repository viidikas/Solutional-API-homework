package com.solutional.homework.product.controller;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class ProductDto {

    private Long id;
    private String name;
    private String price;

}
