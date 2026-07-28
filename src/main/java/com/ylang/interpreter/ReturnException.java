package com.ylang.interpreter;


public class ReturnException extends RuntimeException {

    final Object value;


    public ReturnException(Object value) {

        this.value = value;
    }
}