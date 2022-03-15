package com.solutional.homework.order.service;

import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.model.OrderProduct;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    Order createOrder();
    Order getOrder(String id);
    Order updateOrder(String id, String orderStatusCode) throws IOException;
    List<OrderProduct> getOrderProducts(String id);
    String addOrderProducts(String orderId, List<Long> productIds);

}
