package com.systems.integrated.wineshopbackend.models.exceptions;

public class IllegalAttributeValueException extends RuntimeException{
    public IllegalAttributeValueException(String attributeName, String value){
        super("Attribute \"" + attributeName + "\" has string value (\"" + value + "\"), but accepts only numeric values!");
    }
}
