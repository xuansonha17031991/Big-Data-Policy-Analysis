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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PolicySet
/*     */   extends XACMLContainer
/*     */ {
/*     */   String version;
/*     */   CombiningAlg pcg;
/*     */   URI policySetDefaults;
/*     */   CombinerParameters combParams;
/*     */   public XACMLContainer.Result decision;
/*     */   ArrayList<Policy> policies;
/*     */   ArrayList<PolicySet> pSets;
/*     */   ArrayList<IdReference> idr;
/*     */   
/*     */   public PolicySet(Node pSet)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  42 */       NamedNodeMap nnm = pSet.getAttributes();
/*  43 */       this.Id = nnm.getNamedItem("PolicySetId").getNodeValue();
/*     */       
/*  45 */       this.version = (nnm.getNamedItem("Version") != null ? nnm.getNamedItem("Version").getNodeValue() : null);
/*  46 */       this.pcg = new CombiningAlg(nnm.getNamedItem("PolicyCombiningAlgId").getNodeValue());
/*     */       
/*     */ 
/*     */ 
/*  50 */       this.pSets = new ArrayList();
/*  51 */       this.policies = new ArrayList();
/*  52 */       this.idr = new ArrayList();
/*  53 */       NodeList nl = pSet.getChildNodes();
/*  54 */       if ((nl != null) && (nl.getLength() > 0)) {
/*  55 */         for (int i = 0; i < nl.getLength(); i++) {
/*  56 */           Node item = nl.item(i);
/*  57 */           if (item.getNodeName().toLowerCase().equals("policyset")) {
/*  58 */             if (PRINT) System.out.println(item.getNodeName());
/*  59 */             this.pSets.add(new PolicySet(item));
/*     */           }
/*     */           
/*  62 */           if (item.getNodeName().toLowerCase().equals("policy")) {
/*  63 */             if (PRINT) System.out.println(item.getNodeName());
/*  64 */             this.policies.add(new Policy(item));
/*     */           }
/*     */           
/*  67 */           if (item.getNodeName().toLowerCase().equals("description")) {
/*  68 */             this.description = item.getNodeValue();
/*  69 */             if (PRINT) { System.out.println("Description " + this.description);
/*     */             }
/*     */           }
/*  72 */           if (item.getNodeName().toLowerCase().equals("policysetdefaults")) {
/*  73 */             this.policySetDefaults = new URI(item.getNodeValue());
/*  74 */             if (PRINT) { System.out.println("policyset defaults " + this.policySetDefaults.toString());
/*     */             }
/*     */           }
/*  77 */           if (item.getNodeName().toLowerCase().equals("target")) {
/*  78 */             this.target = new Target(item);
/*     */           }
/*     */           
/*     */ 
/*  82 */           if (item.getNodeName().toLowerCase().indexOf("idreference") != -1) {
/*  83 */             this.idr.add(new IdReference(item, this.Id));
/*     */             
/*  85 */             if (PRINT) { System.out.println("id reference " + item.getTextContent());
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */           if (item.getNodeName().toLowerCase().indexOf("combinerparameter") != -1) {
/*  94 */             this.combParams = new CombinerParameters(item);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 103 */       System.out.println("Exception in PolicySet " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 108 */   public CombiningAlg getCombiningAlg() { return this.pcg; }
/* 109 */   public ArrayList<Policy> getPolicies() { return this.policies; }
/* 110 */   public ArrayList<PolicySet> getPolicySets() { return this.pSets; }
/* 111 */   public ArrayList<IdReference> getIdReferences() { return this.idr; }
/*     */   
/*     */ 
/*     */   public XACMLContainer evaluate(XACMLContext context)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/* 119 */       if (isMatched(context)) {
/* 120 */         if (PRINT) { System.out.println("Evaluation of PolicySet ");
/*     */         }
/*     */         
/* 123 */         this.pcg.elements = new ArrayList();
/*     */         
/* 125 */         for (Iterator<PolicySet> pSetIt = this.pSets.iterator(); pSetIt.hasNext();) {
/* 126 */           PolicySet pSet = (PolicySet)pSetIt.next();
/* 127 */           Iterator<PolicySet> psIt = pSet.pSets.iterator(); if (psIt.hasNext()) {
/* 128 */             PolicySet pSetInternal = (PolicySet)psIt.next();
/* 129 */             return pSetInternal.evaluate(context);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 134 */         for (Iterator<Policy> pIt = this.policies.iterator(); pIt.hasNext();) {
/* 135 */           Policy policy = (Policy)pIt.next();
/* 136 */           XACMLContainer xc = policy.evaluate(context);
/* 137 */           if (xc != null) { this.pcg.insertRule(xc);System.out.println("INSERTED INTO PCGGGGG");
/*     */           } }
/* 139 */         if (PRINT) System.out.println("ELEMENT NUMBER " + this.pcg.elements.size());
/* 140 */         return this.pcg.returnWinner();
/*     */       }
/*     */       
/* 143 */       return new Rule(XACMLContainer.Result.NotApplicable, null);
/*     */     }
/*     */     catch (Exception e) {
/* 146 */       System.out.println("Exception in evaluate of PolicySet " + e.getMessage()); }
/* 147 */     return new Rule(XACMLContainer.Result.Indeterminate, null);
/*     */   }
/*     */   
/*     */   public void print()
/*     */   {
/* 152 */     System.out.print("ID:" + this.Id + " -- TARGET[");
/* 153 */     if (this.target != null) this.target.print();
/* 154 */     System.out.println("]");
/*     */     
/* 156 */     if ((this.pSets != null) && (this.pSets.size() > 0)) System.out.println("<--------- POLICYSETS -------->");
/* 157 */     for (Iterator it = this.pSets.iterator(); it.hasNext();) {
/* 158 */       ((PolicySet)it.next()).print();
/*     */     }
/*     */     
/* 161 */     if ((this.policies != null) && (this.policies.size() > 0)) System.out.println("[--------- POLICIES --------]");
/* 162 */     for (Iterator it = this.policies.iterator(); it.hasNext();) {
/* 163 */       ((Policy)it.next()).print();
/*     */     }
/*     */     
/* 166 */     for (Iterator it = this.idr.iterator(); it.hasNext();) {
/* 167 */       ((IdReference)it.next()).print();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/PolicySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */