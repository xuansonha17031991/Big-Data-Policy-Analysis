/*    */ package utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
import java.util.Map;
/*    */ 
/*    */ public class BreadthFirstTreeIterator implements Iterator<Node>
/*    */ {
/*    */   private static final int ROOT = 0;
/*    */   private LinkedList<Node> list;
/*    */   private HashMap<Integer, ArrayList<String>> levels;
/*    */   
/*    */   public BreadthFirstTreeIterator(HashMap<String, Node> tree, String identifier)
/*    */   {
/* 17 */     this.list = new LinkedList();
/* 18 */     this.levels = new HashMap();
/*    */     
/* 20 */     if (tree.containsKey(identifier)) {
/* 21 */       buildList(tree, identifier, 0);
/*    */       
/* 23 */       for (Map.Entry<Integer, ArrayList<String>> entry : this.levels.entrySet()) {
/* 24 */         for (Object child : (ArrayList)entry.getValue()) {
/* 25 */           this.list.add(tree.get(child));
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private void buildList(HashMap<String, Node> tree, String identifier, int level) {
/* 32 */     if (level == 0) {
/* 33 */       this.list.add(tree.get(identifier));
/*    */     }
/*    */     
/* 36 */     ArrayList<String> children = ((Node)tree.get(identifier)).getChildren();
/*    */     
/* 38 */     if (!this.levels.containsKey(Integer.valueOf(level))) {
/* 39 */       this.levels.put(Integer.valueOf(level), new ArrayList());
/*    */     }
/* 41 */     for (String child : children) {
/* 42 */       ((ArrayList)this.levels.get(Integer.valueOf(level))).add(child);
/*    */       
/*    */ 
/* 45 */       buildList(tree, child, level + 1);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 51 */     return !this.list.isEmpty();
/*    */   }
/*    */   
/*    */   public Node next()
/*    */   {
/* 56 */     return (Node)this.list.poll();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 61 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/BreadthFirstTreeIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */