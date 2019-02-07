/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContextHandler
/*    */ {
/* 15 */   public static boolean PRINT = false;
/*    */   URI attributeID;
/*    */   
/*    */   public ContextHandler(URI attrID) {
/* 19 */     this.attributeID = attrID;
/*    */   }
/*    */   
/*    */   public ArrayList<Object> retrieve(XACMLContext context, int cat) throws URISyntaxException {
/*    */     try {
/* 24 */       if (PRINT) System.out.println("ContextHandler retrieve function.....");
/* 25 */       return context.returnValues(this.attributeID, cat);
/*    */     } catch (Exception e) {
/* 27 */       System.out.println("Exception in retrieve of ContextHandler " + e.getLocalizedMessage()); }
/* 28 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/ContextHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */