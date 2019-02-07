/*     */ package parserv2;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class Match
/*     */ {
/*     */   public XACMLFunction matchID;
/*     */   public Attribute attribute;
/*     */   public AttributeDesignator ad;
/*     */   public AttributeSelector as;
/*     */   
/*     */   public Match()
/*     */   {
/*  32 */     this.as = null;
/*  33 */     this.ad = null;
/*  34 */     this.attribute = null;
/*     */   }
/*     */   
/*     */   public Match(Node match) throws URISyntaxException
/*     */   {
/*     */     try {
/*  40 */       NamedNodeMap nnm = match.getAttributes();
/*  41 */       Node n = nnm.getNamedItem("MatchId");
/*  42 */       String s = n.getNodeValue();
/*     */       
/*  44 */       this.matchID = new XACMLFunction(n);
/*     */       
/*  46 */       NodeList nl = match.getChildNodes();
/*  47 */       for (int k = 0; k < nl.getLength(); k++) {
/*  48 */         Node att = nl.item(k);
/*  49 */         if (att.getNodeName().toLowerCase().equals("attributevalue")) {
/*  50 */           this.attribute = new Attribute();
/*  51 */           NamedNodeMap dataTypeMap = att.getAttributes();
/*  52 */           this.attribute.dataType = new URI(dataTypeMap.getNamedItem("DataType").getNodeValue());
/*  53 */           this.attribute.value = att.getTextContent().trim();
/*     */         }
/*     */         
/*  56 */         if (att.getNodeName().toLowerCase().indexOf("attributedesignator") != -1) {
/*  57 */           if (att.getNodeName().toLowerCase().equals("subjectattributedesignator")) {
/*  58 */             this.ad = new SubjectAttributeDesignator(att);
/*     */           } else {
/*  60 */             this.ad = new AttributeDesignator(att);
/*     */           }
/*     */         }
/*     */         
/*  64 */         if (att.getNodeName().toLowerCase().equals("attributeselector")) {
/*  65 */           this.as = new AttributeSelector(att);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  72 */       System.out.println("Exception in Match: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMatched(ArrayList<Object> rAttributeValues, URI dataType) throws IOException, URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  80 */       boolean result = false;
/*  81 */       if (this.attribute == null) {
/*  82 */         System.out.println("Missing element in policy");
/*  83 */         return result;
/*     */       }
/*     */       
/*     */       Iterator<Object> str;
/*  87 */       if (dataType.equals(this.attribute.dataType))
/*     */       {
/*  89 */         for (str = rAttributeValues.iterator(); (!result) && (str.hasNext());) {
/*  90 */           String strValue = (String)str.next();
/*  91 */           if (strValue.equals(this.attribute.value)) result = true; else {
/*  92 */             result = false;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  98 */       return result;
/*     */     }
/*     */     catch (Exception e) {
/* 101 */       System.out.println("Exception in isMatched of Match " + e.getMessage()); }
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public URI returnId()
/*     */     throws URISyntaxException
/*     */   {
/* 108 */     return this.ad == null ? new URI(this.as.requestContextPath) : this.ad.attributeId;
/*     */   }
/*     */   
/*     */   public void printValues()
/*     */   {
/* 113 */     System.out.println((this.ad == null ? this.as.requestContextPath : this.ad.attributeId.toString()) + " --> " + this.attribute.value);
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Match.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */