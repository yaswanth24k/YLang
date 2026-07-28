package com.ylang.main;

import com.ylang.ast.Stmt;
import com.ylang.interpreter.Interpreter;
import com.ylang.lexer.Lexer;
import com.ylang.parser.Parser;
import com.ylang.token.Token;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class YLang {


    public static void runFile(String path) throws Exception {

        String source =
                Files.readString(
                        Path.of(path)
                );


        run(source);
    }


    public static void run(String source) {


        Lexer lexer =
                new Lexer(source);


        List<Token> tokens =
                lexer.scanTokens();


        Parser parser =
                new Parser(tokens);


        List<Stmt> statements =
                parser.parse();


        Interpreter interpreter =
                new Interpreter();


        interpreter.interpret(statements);
    }
}