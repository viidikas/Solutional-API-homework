package com.solutional.homework.order.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {

    private OrderAmount amount;
    private String id;
    private List<OrderProduct> products;
    private OrderStatusCode status;
}
