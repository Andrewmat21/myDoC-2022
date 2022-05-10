package com.company;

public class CodeGeneration {

    int position = 0;
    String code[] = new String[255];

    public CodeGeneration(){
        for (int i = 0; i < 255; i++) {
            code[i] = "00";
        }
    }

    public void generate(CSTNode n, int progNum) {
        switch (n.type) {
            case "Block":
                break;
            case "VarDecl":
                code[position] = "A9";
                position++;
                code[position] = "00";
                position++;
                code[position] = "8D";
                break;
            case "AssignmentStatement":
                break;
            case "PrintStatement":
                break;
            case "Addition":
                break;
            case "Equality":
                break;
            case "Inequality":
                break;
            case "WhileStatement":
                break;
        }

        if (n.children.size() > 0) {
            for (int j = 0; j < n.children.size(); j++) {
                generate(n.children.get(j), progNum);
            }
        }
    }

    public void logCode(){
        int j = 0;
        System.out.println();
        for (int i = 0; i < code.length; i++){

            if (j < 8){
                System.out.print(code[i] + " ");
                j++;
            }
            else {
                System.out.println();
                j = 0;
            }
        }
    }
}
