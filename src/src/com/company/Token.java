package com.company;

public class Token {

    public String tokenType;
    public int lineNum;
    public int position;
    public int value;
    public String word;
    public char symbl;


    public Token(String tokenType, int lineNum, int position, int value) {
        this.tokenType = tokenType;
        this.lineNum = lineNum;
        this.position = position;
        this.value = value;
    }

    public Token(String tokenType, int lineNum, int position, String word) {
        this.tokenType = tokenType;
        this.lineNum = lineNum;
        this.position = position;
        this.word = word;
    }

    public Token(String tokenType, int lineNum, int position, char symbl) {
        this.tokenType = tokenType;
        this.lineNum = lineNum;
        this.position = position;
        this.symbl = symbl;
    }
}
