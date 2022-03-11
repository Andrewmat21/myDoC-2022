/*
REFERENCES:
used https://stackoverflow.com/questions/14913804/recursive-descent-parser-in-java to help with figuring out parser logic
*/

/*
Project 2
Andrew Mathew
CMPT 432
Dr. Labouseur
*/
package com.company;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    int error = 0;
    int n;
    boolean flag;
    String temp;
    ArrayList<Token> stream = new ArrayList<Token>();
    CST tree = new CST();

    public Parser(ArrayList<Token> tokenStream, int start) {
        stream = tokenStream;
        int errors = error;
        n = start;
    }

    public void parse(int progNum){
        parseProgram();
        System.out.println("INFO  Parser - Parse completed with " + error + " ERROR(s)");
        if (this.getErrorNum() == 0){
            System.out.println();
            System.out.println("INFO  Creating CST for program " + progNum + "...");
            tree.logCST(0, tree.root);
        }
        else {
            System.out.println();
            System.out.println("INFO  CST for program " + progNum + ": Skipped due to Parser ERROR(s)");
        }
    }

    //program parse
    public void parseProgram() {
        tree.addNode("Program", "root");
        System.out.println("DEBUG Parser - Program...");
        parseBlock();
        match("EOP");
    }

    //block parse
    public void parseBlock() {
        tree.addNode("Block","branch");
        System.out.println("DEBUG Parser - Block...");
        match("L_BRACE");
        parseStatementList();
        System.out.println("DEBUG Parser - Block...");
        match("R_BRACE");
        tree.moveUp();
    }

    //stmt list parse
    public void parseStatementList() {
        flag = false;
        tree.addNode("StatementList", "branch");
        System.out.println("DEBUG Parser - Statement List...");
        switch(getToken().tokenType){
            case "PRINT":
                flag = true;
                break;
            case "ID":
                flag = true;
                break;
            case "VAR_TYPE":
                flag = true;
                break;
            case "WHILE":
                flag = true;
                break;
            case "IF":
                flag = true;
                break;
            case "L_BRACE":
                flag = true;
                break;
            default:
                flag = false;
        }
        if (flag){
            parseStatement();
            parseStatementList();
        }
        // epsilon production
        else {
            match("empty");
        }
        tree.moveUp();
    }

    //stmt parse
    public void parseStatement() {
        tree.addNode("Statement", "branch");
        System.out.println("DEBUG Parser - Statement...");
        switch (getToken().tokenType) {
            case "PRINT":
                parsePrintStatement();
                break;
            case "ID":
                parseAssignmentStatement();
                break;
            case "VAR_TYPE":
                parseVarDecl();
                break;
            case "WHILE":
                parseWhileStatement();
                break;
            case "IF":
                parseIfStatement();
                break;
            case "L_BRACE":
                parseBlock();
                break;
        }
        tree.moveUp();
    }

    //print stmt parse
    public void parsePrintStatement() {
        tree.addNode("PrintStatement", "branch");
        System.out.println("DEBUG Parser - Print Statement...");
        match("PRINT");
        match("L_PAREN");
        parseExpr();
        match("R_PAREN");
        tree.moveUp();
    }

    //assign stmt parse
    public void parseAssignmentStatement() {
        tree.addNode("AssignmentStatement", "branch");
        System.out.println("DEBUG Parser - Assignment Statement...");
        parseId();
        match("ASSIGN_OP");
        parseExpr();
        tree.moveUp();
    }

    //var dec parse
    public void parseVarDecl() {
        tree.addNode("VarDecl", "branch");
        System.out.println("DEBUG Parser - Var Decl...");
        parseType();
        parseId();
        tree.moveUp();
    }

    //while stmt parse
    public void parseWhileStatement() {
        tree.addNode("WhileStatement", "branch");
        System.out.println("DEBUG Parser - While Statement...");
        match("WHILE");
        parseBoolExpr();
        parseBlock();
        tree.moveUp();
    }

    //if statement parse
    public void parseIfStatement() {
        tree.addNode("IfStatement", "branch");
        System.out.println("DEBUG Parser - If Statement...");
        match("IF");
        parseBoolExpr();
        parseBlock();
        tree.moveUp();
    }

    //expr parse
    public void parseExpr() {
        tree.addNode("Expr", "branch");
        System.out.println("DEBUG Parser - Expr...");
        switch (getToken().tokenType) {
            case "DIGIT":
                flag = true;
                parseIntExpr();
                break;
            case "QUOTE":
                flag = true;
                parseStringExpr();
                break;
            case "L_PAREN":
                flag = true;
                parseBoolExpr();
                break;
            case "BOOL_VAL":
                flag = true;
                parseBoolExpr();
                break;
            case "ID":
                flag = true;
                parseId();
                break;
        }
        tree.moveUp();
    }

    //int expr parse
    public void parseIntExpr() {
        tree.addNode("IntExpr", "branch");
        System.out.println("DEBUG Parser - Int Expr...");
        parseDigit();
        if (getToken().tokenType == "ADD_OP") {
            parseIntOp();
            parseExpr();
        }
        else
            n+=0;
        tree.moveUp();
    }

    //String expr parse
    public void parseStringExpr() {
        tree.addNode("StringExpr", "branch");
        System.out.println("DEBUG Parser - String Expr...");
        match("QUOTE");
        parseCharList();
        match("QUOTE");
        tree.moveUp();
    }

    //boolExpr parse
    public void parseBoolExpr() {
        tree.addNode("BooleanExpr", "branch");
        System.out.println("DEBUG Parser - Bool Expr...");
        switch (getToken().tokenType){
            case "L_PAREN":
                flag = true;
                match("L_PAREN");
                parseExpr();
                parseBoolOp();
                parseExpr();
                match("R_PAREN");
                break;
            case "BOOL_VAL":
                flag = true;
                match("BOOL_VAL");
                parseBoolVal();
                break;
        }
        tree.moveUp();
    }

    //Id parse
    public void parseId() {
        tree.addNode("Id", "branch");
        match("ID");
        tree.moveUp();
    }

    //charList parse
    public void parseCharList() {

        System.out.println("DEBUG Parser - Char List...");
        if (getToken().tokenType == "CHAR") {
            tree.addNode("CharList", "branch");
            parseChar();
            parseCharList();
        }
        else {
            match("empty");
            n += 0;
        }
        tree.moveUp();
    }

    //find type parse
    public void parseType() {
        tree.addNode("Type", "branch");
        switch (getToken().word) {
            case "string":
                flag = true;
                match("VAR_TYPE");
                break;
            case "int":
                flag = true;
                match("VAR_TYPE");
                break;
            case "boolean":
                flag = true;
                match("VAR_TYPE");
                break;
        }
        tree.moveUp();
    }

    //char parse
    public void parseChar() {
        tree.addNode("Char", "branch");
        match("CHAR");
        tree.moveUp();
    }

    // digit parse
    public void parseDigit() {
        tree.addNode("Digit", "branch");
        match("DIGIT");
        tree.moveUp();
    }

    //boolOp parse
    public void parseBoolOp() {
        tree.addNode("BoolOp", "branch");
        if (getToken().tokenType == "EQUALITY_OP")
            match("EQUALITY_OP");
        else
            match("INEQUALITY_OP");
        tree.moveUp();
    }

    //boolVal parse
    public void parseBoolVal() {
        tree.addNode("BoolVal", "branch");
        match("BOOL_VAL");
        tree.moveUp();
    }

    // intOp parse
    public void parseIntOp() {
        tree.addNode("IntOp", "branch");
        match("ADD_OP");
        tree.moveUp();
    }

    public boolean match(String expected) {
        Token currTok = getToken();
        // detect expected token
        if (currTok.tokenType == expected) {
            // addNode
            if (n < stream.size() - 1) {
                n++;
            }
            parseLog(currTok.word, expected, currTok.lineNum, currTok.position);
            tree.addNode("leaf", currTok.word);
            return true;
        }
        //detect epsilon production
        else if (expected == "empty"){
            System.out.println("VALID Parser - e (Epsilon) production found at (" + (currTok.lineNum) + ":" + (currTok.position+1) + ")");

            return true;
        }
    // display parse error
        else {
            throwError(currTok.tokenType, expected, currTok.lineNum, currTok.position);
            error++;
            return false;
        }
    }

    public Token getToken() {
       return stream.get(n);
    }

    public int getErrorNum(){
        return error;
    }

    public static void throwError(String tokenType, String expected, int line, int position){
        System.out.println("ERROR Parser - Expected [ " + expected + " ] but found [ " + tokenType + " ] at (" + line + ":" + (position+1) + ")");
    }

    public static void parseLog(String tokenName, String expected, int line, int position){
        System.out.println("VALID Parser - Expected [ " + expected + " ] and found [ " + tokenName + " ] at (" + line + ":" + (position+1) + ")");
    }
}
