/*    */ package solver;
/*    */ 
/*    */ import com.microsoft.z3.Expr;
/*    */ import com.microsoft.z3.FuncDecl;
/*    */ import com.microsoft.z3.Z3Exception;
/*    */ import com.microsoft.z3.enumerations.Z3_decl_kind;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormulaStatistics
/*    */ {
/*    */   public int getNumOfVars(Expr formula)
/*    */   {
/* 17 */     Vector<FuncDecl> vars = new Vector();
/* 18 */     numOfVars(formula, vars);
/* 19 */     for (int i = 0; i < vars.size(); i++)
/* 20 */       System.out.println(((FuncDecl)vars.get(i)).toString());
/* 21 */     return vars.size();
/*    */   }
/*    */   
/*    */   private boolean checkDouble(FuncDecl e, Vector<FuncDecl> vars)
/*    */   {
/*    */     try {
/* 27 */       for (int i = 0; i < vars.size(); i++) {
/* 28 */         if (((FuncDecl)vars.get(i)).getId() == e.getId()) return true;
/*    */       }
/*    */     } catch (Z3Exception zex) {
/* 31 */       System.err.println("Z3Exception in motExampleEncoding_AttributeHiding " + zex.getMessage());
/*    */     }
/* 33 */     return false;
/*    */   }
/*    */   
/*    */   private void numOfVars(Expr formula, Vector<FuncDecl> vars)
/*    */   {
/*    */     try {
/* 39 */       FuncDecl var = formula.getFuncDecl();
/*    */       
/* 41 */       if (var.getDeclKind() == Z3_decl_kind.Z3_OP_UNINTERPRETED) {
/* 42 */         if (!checkDouble(var, vars)) vars.add(var);
/*    */       } else {
/* 44 */         for (int i = 0; i < formula.getArgs().length; i++)
/* 45 */           numOfVars(formula.getArgs()[i], vars);
/*    */       }
/*    */     } catch (Z3Exception zex) {
/* 48 */       System.err.println("Z3Exception in motExampleEncoding_AttributeHiding " + zex.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/solver/FormulaStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */