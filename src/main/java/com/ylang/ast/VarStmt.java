package com.ylang.ast;

import com.ylang.token.Token;

public class VarStmt extends Stmt {

    private final Token name;
    private final Expr initializer;

    public VarStmt(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public Token getName() {
        return name;
    }

    public Expr getInitializer() {
        return initializer;
    }
}