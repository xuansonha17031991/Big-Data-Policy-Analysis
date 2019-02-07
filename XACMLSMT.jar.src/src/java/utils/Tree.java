/*    */ package utils;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class Tree
/*    */ {
/*    */   private static final int ROOT = 0;
/*    */   private HashMap<String, Node> nodes;
/*    */   private TraversalStrategy traversalStrategy;
/*    */   
/*    */   public static enum TraversalStrategy
/*    */   {
/* 14 */     DEPTH_FIRST, 
/* 15 */     BREADTH_FIRST;
/*    */     
/*    */ 
/*    */ 
/*    */     private TraversalStrategy() {}
/*    */   }
/*    */   
/*    */ 
/*    */   public Tree()
/*    */   {
/* 25 */     this(TraversalStrategy.DEPTH_FIRST);
/*    */   }
/*    */   
/*    */   public Tree(TraversalStrategy traversalStrategy) {
/* 29 */     this.nodes = new HashMap();
/* 30 */     this.traversalStrategy = traversalStrategy;
/*    */   }
/*    */   
/*    */   public HashMap<String, Node> getNodes()
/*    */   {
/* 35 */     return this.nodes;
/*    */   }
/*    */   
/*    */   public TraversalStrategy getTraversalStrategy() {
/* 39 */     return this.traversalStrategy;
/*    */   }
/*    */   
/*    */   public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
/* 43 */     this.traversalStrategy = traversalStrategy;
/*    */   }
/*    */   
/*    */   public Node addNode(String identifier)
/*    */   {
/* 48 */     return addNode(identifier, null);
/*    */   }
/*    */   
/*    */   public Node addNode(String identifier, String parent) {
/* 52 */     Node node = new Node(identifier);
/* 53 */     this.nodes.put(identifier, node);
/*    */     
/* 55 */     if (parent != null) {
/* 56 */       ((Node)this.nodes.get(parent)).addChild(identifier);
/*    */     }
/*    */     
/* 59 */     return node;
/*    */   }
/*    */   
/*    */   public void display(String identifier) {
/* 63 */     display(identifier, 0);
/*    */   }
/*    */   
/*    */   public void display(String identifier, int depth) {
/* 67 */     java.util.ArrayList<String> children = ((Node)this.nodes.get(identifier)).getChildren();
/*    */     String tabs;
/* 69 */     if (depth == 0) {
/* 70 */       System.out.println(((Node)this.nodes.get(identifier)).getIdentifier());
/*    */     }
/*    */     else {
/* 73 */       tabs = String.format("%0" + depth + "d", new Object[] { Integer.valueOf(0) }).replace("0", "    ");
/* 74 */       System.out.println(tabs + ((Node)this.nodes.get(identifier)).getIdentifier());
/*    */     }
/*    */     
/* 77 */     depth++;
/* 78 */     for (String child : children)
/*    */     {
/*    */ 
/* 81 */       display(child, depth);
/*    */     }
/*    */   }
/*    */   
/*    */   public Iterator<Node> iterator(String identifier) {
/* 86 */     return iterator(identifier, this.traversalStrategy);
/*    */   }
/*    */   
/*    */   public Iterator<Node> iterator(String identifier, TraversalStrategy traversalStrategy) {
/* 90 */     return traversalStrategy == TraversalStrategy.BREADTH_FIRST ? new BreadthFirstTreeIterator(this.nodes, identifier) : new DepthFirstTreeIterator(this.nodes, identifier);
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/Tree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */