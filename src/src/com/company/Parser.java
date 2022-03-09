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
    int n = 0;
    boolean flag;
    ArrayList<Token> stream = new ArrayList<Token>();

    public Parser(ArrayList<Token> tokenStream) {
        stream = tokenStream;
        int errors = error;
    }

    public void parseProgram() {
        parseBlock();
        match("EOP");
    }

    public void parseBlock() {
        match("L_BRACE");
        parseStatementList();
        match("R_BRACE");
    }

    public void parseStatementList() {
        flag = false;
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
        }
        if (flag){
            parseStatement();
            parseStatementList();
        }
        else
            n += 0;
    }

    public void parseStatement() {
        switch (getToken().tokenType) {
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
        }
    }

    public void parsePrintStatement() {

    }

    public void parseAssignmentStatement() {

    }

    public void parseVarDecl() {

    }

    public void parseWhileStatement() {

    }

    public void parseIfStatement() {

    }

    public void parseExpr() {

    }

    public void parseIntExpr() {

    }

    public void parseStringExpr() {

    }

    public void parseBoolExpr() {

    }

    public void parseId() {

    }

    public void parseCharList() {

    }

    public void parseType() {

    }

    public void parseChar() {

    }

    public void parseDigit() {

    }

    public void parseBoolOp() {

    }

    public void parseBoolVal() {

    }

    public void parseIntOp() {

    }

    public boolean match(String expected) {
        Token currTok = getToken();
        if (currTok.tokenType == expected) {
            // addNode
            if (n < stream.size() - 1) {
                n++;
            }
            return true;
        }

        else
            error++;
            throwError(currTok.tokenType, expected, currTok.lineNum, currTok.position);
            return false;
    }
/*
    public boolean match(char expected) {
        Token currTok = getToken();
        if (currTok.symbl == expected) {
            // addNode
        }

        else {
            error++;
            throwError();
        }
    }*/

    public Token getToken() {
       return stream.get(n);
    }

    public static void throwError(String tokenType, String expected, int line, int position){
        System.out.println("ERROR Parser - Expected [ " + expected + " ] but found [ " + tokenType + " ] at (" + line + ":" + (position+1) + ")");

    }
}
