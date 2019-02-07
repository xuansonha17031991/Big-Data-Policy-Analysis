/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class EnvironmentMatch
/*    */   extends Match
/*    */ {
/* 13 */   public static boolean PRINT = false;
/*    */   
/*    */   XACMLFunction matchID;
/*    */   
/*    */ 
/*    */   public EnvironmentMatch() {}
/*    */   
/*    */   public EnvironmentMatch(Node match)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 25 */       NamedNodeMap nnm = match.getAttributes();
/* 26 */       Node n = nnm.getNamedItem("MatchId");
/* 27 */       String s = n.getNodeValue();
/* 28 */       if (PRINT) System.out.println("s value" + s);
/* 29 */       this.matchID = new XACMLFunction(n);
/* 30 */       Attribute xAtt = new Attribute();
/*    */       
/* 32 */       NodeList nl = match.getChildNodes();
/* 33 */       for (int k = 0; k < nl.getLength(); k++) {
/* 34 */         Node att = nl.item(k);
/* 35 */         if (att.getNodeName().toLowerCase().equals("attributevalue")) {
/* 36 */           this.attribute = new Attribute();
/* 37 */           NamedNodeMap dataTypeMap = match.getAttributes();
/* 38 */           this.attribute.dataType = new URI(dataTypeMap.getNamedItem("DataType").getNodeValue());
/* 39 */           this.attribute.value = att.getTextContent().trim();
/*    */         }
/*    */         
/* 42 */         if (att.getNodeName().toLowerCase().indexOf("attributedesignator") != -1) {
/* 43 */           this.ad = new AttributeDesignator(att);
/*    */         }
/*    */         
/* 46 */         if (att.getNodeName().toLowerCase().equals("attributeselector")) {
/* 47 */           this.as = new AttributeSelector(att);
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 52 */       System.out.println("Exception in Environment Match: " + e.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isMatched(Attribute requestAttributes)
/*    */   {
/* 59 */     if (this.attribute == null) {
/* 60 */       System.out.println("Missing element in policy");
/* 61 */       return false;
/*    */     }
/*    */     
/* 64 */     System.out.println("AttributeDesignator " + this.ad.attributeId);
/* 65 */     System.out.println("EnvironmentMatch should do isMatched differently ... ");
/*    */     
/* 67 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public void printValues()
/*    */   {
/* 73 */     System.out.println("Attribute Id : " + (this.ad == null ? this.as.requestContextPath : this.ad.attributeId.toString()));
/* 74 */     System.out.println("Value : " + this.attribute.value);
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/EnvironmentMatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */