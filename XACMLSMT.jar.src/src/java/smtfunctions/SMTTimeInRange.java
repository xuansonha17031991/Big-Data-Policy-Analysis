/*    */ package smtfunctions;
/*    */ 
/*    */ import com.microsoft.z3.ArithExpr;
/*    */ import com.microsoft.z3.BoolExpr;
/*    */ import com.microsoft.z3.Context;
/*    */ import com.microsoft.z3.Z3Exception;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
/*    */ import objects.Expression;
/*    */ 
/*    */ 
/*    */ public class SMTTimeInRange
/*    */   extends SMTFunction
/*    */ {
/*    */   public SMTTimeInRange(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params)
/*    */   {
/* 17 */     this.SMTparameters = params;
/* 18 */     this.parameters = paramExpressions;
/* 19 */     this.context = con;
/*    */     try {
/* 21 */       this.returnSort = this.context.getBoolSort();
/* 22 */       setSorts();
/*    */     } catch (Z3Exception zex) {
/* 24 */       System.err.println("Z3 error in SMTTimeInRange " + zex.getMessage());
/*    */     } catch (Exception ex) {
/* 26 */       System.err.println("Z3 error in SMTTimeInRange " + ex.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public SMTFunction.Z3FuncExpr generateExpr()
/*    */   {
/*    */     try
/*    */     {
/* 35 */       BoolExpr greaterThan = this.context.mkGe((ArithExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(0)).getPrincipalExpr(), (ArithExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(1)).getPrincipalExpr());
/* 36 */       BoolExpr lessThan = this.context.mkLe((ArithExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(0)).getPrincipalExpr(), (ArithExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(2)).getPrincipalExpr());
/* 37 */       return new SMTFunction.Z3FuncExpr(this.context.mkAnd(new BoolExpr[] { lessThan, greaterThan }), getAdditionalExprFromParams(), getCardExprFromParams());
/*    */     } catch (Z3Exception zex) {
/* 39 */       System.err.println("Exception in generateExpr of SMTArithmetic " + zex.getMessage());
/*    */     }
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTTimeInRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */