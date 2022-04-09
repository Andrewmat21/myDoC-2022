package com.company;

import java.util.ArrayList;

public class SemanticA {
    CST ast = new CST();
    int currentScope = 0;
    int error = 0;
    int warning = 0;

    ArrayList<Symbol> symbolTable = new ArrayList();

    public SemanticA(ArrayList<CSTNode> tree){

    }

}

class Symbol{
    String value;
    String type;
    int currentScope;
    int totalScope;
    int index;
}

