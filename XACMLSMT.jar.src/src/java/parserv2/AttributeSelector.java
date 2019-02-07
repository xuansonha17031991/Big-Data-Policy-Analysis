/*    */ package parserv2;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeSelector
/*    */   extends XACMLExpression
/*    */ {
/*    */   XACMLExpression expr;
/*    */   public String requestContextPath;
/*    */   public URI dataType;
/*    */   public boolean mustBePresent;
/*    */   
/*    */   public AttributeSelector() {}
/*    */   
/*    */   public AttributeSelector(Node n)
/*    */     throws URISyntaxException
/*    */   {
/* 41 */     NamedNodeMap nnm = n.getAttributes();
/*    */     
/* 43 */     this.dataType = new URI(nnm.getNamedItem("DataType").getNodeValue().toString());
/* 44 */     this.requestContextPath = nnm.getNamedItem("RequestContextPath").getNodeValue().toString();
/* 45 */     this.mustBePresent = (nnm.getNamedItem("MustBePresent") != null);
/*    */     
/*    */ 
/* 48 */     NodeList nl = n.getChildNodes();
/*    */     
/*    */ 
/*    */ 
/* 52 */     for (int i = 0; i < nl.getLength(); i++) {
/* 53 */       Node ad = nl.item(i);
/* 54 */       String value = ad.getTextContent().trim();
/* 55 */       if (!value.toLowerCase().equals("\n")) {
/* 56 */         this.expr = new XACMLExpression(ad);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/AttributeSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */