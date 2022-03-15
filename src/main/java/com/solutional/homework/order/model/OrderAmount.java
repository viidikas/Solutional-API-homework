package com.solutional.homework.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderAmount {

    private String discount;
    private String paid;
    private String returns;
    private String total;
}
