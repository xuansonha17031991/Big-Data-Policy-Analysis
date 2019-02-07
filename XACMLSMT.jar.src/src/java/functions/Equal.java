/*    */ package functions;
/*    */ 
/*    */ import java.util.Map;
/*    */ import objects.Expression;
/*    */ 
/*    */ public class Equal
/*    */   extends Function
/*    */   implements IFunction
/*    */ {
/*    */   Map<Expression, FunctionFactory.DataTypes> parameterMap;
/*    */   
/*    */   public Equal(String id)
/*    */   {
/* 14 */     setId(id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Boolean evaluate()
/*    */   {
/* 23 */     return Boolean.valueOf(true);
/*    */   }
/*    */   
/*    */   public String getDataType()
/*    */   {
/* 28 */     return FunctionFactory.DataTypes.xboolean.toString();
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/functions/Equal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */