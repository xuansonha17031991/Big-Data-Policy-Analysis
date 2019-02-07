/*    */ package utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ 
/*    */ public class DepthFirstTreeIterator
/*    */   implements Iterator<Node>
/*    */ {
/*    */   private LinkedList<Node> list;
/*    */   
/*    */   public DepthFirstTreeIterator(HashMap<String, Node> tree, String identifier)
/*    */   {
/* 16 */     this.list = new LinkedList();
/*    */     
/* 18 */     if (tree.containsKey(identifier)) {
/* 19 */       buildList(tree, identifier);
/*    */     }
/*    */   }
/*    */   
/*    */   private void buildList(HashMap<String, Node> tree, String identifier) {
/* 24 */     this.list.add(tree.get(identifier));
/* 25 */     ArrayList<String> children = ((Node)tree.get(identifier)).getChildren();
/* 26 */     for (String child : children)
/*    */     {
/*    */ 
/* 29 */       buildList(tree, child);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 35 */     return !this.list.isEmpty();
/*    */   }
/*    */   
/*    */   public Node next()
/*    */   {
/* 40 */     return (Node)this.list.poll();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 45 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/DepthFirstTreeIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */