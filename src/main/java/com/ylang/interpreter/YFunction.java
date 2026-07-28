package com.ylang.interpreter;

import com.ylang.ast.FunctionStmt;

import java.util.List;


public class YFunction {


    private final FunctionStmt declaration;

    private final Interpreter interpreter;



    public YFunction(
            FunctionStmt declaration,
            Interpreter interpreter
    ) {

        this.declaration = declaration;

        this.interpreter = interpreter;

    }




    public Object call(List<Object> arguments) {


        try {


            interpreter.executeFunction(
                    declaration,
                    arguments
            );


        } catch (ReturnException r) {


            return r.value;

        }



        return null;

    }

}