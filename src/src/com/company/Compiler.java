/*
used https://lambda.uta.edu/cse5317/l2.pdf as a resource to help move towards accepting states using DFA.
used on line 139 https://www.geeksforgeeks.org/find-the-index-of-an-array-element-in-java/ for findIndex method that can help find the index of 'nextChar' within the DFA.
*/
/*Andrew Mathew
  CMPT 432
  Dr. Labouseur
  Proj 1
*/
package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Compiler {

    public static enum tokenType {
        PRINT,          //"print"
        WHILE,          //"while"
        ID,             //"[a-z]"
        IF,             //"if"
        VAR_TYPE,       //"String", "boolean", "int"
        DIGIT,          //"[0-9]"
        CHAR,           //"[a-z]"
        SPACE,          //" "
        BOOL_EQ,        //"=="
        BOOL_INEQ,      //"!="
        BOOL_VAL,       //"true", "false"
        ADD_OP,         //"+"
        EOP,            //"$"
        L_PAREN,        //"("
        R_PAREN,        //")"
        L_BRACE,        //"{"
        R_BRACE,        //"}"
        SO_COMMENT,     //"/*"
        EO_COMMENT,     //"*/"
        ASSIGN_OP;      //"="

    }

    public static void main(String[] args) {
	// write your code here
        ArrayList<Token> tokenList = new ArrayList<Token>();
        Scanner scan = new Scanner(System.in);

        char currChar;
        char nextChar;

        // line/position trackers
        int lineNum = 0;
        int currPosition = 0;
        int lastPosition = -1;
        int programCounter = 0;
        int currState = 1;
        int nextState;
        int finalState = 0;

        // brace and paren counters
        int braces = 0;
        int parens = 0;

        // token boolean checkers
        boolean openComment = false;
        boolean openQuote = false;
        boolean EOP = false;

        char[] abcd = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','{','}','(',')','!','=','+','"','/','*','&',' '};

        int edges[][] =     { /* a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  0  1  2  3  4  5  6  7  8  9  {  }  (  )  !  =  +  "  /  *  $  blank */
                /* state 0 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 1 */  {54,38,54,54,54,11,54,54, 2,54,54,54,54,54,54,16,54,54,23, 7,54,54,31,54,54,54, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,47,48,36,46,21,29,37,55,49,50,45,53},
                /* state 2 */  { 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: i
                /* state 3 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //if
                /* state 4 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 5 */  { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //int
                /* state 6 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //digit: [0-9]
                /* state 7 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: t
                /* state 8 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 9 */  { 0, 0, 0, 0,10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 10 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //true
                /* state 11 */ {12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: f
                /* state 12 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 13 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 14 */ { 0, 0, 0, 0,15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 15 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //false
                /* state 16 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: p
                /* state 17 */ { 0, 0, 0, 0, 0, 0, 0, 0,18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 18 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 19 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 20 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //print
                /* state 21 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,22, 0, 0, 0, 0, 0, 0},
                /* state 22 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //NOT_EQ '!='
                /* state 23 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: s
                /* state 24 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 25 */ { 0, 0, 0, 0, 0, 0, 0, 0,26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 26 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 27 */ { 0, 0, 0, 0, 0, 0,28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 28 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //string
                /* state 29 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,30, 0, 0, 0, 0, 0, 0},
                /* state 30 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //IS_EQ '=='
                /* state 31 */ { 0, 0, 0, 0, 0, 0, 0,32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: w
                /* state 32 */ { 0, 0, 0, 0, 0, 0, 0, 0,33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 33 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 34 */ { 0, 0, 0, 0,35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 35 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //while
                /* state 36 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L_PAREN '('
                /* state 37 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ADD_OP '+'
                /* state 38 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: b
                /* state 39 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 40 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,41, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 41 */ { 0, 0, 0, 0,42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 42 */ {43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 43 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,44, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 44 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //boolean
                /* state 45 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //EOP '$'
                /* state 46 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //R_PAREN ')'
                /* state 47 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //R_BRACE '{'
                /* state 48 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L_BRACE '}'
                /* state 49 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,52, 0, 0},
                /* state 50 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,51, 0, 0, 0},
                /* state 51 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //END COMMENT '*/'
                /* state 52 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //START COMMENT '/*'
                /* state 53 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //blank
                /* state 54 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: a,c-e,g,h,j-o,q,r,u,v,x-z
                /* state 55 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 56 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};//QUOTE: "

        while(scan.hasNext()){
            programCounter++;
            System.out.println("INFO  Lexer - Lexing program " + programCounter + "...");

            String line = scan.nextLine();
            lineNum++;

            for(int i = 0; i < line.length(); i++){
                currPosition = i;
                currChar = line.charAt(i);
                nextState = edges[currState][findIndex(abcd, currChar)];
                if (nextState == 0)
                    log("ERROR", currChar, lineNum, currPosition);
                currState = nextState;
                if(currState == 51)
                    openComment = false;
                try {
                   nextChar = line.charAt(i + 1);
                }
                catch (Exception e) {
                    break;
                }

                if(!openQuote){
                    while (!openComment){
                        switch(currState) {
                            case 0:
                                log("ERROR", currChar, lineNum, currPosition);
                            case 2:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 3:
                                tokenList.add(new Token("IF", lineNum, currPosition, "if"));
                                log("IF", currChar, lineNum, currPosition);
                            case 5:
                                tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "INT"));
                                log("VAR_TYPE", currChar, lineNum, currPosition);
                            case 6:
                                tokenList.add(new Token("DIGIT", lineNum, currPosition, currChar));
                                log("DIGIT", currChar, lineNum, currPosition);
                            case 7:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 52:
                                openComment = true;
                            case 53:
                                /*tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);*/
                            case 10:
                                tokenList.add(new Token("BOOL_VAL", lineNum, currPosition, "true"));
                                log("BOOL_VAL", "true", lineNum, currPosition);
                            case 11:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 15:
                                tokenList.add(new Token("BOOL_VAL", lineNum, currPosition, "false"));
                                log("BOOL_VAL", "false", lineNum, currPosition);
                            case 16:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 20:
                                tokenList.add(new Token("PRINT", lineNum, currPosition, "print"));
                                log("PRINT", "print", lineNum, currPosition);
                            case 22:
                                tokenList.add(new Token("INEQUALITY_OP", lineNum, currPosition, "!="));
                                log("INEQUALITY_OP", "!=", lineNum, currPosition);
                            case 23:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 28:
                                tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "string"));
                                log("VAR_TYPE", "string", lineNum, currPosition);
                            case 30:
                                tokenList.add(new Token("EQUALITY_OP", lineNum, currPosition, "=="));
                                log("EQUALITY_OP", "==", lineNum, currPosition);
                            case 31:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 35:
                                tokenList.add(new Token("WHILE", lineNum, currPosition, "while"));
                                log("WHILE", "while", lineNum, currPosition);
                            case 36:
                                tokenList.add(new Token("L_PAREN", lineNum, currPosition, currChar));
                                log("L_PAREN", currChar, lineNum, currPosition);
                            case 37:
                                tokenList.add(new Token("ADD_OP", lineNum, currPosition, currChar));
                                log("ADD_OP", currChar, lineNum, currPosition);
                            case 38:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                            case 44:
                                tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "boolean"));
                                log("VAR_TYPE", "boolean", lineNum, currPosition);
                            case 45:
                                tokenList.add(new Token("EOP", lineNum, currPosition, currChar));
                                log("EOP", currChar, lineNum, currPosition);
                            case 46:
                                tokenList.add(new Token("R_PAREN", lineNum, currPosition, currChar));
                                log("R_PAREN", currChar, lineNum, currPosition);
                            case 47:
                                tokenList.add(new Token("L_BRACE", lineNum, currPosition, currChar));
                                log("L_BRACE", currChar, lineNum, currPosition);
                            case 48:
                                tokenList.add(new Token("R_BRACE", lineNum, currPosition, currChar));
                                log("R_BRACE", currChar, lineNum, currPosition);
                            case 56:
                                tokenList.add(new Token("QUOTE", lineNum, currPosition, currChar));
                                log("QUOTE", currChar, lineNum, currPosition);
                                openQuote = true;
                            case 54:
                                tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                log("ID", currChar, lineNum, currPosition);
                                i++;
                        }


                    }
                }
                else
                    switch (currState) {
                        case 2:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 7:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 11:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 16:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 23:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 31:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 38:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 54:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 53:
                            tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                            log("CHAR", currChar, lineNum, currPosition);
                        case 56:
                            tokenList.add(new Token("QUOTE", lineNum, currPosition, currChar));
                            log("QUOTE", currChar, lineNum, currPosition);
                            openQuote = false;
                    }
            }
        }
    }

    public static void log(String tokenType, char value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unexpected [ " + value + " ] found at (" + line + ":" + position + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + position + ")");
    }

    public static void log(String tokenType, String value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unexpected [ " + value + " ] found at (" + line + ":" + position + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + position + ")");
    }

    public static int findIndex(char arr[], char t){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == t)
                return i;
        }
        return -1;
    }

}
