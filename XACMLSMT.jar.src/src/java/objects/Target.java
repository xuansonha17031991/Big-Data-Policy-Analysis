/*    */ package objects;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Target
/*    */ {
/*    */   public List<List<List<Expression>>> anyOfs;
/*    */   
/*    */   public Target()
/*    */   {
/* 13 */     this.anyOfs = new ArrayList();
/*    */   }
/*    */   
/*    */   public void addAnyOf(List<List<Expression>> allOfs) {
/* 17 */     this.anyOfs.add(allOfs);
/*    */   }
/*    */   
/*    */   public List<List<List<Expression>>> getAnyOfs() {
/* 21 */     return this.anyOfs;
/*    */   }
/*    */   
/*    */   public String convert2String() {
/* 25 */     String str = new String();
/* 26 */     for (Iterator<List<List<Expression>>> itAnyOfs = this.anyOfs.iterator(); itAnyOfs.hasNext();) {
/* 27 */       List<List<Expression>> anyOf = (List)itAnyOfs.next();
/* 28 */       for (Iterator<List<Expression>> itAllOfs = anyOf.iterator(); itAllOfs.hasNext();) {
/* 29 */         List<Expression> attMatches = (List)itAllOfs.next();
/* 30 */         for (Iterator<Expression> itAttMatch = attMatches.iterator(); itAttMatch.hasNext();) {
/* 31 */           Expression attMatch = (Expression)itAttMatch.next();
/* 32 */           str = str + attMatch.convert2String() + "\n";
/*    */         } } }
/*    */     Iterator<List<Expression>> itAllOfs;
/*    */     Iterator<Expression> itAttMatch;
/* 36 */     return str;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/objects/Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */