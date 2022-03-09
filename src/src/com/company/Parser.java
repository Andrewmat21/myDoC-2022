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
    int n = 0;
    boolean flag;
    String temp;
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
        parseDigit();
        if (getToken().tokenType == "ADD_OP") {
            parseIntOp();
            parseExpr();
        }
        else
            n+=0;
    }

    public void parseStringExpr() {
        match("QUOTE");
        parseCharList();
        match("QUOTE");
    }

    public void parseBoolExpr() {
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
    }

    public void parseId() {
        match("ID");
    }

    public void parseCharList() {
        if (getToken().tokenType == "CHAR") {
            parseChar();
            parseCharList();
        }
        else
            n+=0;
    }

    public void parseType() {
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
    }

    public void parseChar() {
        match("CHAR");
    }

    public void parseDigit() {
        match("DIGIT");
    }

    public void parseBoolOp() {
        if (getToken().tokenType == "EQUALITY_OP")
            match("EQUALITY_OP");
        else
            match("INEQUALITY_OP");
    }

    public void parseBoolVal() {
        match("BOOL_VAL");
    }

    public void parseIntOp() {
        match("ADD_OP");
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
