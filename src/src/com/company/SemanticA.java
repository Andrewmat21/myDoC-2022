package com.company;


import java.util.ArrayList;

public class SemanticA {
    CST ast = new CST();
    int currentScope = 0;
    int error = 0;
    int warning = 0;

    ArrayList<Scope> symbolTable = new ArrayList();

    public SemanticA(){

    }

    public void analyze(ArrayList<CSTNode> tree){
        //tree.get();

    }

    public void logSymbolTable(){
        System.out.println("Program " + num + " Symbol Table");
        System.out.println("----------------------------------------------");
        System.out.println("Name Type   Scope   Line");
        System.out.println("----------------------------------------------");
        for (i = 0; i < symbolTable.size(); i++){
            System.out.println();
        }
    }
}

class Scope{
    String value;
    String type;
    int currentScope;
    int totalScope;
    int index;

    public Scope(){

    }
}

