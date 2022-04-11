package com.company;


import java.util.ArrayList;

public class SemanticA {
    CST ast = new CST();
    int currentScope = 0;
    int error = 0;
    int warning = 0;

    boolean isInitialized;

    ArrayList<Scope> symbolTable = new ArrayList();

    public SemanticA(){

    }

    public void analyze(CSTNode n, int progNum){

        switch (n.name){
            case "Block":
                symbolTable.add()
                break;
            case "":
                break;
        }


        if (n.children.size() > 0){
            for(int j = 0; j < n.children.size(); j++){
                analyze(n.children.get(j), progNum);
            }
        }

        this.logSymbolTable(progNum);
    }

    public void logSymbolTable(int x){
        System.out.println("Program " + x + " Symbol Table");
        System.out.println("----------------------------------------------");
        System.out.println("Name Type   Scope   Line");
        System.out.println("----------------------------------------------");
        for (int i = 0; i < symbolTable.size(); i++){
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

    public Scope(int scope, String type, String name, int index, int line){

    }
}

