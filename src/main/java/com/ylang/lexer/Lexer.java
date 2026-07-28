package com.ylang.lexer;

import com.ylang.token.Token;
import com.ylang.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("let", TokenType.LET);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("do", TokenType.DO);
        keywords.put("print", TokenType.PRINT);

        // Functions
        keywords.put("fun", TokenType.FUN);
        keywords.put("return", TokenType.RETURN);


        // Literals
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("null", TokenType.NULL);
    }


    public Lexer(String source) {
        this.source = source;
    }


    public List<Token> scanTokens() {

        while (!isAtEnd()) {

            start = current;

            scanToken();
        }


        tokens.add(
                new Token(
                        TokenType.EOF,
                        "",
                        line
                )
        );


        return tokens;
    }



    private void scanToken() {

        char c = advance();


        switch (c) {


            // Single character tokens

            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);

            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);


            case '+' -> addToken(TokenType.PLUS);
            case '-' -> addToken(TokenType.MINUS);

            case '*' -> addToken(TokenType.STAR);
            case '/' -> addToken(TokenType.SLASH);

            case '%' -> addToken(TokenType.MODULO);

            case '^' -> addToken(TokenType.CARET);


            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case '?' -> addToken(TokenType.QUESTION);
            case ';' -> addToken(TokenType.SEMICOLON);



            case '&' -> {

                if (match('&'))
                    addToken(TokenType.AND);

                else
                    addToken(TokenType.AMPERSAND);
            }



            case '|' -> {

                if (match('|'))
                    addToken(TokenType.OR);

                else
                    addToken(TokenType.PIPE);
            }



            case '=' -> {

                if (match('='))
                    addToken(TokenType.EQUAL_EQUAL);

                else
                    addToken(TokenType.EQUAL);
            }



            case '!' -> {

                if (match('='))
                    addToken(TokenType.NOT_EQUAL);

                else
                    addToken(TokenType.NOT);
            }



            case '<' -> {

                if (match('='))
                    addToken(TokenType.LESS_EQUAL);

                else
                    addToken(TokenType.LESS);
            }



            case '>' -> {

                if (match('='))
                    addToken(TokenType.GREATER_EQUAL);

                else
                    addToken(TokenType.GREATER);
            }




            // whitespace

            case ' ', '\r', '\t' -> {

            }


            case '\n' -> line++;



            // String

            case '"' -> string();



            default -> {


                if (isDigit(c)) {

                    number();

                }

                else if (isAlpha(c)) {

                    identifier();

                }

                else {

                    System.err.println(
                            "Unexpected character '"
                                    + c
                                    + "' at line "
                                    + line
                    );
                }
            }
        }
    }



    private void identifier() {


        while (isAlphaNumeric(peek())) {

            advance();

        }


        String text =
                source.substring(
                        start,
                        current
                );


        TokenType type =
                keywords.get(text);



        if (type == null) {

            type = TokenType.IDENTIFIER;
        }



        tokens.add(
                new Token(
                        type,
                        text,
                        line
                )
        );
    }



    private void number() {


        while (isDigit(peek())) {

            advance();

        }


        addToken(TokenType.NUMBER);
    }



    private void string() {


        while (
                peek() != '"'
                        &&
                        !isAtEnd()
        ) {


            if (peek() == '\n') {

                line++;
            }


            advance();
        }



        if (isAtEnd()) {


            System.err.println(
                    "Unterminated string at line "
                            + line
            );


            return;
        }



        advance();


        addToken(TokenType.STRING);
    }




    private boolean isAtEnd() {

        return current >= source.length();
    }



    private char advance() {

        return source.charAt(current++);
    }



    private boolean match(char expected) {


        if (isAtEnd())
            return false;


        if (source.charAt(current) != expected)
            return false;



        current++;

        return true;
    }



    private char peek() {


        if (isAtEnd())
            return '\0';


        return source.charAt(current);
    }



    private void addToken(TokenType type) {


        String lexeme =
                source.substring(
                        start,
                        current
                );


        tokens.add(
                new Token(
                        type,
                        lexeme,
                        line
                )
        );
    }



    private boolean isDigit(char c) {

        return c >= '0'
                &&
                c <= '9';
    }



    private boolean isAlpha(char c) {

        return (c >= 'a' && c <= 'z')
                ||
                (c >= 'A' && c <= 'Z')
                ||
                c == '_';
    }



    private boolean isAlphaNumeric(char c) {

        return isAlpha(c)
                ||
                isDigit(c);
    }
}