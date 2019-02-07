/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ public class AttributeDesignator
/*    */   extends XACMLExpression
/*    */ {
/*    */   public XACMLExpression expr;
/*    */   public URI attributeId;
/*    */   public URI dataType;
/*    */   public String issuer;
/*    */   public boolean mustBePresent;
/*    */   public int designatorType;
/*    */   
/*    */   public AttributeDesignator()
/*    */   {
/* 28 */     this.designatorType = 0;
/*    */   }
/*    */   
/*    */   public AttributeDesignator(Node n) throws URISyntaxException {
/*    */     try {
/* 33 */       NamedNodeMap nnm = n.getAttributes();
/* 34 */       if (n.getNodeName().toLowerCase().indexOf("subject") != -1) { this.designatorType = 1;
/* 35 */       } else if (n.getNodeName().toLowerCase().indexOf("resource") != -1) { this.designatorType = 2;
/* 36 */       } else if (n.getNodeName().toLowerCase().indexOf("action") != -1) { this.designatorType = 3;
/* 37 */       } else if (n.getNodeName().toLowerCase().indexOf("environment") != -1) this.designatorType = 4; else {
/* 38 */         this.designatorType = 0;
/*    */       }
/*    */       
/* 41 */       this.attributeId = new URI(nnm.getNamedItem("AttributeId").getNodeValue().toString());
/* 42 */       this.dataType = new URI(nnm.getNamedItem("DataType").getNodeValue().toString());
/* 43 */       this.issuer = (nnm.getNamedItem("Issuer") != null ? nnm.getNamedItem("Issuer").getNodeValue().toString() : null);
/* 44 */       this.mustBePresent = (nnm.getNamedItem("MustBePresent") != null);
/*    */       
/*    */ 
/* 47 */       NodeList nl = n.getChildNodes();
/*    */       
/*    */ 
/*    */ 
/* 51 */       for (int i = 0; i < nl.getLength(); i++) {
/* 52 */         Node ad = nl.item(i);
/* 53 */         String value = ad.getTextContent().trim();
/* 54 */         if (!value.toLowerCase().equals("\n")) {
/* 55 */           this.expr = new XACMLExpression(ad);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 59 */       System.out.println("Exception in AttributeDesignator " + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/AttributeDesignator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */