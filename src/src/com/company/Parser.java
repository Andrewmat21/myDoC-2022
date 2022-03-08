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
    ArrayList<Token> stream = new ArrayList<Token>();

    public Parser(ArrayList<Token> tokenStream) {
        stream = tokenStream;
    }

    public boolean parseProgram() {

    }

    public boolean parseBlock() {

    }

    public boolean parseStatementList() {

    }

    public boolean parsePrintStatement() {

    }

    public boolean parseAssignmentStatement() {

    }

    public boolean parseVarDecl() {

    }

    public boolean parseWhileStatement() {

    }

    public boolean parseIfStatement() {

    }

    public boolean parseExpr() {

    }

    public boolean parseIntExpr() {

    }

    public boolean parseStringExpr() {

    }

    public boolean parseBoolExpr() {

    }

    public boolean parseId() {

    }

    public boolean parseCharList() {

    }

    public boolean parseType() {

    }

    public boolean parseChar() {

    }

    public boolean parseDigit() {

    }

    public boolean parseBoolOp() {

    }

    public boolean parseBoolVal() {

    }

    public boolean parseIntOp() {

    }

    public boolean match(String expected) {
        Token currTok = getToken();
        if (currTok.value.equals(expected)) {
            return true;
            n++;
        }
    }

    public boolean match(char expected) {
        Token currTok = getToken();
        if (currTok.symbl.equals(expected)) {
            // addNode
        }

    }

    public Token getToken() {
       return stream.get(n);
    }

}