package com.company;


import java.util.ArrayList;

public class SemanticA {
    CST ast = new CST();
    int currentScope = -1;
    int error = 0;
    int warning = 0;
    int existance;
    String temp;
    String temp2;
    boolean flag;
    boolean flag1;
    String global;

    boolean isInitialized;
    boolean typeCheck;

    // Symbol Table is an array list of scope array lists
    ArrayList<ArrayList<Scope>> symbolTable = new ArrayList<ArrayList<Scope>>();

    public SemanticA(){

    }

    public void analyze(CSTNode n, int progNum){

        if (n.type.equals("branch")){
            switch (n.name){
                case "Program":
                    System.out.println("DEBUG Semantic - Program...");
                    break;
                case "Block":
                    currentScope++;
                    System.out.println("DEBUG Semantic - Block...");
                    symbolTable.add(new ArrayList<Scope>());
                    break;
                case "PrintStatement":
                    existance = exists(symbolTable, n.children.get(0).name, currentScope);
                    // if the id exists...check for initialization
                    if (existance != -1){
                        // change isUsed for id to true
                        use(symbolTable.get(existance), n.children.get(0).name);
                        if(isInitialized(symbolTable.get(existance), n.children.get(0).name)){
                            break;
                        }
                        else{
                            //print warning: var being used but never initialized
                            break;
                        }
                    }
                    break;
                case "VarDecl":
                    System.out.println("DEBUG Semantic - VarDecl...");
                    break;
                case "WhileStatement":
                    System.out.println("DEBUG Semantic - While Statement...");
                    break;
                case "IfStatement":
                    System.out.println("DEBUG Semantic - If Statement...");
                    break;
                case "AssignmentStatement":
                    System.out.println("DEBUG Semantic - Assignment Statement...");
                    break;
                    /*existance = exists(symbolTable, n.children.get(0).name, currentScope);
                    // if the id exists...check for initialization
                    if (existance != -1){
                        // check if second node
                        typeCheck = true;
                        switch (n.children.get(1).name){
                            case"Equality":
                            case"Inequality":
                            case"Addition":
                            case"":
                            default:
                        }
                        typeCheck(symbolTable.get(existance), n.children.get(0).name);
                    }
                    else{
                        //error, it does not exist
                        //error++;
                        System.out.println("ERROR already declared in current scope");
                    }*/
                case "Addition":
                    System.out.println("DEBUG Semantic - Addition...");
                    break;
                case "Equality":
                    System.out.println("DEBUG Semantic - Equality...");
                    break;
                case "Inequality":
                    System.out.println("DEBUG Semantic - Inequality...");
                    break;
            }
        }

        if (n.children.size() > 0){
            for(int j = 0; j < n.children.size(); j++){
                analyze(n.children.get(j), progNum);
            }
            // close scope
            switch (n.name){
                case "Block":
                    currentScope--;
                    break;
                case "AssignmentStatement":
                    // if the id exists...check for initialization
                    // check if var and type int else if check if digit.
                    if (isId(n.children.get(0).name)) {
                        //typeCheck(n.children.get(0).name);
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);

                        if (existance != -1) {
                            logValidVar(n.children.get(0).name);
                            System.out.println("has been initialzed at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");

                            // not the initialization of the ID
                            initialize(symbolTable.get(currentScope), n.children.get(0).name);
                        }
                        else{
                            // log for undeclared variable
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }

                        if (!n.children.get(1).equals("Addition")){
                            temp = getType(symbolTable.get(existance) ,n.children.get(0).name);
                            temp2 = getType(symbolTable.get(existance) ,n.children.get(1).name);
                            flag = typeCheck(temp, temp2, "Comparison");
                            if (flag){
                                System.out.println("VALID Semantic - Type check - ID [ " + n.children.get(0).name + " ] of type " + temp + " matches assignment type for [ " + n.children.get(1).name + " ] of type " + temp2);
                            }
                            else {
                                error++;
                                System.out.println("DEBUG Semantic - ERROR: Type mismatch with " + n.children.get(0).name + " and " + n.children.get(1).name);
                            }
                        }
                        else {
                            temp = getType(symbolTable.get(existance) ,n.children.get(0).name);
                            if (!temp.equals("int")){
                                error++;
                                System.out.println("DEBUG Semantic - ERROR: Type mismatch with " + n.children.get(0).name + " and type [ int ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            }
                        }
                    }
                    break;

                case "Addition":
                    // if the id exists...check for initialization
                    // check if var and type int else if check if digit.
                    if (isId(n.children.get(1).name)) {
                        //typeCheck(n.children.get(0).name);
                        existance = exists(symbolTable, n.children.get(1).name, currentScope);

                        if (existance != -1) {
                            logValidVar(n.children.get(1).name);
                            System.out.println("at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");

                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(1).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(1).name)) {
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(1).name + " ] found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ") is used but was never initialized");
                            }
                        }

                        else{
                            // log for undeclared variable
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }

                        if (!getType(symbolTable.get(currentScope), n.children.get(1).name).equals("int")){
                            // if second addition child is not of type int, log Type Mismatch Error
                            System.out.println("DEBUG Semantic - ERROR: Type mismatch found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") - ID [ " + n.children.get(0).name +  " ] of TYPE [and " + n.children.get(1).name);
                        }

                    }
                    else if (isDigit(n.children.get(1).name)){
                        break;
                    }
                    else if(n.children.get(1).name.equals("Addition")){
                        break;
                    }
                    else {
                        System.out.println("DEBUG Semantic - ERROR: Type mismatch - TYPE [ " + getType(symbolTable.get(currentScope), n.children.get(1).name) + " ] cannot be added to digits of type int");
                        break;
                    }
                    break;

                case "Equality":
                    flag1 = true;
                    if (isId(n.children.get(0).name)) {
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(0).name);
                            System.out.println("at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(0).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(0).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") is used but was never initialized");
                            }
                        }
                        else{
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }
                    }

                    if (isId(n.children.get(1).name)){
                        existance = exists(symbolTable, n.children.get(1).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(1).name);
                            System.out.println("at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(1).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(1).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(1).name + " ] found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ") is used but was never initialized");
                            }
                        }
                        else {
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(1).name + " ] found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");
                            break;
                        }
                    }

                    if (flag1){
                        temp = getType(symbolTable.get(existance) ,n.children.get(0).name);
                        temp2 = getType(symbolTable.get(existance) ,n.children.get(1).name);
                        flag = typeCheck(temp, temp2, "Comparison");
                        if (flag){
                            System.out.println("VALID Semantic - Type check with " + n.children.get(0).name + " and " + n.children.get(1).name);
                        }
                        else {
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Invalid type check with " + n.children.get(0).name + " and "+ n.children.get(1).name);
                        }
                    }
                    else
                        break;
                    /*else if (n.children.get(0).name.getClass().equals(String.class)){
                    }*/
                case "Inequality":
                    flag1 = true;
                    if (isId(n.children.get(0).name)) {
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(0).name);
                            System.out.println("at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(0).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(0).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") is used but was never initialized");
                            }
                        }
                        else{
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }
                    }

                    if (isId(n.children.get(1).name)){
                        existance = exists(symbolTable, n.children.get(1).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(1).name);
                            System.out.println("at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(1).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(1).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(1).name + " ] found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ") is used but was never initialized");
                            }
                        }
                        else {
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(1).name + " ] found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");
                            break;
                        }
                    }

                    if (flag1){
                        temp = getType(symbolTable.get(existance) ,n.children.get(0).name);
                        temp2 = getType(symbolTable.get(existance) ,n.children.get(1).name);
                        flag = typeCheck(temp, temp2, "Comparison");
                        if (flag){
                            System.out.println("VALID Semantic - Type check with " + n.children.get(0).name + " and "+n.children.get(1).name);
                        }
                        else {
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Invalid type check with " + n.children.get(0).name + " and "+n.children.get(1).name);
                        }
                    }

                    else
                        break;
                case"VarDecl":
                    // if var does exists, add to symbol table
                    if (!existsInScope(symbolTable, n.children.get(1).name, currentScope)) {
                        symbolTable.get(currentScope).add(new Scope(currentScope, n.children.get(0).name, n.children.get(1).name, n.children.get(1).lineNum, n.children.get(1).position ,false, false));
                        logValidVar(n.children.get(1).name);
                        System.out.println("of type " +  n.children.get(0).name + " has been declared at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ")");
                    }
                    else {
                        // if already exists throw error
                        existsWhere(symbolTable, n.children.get(1).name, currentScope);
                        error++;
                        System.out.println("DEBUG Semantic - ERROR: Duplicate ID [ " + n.children.get(1).name + " ] was found at (" + n.children.get(1).lineNum + ":" + n.children.get(1).position + ") - Original ID was previously declared in the current scope on line " + existsWhere(symbolTable, n.children.get(1).name, currentScope));
                    }
                    break;
                case "WhileStatement":
                    if (isId(n.children.get(0).name)) {
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(0).name);
                            System.out.println("at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(0).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(0).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") is used but was never initialized");
                            }
                        }
                        else{
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }
                    }
                case "IfStatement":
                    if (isId(n.children.get(0).name)) {
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(0).name);
                            System.out.println("at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope),n.children.get(0).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope),n.children.get(0).name)){
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") is used but was never initialized");
                            }
                        }
                        else{
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                        }
                    }
                case"PrintStatement":
                    if (isId(n.children.get(0).name)){
                        existance = exists(symbolTable, n.children.get(0).name, currentScope);
                        if (existance != -1) {
                            // log for declared variable
                            logValidVar(n.children.get(0).name);
                            System.out.println("at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            // mark that the ID has been used
                            use(symbolTable.get(currentScope), n.children.get(0).name);

                            // give warning if ID is used but hasn't been initialized
                            if (!isInitialized(symbolTable.get(currentScope), n.children.get(0).name)) {
                                warning++;
                                System.out.println("WARNING Semantic - ID [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ") is used but was never initialized");
                            }
                        }

                        else {
                            // log for undeclared variable
                            flag1 = false;
                            error++;
                            System.out.println("DEBUG Semantic - ERROR: Undeclared variable [ " + n.children.get(0).name + " ] found at (" + n.children.get(0).lineNum + ":" + n.children.get(0).position + ")");
                            break;
                        }
                    }
                    break;
            }
        }
    }

    public int errTotal(){
        return error;
    }

    public int warningTotal(){
        return warning;
    }

    public static boolean isId(String id){
        String[] abc = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        for (int i = 0; i < abc.length; i++){
            if (id.equals(abc[i]))
                return true;
        }
        return false;
    }

    public static boolean isDigit(String id){
        String[] num = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < num.length; i++){
            if (id.equals(num[i]))
                return true;
        }
        return false;
    }

    public static boolean isBoolean(String id){
        String[] bools = {"true","false"};
        for (int i = 0; i < bools.length; i++){
            if (id.equals(bools[i]))
                return true;
        }
        return false;
    }

    public void logWarning(){
        for (int i = 0; i < symbolTable.size(); i++){
            for (int j = 0; j < symbolTable.get(i).size(); j++){
                if (!symbolTable.get(i).get(j).isInit){
                    warning++;
                    System.out.println("DEBUG Semantic - WARNING: ID [ " + symbolTable.get(i).get(j).value + " ] was declared at (" + symbolTable.get(i).get(j).line + ":" + symbolTable.get(i).get(j).position + ") but was never initialized");
                }

                if (!symbolTable.get(i).get(j).isUsed){
                    warning++;
                    System.out.println("DEBUG Semantic - WARNING: ID [ " + symbolTable.get(i).get(j).value + " ] was declared at (" + symbolTable.get(i).get(j).line + ":" + symbolTable.get(i).get(j).position + ") but was never used");
                }
            }
        }
    }

    public void logSymbolTable(int x){
        System.out.println("Program " + x + " Symbol Table");
        System.out.println("----------------------------------------------");
        System.out.println("Name    Type    Scope    Line    Position");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < symbolTable.size(); i++){
            for (int j = 0; j < symbolTable.get(i).size(); j++){

                // add swtch case to output correct spacing
                switch (symbolTable.get(i).get(j).type){
                    case "int":
                        System.out.println(symbolTable.get(i).get(j).value + "       " + symbolTable.get(i).get(j).type + "     " + symbolTable.get(i).get(j).scope + "        " + symbolTable.get(i).get(j).line + "        " + symbolTable.get(i).get(j).position);
                        break;
                    case "string":
                        System.out.println(symbolTable.get(i).get(j).value + "       " + symbolTable.get(i).get(j).type + "  " + symbolTable.get(i).get(j).scope + "        " + symbolTable.get(i).get(j).line + "        " + symbolTable.get(i).get(j).position);
                        break;
                    case "boolean":
                        System.out.println(symbolTable.get(i).get(j).value + "       " + symbolTable.get(i).get(j).type + " " + symbolTable.get(i).get(j).scope + "        " + symbolTable.get(i).get(j).line + "        " + symbolTable.get(i).get(j).position);
                        break;
                }
            }
        }
    }

    // checks to see if var exists in program
    // if true, returns scope # of id occurrence in the nearest scope, else, return -1
    public static int exists(ArrayList<ArrayList<Scope>> table, String id, int scopeNum){
        for (int i = scopeNum; i >= 0; i--){
            for (int j = 0; j < table.get(i).size(); j++){
                if (table.get(i).get(j).value.equals(id)){
                    return i;
                }
            }
        }
        return -1;
    }

    // checks to see if var exists in specified scope
    public static boolean existsInScope(ArrayList<ArrayList<Scope>> table, String id, int scopeNum){
        for (int j = 0; j < table.get(scopeNum).size(); j++){
            if (table.get(scopeNum).get(j).value.equals(id)){
                return true;
            }
        }
        return false;
    }

    public static int existsWhere(ArrayList<ArrayList<Scope>> table, String id, int scopeNum){
        for (int j = 0; j < table.get(scopeNum).size(); j++){
            if (table.get(scopeNum).get(j).value.equals(id)){
                return table.get(scopeNum).get(j).line;
            }
        }
        return -1;
    }

    public static boolean isInitialized(ArrayList<Scope> scope, String id){
        for (int i = 0; i < scope.size(); i++){
            if (scope.get(i).value == id){
                if (scope.get(i).isInit == true){
                    return true;
                }
            }
        }
        return false;
    }

    public static String typeCheck(ArrayList<Scope> scope, String val){
        for (int i = 0; i < scope.size(); i++){
            if (scope.get(i).value == val) {
                switch (scope.get(i).type){
                    case"string":
                        //check if the id type is compatible with the
                        return "string";
                    case"int":
                        return "int";
                    case"boolean":
                        return "boolean";
                }
            }
        }
        return "null";
    }

    public static boolean typeCheck(String type1, String type2, String function){
        switch (function){
            case"Comparison":
                if (type1.equals(type2))
                    return true;
            case"AssignmentStatement":
                if (type1.equals(type2))
                    return true;
            case "IfStatement":
        }
        return true;
    }


    public static String getType(ArrayList<Scope> scope, String val){
        if (isId(val)){
            for (int i = 0; i < scope.size(); i++){
                if (scope.get(i).value.equals(val)) {
                    switch (scope.get(i).type){
                        case"string":
                            //check if the id type is compatible with the
                            return "string";
                        case"int":
                            return "int";
                        case"boolean":
                            return "boolean";
                    }
                }
            }
        }
        else if (isDigit(val)){
            return "int";
        }
        else if (isBoolean(val)){
            return "boolean";
        }
        else return "string";
        return "null";
    }


    public static void use(ArrayList<Scope> s, String id){
        for (int i = 0; i < s.size(); i++){
            if (s.get(i).value.equals(id)){
                s.get(i).isUsed = true;
                System.out.print("VALID Semantic - Variable [ " + id + " ] ");
            }
        }
    }

    public static void initialize(ArrayList<Scope> s, String id){
        for (int i = 0; i < s.size(); i++){
            if (s.get(i).value.equals(id)){
                s.get(i).isInit = true;
            }
        }
    }

    public static void logValidVar(String name)
    {
        System.out.print("VALID Semantic - ID [ " + name + " ] ");
    }
}

class Scope{
    String value;
    String type;
    int scope;
    int totalScope;
    int line;
    int position;
    boolean isInit;
    boolean isUsed;

    public Scope(){

    }

    public Scope(int scope, String type, String name, int line, int position ,boolean init, boolean used){
        this.scope = scope;
        this.type = type;
        this.value = name;
        this.line = line;
        this.position = position;
        this.isInit = init;
        this.isUsed = used;
    }
}

class ScopeList {

    //ArrayList<Scope> list = new ArrayList<>();
    Scope[] list = new Scope[26];
    int scopeNum;

    public ScopeList(int scope){
        //this.list;
        this.scopeNum = scope;
    }
}

