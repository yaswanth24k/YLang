package com.ylang.token;

    public enum TokenType {

        // Single-character operators and punctuation
        MINUS, NOT, MODULO, CARET, AMPERSAND, STAR,
        LEFT_PAREN, RIGHT_PAREN,
        LEFT_BRACE, RIGHT_BRACE,
        PIPE,
        LESS, GREATER,
        COMMA, DOT, QUESTION,PLUS,
        SLASH,
        EQUAL,
        SEMICOLON,

        // Multi-character operators
        EQUAL_EQUAL,
        OR,
        AND,
        NOT_EQUAL,
        GREATER_EQUAL,
        LESS_EQUAL,

        // Literals
        IDENTIFIER,
        NUMBER,
        STRING,
        TRUE,
        FALSE,
        NULL,

        // Keywords
        LET,
        IF,
        ELSE,
        WHILE,
        FOR,
        DO,
        PRINT,
        FUN,
        RETURN,

        EOF
    }
