package com.solutional.homework.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductError {

    private final ProductErrorDetail errors;

}


