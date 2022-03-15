package com.solutional.homework.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ClientError extends RuntimeException{

    @Getter
    private final String message;

    @Getter
    private final HttpStatus status;


    public ClientError(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
