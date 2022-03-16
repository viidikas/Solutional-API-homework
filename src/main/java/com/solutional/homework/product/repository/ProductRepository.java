package com.solutional.homework.product.repository;

import com.solutional.homework.product.repository.entity.ProductDbo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductDbo, Long> {

    List<ProductDbo> findAll();

}