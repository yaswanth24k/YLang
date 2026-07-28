package com.ylang.main;


public class Main {

    public static void main(String[] args) throws Exception {


        if(args.length == 0){

            System.out.println(
                    "Usage: ylang <file>"
            );

            return;
        }


        YLang.runFile(args[0]);
    }
}