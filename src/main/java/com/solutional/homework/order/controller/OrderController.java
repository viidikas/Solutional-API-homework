package com.solutional.homework.order.controller;

import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.service.OrderService;
import com.solutional.homework.product.service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder() {
        return ResponseEntity.ok(orderService.createOrder());
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PatchMapping("{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody String orderStatus) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderStatus));
    }

    @GetMapping("{orderId}/products")
    public ResponseEntity<List<Product>> getOrderProducts(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderProducts(orderId));
    }


}
