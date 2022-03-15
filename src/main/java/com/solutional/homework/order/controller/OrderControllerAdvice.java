package com.solutional.homework.order.controller;

import com.solutional.homework.error.ClientError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = OrderController.class)
public class OrderControllerAdvice {

    @ExceptionHandler(ClientError.class)
    public ResponseEntity<String> onClientError(ClientError clientError) {
        return ResponseEntity.status(clientError.getStatus()).body(clientError.getMessage());
    }
}
