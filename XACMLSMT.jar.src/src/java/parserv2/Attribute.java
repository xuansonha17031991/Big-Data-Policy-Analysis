/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Attribute
/*    */   extends XACMLExpression
/*    */ {
/*    */   Node element;
/*    */   public URI dataType;
/* 18 */   public XACMLExpression expr = null;
/*    */   public String value;
/*    */   
/*    */   public Attribute() {
/* 22 */     this.dataType = null;
/* 23 */     this.expr = null;
/* 24 */     this.value = null;
/*    */   }
/*    */   
/*    */   public Attribute(Node n) throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 31 */       NamedNodeMap nnm = n.getAttributes();
/* 32 */       this.dataType = new URI(nnm.getNamedItem("DataType").getNodeValue().toString());
/* 33 */       this.value = n.getTextContent().trim();
/*    */     } catch (Exception e) {
/* 35 */       System.out.println("Exception in Attribute Constructor " + e.getLocalizedMessage());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Attribute(URI ty, String val)
/*    */   {
/* 44 */     this.dataType = ty;
/* 45 */     this.value = val;
/*    */   }
/*    */   
/*    */   public Attribute(URI ty, String val, XACMLExpression ex) {
/* 49 */     this.dataType = ty;
/* 50 */     this.value = val;
/* 51 */     this.expr = ex;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */