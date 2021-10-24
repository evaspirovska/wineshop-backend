package com.systems.integrated.wineshopbackend.models.exceptions;

import java.util.function.Supplier;

public class EntityNotFoundException extends RuntimeException implements Supplier<X> {
    public EntityNotFoundException(String m){
        super(m);
    }
}
