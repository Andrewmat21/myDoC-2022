/*
References:
For help with depth first tree traversal I used baedung.com/java-depth-first-search
*/

/*
Andrew Mathew
Dr. Labouseur
CMPT 432
Project 2
*/
package com.company;
import java.util.ArrayList;

    class CSTNode {

    ArrayList<CSTNode> children = new ArrayList<CSTNode>();
    CSTNode parent = null;
    String type;
    String name;
    int lineNum;

    public CSTNode(){}

    public CSTNode(String tokenType, String tokenName) {
        //this.root = null;
        //this.current = null;
        this.type = tokenType;
        this.name = tokenName;
        this.parent = null;
    }

    public CSTNode(String tokenType, String tokenName, int line) {
        this.type = tokenType;
        this.name = tokenName;
        this.lineNum = line;
        this.parent = null;
    }
}

    public class CST{
        CSTNode root = null;
        CSTNode current = null;

        public CST(){
            this.root = null;
            this.current = null;

        }

        public void addNode(String nodeType, String nTerm){
            CSTNode node = new CSTNode(nodeType, nTerm);
            if (this.root == null){
                this.root = node;
                //node.parent = null;
            }
            else{
                node.parent = this.current;
                this.current.children.add(node);
            }
            if (nodeType != "leaf")
                this.current = node;
        }

        // prints CST
        public void logCST(int length, CSTNode n){
            String lengthIn = "";

            for(int i = 0; i < length; i++){
                lengthIn += "-";
            }

            //if node has children, print Non Term name
            if (n.children.size() > 0){
                lengthIn += "<" + n.name + ">";
                System.out.println(lengthIn);

                for (int j = 0; j < n.children.size(); j++){
                    logCST(length+1 ,n.children.get(j));
                }
            }

            else {
                // handles bug where node with no children isnt printed as a terminal
                if (n.type != "branch") {
                    lengthIn += "[ " + n.name + " ]";
                    System.out.println(lengthIn);
                }
            }
        }

        public void addANode(String nodeType, String nTerm){
            CSTNode node = new CSTNode(nodeType, nTerm);
            if (this.root == null){
                this.root = node;
                //node.parent = null;
            }
            else{
                node.parent = this.current;
                this.current.children.add(node);
            }
            if (nodeType != "leaf")
                this.current = node;
        }

        // function for printing AST
        public void logAST(int length, CSTNode n){
            String lengthIn = "";

            for(int i = 0; i < length; i++){
                lengthIn += "-";
            }

            //if node has children, print Non Term name
            if (n.children.size() > 0){
                lengthIn += "<" + n.name + ">";
                System.out.println(lengthIn);
            }

            else {
                // handles bug where node with no children isnt printed as a terminal
                if (n.type != "branch") {
                    lengthIn += "[ " + n.name + " ]";
                    System.out.println(lengthIn);
                }

                else{
                    lengthIn += "< " + n.name + " >";
                    System.out.println(lengthIn);
                }
            }

            if (n.children.size() > 0){
                for(int j = 0; j < n.children.size(); j++){
                    logAST(length+1, n.children.get(j));
                }
            }
        }

        //put an end to the children
        public void moveUp(){
            this.current = this.current.parent;
        }
}
