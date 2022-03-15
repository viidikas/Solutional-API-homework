package com.solutional.homework.order.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solutional.homework.error.ClientError;
import com.solutional.homework.order.model.*;
import com.solutional.homework.order.util.OrderProductFactory;
import com.solutional.homework.product.service.ProductService;
import com.solutional.homework.product.service.model.Product;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    public static final String CREATE_ORDER_STARTING_AMOUNT = "0.00";

    private HashMap<String, Order> orderHashMap = new HashMap<>();
    private HashMap<String, HashMap<Long, OrderProduct>> orderProductHashMap = new HashMap<>();

    private final ProductService productService;

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

    @Override
    public String addOrderProducts(String orderId, List<Long> productIds) {
        confirmOrderExistence(orderId);
        confirmOrderNotPaid(orderId);
        productIds.forEach(productId -> addProductToOrder(productId, orderId));
        return "OK";
    }

    private void addProductToOrder(Long productId, String orderId){
        HashMap<Long, OrderProduct> orderProducts = orderProductHashMap.get(orderId);

        //Kui ostukorv on tühi.
        if (orderProducts == null) {
            HashMap<Long, OrderProduct> orderProductNew = new HashMap<>();

            Product product = productService.getProduct(productId);
            OrderProduct orderProduct = OrderProductFactory.fromProduct(product);

            orderProductNew.put(productId, orderProduct);
            orderProductHashMap.put(orderId, orderProductNew);

            


        } else {
            OrderProduct orderProduct = orderProducts.get(productId);
            orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            orderProducts.put(productId, orderProduct);
        }
        orderProductHashMap.put(orderId, orderProducts);
    }

    @Override
    public Order getOrder(String id){
        Order order = orderHashMap.get(id);
        if (order == null){
            throw new ClientError("Not found", HttpStatus.NOT_FOUND);
        }
//        HashMap<Long, OrderProduct> orderProducts = orderProductHashMap.get(id);
//        order.setProducts(orderProducts);
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
        HashMap<Long, OrderProduct> order = orderProductHashMap.get(id);
        List<OrderProduct> list = new ArrayList<OrderProduct>(order.values());
        return list;
        
    }

    @Override
    public Order updateOrder(String id, String orderStatusCode) throws JsonProcessingException {
        Order order = getOrder(id);
        OrderStatusCode orderStatusCodeConverted;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode neoJsonNode = mapper.readTree(orderStatusCode);

        try {
            orderStatusCodeConverted = OrderStatusCode.valueOf(neoJsonNode.get("status").asText());
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
