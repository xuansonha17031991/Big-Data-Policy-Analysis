/*     */ package parserv2;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombiningAlg
/*     */   extends XACMLContainer
/*     */ {
/*  13 */   public static boolean PRINT = false;
/*     */   
/*     */   public ArrayList<XACMLContainer> elements;
/*     */   public int algorithm;
/*     */   
/*     */   public CombiningAlg()
/*     */   {
/*  20 */     this.elements = new ArrayList();
/*     */   }
/*     */   
/*     */   public CombiningAlg(String alg)
/*     */   {
/*  25 */     if (PRINT) System.out.println();
/*  26 */     if (alg.indexOf("permit-overrides") != -1) this.algorithm = 1;
/*  27 */     if (alg.indexOf("deny-overrides") != -1) this.algorithm = 2;
/*  28 */     if (alg.indexOf("ordered-permit-overrides") != -1) this.algorithm = 3;
/*  29 */     if (alg.indexOf("ordered-deny-overrides") != -1) this.algorithm = 4;
/*  30 */     if (alg.indexOf("first-applicable") != -1) this.algorithm = 5;
/*  31 */     if (alg.indexOf("only-one-applicable") != -1) this.algorithm = 6;
/*     */   }
/*     */   
/*     */   public String getAlgorithmText(int type) {
/*  35 */     switch (this.algorithm) {
/*     */     case 1: 
/*  37 */       if (type == 2)
/*  38 */         return "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:permit-overrides";
/*  39 */       return "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides";
/*     */     case 2: 
/*  41 */       if (type == 2) {
/*  42 */         return "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides";
/*     */       }
/*  44 */       return "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides";
/*     */     case 3: 
/*  46 */       if (type == 2) {
/*  47 */         return "urn:oasis:names:tc:xacml:1.1:policy-combining-algorithm:ordered-permit-overrides";
/*     */       }
/*  49 */       return "urn:oasis:names:tc:xacml:1.1:rule-combining-algorithm:ordered-permit-overrides";
/*     */     case 4: 
/*  51 */       if (type == 2) {
/*  52 */         return "urn:oasis:names:tc:xacml:1.1:policy-combining-algorithm:ordered-deny-overrides";
/*     */       }
/*  54 */       return "urn:oasis:names:tc:xacml:1.1:rule-combining-algorithm:ordered-deny-overrides";
/*     */     case 5: 
/*  56 */       if (type == 2) {
/*  57 */         return "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:first-applicable";
/*     */       }
/*  59 */       return "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable";
/*     */     case 6: 
/*  61 */       return "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:only-one-applicable";
/*     */     }
/*     */     
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   public XACMLContainer returnWinner()
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  72 */       XACMLContainer res = null;
/*     */       
/*  74 */       if (this.elements.size() < 1) { return new Rule(XACMLContainer.Result.NotApplicable, null);
/*     */       }
/*  76 */       switch (this.algorithm) {
/*     */       case 1: 
/*  78 */         if (PRINT) System.out.println("Number of elements for conflict resolution " + this.elements.size());
/*  79 */         for (int i = this.elements.size() - 1; i >= 0; i++) {
/*  80 */           Rule rule = (Rule)this.elements.get(i);
/*  81 */           if (PRINT) System.out.println("Result from " + rule.Id + " is " + rule.effect.toString());
/*  82 */           if (rule.effect.equals(XACMLContainer.Result.Permit)) return rule; }
/*  83 */         return new Rule(XACMLContainer.Result.Deny, null);
/*     */       
/*     */       case 2: 
/*  86 */         if (PRINT) System.out.println("Number of elements for conflict resolution " + this.elements.size());
/*  87 */         for (int i = this.elements.size() - 1; i >= 0; i++) {
/*  88 */           Rule rule = (Rule)this.elements.get(i);
/*  89 */           if (PRINT) System.out.println("Result from " + rule.Id + " is " + rule.effect.toString());
/*  90 */           if (rule.effect.equals(XACMLContainer.Result.Deny)) return rule; }
/*  91 */         return new Rule(XACMLContainer.Result.Permit, null);
/*     */       
/*     */       case 3: 
/*  94 */         if (PRINT) System.out.println("Number of elements for conflict resolution " + this.elements.size());
/*  95 */         for (int i = this.elements.size() - 1; i >= 0; i++) {
/*  96 */           Rule rule = (Rule)this.elements.get(i);
/*  97 */           if (PRINT) System.out.println("Result from " + rule.Id + " is " + rule.effect.toString());
/*  98 */           if (rule.effect.equals(XACMLContainer.Result.Permit)) return rule; }
/*  99 */         return new Rule(XACMLContainer.Result.Deny, null);
/*     */       
/*     */       case 4: 
/* 102 */         if (PRINT) System.out.println("Number of elements for conflict resolution " + this.elements.size());
/* 103 */         for (int i = this.elements.size() - 1; i >= 0; i++) {
/* 104 */           Rule rule = (Rule)this.elements.get(i);
/* 105 */           if (PRINT) System.out.println("Result from " + rule.Id + " is " + rule.effect.toString());
/* 106 */           if (rule.effect.equals(XACMLContainer.Result.Deny)) return rule; }
/* 107 */         return new Rule(XACMLContainer.Result.Permit, null);
/*     */       case 5: 
/* 109 */         return (Rule)this.elements.get(0);
/*     */       case 6: 
/* 111 */         int count = 0;int index = 0;
/* 112 */         for (int i = 0; i < this.elements.size(); i++) {
/* 113 */           if (!((Rule)this.elements.get(i)).effect.equals(XACMLContainer.Result.NotApplicable)) { index = i;count++;
/*     */           }
/*     */         }
/* 116 */         if (count > 1) return new Rule(XACMLContainer.Result.Deny, null);
/* 117 */         if (count == 1) return (XACMLContainer)this.elements.get(index);
/* 118 */         return (Rule)this.elements.get(0);
/*     */       }
/*     */       
/* 121 */       return res;
/*     */     } catch (Exception e) {
/* 123 */       System.out.println("Exception in returnWinner of CombiningAlg " + e.getMessage()); }
/* 124 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<XACMLContainer> returnElements()
/*     */   {
/* 132 */     if (PRINT) System.out.println("Number of elements for conflict resolution " + this.elements.size());
/* 133 */     for (Iterator<XACMLContainer> elem = this.elements.iterator(); elem.hasNext();) {
/* 134 */       Rule rule = (Rule)elem.next();
/* 135 */       if (PRINT) System.out.println("Result from " + rule.Id + " is " + rule.effect.toString());
/*     */     }
/* 137 */     return this.elements;
/*     */   }
/*     */   
/*     */   public boolean insertRule(XACMLContainer con) throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       switch (this.algorithm) {
/* 145 */       case 1:  if (PRINT) System.out.println("Overrides Permit For Rule"); return overridesForRule(con, 1);
/* 146 */       case 2:  if (PRINT) System.out.println("Overrides Deny For Rule"); return overridesForRule(con, 2);
/* 147 */       case 3:  if (PRINT) System.out.println("Ordered Permit Overrides For Rule"); return ordered_overridesForRule(con, 1);
/* 148 */       case 4:  if (PRINT) System.out.println("Ordered Deny Overrides FOR RULE"); return ordered_overridesForRule(con, 2);
/* 149 */       case 5:  if (PRINT) System.out.println("First Applicable"); return firstApplicable(con);
/* 150 */       case 6:  if (PRINT) System.out.println("Only One Applicable"); return onlyOneApplicable(con);
/*     */       }
/* 152 */       return false;
/*     */     } catch (Exception e) {
/* 154 */       System.out.println("Exception in insertRule of CombiningAlg" + e.getMessage()); }
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public boolean overridesForRule(XACMLContainer con, int alg)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/* 163 */       if (((alg == 1) && (((Rule)con).effect.equals(XACMLContainer.Result.Permit))) || ((alg == 2) && 
/* 164 */         (((Rule)con).effect.equals(XACMLContainer.Result.Deny)))) {
/* 165 */         this.elements.add(con);
/* 166 */         return true;
/*     */       }
/* 168 */       return false;
/*     */     } catch (Exception e) {
/* 170 */       System.out.println("Exception in overridesForRule " + e.getLocalizedMessage()); }
/* 171 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean ordered_overridesForRule(XACMLContainer con, int decision)
/*     */   {
/* 182 */     if (decision == 1) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean firstApplicable(XACMLContainer con)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/* 197 */       if (con != null)
/*     */       {
/* 199 */         Rule r = (Rule)con;
/* 200 */         this.elements.add(r);
/* 201 */         if (PRINT) System.out.println("Result from " + r.Id + " is " + r.effect.toString());
/* 202 */         return true; }
/* 203 */       return false;
/*     */     } catch (Exception e) {
/* 205 */       System.out.println("Exception in firstApplicable " + e.getMessage()); }
/* 206 */     return false;
/*     */   }
/*     */   
/*     */   public boolean onlyOneApplicable(XACMLContainer con)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/* 214 */       if (con != null) {
/* 215 */         this.elements.add(con);
/* 216 */         if (PRINT) System.out.println("Only one Applicable: Number of elements for conflict resolution " + this.elements.size());
/* 217 */         int size = this.elements.size();
/* 218 */         if (size == 1) return true;
/* 219 */         return false; }
/* 220 */       return false;
/*     */     } catch (Exception e) {
/* 222 */       System.out.println("Exception in onlyOneApplicable " + e.getLocalizedMessage()); }
/* 223 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/CombiningAlg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */