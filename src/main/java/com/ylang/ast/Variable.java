package com.ylang.ast;

import com.ylang.token.Token;

public class Variable extends Expr {

    private final Token name;

    public Variable(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }
}