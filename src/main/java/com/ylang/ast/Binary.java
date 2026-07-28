package com.ylang.ast;

import com.ylang.token.Token;

public class Binary extends Expr {

    private final Expr left;
    private final Token operator;
    private final Expr right;

    public Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }
}