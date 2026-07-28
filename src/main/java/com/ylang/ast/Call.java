package com.ylang.ast;

import com.ylang.token.Token;
import java.util.List;

public class Call extends Expr {

    private final Expr callee;
    private final List<Expr> arguments;


    public Call(
            Expr callee,
            List<Expr> arguments
    ) {
        this.callee = callee;
        this.arguments = arguments;
    }


    public Expr getCallee() {
        return callee;
    }


    public List<Expr> getArguments() {
        return arguments;
    }
}