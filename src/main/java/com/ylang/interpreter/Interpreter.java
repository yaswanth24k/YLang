package com.ylang.interpreter;

import com.ylang.ast.*;
import com.ylang.environment.Environment;

import java.util.ArrayList;
import java.util.List;


public class Interpreter {


    private final Environment environment =
            new Environment();



    public void interpret(List<Stmt> statements){

        for(Stmt stmt : statements){

            execute(stmt);

        }

    }



    private void execute(Stmt stmt){


        if (stmt instanceof ReturnStmt) {

            ReturnStmt returnStmt =
                    (ReturnStmt) stmt;


            Object value = null;


            if (returnStmt.getValue() != null) {

                value =
                        evaluate(returnStmt.getValue());

            }


            throw new ReturnException(value);
        }



        if (stmt instanceof FunctionStmt) {


            FunctionStmt function =
                    (FunctionStmt) stmt;


            environment.define(
                    function.getName().getLexeme(),
                    new YFunction(
                            function,
                            this
                    )
            );


            return;
        }



        if(stmt instanceof PrintStmt){


            PrintStmt print =
                    (PrintStmt) stmt;


            System.out.println(
                    evaluate(print.getExpression())
            );


        }



        else if(stmt instanceof ExpressionStmt){


            evaluate(
                    ((ExpressionStmt)stmt)
                            .getExpression()
            );


        }



        else if(stmt instanceof VarStmt){


            VarStmt var =
                    (VarStmt) stmt;


            Object value =
                    evaluate(var.getInitializer());


            environment.define(
                    var.getName().getLexeme(),
                    value
            );

        }



        else if(stmt instanceof BlockStmt){


            for(Stmt s :
                    ((BlockStmt)stmt).getStatements()){


                execute(s);

            }

        }



        else if(stmt instanceof IfStmt){


            IfStmt ifs =
                    (IfStmt) stmt;



            if(isTruthy(
                    evaluate(ifs.getCondition())
            )){


                execute(
                        ifs.getThenBranch()
                );


            }


            else if(ifs.getElseBranch()!=null){


                execute(
                        ifs.getElseBranch()
                );

            }

        }




        else if(stmt instanceof WhileStmt){


            WhileStmt loop =
                    (WhileStmt) stmt;



            while(isTruthy(
                    evaluate(loop.getCondition())
            )){


                execute(
                        loop.getBody()
                );

            }

        }

    }




    public void executeFunction(
            FunctionStmt function,
            List<Object> arguments
    ){


        for(int i = 0; i < function.getParams().size(); i++){


            environment.define(

                    function.getParams()
                            .get(i)
                            .getLexeme(),

                    arguments.get(i)

            );

        }



        for(Stmt stmt : function.getBody()){


            execute(stmt);

        }

    }





    private Object evaluate(Expr expr){



        if(expr instanceof Call){


            Call call =
                    (Call) expr;



            Object callee =
                    evaluate(
                            call.getCallee()
                    );



            YFunction function =
                    (YFunction) callee;



            List<Object> arguments =
                    new ArrayList<>();



            for(Expr argument : call.getArguments()){


                arguments.add(
                        evaluate(argument)
                );

            }



            return function.call(arguments);

        }




        if(expr instanceof Assign){


            Assign assign =
                    (Assign) expr;



            Object value =
                    evaluate(assign.getValue());



            environment.assign(
                    assign.getName().getLexeme(),
                    value
            );



            return value;

        }




        if(expr instanceof Literal){


            return ((Literal)expr)
                    .getValue();

        }




        if(expr instanceof Variable){


            Variable v =
                    (Variable)expr;



            return environment.get(
                    v.getName().getLexeme()
            );

        }




        if(expr instanceof Grouping){


            return evaluate(
                    ((Grouping)expr)
                            .getExpression()
            );

        }




        if(expr instanceof Binary){


            Binary b =
                    (Binary)expr;



            Object left =
                    evaluate(b.getLeft());


            Object right =
                    evaluate(b.getRight());



            switch(b.getOperator().getType()){



                case PLUS:

                    return (double)left +
                            (double)right;



                case MINUS:

                    return (double)left -
                            (double)right;



                case STAR:

                    return (double)left *
                            (double)right;



                case GREATER:

                    return (double)left >
                            (double)right;



                case LESS:

                    return (double)left <
                            (double)right;

            }

        }



        return null;

    }





    private boolean isTruthy(Object value){


        if(value == null)

            return false;



        if(value instanceof Boolean)

            return (boolean)value;



        return true;

    }

}