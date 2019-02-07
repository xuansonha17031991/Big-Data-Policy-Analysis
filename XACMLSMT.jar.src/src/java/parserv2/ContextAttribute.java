/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class ContextAttribute
/*    */ {
/*    */   public URI type;
/*    */   public String issuer;
/*    */   public URI attributeId;
/*    */   public ArrayList<Object> attValues;
/*    */   
/*    */   public ContextAttribute(Node attElement) throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 22 */       NamedNodeMap nnm = attElement.getAttributes();
/* 23 */       this.attributeId = new URI(nnm.getNamedItem("AttributeId").getNodeValue());
/* 24 */       this.type = new URI(nnm.getNamedItem("DataType").getNodeValue());
/* 25 */       this.issuer = (nnm.getNamedItem("Issuer") != null ? nnm.getNamedItem("Issuer").getNodeValue() : null);
/*    */       
/*    */ 
/* 28 */       NodeList nAttributeList = attElement.getChildNodes();
/* 29 */       this.attValues = new ArrayList();
/* 30 */       for (int k = 0; k < nAttributeList.getLength(); k++) {
/* 31 */         Node nMatches = nAttributeList.item(k);
/* 32 */         if (nMatches.getNodeName().toLowerCase().equals("attributevalue")) {
/* 33 */           this.attValues.add(nMatches.getTextContent().trim());
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 37 */       System.out.println("Exception in ContextAttribute " + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/ContextAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */