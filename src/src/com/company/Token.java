package com.company;

public class Token {

    public String tokenType;
    public int lineNum;
    public int position;

    public Token(String tokenType, int lineNum, int position){
        this.tokenType = tokenType;
        this.lineNum = lineNum;
        this.position = position;
    }
}
