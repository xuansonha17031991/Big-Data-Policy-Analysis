/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class Resource
/*    */ {
/*    */   public ArrayList<ContextAttribute> attrs;
/*    */   public String resourceContentAtt;
/*    */   public ArrayList<String> resourceContent;
/*    */   
/*    */   public Resource(Node element)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 21 */       NodeList nl = element.getChildNodes();
/* 22 */       this.resourceContent = new ArrayList();
/* 23 */       this.attrs = new ArrayList();
/* 24 */       for (int i = 0; i < nl.getLength(); i++) {
/* 25 */         Node att = nl.item(i);
/*    */         
/*    */ 
/* 28 */         if (att.getNodeName().toLowerCase().equals("resourcecontent")) {
/* 29 */           NamedNodeMap resAttList = att.getAttributes();
/* 30 */           this.resourceContentAtt = resAttList.item(0).toString();
/* 31 */           NodeList resConList = att.getChildNodes();
/* 32 */           this.resourceContent.add(resConList.item(1).getTextContent());
/*    */         }
/*    */         
/* 35 */         if (att.getNodeName().toLowerCase().equals("attribute")) {
/* 36 */           ContextAttribute ca = new ContextAttribute(att);
/* 37 */           this.attrs.add(ca);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 41 */       System.out.println("Exception e " + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */