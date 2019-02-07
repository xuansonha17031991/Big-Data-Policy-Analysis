/*    */ package combiningalg;
/*    */ 
/*    */ import com.microsoft.z3.Context;
/*    */ import policy.SMTPolicyElement;
/*    */ 
/*    */ 
/*    */ public class SMTAlgFactory
/*    */ {
/*    */   public static enum SMTCombiningAlg
/*    */   {
/* 11 */     PermitOverrides,  DenyOverrides,  FirstApplicable,  OnlyOneApplicable, 
/* 12 */     DenyUnlessPermit,  PermitUnlessDeny;
/*    */     
/*    */     private SMTCombiningAlg() {}
/*    */   }
/*    */   
/* 17 */   public ISMTCombAlg getAlgorithm(SMTCombiningAlg alg, SMTPolicyElement[] eList, Context con, int v) { switch (alg) {
/*    */     case DenyOverrides: 
/* 19 */       return new SMTDenyOverrides(eList, con, v);
/*    */     case PermitOverrides: 
/* 21 */       return new SMTPermitOverrides(eList, con, v);
/*    */     case FirstApplicable: 
/* 23 */       return new SMTFirstApplicable(eList, con, v);
/*    */     case DenyUnlessPermit: 
/* 25 */       return new SMTDenyUnlessPermit(eList, con, v);
/*    */     case PermitUnlessDeny: 
/* 27 */       return new SMTPermitUnlessDeny(eList, con, v);
/*    */     }
/* 29 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/SMTAlgFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */