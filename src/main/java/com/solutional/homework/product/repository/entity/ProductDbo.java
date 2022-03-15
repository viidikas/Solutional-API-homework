package com.solutional.homework.product.repository.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "product")
@Data
public class ProductDbo {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;
}
