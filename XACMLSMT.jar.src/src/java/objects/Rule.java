/*    */ package objects;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Rule
/*    */   extends PolicyElement
/*    */ {
/*    */   private PolicyElement.Effect effect;
/*    */   private Expression con;
/*    */   
/*    */   public void setEffect(String value)
/*    */   {
/* 16 */     if (value.toUpperCase().equals(PolicyElement.Effect.Deny.toString().toUpperCase())) this.effect = PolicyElement.Effect.Deny;
/* 17 */     if (value.toUpperCase().equals(PolicyElement.Effect.Permit.toString().toUpperCase())) this.effect = PolicyElement.Effect.Permit;
/*    */   }
/*    */   
/* 20 */   public void setCondition(Expression c) { this.con = c; }
/*    */   
/* 22 */   public Expression getCondition() { return this.con; }
/*    */   
/*    */   public PolicyElement.Effect getEfect() {
/* 25 */     return this.effect;
/*    */   }
/*    */   
/*    */   public PolicyElement.Effect evalaute()
/*    */   {
/* 30 */     if (isApplicable()) return this.effect;
/* 31 */     return PolicyElement.Effect.NotApplicable;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String convert2String()
/*    */   {
/* 39 */     String str = new String("\n[RuleId:" + this.id + " --> " + this.effect + "]  " + (this.description != null ? "\nDesc:" + this.description + " " : "") + ((this.target != null) && (this.target.anyOfs.size() != 0) ? "\n[Target]" + this.target.convert2String() : "\nTarget:EMPTY\n") + (this.con != null ? this.con.convert2String() : ""));
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */     return str;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/objects/Rule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */