package com.solutional.homework.order.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solutional.homework.error.ClientError;
import com.solutional.homework.error.ProductError;
import com.solutional.homework.error.SystemError;
import com.solutional.homework.order.model.*;
import com.solutional.homework.order.util.OrderProductFactory;
import com.solutional.homework.product.service.ProductService;
import com.solutional.homework.product.service.model.Product;
import lombok.RequiredArgsConstructor;
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

    private void confirmOrderProductListExistence(String orderId) {
        if(!orderProductHashMap.containsKey(orderId)) {
            HashMap<Long, OrderProduct> orderProducts = new HashMap<>();
            orderProductHashMap.put(orderId, orderProducts);
        }
    }

    @Override
    public String addOrderProducts(String orderId, List<Long> productIds) {
        confirmOrderExistence(orderId);
        confirmOrderNotPaid(orderId);
        confirmOrderProductListExistence(orderId);
        productIds.forEach(productId -> addProductToOrder(productId, orderId));
        return "OK";
    }

    private void addProductToOrder(Long productId, String orderId){
        Product product = productService.getProduct(productId);
        OrderProduct orderProduct = OrderProductFactory.fromProduct(product);

        boolean productExistsInOrder = orderProductHashMap.get(orderId).containsKey(productId);
        HashMap<Long, OrderProduct> orderProducts = orderProductHashMap.get(orderId);

        if(!productExistsInOrder) {
            orderProducts.put(productId, orderProduct);
            orderProductHashMap.put(orderId, orderProducts);

            Order order = orderHashMap.get(orderId);
            List<OrderProduct> list = new ArrayList<>(orderProducts.values());
            order.setProducts(list);
        }
         else {
            long currentQuantity = orderProducts.get(productId).getQuantity();
            orderProducts.get(productId).setQuantity(currentQuantity + 1);
            orderProductHashMap.put(orderId, orderProducts);

            Order order = orderHashMap.get(orderId);
            List<OrderProduct> list = new ArrayList<>(orderProducts.values());
            order.setProducts(list);
        }
    }
    @Override
    public String updateProductQuantity(String orderId, String productId, String quantity) {

        OrderProduct product = getOrderProductById(orderId, productId);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode neoJsonNode;
        try {
            neoJsonNode = mapper.readTree(quantity);
        } catch (JsonProcessingException e) {
            throw new SystemError("System error");
        }
        product.setQuantity(product.getQuantity() + neoJsonNode.get("quantity").asLong());
        return "OK";
    }

    private OrderProduct getOrderProductById(String orderId, String id) {
        HashMap<Long, OrderProduct> orderProductHashMap = this.orderProductHashMap.get(orderId);

        Optional<Map.Entry<Long, OrderProduct>> orderProductById = orderProductHashMap.entrySet()
                .stream().filter(x -> x.getValue().getId().equals(id))
                .findFirst();

        if(orderProductById.isPresent()) {
            return orderProductById.get().getValue();
        } else {
            throw new ClientError("Not Found", HttpStatus.BAD_REQUEST);
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

        HashMap<Long, OrderProduct> order = orderProductHashMap.get(id);
        if (order == null) {
            throw new ClientError("Not Found", HttpStatus.NOT_FOUND);
        }
                return new ArrayList<>(order.values());
    }

    @Override
    public Order updateOrder(String id, String orderStatusCode) {
        Order order = getOrder(id);
        OrderStatusCode orderStatusCodeConverted;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode neoJsonNode = null;
        try {
            neoJsonNode = mapper.readTree(orderStatusCode);
        } catch (JsonProcessingException e) {
            throw new SystemError("Invalid JSON");
        }
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

    @Override
    public String replaceProduct(String orderId, String productId, String replacedWith) {

        confirmOrderExistence(orderId);
        confirmOrderNotPaid(orderId);
        OrderProduct orderProduct;

        try {
            orderProduct = getOrderProductById(orderId, productId);
        } catch(NullPointerException e) {
            String error = ProductError.class.toString();
            throw new ClientError(error, HttpStatus.BAD_REQUEST);
            //Siin peaks hoopis ProductErrorDetaili sisse kirjutama Bad Request.
        }

        try {
        JsonNode neoJsonNode;
        neoJsonNode = new ObjectMapper().readTree(replacedWith);

        long quantity = neoJsonNode.get("replaced_with").get("quantity").asLong();
        long id = neoJsonNode.get("replaced_with").get("product_id").asLong();

        Product product = productService.getProduct(id);
        product.setQuantity(quantity);
        orderProduct.setReplacedWith(product);

        HashMap<Long, OrderProduct> orderProducts = orderProductHashMap.get(orderId);
        OrderProduct orderProductInHashMap = orderProducts.get(productId);
        orderProductInHashMap.setReplacedWith(product);

    } catch (JsonProcessingException e) {
            String error = ProductError.class.toString();
        throw new ClientError(error, HttpStatus.NOT_FOUND);
       //Siin peaks hoopis ProductErrorDetaili sisse kirjutama Not found.
    }


     return "OK";
    }
}
