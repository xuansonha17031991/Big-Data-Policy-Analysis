/*    */ package translator;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import objects.Policy;
/*    */ import simplifiedparser.SimplifiedParser;
/*    */ 
/*    */ public class SimplifiedPolicyLoader
/*    */ {
/*    */   public Policy parseSimplified(String fileName)
/*    */   {
/* 11 */     SimplifiedParser sp = new SimplifiedParser();
/* 12 */     Policy p = sp.parse(fileName);
/* 13 */     System.out.println(p.convert2String());
/* 14 */     return p;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/translator/SimplifiedPolicyLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */