package com.ylang.ast;

import com.ylang.token.Token;

public class Unary extends Expr {

    private final Token operator;
    private final Expr right;

    public Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    public Token getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }
}