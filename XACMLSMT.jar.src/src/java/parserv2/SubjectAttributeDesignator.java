/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class SubjectAttributeDesignator
/*    */   extends AttributeDesignator
/*    */ {
/*    */   URI subjectCat;
/*    */   
/*    */   public SubjectAttributeDesignator() {}
/*    */   
/*    */   public SubjectAttributeDesignator(Node n)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 22 */       NamedNodeMap nnm = n.getAttributes();
/*    */       
/* 24 */       this.attributeId = new URI(nnm.getNamedItem("AttributeId").getNodeValue());
/* 25 */       this.dataType = new URI(nnm.getNamedItem("DataType").getNodeValue());
/*    */       
/* 27 */       this.issuer = (nnm.getNamedItem("Issuer") != null ? nnm.getNamedItem("Issuer").getNodeValue().toString() : null);
/* 28 */       this.mustBePresent = (nnm.getNamedItem("MustBePresent") != null);
/*    */       
/* 30 */       this.subjectCat = (nnm.getNamedItem("SubjectCategory") != null ? new URI(nnm.getNamedItem("SubjectCategory").getNodeValue().toString()) : null);
/*    */       
/*    */ 
/* 33 */       NodeList nl = n.getChildNodes();
/*    */       
/*    */ 
/*    */ 
/* 37 */       for (int i = 0; i < nl.getLength(); i++) {
/* 38 */         Node ad = nl.item(i);
/* 39 */         String value = ad.getTextContent().trim();
/* 40 */         if (!value.toLowerCase().equals("\n")) {
/* 41 */           this.expr = new XACMLExpression(ad);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 45 */       System.out.println("Exception in SubjectAttributeDesignator " + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/SubjectAttributeDesignator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */