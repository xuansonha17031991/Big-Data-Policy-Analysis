/*    */ package functions;
/*    */ 
/*    */ public class DummyFunction
/*    */   extends Function
/*    */   implements IFunction
/*    */ {
/*  7 */   boolean userDefined = false;
/*    */   
/*    */   public DummyFunction(String id, boolean udef) {
/* 10 */     setId(id);
/* 11 */     this.userDefined = udef;
/*    */   }
/*    */   
/* 14 */   public boolean isUserDefined() { return this.userDefined; }
/*    */   
/*    */   public Boolean evaluate() {
/* 17 */     return Boolean.valueOf(true);
/*    */   }
/*    */   
/*    */   public String getDataType()
/*    */   {
/* 22 */     return FunctionFactory.DataTypes.xboolean.toString();
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/functions/DummyFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */