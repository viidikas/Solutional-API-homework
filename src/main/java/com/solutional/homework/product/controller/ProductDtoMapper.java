package com.solutional.homework.product.controller;

import com.solutional.homework.product.service.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    ProductDto map(Product product);
}
