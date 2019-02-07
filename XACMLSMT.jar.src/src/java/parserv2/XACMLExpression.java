/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XACMLExpression
/*    */ {
/*    */   Node n;
/* 12 */   public static boolean PRINT = false;
/*    */   
/*    */   public Apply apply;
/*    */   
/*    */   public AttributeSelector atts;
/*    */   public Attribute att;
/*    */   public String variableId;
/*    */   public AttributeDesignator ad;
/*    */   public int type;
/*    */   
/*    */   public XACMLExpression()
/*    */   {
/* 24 */     this.apply = null;
/* 25 */     this.atts = null;
/* 26 */     this.att = null;
/* 27 */     this.variableId = null;
/* 28 */     this.ad = null;
/* 29 */     this.type = 0;
/*    */   }
/*    */   
/*    */   public XACMLExpression(Node nd) {
/* 33 */     this.n = nd;
/* 34 */     this.type = 0;
/*    */   }
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
/*    */   public XACMLExpression(String value, String dataType)
/*    */   {
/* 68 */     this.type = 7;
/*    */   }
/*    */   
/*    */ 
/*    */   public ConFunctionResult evaluate(XACMLContext context)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 77 */       ConFunctionResult returnValue = null;
/*    */       
/*    */ 
/* 80 */       switch (this.type) {
/*    */       case 1: 
/*    */         break; case 2:  if (PRINT) System.out.println("APPLY FOUND IN EXPRESSION"); returnValue = this.apply.executeFunction(context); break;
/*    */       case 3: 
/*    */         break;
/*    */       case 4: 
/*    */         break;
/*    */       case 5: 
/*    */         break; case 6:  break; }
/* 89 */       return returnValue;
/*    */     } catch (Exception e) {
/* 91 */       System.out.println("Exception in evaluate of Expression" + e.getMessage()); }
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/XACMLExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */