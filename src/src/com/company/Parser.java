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
                parsePrintStatement();
                break;
            case "ID":
                flag = true;
                parseAssignmentStatement();
                break;
            case "VAR_TYPE":
                flag = true;
                parseVarDecl();
                break;
            case "WHILE":
                flag = true;
                parseWhileStatement();
                break;
            case "IF":
                flag = true;
                parseIfStatement();
                break;
            case "L_BRACE":
                flag = true;
                parseBlock();
                break;
        }
    }

    public void parsePrintStatement() {
        match("PRINT");
        match("L_PAREN");
        parseExpr();
        match("R_PAREN");
    }

    public void parseAssignmentStatement() {
        parseId();
        match("ASSIGN_OP");
        parseExpr();
    }

    public void parseVarDecl() {
        parseType();
        parseId();
    }

    public void parseWhileStatement() {
        match("WHILE");
        parseBoolExpr();
        parseBlock();
    }

    public void parseIfStatement() {
        match("IF");
        parseBoolExpr();
        parseBlock();
    }

    public void parseExpr() {
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
            case "ID":
                flag = true;
                parseId();
                break;
        }
    }

    public void parseIntExpr() {
        match(getToken().tokenType);
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

    public Token getToken() {
       return stream.get(n);
    }

    public static void throwError(String tokenType, String expected, int line, int position){
        System.out.println("ERROR Parser - Expected [ " + expected + " ] but found [ " + tokenType + " ] at (" + line + ":" + (position+1) + ")");

    }
}
