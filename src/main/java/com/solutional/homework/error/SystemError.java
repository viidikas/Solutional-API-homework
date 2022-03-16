package com.solutional.homework.error;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class SystemError extends RuntimeException {

    @Getter
    private final String message;

    public SystemError(String message) {
        this.message = message;
    }

}
