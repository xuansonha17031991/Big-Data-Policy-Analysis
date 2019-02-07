/*    */ package parserv2;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConFunctionResult
/*    */ {
/*    */   static ArrayList<String> dataTypes;
/*    */   public ArrayList<Object> result;
/*    */   public ArrayList<Object> dataType;
/*    */   
/*    */   public ConFunctionResult(int size)
/*    */   {
/* 17 */     initialize();
/*    */   }
/*    */   
/*    */   public ConFunctionResult(ArrayList<Object> val, ArrayList<Object> type) {
/* 21 */     initialize();
/* 22 */     this.result = val;
/* 23 */     this.dataType = type;
/*    */   }
/*    */   
/*    */   public static void initialize() {
/* 27 */     dataTypes = new ArrayList();
/* 28 */     dataTypes.add("boolean");
/* 29 */     dataTypes.add("integer");
/* 30 */     dataTypes.add("double");
/* 31 */     dataTypes.add("datetime");
/* 32 */     dataTypes.add("date");
/* 33 */     dataTypes.add("string");
/* 34 */     dataTypes.add("anyuri");
/*    */   }
/*    */   
/*    */   public static String findType(URI type)
/*    */   {
/* 39 */     if (dataTypes == null) initialize();
/* 40 */     for (int i = 0; i < dataTypes.size(); i++) {
/* 41 */       if (type.toString().toLowerCase().indexOf((String)dataTypes.get(i)) != -1) {
/* 42 */         return (String)dataTypes.get(i);
/*    */       }
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public Object returnResult() {
/* 49 */     return this.result;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/ConFunctionResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */