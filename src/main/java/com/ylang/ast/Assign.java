package com.ylang.ast;

import com.ylang.token.Token;

public class Assign extends Expr {

    private final Token name;
    private final Expr value;


    public Assign(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }


    public Token getName() {
        return name;
    }


    public Expr getValue() {
        return value;
    }
}