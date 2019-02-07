/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XACMLContainer
/*    */ {
/* 11 */   public static boolean PRINT = false;
/*    */   public String Id;
/*    */   
/* 14 */   public static enum Result { Permit,  Deny,  NotApplicable,  Indeterminate;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     private Result() {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String description;
/*    */   
/*    */ 
/*    */ 
/*    */   public Target target;
/*    */   
/*    */ 
/*    */ 
/*    */   public XACMLContainer winner;
/*    */   
/*    */ 
/*    */ 
/*    */   public String parentId;
/*    */   
/*    */ 
/*    */   public void print() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isMatched(XACMLContext context)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 49 */       boolean result = false;
/* 50 */       if (PRINT) { System.out.println(this.Id + " is checked for match");
/*    */       }
/* 52 */       if ((this.target == null) || (this.target.isMatched(context)))
/* 53 */         if (PRINT) System.out.println("Target of " + this.Id + " is matched");
/* 54 */       return true;
/*    */ 
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 59 */       System.out.println("Exception in isMatched of XACMLContainer " + e.getMessage()); }
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   public XACMLContainer evaluate(XACMLContext context) throws URISyntaxException
/*    */   {
/* 65 */     this.winner = evaluate(context);
/* 66 */     return this.winner;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/XACMLContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */