package com.solutional.homework.order.controller;

import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody String orderStatus) throws IOException {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderStatus));
    }

    @GetMapping("{orderId}/products")
    public ResponseEntity<List<OrderProduct>> getOrderProducts(@PathVariable String orderId) {

         return ResponseEntity.ok(orderService.getOrderProducts(orderId));
    }

    @PostMapping("{orderId}/products")
    public ResponseEntity<String> addProductsToOrder(@PathVariable String orderId, @RequestBody List<Long> productIds) {
        return ResponseEntity.ok(orderService.addOrderProducts(orderId, productIds));
    }


}
