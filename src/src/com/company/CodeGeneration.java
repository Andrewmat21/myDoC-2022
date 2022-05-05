package com.company;

public class CodeGeneration {

    int position = 0;
    String generatedCode[] = new String[255];


    public void generate(CSTNode n, int progNum){
        switch (n.type){
            case "VarDecl":
            case "AssignmentStatement":
            case "PrintStatement":
            case "Block":
            case "Addition":
            case "Equality":
            case "Inequality":
            case "":
        }
    }
}
