package com.company;
import java.util.ArrayList;

    class CSTNode {

    ArrayList<CSTNode> children = new ArrayList<CSTNode>();
    CSTNode parent = null;
    String type;
    String name;

    public CSTNode(String tokenType, String tokenName) {
        //this.root = null;
        //this.current = null;
        this.type = tokenType;
        this.name = tokenName;
        this.parent = null;
    }

    public void endChildren() {

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
                node.parent = null;
            }
            else{
                node.parent = this.current;
                node.parent.children.add(node);
            }
            if (nodeType != "leaf")
                this.current = node;
        }

        public void logCST(int length, CSTNode n){
            String lengthIn = "";

            for(int i = 0; i < length; i++){
                lengthIn += "-";
            }

            if (n.children.size() > 0){
                lengthIn += "<" + n.type + ">";
                System.out.println(lengthIn);

                for (int j = 0; j < n.children.size(); j++){
                    logCST(length + 1,n.children.get(j));
                }
            }

            else {
                lengthIn += "[ " + n.name + " ]";
                System.out.println(lengthIn);
            }
        }

        public void moveUp(){
            this.current = this.current.parent;
        }
}
