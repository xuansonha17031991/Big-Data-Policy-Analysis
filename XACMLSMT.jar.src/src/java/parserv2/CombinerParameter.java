/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CombinerParameter
/*    */ {
/*    */   String parameterName;
/*    */   Attribute att;
/*    */   
/*    */   public CombinerParameter(String paramName, Attribute at)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 49 */       this.parameterName = paramName;
/* 50 */       this.att = at;
/*    */     } catch (Exception e) {
/* 52 */       System.out.println("Exception in Constructor of CombinerParameter " + e.getLocalizedMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/CombinerParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */