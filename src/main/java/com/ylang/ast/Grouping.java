package com.ylang.ast;

public class Grouping extends Expr {

    private final Expr expression;

    public Grouping(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }
}