/*    */ package smtfunctions;
/*    */ 
/*    */ import com.microsoft.z3.ArrayExpr;
/*    */ import com.microsoft.z3.BoolExpr;
/*    */ import com.microsoft.z3.Context;
/*    */ import com.microsoft.z3.EnumSort;
/*    */ import com.microsoft.z3.Expr;
/*    */ import com.microsoft.z3.SetSort;
/*    */ import com.microsoft.z3.Sort;
/*    */ import com.microsoft.z3.Z3Exception;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import objects.Expression;
/*    */ import utils.Pair;
/*    */ 
/*    */ 
/*    */ public class SMTIsIn
/*    */   extends SMTFunction
/*    */ {
/*    */   Sort enumSort;
/*    */   
/*    */   public SMTIsIn(String funcName, Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t, Map<String, Pair<SetSort, EnumSort>> listOfSorts)
/*    */   {
/* 25 */     this.SMTparameters = params;
/* 26 */     this.parameters = paramExpressions;
/* 27 */     this.context = con;
/*    */     try {
/* 29 */       this.returnSort = this.context.getBoolSort();
/* 30 */       this.enumSort = ((Sort)findSort(funcName.substring(0, funcName.indexOf("-")), listOfSorts, con).second);
/* 31 */       setSorts();
/*    */     } catch (Z3Exception zex) {
/* 33 */       System.err.println("Z3 error in SMTIsIn " + zex.getMessage());
/*    */     } catch (Exception ex) {
/* 35 */       System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   public SMTFunction.Z3FuncExpr generateExpr()
/*    */   {
/*    */     try
/*    */     {
/* 43 */       Expr intersect = this.context.mkSetIntersection(new ArrayExpr[] { (ArrayExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(0)).getPrincipalExpr(), (ArrayExpr)((SMTFunction.Z3FuncExpr)this.SMTparameters.get(1)).getPrincipalExpr() });
/* 44 */       Expr emptySet = this.context.mkEmptySet(this.enumSort);
/* 45 */       Expr expr = this.context.mkDistinct(new Expr[] { intersect, emptySet });
/* 46 */       return new SMTFunction.Z3FuncExpr((BoolExpr)expr, getAdditionalExprFromParams(), getCardExprFromParams());
/*    */     } catch (Z3Exception zex) {
/* 48 */       System.err.println("Exception in generateExpr of SMTIsIn " + zex.getMessage());
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTIsIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */