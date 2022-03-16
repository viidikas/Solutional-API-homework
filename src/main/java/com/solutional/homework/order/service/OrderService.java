package com.solutional.homework.order.service;

import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.model.OrderProduct;

import java.util.List;

public interface OrderService {

    Order createOrder();
    Order getOrder(String id);
    Order updateOrder(String id, String orderStatusCode);
    List<OrderProduct> getOrderProducts(String id);
    String addOrderProducts(String orderId, List<Long> productIds);
    String updateProductQuantity(String orderId, String productId, String quantity);
    String replaceProduct(String orderId, String productId, String replacedWith);


}
