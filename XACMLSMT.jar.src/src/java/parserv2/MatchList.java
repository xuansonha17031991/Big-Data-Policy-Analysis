/*     */ package parserv2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;

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
/*     */ public class MatchList
/*     */ {
/*  25 */   public static boolean PRINT = false;
/*     */   ArrayList mList;
/*  27 */   private int mListType = 0;
/*     */   
/*     */   public MatchList(Node nTargetElement, String element) throws URISyntaxException {
/*     */     try {
/*  31 */       int env = 0;
/*  32 */       if (element.equals("environment")) { env = this.mListType = 1;this.mList = new ArrayList();
/*  33 */       } else { this.mListType = 0;this.mList = new ArrayList();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  38 */       NodeList nMatchList = nTargetElement.getChildNodes();
/*  39 */       for (int m = 0; m < nMatchList.getLength(); m++) {
/*  40 */         Node nMatch = nMatchList.item(m);
/*  41 */         if (nMatch.getNodeName().toLowerCase().equals(element + "match")) {
/*  42 */           if (env == 1) {
/*  43 */             EnvironmentMatch mElement = new EnvironmentMatch(nMatch);
/*  44 */             this.mList.add(mElement);
/*  45 */             if (PRINT) System.out.println(element + "match: " + mElement.attribute.value);
/*     */           } else {
/*  47 */             Match mElement = new Match(nMatch);
/*  48 */             this.mList.add(mElement);
/*  49 */             if (PRINT) { System.out.println(element + "match: " + mElement.attribute.value);
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  62 */       System.out.println("Exception in MatchList constructor" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*  66 */   public int getMListType() { return this.mListType; }
/*  67 */   public ArrayList<Match> getMatchList() { return this.mList; }
/*     */   
/*     */ 
/*     */   public boolean isMatched(ContextAttribute ca)
/*     */     throws IOException, URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  75 */       boolean result = false;
/*  76 */       for (Iterator<Match> m = this.mList.iterator(); m.hasNext();) {
/*  77 */         Match match = (Match)m.next();
/*  78 */         match.printValues();
/*  79 */         if (ca.attributeId.equals(match.returnId())) {
/*  80 */           result = match.isMatched(ca.attValues, ca.type);
/*     */         }
/*     */       }
/*     */       
/*  84 */       return result;
/*     */     } catch (Exception e) {
/*  86 */       System.out.println("Exception in isMatched of MatchList " + e.getMessage()); }
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public void print()
/*     */   {
/*     */     Iterator it;
/*  94 */     if (this.mListType == 0) {
/*  95 */       for (it = this.mList.iterator(); it.hasNext();) {
/*  96 */         ((Match)it.next()).printValues();
/*     */       }
/*     */     } else {
/*  99 */       for (it = this.mList.iterator(); it.hasNext();) {
/* 100 */         ((EnvironmentMatch)it.next()).printValues();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/MatchList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */