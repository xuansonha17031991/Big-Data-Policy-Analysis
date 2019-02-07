/*    */ package parserv2;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class Subject
/*    */ {
/*    */   public ArrayList<ContextAttribute> attrs;
/*    */   public URI subjectCategory;
/*    */   
/*    */   public Subject(Node element) throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 19 */       NamedNodeMap nnm = element.getAttributes();
/*    */       
/* 21 */       this.subjectCategory = (nnm.getNamedItem("SubjectCategory") != null ? new URI(nnm.getNamedItem("SubjectCategory").toString()) : new URI("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));
/*    */       
/* 23 */       this.attrs = new ArrayList();
/*    */       
/* 25 */       NodeList nl = element.getChildNodes();
/* 26 */       for (int i = 0; i < nl.getLength(); i++) {
/* 27 */         Node att = nl.item(i);
/* 28 */         if (att.getNodeName().toLowerCase().equals("attribute")) {
/* 29 */           ContextAttribute ca = new ContextAttribute(att);
/* 30 */           this.attrs.add(ca);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 34 */       System.out.println("Exception in Subject " + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Subject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */