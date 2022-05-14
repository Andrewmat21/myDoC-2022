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
    int jump;
    int heap = 243;
    int error = 0;


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
                System.out.println("DEBUG CodeGen - Generating Op Codes for VarDecl");
                /*
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
                position++;*/
                break;

            case "AssignmentStatement":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Assignment");
                /*
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

                //eq*/
                break;

            case "PrintStatement":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Print");
                /*
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
                */
                break;
            case "Addition":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Addition");
                /*
                genAddition(staticData, code, position, n, currentScope);*/
                break;
            case "Equality":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Comparison");
                break;
            case "Inequality":
                System.out.println("DEBUG CodeGen - Generating Op Codes for Comparison");
                break;
            case "WhileStatement":
                System.out.println("DEBUG CodeGen - Generating Op Codes for While Loop");
                break;
            case "IfStatement":
                System.out.println("DEBUG CodeGen - Generating Op Codes for If Statement");
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
                        if (heapTemp != -1) {
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
                    // addition
                    else if (n.children.get(1).name.equals("Addition")) {
                        this.genAddition(n.children.get(1), currentScope);
                    }
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
                    else if (n.children.get(1).name.equals("Equality")) {
                        this.genEq(n.children.get(1), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [F5] into memory");
                        code[position] = "F5";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "FB";
                        position++;
                    }

                    else if (n.children.get(1).name.equals("Inequality")) {
                        this.genEq(n.children.get(1), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;


                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [01] into memory");
                        code[position] = "01";
                        position++;


                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;




                        System.out.println("DEBUG CodeGen - Writing [8D] into memory");
                        code[position] = "8D";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;


                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "F5";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "FB";
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

                    break;

                case "PrintStatement":
                    System.out.println("DEBUG CodeGen - Generating Op Codes for Printing a(n) " + n.children.get(0).name);

                    // print id
                    if (isId(n.children.get(0).name)) {
                        System.out.println("DEBUG CodeGen - Writing [AC] into memory");
                        code[position] = "AC";
                        position++;

                        temporaryStaticVar1 = checkStatic(staticData, n.children.get(0).name, currentScope, 1);
                        code[position] = temporaryStaticVar1;
                        position++;

                        temporaryStaticVar2 = checkStatic(staticData, n.children.get(0).name, currentScope, 2);
                        code[position] = temporaryStaticVar2;
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        if (n.children.get(0).value == ("string") || n.children.get(0).value == ("boolean")) {
                            System.out.println("DEBUG CodeGen - Writing [02] into memory");
                            code[position] = "02";
                            position++;
                        }
                        else {

                            System.out.println("DEBUG CodeGen - Writing [01] into memory");
                            code[position] = "01";
                            position++;
                        }

                    }

                    // print digit
                    else if (isDigit(n.children.get(0).name)) {
                        System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                        code[position] = "A0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(0).name + "] into memory");
                        code[position] = "0" + n.children.get(0).name;
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [01] into memory");
                        code[position] = "01";
                        position++;
                    }

                    else if (n.children.get(0).name.equals("Addition")) {
                        this.genAddition(n.children.get(0), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "AC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [01] into memory");
                        code[position] = "01";
                        position++;
                    }

                    else if (n.children.get(0).value.equals("string")) {

                        System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                        code[position] = "A0";
                        position++;

                        // check if string already exists in heap mem
                        String str = n.children.get(0).name;
                        int heapTemp;

                        //check if string is already in heap memory
                        //reuse heap pointer if already exists
                        heapTemp = inHeap(heapMem, str);
                        if (heapTemp != -1) {
                            System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heapTemp).toUpperCase() + "] into memory");
                            code[position] = Integer.toHexString(heapTemp).toUpperCase();
                            position++;
                        }

                        //store new string in heap
                        else {
                            for (int i = n.children.get(0).name.length() - 1; i >= 0; i--) {
                                code[heap] = toHex(str.charAt(i));
                                heap--;
                            }
                            System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heap+1).toUpperCase() + "] into memory");
                            code[position] = Integer.toHexString(heap+1).toUpperCase();
                            position++;
                        }

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;
                    }

                    else if (n.children.get(0).value.equals("boolean")){

                        System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                        code[position] = "A0";
                        position++;

                        if (n.children.get(0).name == "true")
                            code[position] = "FB";
                        else
                            code[position] = "F5";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;
                    }

                    else if (n.children.get(0).value.equals("Equality")) {
                        this.genEq(n.children.get(0), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [0A] into memory");
                        code[position] = "0A";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [AO] into memory");
                        code[position] = "A0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "FB";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [AE] into memory");
                        code[position] = "AE";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FF] into memory");
                        code[position] = "FF";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [FE] into memory");
                        code[position] = "FE";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                        code[position] = "A0";
                        position++;

                        // load false
                        System.out.println("DEBUG CodeGen - Writing [F5] into memory");
                        code[position] = "F5";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A2] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;

                    }

                    else if (n.children.get(0).value.equals("Inequality")) {
                        this.genEq(n.children.get(0), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [0A] into memory");
                        code[position] = "0A";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [AO] into memory");
                        code[position] = "A0";
                        position++;

                        //load false
                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "F5";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [AE] into memory");
                        code[position] = "AE";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FF] into memory");
                        code[position] = "FF";
                        position++;




                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [FE] into memory");
                        code[position] = "FE";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;



                        System.out.println("DEBUG CodeGen - Writing [A0] into memory");
                        code[position] = "A0";
                        position++;

                        //load true
                        System.out.println("DEBUG CodeGen - Writing [FF] into memory");
                        code[position] = "FB";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "02";
                        position++;
                    }

                    System.out.println("DEBUG CodeGen - Writing [FF] into memory");
                    code[position] = "FF";
                    position++;

                    break;
                case "Addition":
                    //genAddition(/*staticData, code, position, */n, currentScope);
                    break;
                case "Equality":
                    break;
                case "Inequality":
                    break;
                case "WhileStatement":
                    int start = position;
                    if (n.children.get(0).value.equals("boolean")) {
                        if (n.children.get(0).name.equals("true")) {
                            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
                            code[position] = "AE";
                            position++;

                            System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                            code[position] = "FB";
                            position++;

                            System.out.println("DEBUG CodeGen - Writing [00] into memory");
                            code[position] = "00";
                            position++;
                        }
                        // false
                        else {
                            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
                            code[position] = "AE";
                            position++;

                            System.out.println("DEBUG CodeGen - Writing [F5] into memory");
                            code[position] = "F5";
                            position++;

                            System.out.println("DEBUG CodeGen - Writing [00] into memory");
                            code[position] = "00";
                            position++;
                        }

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "FB";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;
                    }
                    else if (n.children.get(0).name.equals("Equality")){
                        this.genEq(n.children.get(0), currentScope);

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;
                    }

                    else if (n.children.get(0).name.equals("Inequality")){
                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "EC";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                        code[position] = "D0";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [02] into memory");
                        code[position] = "02";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                        code[position] = "A9";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [01] into memory");
                        code[position] = "01";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "A2";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "8D";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [00] into memory");
                        code[position] = "EC";
                        position++;


                        System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                        code[position] = "00";
                        position++;

                        System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                        code[position] = "00";
                        position++;
                    }

                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A9";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [01] into memory");
                    code[position] = "01";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [D0] into memory");
                    code[position] = "D0";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [02] into memory");
                    code[position] = "02";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A9";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "00";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [A9] into memory");
                    code[position] = "A2";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "00";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "8D";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [EC] into memory");
                    code[position] = "00";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [FB] into memory");
                    code[position] = "00";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "EC";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "00";
                    position++;

                    System.out.println("DEBUG CodeGen - Writing [00] into memory");
                    code[position] = "00";
                    position++;


                    break;
                case "IfStatement":
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

        position++;
        for (int x = 0; x < staticData.size(); x++){
            staticData.get(x).newPos = Integer.toHexString(position).toUpperCase();
            position++;
        }

        for (int i = 0; i < staticData.size(); i++){
            for (int j = 0; j < code.length; j++){
                if (staticData.get(i).t.equals(code[j])){
                    if (staticData.get(i).t2.equals(code[j+1])) {
                        code[j] = staticData.get(i).newPos;
                        code[j + 1] = "00";
                    }
                }
            }
        }


    }

    public static String toHex(char s) {

        int ascii = (int) s;

        return Integer.toHexString(ascii).toUpperCase();
    }

    public static int inHeap(ArrayList<Heap> heap, String s) {
        for (int i = 0; i < heap.size(); i++){
            if (heap.get(i).word.equals(s))
                return heap.get(i).index;
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

    public void genAddition(CSTNode n, int currentScope){
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

            System.out.println("DEBUG CodeGen - Writing [" + st3 + "] into memory");
            code[position] = st3;
            position++;

            System.out.println("DEBUG CodeGen - Writing [00] into memory");
            code[position] = "00";
            position++;
        }

        else if (isId(n.children.get(1).name)){
            System.out.println("DEBUG CodeGen - Writing [AD] into memory");
            code[position] = "AD";
            position++;

            st1 = checkStatic(staticData, n.children.get(1).name, currentScope, 1);
            code[position] = st1;
            position++;

            st2 = checkStatic(staticData, n.children.get(1).name, currentScope, 2);
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
            genAddition(n.children.get(1), currentScope);
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

    public void genEq(CSTNode n, int currentScope) {
        String st1;
        String st2;
        String st3 = "00";

        if (isDigit(n.children.get(0).name)){
            System.out.println("DEBUG CodeGen - Writing [A2] into memory");
            code[position] = "A2";
            position++;

            System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(0).name + "] into memory");
            code[position] = "0" + n.children.get(0).name;
            position++;
        }

        else if (n.children.get(0).value.equals("string")) {
            System.out.println("DEBUG CodeGen - Writing [A2] into memory");
            code[position] = "A2";
            position++;

            String str = n.children.get(1).name;
            int heapTemp;

            //check if string is already in heap memory
            //reuse heap pointer if already exists
            heapTemp = inHeap(heapMem, str);
            if (heapTemp != -1) {
                System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heapTemp).toUpperCase() + "] into memory");
                code[position] = Integer.toHexString(heapTemp).toUpperCase();
                position++;
            }

            //store new string in heap
            else {
                for (int i = n.children.get(1).name.length() - 1; i >= 0; i--) {
                    code[heap] = toHex(str.charAt(i));
                    heap--;
                }
                System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heap+1).toUpperCase() + "] into memory");
                code[position] = Integer.toHexString(heap+1).toUpperCase();
                position++;
            }
        }

        else if (isId(n.children.get(0).name)){
            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
            code[position] = "AE";
            position++;

            st1 = checkStatic(staticData, n.children.get(1).name, currentScope, 1);
            code[position] = st1;
            position++;

            st2 = checkStatic(staticData, n.children.get(1).name, currentScope, 2);
            code[position] = st2;
            position++;
        }

        else if (n.children.get(0).name == "Addition"){
            this.genAddition(n.children.get(0), currentScope);

            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
            code[position] = "AE";
            position++;

            System.out.println("DEBUG CodeGen - Writing [" + st3 + "] into memory");
            code[position] = st3;
            position++;

            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
            code[position] = "00";
            position++;
        }

        else if (n.children.get(0).value.equals("boolean")){
            System.out.println("DEBUG CodeGen - Writing [A2] into memory");
            code[position] = "A2";
            position++;

            if (n.children.get(0).name == "true")
                code[position] = "FB";
            else
                code[position] = "F5";
            position++;
        }

        // check second node
        if (isDigit(n.children.get(1).name)){
            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "A9";
            position++;

            System.out.println("DEBUG CodeGen - Writing [0" + n.children.get(1).name + "] into memory");
            code[position] = "0" + n.children.get(1).name;
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

        else if (n.children.get(1).value.equals("string"))
        {
            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "A9";
            position++;

            String str = n.children.get(1).name;
            int heapTemp;

            //check if string is already in heap memory
            //reuse heap pointer if already exists
            heapTemp = inHeap(heapMem, str);
            if (heapTemp != -1) {
                System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heapTemp).toUpperCase() + "] into memory");
                code[position] = Integer.toHexString(heapTemp).toUpperCase();
                position++;
            }

            //store new string in heap
            else {
                for (int i = n.children.get(1).name.length() - 1; i >= 0; i--) {
                    code[heap] = toHex(str.charAt(i));
                    heap--;
                }
                System.out.println("DEBUG CodeGen - Writing [" + Integer.toHexString(heap+1).toUpperCase() + "] into memory");
                code[position] = Integer.toHexString(heap+1).toUpperCase();
                position++;
            }

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

        else if (n.children.get(1).value.equals("boolean")){
            System.out.println("DEBUG CodeGen - Writing [A9] into memory");
            code[position] = "A9";
            position++;

            if (n.children.get(1).name.equals("true"))
                code[position] = "FB";
            else
                code[position] = "F5";
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

        else if (isId(n.children.get(1).name)){
            System.out.println("DEBUG CodeGen - Writing [AE] into memory");
            code[position] = "AE";
            position++;

            st1 = checkStatic(staticData, n.children.get(1).name, currentScope, 1);
            code[position] = st1;
            position++;

            st2 = checkStatic(staticData, n.children.get(1).name, currentScope, 2);
            code[position] = st2;
            position++;
        }
        else if (n.children.get(1).name.equals("Addition")){
            this.genAddition(n.children.get(1), currentScope);
        }

        else{
            System.out.println("ERROR CodeGen - Unsupported Function");
            error++;
        }

    }

}

class Static{
    String t;
    String t2;
    String name;
    String newPos;
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
        newPos = "00";
    }
}

class Jump{
    public Jump(int temp){

    }
}

class Heap{
    String word;
    int index;
    public Heap(String string, int i){
        this.word = string;
        this.index = i;
    }
}



