/*     */ package simplifiedparser;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;

/*     */ 
/*     */ import com.microsoft.z3.Context;
/*     */ import com.microsoft.z3.EnumSort;
/*     */ import com.microsoft.z3.Expr;
/*     */ import com.microsoft.z3.SetSort;
/*     */ import com.microsoft.z3.Symbol;
/*     */ import com.microsoft.z3.Z3Exception;

/*     */ import objects.Policy;
import objects.PolicyElement;
/*     */ import parserv3.IParser;
/*     */ import utils.Tree;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimplifiedParser
/*     */   implements IParser
/*     */ {
/*     */   Map<String, Expr> var2Expr;
/*     */   List<SetSort> listOfSorts;
/*     */   Context ctx;
/*     */   
/*     */   public SimplifiedParser()
/*     */   {
/*  37 */     this.var2Expr = new HashMap();
/*  38 */     this.listOfSorts = new ArrayList();
/*  39 */     initializeContext();
/*     */   }
/*     */   
/*     */   private void initializeContext() {
/*  43 */     HashMap<String, String> cfg = new HashMap();
/*     */     
/*     */ 
/*  46 */     cfg.put("model", "true");
/*  47 */     cfg.put("proof", "true");
/*     */     try
/*     */     {
/*  50 */       this.ctx = new Context(cfg);
/*     */     }
/*     */     catch (Z3Exception zex) {
/*  53 */       System.err.println("Z3Exception " + zex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private SetSort findSort(String id)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       for (Iterator<SetSort> it = this.listOfSorts.iterator(); it.hasNext();) {
/*  62 */         SetSort sort = (SetSort)it.next();
/*  63 */         if (sort.getName().equals(id)) return sort;
/*     */       }
/*     */     } catch (Z3Exception zex) { Iterator<SetSort> it;
/*  66 */       System.err.println("Z3Exception in sortExist " + zex.getMessage());
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   private Expr parseVariable(String str) {
/*  72 */     String varId = str.substring(0, str.indexOf(":") - 1).trim();
/*  73 */     String type = str.substring(str.indexOf(":") + 1, str.length()).trim();
/*     */     try {
/*  75 */       if (type.equals("integer")) {
/*  76 */         Expr ex = this.ctx.mkIntConst(varId);
/*  77 */         this.var2Expr.put(varId, ex);
/*  78 */         return ex; }
/*  79 */       if (type.equals("double")) {
/*  80 */         Expr ex = this.ctx.mkRealConst(varId);
/*  81 */         this.var2Expr.put(varId, ex);
/*  82 */         return ex;
/*     */       }
/*     */       
/*  85 */       if (type.startsWith("enum-")) {
/*  86 */         SetSort eSetSort = findSort(varId + "_sort");
/*  87 */         if (eSetSort == null) {
/*  88 */           String[] values = type.substring(type.indexOf("{") + 1, type.indexOf("}") - 1).split(",");
/*     */           
/*  90 */           Symbol eNames = this.ctx.mkSymbol(varId + "_sort");
/*  91 */           Symbol[] symbols = new Symbol[values.length];
/*  92 */           for (int i = 0; i < values.length; i++) symbols[i] = this.ctx.mkSymbol(values[i]);
/*  93 */           EnumSort eSort = this.ctx.mkEnumSort(eNames, symbols);
/*  94 */           eSetSort = this.ctx.mkSetSort(eSort);
/*  95 */           this.listOfSorts.add(eSetSort);
/*     */         }
/*  97 */         Expr ex = this.ctx.mkConst(varId, eSetSort);
/*  98 */         this.var2Expr.put(varId, ex);
/*  99 */         return ex;
/*     */       }
/*     */     } catch (Z3Exception zex) {
/* 102 */       System.err.println("Z3Exception is parseVariable " + zex.getMessage());
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private Tree parseExpression()
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   public Policy parsePolicy(String str)
/*     */   {
/* 115 */     String polId = str.substring(0, str.indexOf("[") - 1);
/* 116 */     PolicyElement.CombiningAlg alg = null;
/* 117 */     if (str.substring(str.indexOf("[") + 1, str.indexOf("]") - 1).equals("dov")) { alg = PolicyElement.CombiningAlg.DenyOverrides;
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public Policy parse(String fileName)
/*     */   {
/* 124 */     BufferedReader br = null;
/*     */     try
/*     */     {
/* 127 */       br = new BufferedReader(new FileReader(fileName));
/* 128 */       boolean variablesFlag = false;
/* 129 */       boolean policyFlag = false;
/* 130 */       boolean rulesFlag = false;
/* 131 */       String str = "";
/* 132 */       String sCurrentLine; while ((sCurrentLine = br.readLine()) != null) {
/* 133 */         if (variablesFlag) {
/* 134 */           parseVariable(sCurrentLine);
/* 135 */           if (sCurrentLine.endsWith("\\")) variablesFlag = false;
/* 136 */         } else if (policyFlag) {
/* 137 */           str = str + sCurrentLine + " ";
/* 138 */           if (sCurrentLine.endsWith("\\")) {
/* 139 */             policyFlag = false;
/* 140 */             System.out.println("Policy : " + str);
/* 141 */             str = "";
/*     */           }
/* 143 */         } else if (rulesFlag) {
/* 144 */           str = str + sCurrentLine;
/* 145 */           if (sCurrentLine.endsWith("\\")) {
/* 146 */             System.out.println("Rules : " + str);
/* 147 */             str = "";
/*     */           }
/*     */         }
/*     */         
/* 151 */         if (sCurrentLine.trim().toLowerCase().equals("variables")) variablesFlag = true;
/* 152 */         if (sCurrentLine.trim().toLowerCase().equals("policy")) policyFlag = true;
/* 153 */         if (sCurrentLine.trim().toLowerCase().equals("rules")) { rulesFlag = true;
/*     */         }
/*     */       }
/* 156 */       return new Policy();
/*     */     } catch (IOException e) {
/* 158 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 161 */         if (br != null) br.close();
/*     */       } catch (IOException ex) {
/* 163 */         ex.printStackTrace();
/*     */       }
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 195 */     SimplifiedParser sp = new SimplifiedParser();
/* 196 */     sp.parse("policies\\simplified\\simplified.txt");
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/simplifiedparser/SimplifiedParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */