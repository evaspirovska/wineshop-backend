package com.systems.integrated.wineshopbackend.models.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String m){
        super(m);
    }
}
