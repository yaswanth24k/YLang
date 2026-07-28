package com.ylang.parser;

import com.ylang.ast.*;
import com.ylang.token.Token;
import com.ylang.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }


    public List<Stmt> parse() {

        List<Stmt> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(statement());
        }

        return statements;
    }


    private Stmt statement() {

        if (match(TokenType.FUN)) {
            return functionStatement();
        }

        if (match(TokenType.RETURN)) {
            return returnStatement();
        }

        if (match(TokenType.PRINT)) {
            return printStatement();
        }

        if (match(TokenType.LET)) {
            return varStatement();
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        if (match(TokenType.WHILE)) {
            return whileStatement();
        }

        if (match(TokenType.LEFT_BRACE)) {
            return new BlockStmt(block());
        }

        return expressionStatement();
    }


    private Stmt printStatement() {

        Expr value = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after value."
        );

        return new PrintStmt(value);
    }


    private Stmt varStatement() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected variable name."
        );

        consume(
                TokenType.EQUAL,
                "Expected '=' after variable name."
        );

        Expr initializer = expression();


        consume(
                TokenType.SEMICOLON,
                "Expected ';' after variable declaration."
        );


        return new VarStmt(
                name,
                initializer
        );
    }

    private Stmt functionStatement() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected function name."
        );


        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after function name."
        );


        List<Token> parameters = new ArrayList<>();


        if (!check(TokenType.RIGHT_PAREN)) {

            do {

                parameters.add(
                        consume(
                                TokenType.IDENTIFIER,
                                "Expected parameter name."
                        )
                );

            } while(match(TokenType.COMMA));
        }


        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after parameters."
        );


        consume(
                TokenType.LEFT_BRACE,
                "Expected '{' before function body."
        );


        List<Stmt> body = block();


        return new FunctionStmt(
                name,
                parameters,
                body
        );
    }

    private Stmt returnStatement() {

        Expr value = null;


        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }


        consume(
                TokenType.SEMICOLON,
                "Expected ';' after return."
        );


        return new ReturnStmt(value);
    }


    private Stmt ifStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after if."
        );

        Expr condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after condition."
        );


        Stmt thenBranch = statement();

        Stmt elseBranch = null;


        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }


        return new IfStmt(
                condition,
                thenBranch,
                elseBranch
        );
    }


    private Stmt whileStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after while."
        );


        Expr condition = expression();


        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after condition."
        );


        Stmt body = statement();


        return new WhileStmt(
                condition,
                body
        );
    }


    private List<Stmt> block() {

        List<Stmt> statements = new ArrayList<>();


        while (!check(TokenType.RIGHT_BRACE)
                && !isAtEnd()) {

            statements.add(statement());

        }


        consume(
                TokenType.RIGHT_BRACE,
                "Expected '}' after block."
        );


        return statements;
    }


    private Stmt expressionStatement() {

        Expr expr = expression();


        consume(
                TokenType.SEMICOLON,
                "Expected ';' after expression."
        );


        return new ExpressionStmt(expr);
    }



    // ============================
    // EXPRESSIONS
    // ============================


    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {

        Expr expr = or();


        if (match(TokenType.EQUAL)) {

            Token equals = previous();

            Expr value = assignment();


            if (expr instanceof Variable) {

                Token name =
                        ((Variable) expr).getName();


                return new Assign(
                        name,
                        value
                );
            }


            throw error(
                    equals,
                    "Invalid assignment target."
            );
        }


        return expr;
    }


    private Expr or() {

        Expr expr = and();

        while (match(TokenType.OR)) {

            Token operator = previous();

            Expr right = and();

            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }


    private Expr and() {

        Expr expr = equality();


        while (match(TokenType.AND)) {

            Token operator = previous();

            Expr right = equality();

            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }


    private Expr equality() {

        Expr expr = comparison();


        while (match(
                TokenType.EQUAL_EQUAL,
                TokenType.NOT_EQUAL)) {


            Token operator = previous();

            Expr right = comparison();


            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }


        return expr;
    }



    private Expr comparison() {

        Expr expr = term();


        while (match(
                TokenType.GREATER,
                TokenType.GREATER_EQUAL,
                TokenType.LESS,
                TokenType.LESS_EQUAL)) {


            Token operator = previous();

            Expr right = term();


            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }


        return expr;
    }



    private Expr term() {

        Expr expr = factor();


        while (match(
                TokenType.PLUS,
                TokenType.MINUS)) {


            Token operator = previous();

            Expr right = factor();


            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }


        return expr;
    }



    private Expr factor() {

        Expr expr = power();


        while (match(
                TokenType.STAR,
                TokenType.SLASH,
                TokenType.MODULO)) {


            Token operator = previous();

            Expr right = power();


            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }


        return expr;
    }



    private Expr power() {

        Expr expr = unary();


        while (match(TokenType.CARET)) {

            Token operator = previous();

            Expr right = unary();


            expr = new Binary(
                    expr,
                    operator,
                    right
            );
        }


        return expr;
    }



    private Expr unary() {

        if (match(
                TokenType.NOT,
                TokenType.MINUS)) {


            Token operator = previous();

            Expr right = unary();


            return new Unary(
                    operator,
                    right
            );
        }


        return primary();
    }



    private Expr primary() {


        if (match(TokenType.FALSE))
            return new Literal(false);


        if (match(TokenType.TRUE))
            return new Literal(true);


        if (match(TokenType.NULL))
            return new Literal(null);


        if (match(TokenType.NUMBER))
            return new Literal(
                    Double.parseDouble(previous().getLexeme())
            );


        if (match(TokenType.STRING))
            return new Literal(
                    previous().getLexeme().substring(
                            1,
                            previous().getLexeme().length() - 1
                    )
            );


        if (match(TokenType.IDENTIFIER)) {

            Expr expr = new Variable(previous());

            return finishCall(expr);
        }



        if (match(TokenType.LEFT_PAREN)) {

            Expr expr = expression();


            consume(
                    TokenType.RIGHT_PAREN,
                    "Expected ')' after expression."
            );


            return new Grouping(expr);
        }


        throw error(
                peek(),
                "Expected expression."
        );
    }



    // ============================
    // HELPERS
    // ============================
    private Expr finishCall(Expr callee) {


        if (!match(TokenType.LEFT_PAREN)) {
            return callee;
        }


        List<Expr> arguments = new ArrayList<>();


        if (!check(TokenType.RIGHT_PAREN)) {

            do {

                arguments.add(expression());

            } while(match(TokenType.COMMA));
        }


        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after arguments."
        );


        return new Call(
                callee,
                arguments
        );
    }

    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (check(type)) {

                advance();

                return true;
            }
        }

        return false;
    }



    private Token consume(
            TokenType type,
            String message) {


        if (check(type))
            return advance();


        throw error(
                peek(),
                message
        );
    }



    private boolean check(TokenType type) {

        if (isAtEnd())
            return false;


        return peek().getType() == type;
    }



    private Token advance() {

        if (!isAtEnd())
            current++;


        return previous();
    }



    private boolean isAtEnd() {

        return peek().getType()
                == TokenType.EOF;
    }



    private Token peek() {

        return tokens.get(current);
    }



    private Token previous() {

        return tokens.get(current - 1);
    }



    private ParseError error(
            Token token,
            String message) {


        System.err.println(
                "[line " +
                        token.getLine() +
                        "] Error at '" +
                        token.getLexeme() +
                        "': " +
                        message
        );


        return new ParseError();
    }



    private static class ParseError
            extends RuntimeException {
    }
}