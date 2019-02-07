/*     */ package parserv2;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Policy
/*     */   extends XACMLContainer
/*     */ {
/*     */   String version;
/*     */   ArrayList<Rule> rules;
/*     */   CombiningAlg rcg;
/*     */   URI policySetDefaults;
/*     */   CombinerParameters combParams;
/*     */   public XACMLContainer.Result decision;
/*     */   
/*     */   public Policy(Node policy)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  35 */       NamedNodeMap nnm = policy.getAttributes();
/*  36 */       this.rules = new ArrayList();
/*  37 */       this.Id = nnm.getNamedItem("PolicyId").getNodeValue();
/*  38 */       this.rcg = new CombiningAlg(nnm.getNamedItem("RuleCombiningAlgId").getNodeValue());
/*  39 */       this.version = (nnm.getNamedItem("Version") != null ? nnm.getNamedItem("Version").getNodeValue() : null);
/*     */       
/*     */ 
/*  42 */       NodeList nl = policy.getChildNodes();
/*  43 */       for (int i = 0; i < nl.getLength(); i++) {
/*  44 */         Node item = nl.item(i);
/*     */         
/*  46 */         if (item.getNodeName().toLowerCase().equals("description")) {
/*  47 */           this.description = item.getTextContent().trim();
/*  48 */           if (PRINT) { System.out.println("Description " + this.description);
/*     */           }
/*     */         }
/*  51 */         if (item.getNodeName().toLowerCase().equals("policysetdefaults")) {
/*  52 */           this.policySetDefaults = new URI(item.getTextContent().trim());
/*  53 */           if (PRINT) { System.out.println("policyset defaults " + this.policySetDefaults.toString());
/*     */           }
/*     */         }
/*  56 */         if (item.getNodeName().toLowerCase().equals("target")) {
/*  57 */           this.target = new Target(item);
/*     */         }
/*     */         
/*  60 */         if (item.getNodeName().toLowerCase().indexOf("combinerparameter") != -1) {
/*  61 */           this.combParams = new CombinerParameters(item);
/*     */         }
/*     */         
/*  64 */         if (item.getNodeName().toLowerCase().equals("rule")) {
/*  65 */           this.rules.add(new Rule(item));
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/*  69 */       System.out.println("Exception in Policy " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*  73 */   public CombiningAlg getCombiningAlg() { return this.rcg; }
/*     */   
/*     */   public ArrayList<Rule> getRules() {
/*  76 */     return this.rules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public XACMLContainer evaluate(XACMLContext context)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       if (isMatched(context)) {
/*  87 */         boolean found = false;
/*  88 */         this.rcg.elements = new ArrayList();
/*  89 */         for (Iterator<Rule> rIt = this.rules.iterator(); (rIt.hasNext()) && (!found);) {
/*  90 */           Rule rule = (Rule)rIt.next();
/*  91 */           if (PRINT) System.out.println("RULE CHECK CAME");
/*  92 */           if ((rule.isMatched(context)) && ((rule.condition == null) || (rule.condition.evaluate(context)))) {
/*  93 */             if (PRINT) System.out.println("INSERTED INTO RCGGGGGGG");
/*  94 */             found = this.rcg.insertRule(rule);
/*  95 */           } else { System.out.println("Rule is not matched or condition does not hold ");
/*     */           } }
/*  97 */         if (PRINT) System.out.println("SIZE " + this.rcg.elements.size());
/*  98 */         XACMLContainer con = this.rcg.returnWinner();
/*  99 */         if (con != null) System.out.println("RESULT " + con.Id);
/* 100 */         return con; }
/* 101 */       return new Rule(XACMLContainer.Result.NotApplicable, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */       System.out.println("Exception in evaluate of Policy " + e.getMessage()); }
/* 119 */     return new Rule(XACMLContainer.Result.Indeterminate, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print()
/*     */   {
/* 125 */     System.out.print("ID:" + this.Id + " -- TARGET[");
/* 126 */     if (this.target != null) this.target.print();
/* 127 */     System.out.println("]");
/* 128 */     Iterator it; if ((this.rules != null) && (this.rules.size() > 0)) {
/* 129 */       System.out.println("{--------- RULES --------}");
/* 130 */       for (it = this.rules.iterator(); it.hasNext();) {
/* 131 */         ((Rule)it.next()).print();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Policy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */