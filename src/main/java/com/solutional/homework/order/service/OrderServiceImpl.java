package com.solutional.homework.order.service;

import com.solutional.homework.error.ClientError;
import com.solutional.homework.order.model.Order;
import com.solutional.homework.order.model.OrderAmount;
import com.solutional.homework.order.model.OrderProduct;
import com.solutional.homework.order.model.OrderStatusCode;
import com.solutional.homework.product.repository.entity.ProductDbo;
import com.solutional.homework.product.service.ProductService;
import com.solutional.homework.product.service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    public static final String CREATE_ORDER_STARTING_AMOUNT = "0.00";

    private HashMap<String, Order> orderHashMap = new HashMap<>();
    private HashMap<String, HashMap<Long, OrderProduct>> orderProductHashMap = new HashMap<>();

    private final ProductService productService;

    @Override
    public String addOrderProducts(String orderId, List<Long> productIds) {
        confirmOrderExistence(orderId);
        productIds.forEach(productId -> addProductToOrder(productId, orderId));
        return "OK";
    }

    private void confirmOrderExistence(String orderId) {
        Order order = orderHashMap.get(orderId);
        if (order == null) {
            throw new ClientError("Not found", HttpStatus.BAD_REQUEST);
        }
    }

    private void confirmOrderNotPaid(String orderId) {
        Order order = orderHashMap.get(orderId);
        if (order.getStatus().equals(OrderStatusCode.PAID)) {
            throw new ClientError("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    private void addProductToOrder(Long productId, String orderId){
        HashMap<Long, OrderProduct> orderProducts = orderProductHashMap.get(orderId);
        OrderProduct orderProduct = orderProducts.get(productId);
        if(orderProduct != null) {
            orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            orderProducts.put(productId, orderProduct);
        } else {
            Product product = productService.getProduct(productId);
        }

    }

    @Override
    public Order getOrder(String id){
        Order order = orderHashMap.get(id);
        if (order == null){
            throw new ClientError("Not found", HttpStatus.NOT_FOUND);
        }
        return order;
    }

    @Override
    public Order createOrder() {
        Order newOrder = Order.builder()
                .amount(new OrderAmount(
                        CREATE_ORDER_STARTING_AMOUNT,
                        CREATE_ORDER_STARTING_AMOUNT,
                        CREATE_ORDER_STARTING_AMOUNT,
                        CREATE_ORDER_STARTING_AMOUNT))
                .id(UUID.randomUUID().toString())
                .products(Collections.emptyList())
                .status(OrderStatusCode.NEW)
                .build();

        orderHashMap.put(newOrder.getId(), newOrder);
        return newOrder;
    }

    @Override
    public List<OrderProduct> getOrderProducts(String id) {
        Order order = orderHashMap.get(id);
        return order.getProducts();
        
    }

    @Override
    public Order updateOrder(String id, String orderStatusCode) {
        Order order = getOrder(id);
        OrderStatusCode orderStatusCodeConverted;

        try {
            orderStatusCodeConverted = OrderStatusCode.valueOf(orderStatusCode);
        } catch (IllegalArgumentException e){
            throw new ClientError("Invalid order status", HttpStatus.BAD_REQUEST);
        }
        if(!OrderStatusCode.PAID.equals(orderStatusCodeConverted) || !OrderStatusCode.NEW.equals(order.getStatus())){
            throw new ClientError("Invalid order status", HttpStatus.BAD_REQUEST);
        }

        order.setStatus(orderStatusCodeConverted);
        orderHashMap.put(order.getId(), order);
        return order;
    }

//    @Override
//    public List<OrderProduct> getOrderProducts(String id) {
//        Order order = getOrder(id);
//        if (order == null){
//            throw new ClientError("Not found", HttpStatus.NOT_FOUND);
//        }
//        return order.getProducts();
//    }
//
//    public OrderProduct addProductsToOrder(String orderId, Long productId) {
//        Order order = getOrder(orderId);
//
//        if (order == null){
//            throw new ClientError("Not found", HttpStatus.NOT_FOUND);
//        }
//
//       // Product product = productService.getProduct(productId);
//    }




}
