/*    */ package utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Node
/*    */ {
/*    */   private String identifier;
/*    */   private ArrayList<String> children;
/*    */   
/*    */   public Node(String identifier)
/*    */   {
/* 15 */     this.identifier = identifier;
/* 16 */     this.children = new ArrayList();
/*    */   }
/*    */   
/*    */   public String getIdentifier()
/*    */   {
/* 21 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public ArrayList<String> getChildren() {
/* 25 */     return this.children;
/*    */   }
/*    */   
/*    */   public void addChild(String identifier)
/*    */   {
/* 30 */     this.children.add(identifier);
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */