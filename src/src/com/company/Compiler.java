/*
used https://lambda.uta.edu/cse5317/l2.pdf as a resource to help with finding longest match using DFA. line 126
used on line 139 https://www.geeksforgeeks.org/find-the-index-of-an-array-element-in-java/ for findIndex method that can help find the index of 'nextChar' within the DFA.

Andrew Mathew
CMPT 432
Dr. Labouseur
Proj 1

*/
package com.company;

import java.util.ArrayList;
import java.util.Scanner;


public class Compiler {

    public static void main(String[] args) {
	    //
        ArrayList<Token> tokenList = new ArrayList<Token>();
        Scanner scan = new Scanner(System.in);

        char currChar;
        char nextChar;

        // line/position trackers
        int lineNum = 0;
        int currPosition = 0;
        int lastPosition = -1;
        int programCounter = 0;
        int currState;
        int nextState;
        int finalState;
        int x;

        // program counters
        int warnings = 0;
        int errors = 0;

        // token boolean checkers
        boolean openComment = false;
        boolean openQuote = false;
        //end of program '$' indicator
        boolean EOP = false;
        boolean flag = true;

        // character reference table
        char[] abcd = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','{','}','(',')','!','=','+','"','/','*','$','\t'};

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
                /* state 47 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L_BRACE '{'
                /* state 48 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //R_BRACE '}'
                /* state 49 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,52, 0, 0},
                /* state 50 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,51, 0, 0, 0},
                /* state 51 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //END COMMENT '*/'
                /* state 52 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //START COMMENT '/*'
                /* state 53 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //blank
                /* state 54 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //ID: a,c-e,g,h,j-o,q,r,u,v,x-z
                /* state 55 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                /* state 56 */ { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};//QUOTE: "

        //run lexer until there is no more output available
        while(scan.hasNext()){
            EOP = false;
            programCounter++;
            System.out.println("INFO  Lexer - Lexing program " + programCounter + "...");

            //turn input line into string
            String line = scan.nextLine();
            lineNum++;
            if (!EOP) {
                for (int i = 0; i < line.length(); i++) {

                    //set start state to 1
                    currState = 1;
                    // set final state to 0 to catch errors
                    finalState = 0;
                    currChar = line.charAt(i);
                    while (true) {
                        currPosition = i;
                        x = findIndex(abcd, currChar);
                        nextState = edges[currState][x];
                        if (nextState == 0)
                            break;
                        currState = nextState;
                        if (isFinalState(currState))
                            finalState = currState;

                        try {
                            nextChar = line.charAt(i + 1);
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                    }
                    /*if (finalState == 0)
                        log("ERROR", currChar, lineNum, currPosition);
                    else if (currState != finalState) {
                        break;
                    }

                    else {*/
                    if (!openQuote) {
                        if (!openComment) {
                            switch (finalState) {
                                case 0: //error
                                    log("ERROR", currChar, lineNum, currPosition);
                                    errors++;
                                case 2: //check for 'i'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 3: // check for 'if'
                                    tokenList.add(new Token("IF", lineNum, currPosition, "if"));
                                    log("IF", "if", lineNum, currPosition);
                                    break;
                                case 5: // check for 'int'
                                    tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "INT"));
                                    log("VAR_TYPE", "int", lineNum, currPosition);
                                    nextState = 1;
                                    flag = false;
                                    break;
                                case 6: // check for 'digit'
                                    tokenList.add(new Token("DIGIT", lineNum, currPosition, currChar));
                                    log("DIGIT", currChar, lineNum, currPosition);
                                    break;
                                case 7: // check for 't'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 52: // check for '/*'
                                    openComment = true;
                                case 53: // check for blank ' '
                                        tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                        log("ID", currChar, lineNum, currPosition);
                                        break;
                                case 10: // check for 'true'
                                    tokenList.add(new Token("BOOL_VAL", lineNum, currPosition, "true"));
                                    log("BOOL_VAL", "true", lineNum, currPosition);
                                    break;
                                case 11: // check for 'f'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 15: // check for 'false'
                                    tokenList.add(new Token("BOOL_VAL", lineNum, currPosition, "false"));
                                    log("BOOL_VAL", "false", lineNum, currPosition);
                                    break;
                                case 16: // check for 'p'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 20: // check for 'print'
                                    tokenList.add(new Token("PRINT", lineNum, currPosition, "print"));
                                    log("PRINT", "print", lineNum, currPosition);
                                    break;
                                case 22: // check for '!='
                                    tokenList.add(new Token("INEQUALITY_OP", lineNum, currPosition, "!="));
                                    log("INEQUALITY_OP", "!=", lineNum, currPosition);
                                    break;
                                case 23: // check for 's'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 28: // check for 'string'
                                    tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "string"));
                                    log("VAR_TYPE", "string", lineNum, currPosition);
                                    break;

                                case 30:   // creation of EQUALITY '==' Token
                                    tokenList.add(new Token("EQUALITY_OP", lineNum, currPosition, "=="));
                                    log("EQUALITY_OP", "==", lineNum, currPosition);
                                    nextState = 1;
                                    flag = false;
                                    break;
                                case 31: // check for 'w'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 35: // check for 'while'
                                    tokenList.add(new Token("WHILE", lineNum, currPosition, "while"));
                                    log("WHILE", "while", lineNum, currPosition);
                                    break;
                                case 36: // check for '('
                                    tokenList.add(new Token("L_PAREN", lineNum, currPosition, currChar));
                                    log("L_PAREN", currChar, lineNum, currPosition);
                                    break;
                                case 37: // check for '+'
                                    tokenList.add(new Token("ADD_OP", lineNum, currPosition, currChar));
                                    log("ADD_OP", currChar, lineNum, currPosition);
                                    break;
                                case 38: // check for 'b'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                                case 44: // check for 'boolean'
                                    tokenList.add(new Token("VAR_TYPE", lineNum, currPosition, "boolean"));
                                    log("VAR_TYPE", "boolean", lineNum, currPosition);
                                    break;
                                case 45: // check for '$'
                                    tokenList.add(new Token("EOP", lineNum, currPosition, currChar));
                                    log("EOP", currChar, lineNum, currPosition);
                                    EOP = true;
                                    break;
                                case 46: // check for ')'
                                    tokenList.add(new Token("R_PAREN", lineNum, currPosition, currChar));
                                    log("R_PAREN", currChar, lineNum, currPosition);
                                    break;
                                case 47: // check for '{'
                                    tokenList.add(new Token("L_BRACE", lineNum, currPosition, currChar));
                                    log("L_BRACE", currChar, lineNum, currPosition);
                                    currState = 1;
                                    flag = false;
                                    break;
                                case 48: // check for '}'
                                    tokenList.add(new Token("R_BRACE", lineNum, currPosition, currChar));
                                    log("R_BRACE", currChar, lineNum, currPosition);
                                    currState = 1;
                                    flag = false;
                                    break;
                                case 56: // check for '"'
                                    tokenList.add(new Token("QUOTE", lineNum, currPosition, currChar));
                                    log("QUOTE", currChar, lineNum, currPosition);
                                    openQuote = true;
                                    break;
                                case 54: // check for '[a | c-e | g | h | j-o | q | r | u | v | x-z]'
                                    tokenList.add(new Token("ID", lineNum, currPosition, currChar));
                                    log("ID", currChar, lineNum, currPosition);
                                    break;
                            }
                        }
                        else if(finalState == 51)
                            openComment = false;

                    } else
                        //'CHAR' formatted token outputs for when open Quotes are detected
                        switch (currState) {
                            case 2: // char 'i'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 7: // char 't'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 11: // char 'f'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 16: // char 'p'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 23: // char 's'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 31: // char 'w'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 38: // char 'b'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 54: // char '[a | c-e | g | h | j-o | q | r | u | v | x-z]'
                                tokenList.add(new Token("CHAR", lineNum, currPosition, currChar));
                                log("CHAR", currChar, lineNum, currPosition);
                                break;
                            case 53: // char ' ' (space)
                                tokenList.add(new Token("CHAR", lineNum, currPosition, ' '));
                                log("CHAR", ' ', lineNum, currPosition);
                                break;
                            case 56: // char '"'
                                tokenList.add(new Token("QUOTE", lineNum, currPosition, currChar));
                                log("QUOTE", currChar, lineNum, currPosition);
                                openQuote = false;
                                break;
                        }
                }
            }
            // End of program statement/error summary
            System.out.println("INFO  Lexer - Lex completed with " + errors + " error(s)");

        }
    }

    // output function for char Tokens
    public static void log(String tokenType, char value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unexpected [ " + value + " ] found at (" + line + ":" + position + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + position + ")");
    }

    //output function for keyword tokens
    public static void log(String tokenType, String value, int line, int position){

        if (tokenType == "ERROR")
            System.out.println("ERROR Lexer - Unexpected [ " + value + " ] found at (" + line + ":" + position + ")");
        else
            System.out.println("DEGUG Lexer - " + tokenType + " [ " + value + " ] found at (" + line + ":" + position + ")");
    }

    // potential function to output stream after ArrayList is finished
    public static void logTokens(ArrayList<Token> tokenList){
        //if (token == "ERROR")
        {
            System.out.println();
        }
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
        int[] finalStates = {0, 2, 3, 5, 6, 7, 10, 11, 15, 16, 20, 22, 23, 28, 30, 31, 35, 36, 37, 38, 44, 45, 46, 47, 48, 51, 52, 53, 54, 56};
        for (int i = 0; i < finalStates.length; i++){
            if(state == finalStates[i])
                return true;
        }
        return false;
    }

}
