package com.company;


import java.util.ArrayList;

public class SemanticA {
    CST ast = new CST();
    int currentScope = -1;
    int error = 0;
    int warning = 0;
    int existance;

    boolean isInitialized;

    ArrayList<ArrayList<Scope>> symbolTable = new ArrayList<ArrayList<Scope>>();

    public SemanticA(){

    }

    public void analyze(CSTNode n, int progNum){

        switch (n.name){
            case "Block":
                currentScope++;
                symbolTable.add(new ArrayList<Scope>());
                break;
            case "PrintStatement":
                analyze(n.children.get(2), progNum);
                existance = exists(symbolTable, n.children.get(0).name, currentScope);
                if (existance != -1)
                    if(isInitialized(symbolTable.get(existance), n.children.get(0).name))
                        break;
                    else{
                        //print warning: var being used but never initialized
                        break;
                    }
                break;
            case "VarDecl":
                //analyze(n.children.get(1), progNum);
                // check if it exists
                // if not, add to scope list under currentScope
                if (!(existsInScope(symbolTable, n.children.get(1).name, currentScope)))
                    symbolTable.get(currentScope).add(new Scope(currentScope, n.children.get(0).name, n.children.get(1).name, n.children.get(1).lineNum, false, false));
                break;
            case "WhileStatement":
                analyze(n.children.get(2), progNum);
                existance = exists(symbolTable, n.children.get(0).name, currentScope);
                if (existance != -1)
                    if(isInitialized(symbolTable.get(existance), n.children.get(0).name))
                        break;
                    else{
                        //print warning: var being used but never initialized
                        break;
                    }
                break;
            case "IfStatement":
            case "AssignmentStatement":
            case "Id":
                for (int i = 0; i < currentScope; i++){
                //symbolTable.get(currentScope).list[0] = new Scope(currentScope, n.name, n.);
                // case "":
            }
        }

        if (n.children.size() > 0){
            for(int j = 0; j < n.children.size(); j++){
                analyze(n.children.get(j), progNum);
            }
        }

        this.logSymbolTable(progNum);
    }

    public void checkId(){
        String[] abc = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    }

    public void logSymbolTable(int x){
        System.out.println("Program " + x + " Symbol Table");
        System.out.println("----------------------------------------------");
        System.out.println("Name Type   Scope   Line");
        System.out.println("----------------------------------------------");
        for (int i = 0; i < symbolTable.size(); i++){
            System.out.println();
        }
    }

    public static int exists(ArrayList<ArrayList<Scope>> table, String id, int scopeNum){
        for (int i = scopeNum; i >= 0; i--){
            for (int j = 0; j < table.get(i).size(); j++){
                if (table.get(i).get(j).value == id){
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean existsInScope(ArrayList<ArrayList<Scope>> table, String id, int scopeNum){
        for (int j = 0; j < table.get(scopeNum).size(); j++){
            if (table.get(scopeNum).get(j).value == id){
                return true;
            }
        }
        return false;
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
}

class Scope{
    String value;
    String type;
    int currentScope;
    int totalScope;
    int line;
    boolean isInit;
    boolean isUsed;

    public Scope(){

    }

    public Scope(int scope, String type, String name, int line, boolean init, boolean used){
        this.currentScope = scope;
        this.type = type;
        this.value = name;
        this.line = line;
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

