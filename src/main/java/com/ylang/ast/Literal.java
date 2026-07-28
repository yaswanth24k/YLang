package com.ylang.ast;

public class Literal extends Expr {

    private final Object value;

    public Literal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}