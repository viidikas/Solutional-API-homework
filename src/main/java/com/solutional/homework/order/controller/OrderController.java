package com.solutional.homework.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solutional.homework.error.ClientError;
import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<OrderProduct>> getOrderProducts(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderProducts(orderId));
    }

    @PostMapping("{orderId}/products")
    public ResponseEntity<String> addProductsToOrder(@PathVariable String orderId, @RequestBody List<Long> productIds) {
        return ResponseEntity.ok(orderService.addOrderProducts(orderId, productIds));
    }

    @PatchMapping("{orderId}/products/{productId}")
    public ResponseEntity<String> changeData(@PathVariable String orderId, @PathVariable String productId, @RequestBody String data) {
        try {
            JsonNode neoJsonNode = new ObjectMapper().readTree(data);

            if (neoJsonNode.has("quantity")) {
                return ResponseEntity.ok(orderService.updateProductQuantity(orderId, productId, data));
            } else {
                return ResponseEntity.ok(orderService.replaceProduct(orderId, productId, data));
            }
            } catch (JsonProcessingException e) {
                throw new ClientError("Not found", HttpStatus.NOT_FOUND);
        }
    }
}
