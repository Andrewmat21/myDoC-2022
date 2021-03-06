/*
used https://lambda.uta.edu/cse5317/l2.pdf as a resource to help with finding longest match using DFA. line 126
used on line 139 https://www.geeksforgeeks.org/find-the-index-of-an-array-element-in-java/ for findIndex method that can help find the index of 'nextChar' within the DFA.

Andrew Mathew
CMPT 432
Dr. Labouseur

*/
package com.company;

import java.util.ArrayList;
import java.util.Scanner;


public class Compiler {

    public static void main(String[] args) {

        ArrayList<Token> tokenList = new ArrayList<Token>();
        Scanner scan = new Scanner(System.in);

        char currChar;
        char nextChar;
        String line;

        // line/position trackers
        int lineNum = 0;
        int currPosition = 0;
        int programCounter = 0;
        int currState = 1;
        int nextState;
        int otherState = 0;
        int finalState = 0;
        int tempState = 0;
        int x;
        int y;
        int i;
        int lastMatch = 0;
        int start = 0;
        int startToken = 0;

        // program counters
        int warnings = 0;
        int errors = 0;

        // token boolean checkers
        boolean openComment = false;
        boolean openQuote = false;

        //end of program '$' indicator
        boolean EOP = true;
        int EOPcount = 0;

        // character reference table
        char[] abcd = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','{','}','(',')','!','=','+','"','/','*','$',' '};

        int edges[][] =     { /* a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  0  1  2  3  4  5  6  7  8  9  {  }  (  )  !  =  +  "  /  *  $  blank */
                /* state 0 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 1 */  {54,38,54,54,54,11,54,54, 2,54,54,54,54,54,54,16,54,54,23, 7,54,54,31,54,54,54, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,47,48,36,46,21,29,37,55,49,50,45,53},
                /* state 2 */  { 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: i
                /* state 3 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //if
                /* state 4 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 5 */  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //int
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
                /* state 29 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,30, 0, 0, 0, 0, 0, 0}, //ASSIGN_OP '='
                /* state 30 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //EQUALITY_OP '=='
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
                /* state 47 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L_BRACE '{'
                /* state 48 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //R_BRACE '}'
                /* state 49 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,52, 0, 0},
                /* state 50 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,51, 0, 0, 0},
                /* state 51 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //END COMMENT '*/'
                /* state 52 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //START COMMENT '/*'
                /* state 53 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //blank
                /* state 54 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: a,c-e,g,h,j-o,q,r,u,v,x-z
                /* state 55 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //QUOTE: "
                /* state 56 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

        // run lexer until there is no more output available
        while(scan.hasNext()) {
            //EOP = true;
            if (EOP) {
                EOPcount = 0;
                programCounter++;
                errors = 0;
                warnings = 0;
                System.out.println();
                System.out.println("INFO  Lexer - Lexing program " + programCounter + "...");
            }
            EOP = false;
            // turn input line into string
            line = scan.nextLine();
            lineNum++;
            if (!EOP) {

                // check for unterminated string on previous line.
                if (openQuote){
                    System.out.println("ERROR Lexer - Unterminated string found at the end of line " + (lineNum-1));
                    errors++;
                }

                i = 0;
                while (i < line.length()) {
                    // set start state to 1
                    // set final state to 0 to catch errors


                    currChar = line.charAt(i);
                    while (true) {
                        currPosition = i;

                        x = findIndex(abcd, currChar);
                        // output error for invalid char
                        if (x == -1) {
                            if (openComment){
                                break;
                            }
                            else if (finalState == 52) {
                                break;
                            }
                            else {
                                log("ERROR", currChar, lineNum, currPosition);
                                errors++;
                                break;
                            }
                        }
                        try {
                            nextState = edges[currState][x];
                            if (nextState == 0)
                                break;
                            currState = nextState;
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                        // tempState = nextState;
                        if (isFinalState(currState)) {
                            finalState = currState;
                            lastMatch = i;
                            tempState = currState;
                        }
                        try {
                            nextChar = line.charAt(i + 1);
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }

                        // output error for invalid char

                        y = findIndex(abcd, nextChar);
                        // check if char is not a valid character in our language
                        if(y == -1) {
                            if (openComment){
                                break;
                            }
                            else if (finalState == 52)
                                break;
                            else {
                                //log("ERROR", currChar, lineNum, currPosition);
                                //errors++;
                                break;
                            }
                        }

                        // look ahead 1 char
                        // skip look ahead if within quote
                        if(!openQuote) {
                            try {
                                otherState = edges[currState][y];
                            } catch (IndexOutOfBoundsException e) {
                                break;
                            }
                        }
                        // if transition state found ahead, keep going
                        if (otherState != 0) {
                            currChar = nextChar;
                            i++;
                        }
                        // catch error for unexpected '!'
                        else if (currState == 21){
                            log("ERROR", currChar, lineNum, currPosition);
                            errors++;
                            currState = 1;
                            finalState = 0;
                            break;
                        }
                        // fall back to last final state
                        else {
                            i = lastMatch;
                            currState = finalState;
                            currChar = line.charAt(i);
                            break;
                        }
                    }

                    if (finalState == 0) {
                        if (openComment) {
                            currState = 1;
                            finalState = 0;
                        }
                        else {

                        }
                    }

                    else if ((currState != finalState)) {
                        if (openComment) {
                            currState = 1;
                            finalState = 0;
                        }
                        else
                            i = lastMatch;
                    }

                    else {
                        if (!openQuote) {
                            if (!openComment) {
                                switch (finalState) {
                                    case 0: //error
                                        log("ERROR", currChar, lineNum, currPosition);
                                        errors++;
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 2: //check for 'i'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 3: // check for 'if'
                                        tokenList.add(new Token("IF", lineNum, currPosition, "if"));
                                        start++;
                                        log("IF", "if", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 5: // check for 'int'
                                        tokenList.add(new Token("VAR_TYPE", lineNum, currPosition-1, "int"));
                                        start++;
                                        log("VAR_TYPE", "int", lineNum, currPosition-1);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 6: // check for 'digit'
                                        tokenList.add(new Token("DIGIT", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("DIGIT", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 7: // check for 't'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 52: // check for '/*'
                                        openComment = true;
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 53: // check for blank ' '
                                        //tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 10: // check for 'true'
                                        tokenList.add(new Token("BOOL_VAL", lineNum, currPosition-2, "true"));
                                        start++;
                                        log("BOOL_VAL", "true", lineNum, currPosition-2);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 11: // check for 'f'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 15: // check for 'false'
                                        tokenList.add(new Token("BOOL_VAL", lineNum, currPosition-3, "false"));
                                        start++;
                                        log("BOOL_VAL", "false", lineNum, currPosition-3);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 16: // check for 'p'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 20: // check for 'print'
                                        tokenList.add(new Token("PRINT", lineNum, currPosition, "print"));
                                        start++;
                                        log("PRINT", "print", lineNum, (currPosition-3));
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 22: // check for '!='
                                        tokenList.add(new Token("INEQUALITY_OP", lineNum, currPosition, "!="));
                                        start++;
                                        log("INEQUALITY_OP", "!=", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 23: // check for 's'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 28: // check for 'string'
                                        tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "string"));
                                        start++;
                                        log("VAR_TYPE", "string", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 29: // check for ASSIGN OP '='
                                        tokenList.add(new Token("ASSIGN_OP", lineNum, currPosition, "="));
                                        start++;
                                        log("ASSIGN_OP", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;

                                    case 30: // creation of EQUALITY '==' Token
                                        tokenList.add(new Token("EQUALITY_OP", lineNum, currPosition, "=="));
                                        start++;
                                        log("EQUALITY_OP", "==", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 31: // check for 'w'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 35: // check for 'while'
                                        tokenList.add(new Token("WHILE", lineNum, currPosition, "while"));
                                        start++;
                                        log("WHILE", "while", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 36: // check for '('
                                        tokenList.add(new Token("L_PAREN", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("L_PAREN", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 37: // check for '+'
                                        tokenList.add(new Token("ADD_OP", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ADD_OP", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 38: // check for 'b'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 44: // check for 'boolean'
                                        tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "boolean"));
                                        start++;
                                        log("VAR_TYPE", "boolean", lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 45: // check for '$'
                                        tokenList.add(new Token("EOP", lineNum, currPosition, "$"));
                                        start++;
                                        log("EOP", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        EOPcount++;
                                        EOP = true;
                                        break;
                                    case 46: // check for ')'
                                        tokenList.add(new Token("R_PAREN", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("R_PAREN", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 47: // check for '{'
                                        tokenList.add(new Token("L_BRACE", lineNum, currPosition, "{"));
                                        start++;
                                        log("L_BRACE", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 48: // check for '}'
                                        tokenList.add(new Token("R_BRACE", lineNum, currPosition, "}"));
                                        start++;
                                        log("R_BRACE", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    case 55: // check for '"'
                                        tokenList.add(new Token("QUOTE", lineNum, currPosition, "\""));
                                        start++;
                                        log("QUOTE", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        openQuote = true;
                                        break;
                                    case 54: // check for '[a | c-e | g | h | j-o | q | r | u | v | x-z]'
                                        tokenList.add(new Token("ID", lineNum, currPosition, Character.toString(currChar)));
                                        start++;
                                        log("ID", currChar, lineNum, currPosition);
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                    default: // invalid char within string
                                        System.out.println("ERROR Lexer - Invalid [ " + currChar + " ] string literal found at (" + lineNum + ":" + (currPosition+1) + ") - Only characters [a-z] are allowed.");
                                        warnings++;
                                        currState = 1;
                                        finalState = 0;
                                        break;
                                }
                            }
                            else if (finalState == 51) {
                                openComment = false;
                                currState = 1;
                                finalState = 0;
                            }
                            else {
                                currState = 1;
                                finalState = 0;
                            }
                        }
                        else if (finalState == 55) {
                            tokenList.add(new Token("QUOTE", lineNum, currPosition, "\""));
                            start++;
                            log("QUOTE", currChar, lineNum, currPosition);
                            currState = 1;
                            finalState = 0;
                            openQuote = false;
                        }
                        else
                            // 'CHAR' formatted token outputs for when open Quotes are detected
                            switch (finalState) {
                                case 2: // char 'i'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 7: // char 't'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 11: // char 'f'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 16: // char 'p'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 23: // char 's'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 31: // char 'w'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 38: // char 'b'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 54: // char '[a | c-e | g | h | j-o | q | r | u | v | x-z]'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("CHAR", currChar, lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 53: // char ' ' (space)
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, " "));
                                    start++;
                                    log("CHAR", ' ', lineNum, currPosition);
                                    currState = 1;
                                    finalState = 0;
                                    break;
                                case 55: // char '"'
                                    tokenList.add(new Token("CHAR", lineNum, currPosition, Character.toString(currChar)));
                                    start++;
                                    log("QUOTE", currChar, lineNum, currPosition);
                                    openQuote = false;
                                    break;
                                case 45: // char '$'
                                    System.out.println("DEBUG Lexer - WARNING: Unexpected EOP [$] symbol found at (" + lineNum + ":" + (currPosition+1) + ") - Only characters [a-z] are allowed.");
                                    currState = 1;
                                    finalState = 0;
                                    warnings++;
                                    break;
                                default: // invalid char within string
                                    System.out.println("DEBUG Lexer - ERROR: Invalid [ " + currChar + " ] string literal found at (" + lineNum + ":" + (currPosition+1) + ") - Only characters [a-z] are allowed.");
                                    errors++;
                                    currState = 1;
                                    finalState = 0;
                                    break;
                            }
                        }
                    // move index forward
                    i++;
                    }
                if (EOP) {
                    if (openQuote) {
                        System.out.println("DEBUG - Lexer - WARNING: closed quote undetected at end-of-file.");
                        warnings++;

                    }
                    if (openComment) {
                        System.out.println("DEBUG - Lexer - WARNING: Unterminated comment detected at end-of-file.");
                        warnings++;

                    }
                    if (EOPcount == 0) {
                        warnings++;
                        System.out.println("DEBUG - Lexer - WARNING: No EOP [$] detected at end-of-file.");

                    }

                    System.out.println("INFO  Lexer - Lex completed with " + warnings + " WARNING(s) and " + errors + " ERROR(s)");
                    System.out.println();

                    // if lex has 0 errors, continue to parse...
                    // otherwise, skip it
                    if (errors == 0){
                        System.out.println("INFO  Parser - Parsing program " + programCounter + "...");
                        Parser parse = new Parser(tokenList, startToken);
                        parse.parse(programCounter);
                        if (parse.getErrorNum() == 0){
                            System.out.println();
                        }
                    }
                    else{
                        System.out.println("INFO  Parser - Skipped due to Lexer ERROR(s)");
                    }
                    startToken = start;
                } else /*if (!(scan.hasNext()))*/{

                    /*if (EOPcount == 0) {
                        warnings++;
                        System.out.println("DEBUG - Lexer - WARNING: No EOP [$] detected at end-of-file. Adding one for you...You're Welcome.");
                        tokenList.add(new Token("EOP", lineNum, currPosition+1, '$'));
                    }
                    /*
                    System.out.println("INFO  Lexer - Lex completed with " + warnings + " WARNING(s) and " + errors + " ERROR(s)");
                    System.out.println();

                    if (errors == 0){
                        System.out.println("INFO  Parser - Parsing program " + programCounter + "...");
                        //parseProgram
                    }
                    else{
                        System.out.println("INFO  Parser - Skipped due to Lexer ERROR(s)");

                    }*/
                }
            }
            // End of program statement/error summary
            else if (EOP) {
                if (openQuote) {
                    System.out.println("DEBUG - Lexer - WARNING: closed quote undetected at end-of-file.");
                    warnings++;

                }
                if (openComment) {
                    System.out.println("DEBUG - Lexer - WARNING: Unterminated comment detected at end-of-file.");
                    warnings++;

                }
                if (EOPcount == 0) {
                    warnings++;
                    System.out.println("DEBUG - Lexer - WARNING: No EOP [$] detected at end-of-file.");

                }

                System.out.println("INFO  Lexer - Lex completed with " + warnings + " WARNING(s) and " + errors + " ERROR(s)");
                System.out.println();
            }

        }

        if ((!(scan.hasNext()))) {
            if (EOPcount == 0) {
                warnings++;
                System.out.println("DEBUG - Lexer - WARNING: No EOP [$] detected at end-of-file. Adding one for you...You're Welcome.");
                tokenList.add(new Token("EOP", lineNum, currPosition + 1, '$'));
            }
        }
    }

    // output function for char Tokens
    public static void log(String tokenType, char value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unrecognized token [ " + value + " ] found at (" + line + ":" + (position+1) + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + (position+1) + ")");
    }

    // output function for keyword tokens
    public static void log(String tokenType, String value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unexpected [ " + value + " ] found at (" + line + ":" + position + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + position + ")");
    }

    // used to find index of char in abcd array
    public static int findIndex(char arr[], char t){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == t)
                return i;
        }
        return -1;
    }

    // function used to check if current state is a final state
    public static boolean isFinalState(int state){
        int[] finalStates = {0, 2, 3, 5, 6, 7, 10, 11, 15, 16, 20, 22, 23, 28, 29, 30, 31, 35, 36, 37, 38, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55};
        for (int i = 0; i < finalStates.length; i++){
            if(state == finalStates[i])
                return true;
        }
        return false;
    }

}
