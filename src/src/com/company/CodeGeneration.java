package com.company;

import java.util.ArrayList;

import static com.company.SemanticA.isId;

public class CodeGeneration {

    int position = 0;
    int tempX = 0;
    int currentScope = -1;
    String Temp;
    int offSet = 0;

    ArrayList<Static> staticData = new ArrayList<Static>();
    ArrayList<Jump> jumpTable = new ArrayList<Jump>();

    String code[] = new String[255];

    public CodeGeneration() {
        for (int i = 0; i < 255; i++) {
            code[i] = "00";
        }
    }

    public void generate(CSTNode n, int progNum) {

        switch (n.name) {
            case "Program":
                //store false
                System.out.println("DEBUG CodeGen - Writing [true] into heap memory");
                code[245] = toHex("f");
                code[246] = toHex("a");
                code[247] = toHex("l");
                code[248] = toHex("s");
                code[249] = toHex("e");

                //store true
                System.out.println("DEBUG CodeGen - Writing [A9] into heap memory");
                code[251] = toHex("t");
                code[252] = toHex("r");
                code[253] = toHex("u");
                code[254] = toHex("e");

                break;
            case "Block":
                // enter new scope
                currentScope++;
                System.out.println("DEBUG CodeGen - Generating Block on line " + n.lineNum);
                System.out.println("DEBUG CodeGen - Entering Scope " + currentScope);
                break;

            case "VarDecl":
                // add opcodes for new var
                // load accumulator
                System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                code[position] = "A9";
                position++;

                //auto intialize to 0
                System.out.println("DEBUG CodeGen - Writing [00] into memory");
                code[position] = "00";
                position++;

                // store accumulator contents in memory
                System.out.println("DEBUG CodeGen - Writing [8D] into memory");
                code[position] = "8D";
                position++;

                // add static data
                System.out.println("DEBUG CodeGen - Writing [T" + tempX + "] into memory");
                Temp = "T" + tempX;
                code[position] = Temp;
                Static s = new Static(Temp, "XX", n.name, currentScope, offSet);
                staticData.add(s);

                offSet++;
                tempX++;
                position++;

                System.out.println("DEBUG CodeGen - Writing [XX] into memory");
                code[position] = "XX";
                position++;
                break;

            case "AssignmentStatement":
                System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                code[position] = "A9";
                position++;

                // intialize to var val
                System.out.println("DEBUG CodeGen - Writing [00] into memory");
                code[position] = "0" + n.children.get(1);
                position++;
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

            switch (n.name) {
                case "Block":
                    System.out.println("DEBUG CodeGen - Leaving Scope " + currentScope);
                    System.out.println("DEBUG CodeGen - Closing Brace found");
                    currentScope--;
                    break;
                case "VarDecl":
                    //isId()
                    break;
                case "AssignmentStatement":
                    break;
                case "PrintStatement":
                    switch (n.children.get(0).name){
                        case "d":
                        case "a":
                        case "w":
                        case "s":
                        case "b":
                    }
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
        }
    }

    public void logCode() {
        int j = 0;
        System.out.println();
        for (int i = 0; i < code.length; i++) {

            if (j < 8) {
                System.out.print(code[i] + " ");
                j++;
            } else {
                System.out.println();
                j = 0;
            }
        }
    }

    public void backpatch(){

    }

    public static String toHex(String s){
        char c = s.charAt(0);
        int ascii = (int)c;
        return Integer.toHexString(ascii);
    }
}

class Static{
    public Static(String temp, String temp2, String varName, int scope, int offSet){

    }
}

class Jump{
    public Jump(int temp){

    }
}



