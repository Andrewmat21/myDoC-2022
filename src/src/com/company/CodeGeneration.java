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

    String temporaryStaticVar1;
    String temporaryStaticVar2;

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
                //store false at
                System.out.println("DEBUG CodeGen - Writing [false] into heap memory");
                code[245] = toHex('f');
                code[246] = toHex('a');
                code[247] = toHex('l');
                code[248] = toHex('s');
                code[249] = toHex('e');

                //store true at
                System.out.println("DEBUG CodeGen - Writing [true] into heap memory");
                code[251] = toHex('t');
                code[252] = toHex('r');
                code[253] = toHex('u');
                code[254] = toHex('e');

                break;
            case "Block":
                // enter new scope
                currentScope++;
                //System.out.println("DEBUG CodeGen - Generating Block on line " + n.lineNum);
                System.out.println("DEBUG CodeGen - Entering Scope " + currentScope);
                // add opcodes for new var
                // load accumulator
                System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                code[position] = "A9";
                position++;

                //auto intialize to 0
                System.out.println("DEBUG CodeGen - Writing [00] into memory");
                code[position] = "00";
                position++;
                break;

            case "VarDecl":

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

                // codeGen for int assign
                if (n.children.get(1).value.equals("Digit")) {
                    // intialize to var val
                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A9";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(1).name + "] into memory");
                    code[position] = "0" + n.children.get(1).name;
                    position++;
                }
                // codeGen for string assign
                else if (n.children.get(1).value == "string") {

                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A9";
                    position++;

                    // check if string already exists in heap mem
                    String str = n.children.get(1).name;
                    int heapTemp;

                    //check if string is already in heap memory
                    heapTemp = inHeap(heapMem, str);
                    if (heapTemp != -1){
                        code[position] = Integer.toHexString(heapTemp).toUpperCase();
                        position++;
                    }

                    //reuse heap pointer if already exists
                    else {
                        for (int i = n.children.get(1).name.length() - 1; i >= 0; i--) {
                            code[heap] = toHex(str.charAt(i));
                            heap--;
                        }
                    }

                }
                // codeGen for boolean assign
                else if (n.children.get(1).value == "boolean") {
                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A9";
                    position++;

                    if (n.children.get(1).name == "true")
                        code[position] = "FB";
                    else
                        code[position] = "F5";
                    position++;
                }
                else if (n.children.get(1).name.equals("Addition")){
                    genAddition(staticData, code, position, n.children.get(1), currentScope);
                }
                    //id
                    //{int a a = 3 int b b = 4 a = b}$
                else if (isId(n.children.get(1).name)) {

                    System.out.println("DEBUG CodeGen - Writing [AD] into memory");
                    code[position] = "AD";
                    position++;

                    temporaryStaticVar1 = checkStatic(staticData, n.children.get(1).name, currentScope, 1);
                    code[position] = temporaryStaticVar1;
                    position++;

                    temporaryStaticVar2 = checkStatic(staticData, n.children.get(1).name, currentScope, 2);
                    code[position] = temporaryStaticVar2;
                    position++;
                }

                code[position] = "8D";
                position++;

                // store the Temp into 2 parts
                temporaryStaticVar1 = checkStatic(staticData, n.children.get(0).name, currentScope, 1);
                code[position] = temporaryStaticVar1;
                position++;

                temporaryStaticVar2 = checkStatic(staticData, n.children.get(0).name, currentScope, 2);
                code[position] = temporaryStaticVar2;
                position++;

                //addition

                //inequ

                //eq
                break;

            case "PrintStatement":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Printing a(n) " + n.children.get(0).name);

                // print id
                if (isId(n.children.get(0).name)){
                    System.out.println("DEBUG CodeGen - Writing [AC] into memory");
                    code[position] = "AC";
                    position++;

                    temporaryStaticVar1 = checkStatic(staticData, n.children.get(0).name, currentScope, 1);
                    code[position] = temporaryStaticVar1;
                    position++;

                    temporaryStaticVar2 = checkStatic(staticData, n.children.get(0).name, currentScope, 2);
                    code[position] = temporaryStaticVar2;
                    position++;
                }

                // print digit
                else if (isDigit(n.children.get(0).name)){
                    System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                    code[position] = "A0";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(0).name + "] into memory");
                    code[position] = "0" + n.children.get(0).name;
                    position++;
                }

                else if (n.children.get(0).name == "Addition"){
                    genAddition(staticData, code, position, n.children.get(0), currentScope);
                }

                else if (n.children.get(0).value == "boolean"){

                    System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                    code[position] = "A0";
                    position++;

                    if (n.children.get(0).name == "true")
                        code[position] = "FB";
                    else
                        code[position] = "F5";
                    position++;

                }

                System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                code[position] = "A2";
                position++;

                if (n.children.get(0).value == ("string") || n.children.get(0).value == ("boolean")){
                    System.out.println("DEBUG CodeGen - Writing [02] into memory");
                    code[position] = "02";
                    position++;
                }
                else {

                    System.out.println("DEBUG CodeGen - Writing [01] into memory");
                    code[position] = "01";
                    position++;
                }

                System.out.println("DEBUG CodeGen - Writing [FF] into memory");
                code[position] = "FF";
                position++;

                break;
            case "Addition":
                genAddition(staticData, code, position, n, currentScope);
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

            if (j < 7) {
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

    public static String checkStatic(ArrayList<Static> table, String var, int scope, int num){
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).name.equals(var) && table.get(i).sc == scope) {
                if (num == 1) {
                    String dataTemp = table.get(i).t;
                    return dataTemp;
                } else if (num == 2) {
                    String dataTemp = table.get(i).t2;
                    return dataTemp;
                }
            }
        }

        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).name.equals(var) && table.get(i).sc <= scope) {
                if (num == 1) {
                    String dataTemp = table.get(i).t;
                    return dataTemp;
                } else if (num == 2) {
                    String dataTemp = table.get(i).t2;
                    return dataTemp;
                }
            }
        }

        return "null";
    }

    public static void genAddition(ArrayList<Static> s, String code[], int position, CSTNode n, int currentScope){
        String st1;
        String st2;
        String st3 = "00";

        if (isDigit(n.children.get(1).name)) {
            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "A9";
            position++;

            System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(1).name + "] into memory");
            code[position] = "0" + n.children.get(1).name;
            position++;

            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "8D";
            position++;
        }

        else if (isId(n.children.get(1).name)){
            System.out.println("DEBUG CodeGen - Writing [AD] into memory");
            code[position] = "AD";
            position++;

            st1 = checkStatic(s, n.children.get(1).name, currentScope, 1);
            code[position] = st1;
            position++;

            st2 = checkStatic(s, n.children.get(1).name, currentScope, 2);
            code[position] = st2;
            position++;

            System.out.println("DEBUG CodeGen - Writing [8D] into memory");
            code[position] = "8D";
            position++;

            System.out.println("DEBUG CodeGen - Writing [" + st3 + "] into memory");
            code[position] = st3;
            position++;

            System.out.println("DEBUG CodeGen - Writing [00] into memory");
            code[position] = "00";
            position++;
        }

        else if (n.children.get(1).name.equals("Addition")){
            genAddition(s, code, position, n.children.get(1), currentScope);
        }

        if (isDigit(n.children.get(0).name)){
            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "A9";
            position++;

            System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(0).name + "] into memory");
            code[position] = "0" + n.children.get(0).name;
            position++;
        }

        System.out.println("DEBUG CodeGen - Writing [6D] into memory");
        code[position] = "6D";
        position++;

        System.out.println("DEBUG CodeGen - Writing [" + st3 + "] into memory");
        code[position] = st3;
        position++;

        System.out.println("DEBUG CodeGen - Writing [00] into memory");
        code[position] = "00";
        position++;

        System.out.println("DEBUG CodeGen - Writing [00] into memory");
        code[position] = "8D";
        position++;

        System.out.println("DEBUG CodeGen - Writing [00] into memory");
        code[position] = st3;
        position++;

        System.out.println("DEBUG CodeGen - Writing [00] into memory");
        code[position] = "00";
        position++;

    }

    /*public static int existsStatic(ArrayList<Static> table, String id, int scopeNum){
        for (int i = scopeNum; i >= 0; i--){
            for (int j = 0; j < table.get(i).size(); j++){
                if (table.get(i).get(j).value.equals(id)){
                    return i;
                }
            }
        }
        return -1;
    }*/
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



