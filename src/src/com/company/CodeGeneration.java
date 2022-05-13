package com.company;

import java.util.ArrayList;
import java.util.Locale;

import static com.company.SemanticA.isDigit;
import static com.company.SemanticA.isId;

public class CodeGeneration {

    int position = 0;
    int tempX = 0;
    int currentScope = -1;
    String Temp;
    int offSet = 0;
    int heap = 243;

    ArrayList<Static> staticData = new ArrayList<Static>();
    ArrayList<Jump> jumpTable = new ArrayList<Jump>();
    ArrayList<Heap> heapMem = new ArrayList<Heap>();

    String code[] = new String[256];

    public CodeGeneration() {
        for (int i = 0; i < 256; i++) {
            code[i] = "00";
        }
    }

    public void generate(CSTNode n, int progNum) {

        switch (n.name) {
            case "Program":
                //store false
                System.out.println("DEBUG CodeGen - Writing [false] into heap memory");
                code[245] = toHex('f');
                code[246] = toHex('a');
                code[247] = toHex('l');
                code[248] = toHex('s');
                code[249] = toHex('e');

                //store true
                System.out.println("DEBUG CodeGen - Writing [true] into heap memory");
                code[251] = toHex('t');
                code[252] = toHex('r');
                code[253] = toHex('u');
                code[254] = toHex('e');

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
                Static s = new Static(Temp, "XX", n.children.get(1).name, currentScope, offSet, position);
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

                if (isDigit(n.children.get(1).name)) {
                    // intialize to var val
                    System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(1).name + " 0] into memory");
                    code[position] = "0" + n.children.get(1);
                    position++;
                }
                else if (n.children.get(1).type == "string") {
                    // check if string already exists in heap mem
                    String str = n.children.get(1).name;
                    int heapTemp;
                    
                    heapTemp = inHeap(heapMem, str);
                    if (heapTemp != -1){
                        code[position] = Integer.toHexString(heapTemp).toUpperCase();
                        position++;
                    }

                    else {
                        for (int i = 0; i < n.children.get(1).name.length(); i++) {
                            code[heap] = toHex(str.charAt(i));
                            heap--;
                        }
                    }
                } else if () {

                }
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
                    switch (n.children.get(0).name) {
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
                System.out.println(code[i] + " ");
                j = 0;
            }
        }
    }

    public void backpatch() {

    }

    public static String toHex(char s) {

        int ascii = (int) s;

        return Integer.toHexString(ascii).toUpperCase();
    }

    public static int inHeap(ArrayList<Heap> heap, String s) {
        for (int i = 0; i < heap.size(); i++){
            if (heap.get(i).t.equals(s))
                return heap.get(i).i;
        }
        return -1;
    }
}

class Static{
    String t;
    String t2;
    String name;
    int sc;
    int oS;
    int position;

    public Static(String temp, String temp2, String varName, int scope, int offSet, int pos){
        t = temp;
        t2 = temp2;
        name = varName;
        sc = scope;
        oS = offSet;
        position = pos;
    }
}

class Jump{
    public Jump(int temp){

    }
}

class Heap{
    String t;
    int i;
    public Heap(String string, int index){
        this.t = string;
        this.i = index;
    }
}



