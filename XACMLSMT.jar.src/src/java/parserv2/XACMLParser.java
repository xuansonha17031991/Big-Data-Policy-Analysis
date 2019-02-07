/*    */ package parserv2;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class XACMLParser
/*    */ {
/*    */   public Document dom;
/*    */   public static Element docEle;
/*    */   public XACMLContainer container;
/*    */   
/*    */   public XACMLParser(String file)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 33 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*    */       
/* 35 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 36 */       this.dom = db.parse(file);
/* 37 */       docEle = this.dom.getDocumentElement();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 42 */       this.container = new XACMLContainer();
/* 43 */       if (docEle != null) {
/* 44 */         if (docEle.getNodeName().toLowerCase().equals("policyset"))
/*    */         {
/* 46 */           this.container = new PolicySet(docEle);
/* 47 */         } else if (docEle.getNodeName().toLowerCase().equals("policy"))
/*    */         {
/* 49 */           this.container = new Policy(docEle);
/*    */         }
/*    */         
/*    */       }
/*    */       
/*    */     }
/*    */     catch (ParserConfigurationException pce)
/*    */     {
/* 57 */       System.err.println("ParserException during XACML Parsing ..." + pce.getMessage());
/*    */     }
/*    */     catch (SAXException se) {
/* 60 */       System.err.println("SAXException during XACML Parsing ..." + se.getMessage());
/*    */     }
/*    */     catch (IOException ioe) {
/* 63 */       System.err.println("IOException during XACML Parsing ..." + ioe.getMessage());
/*    */     }
/*    */     catch (Exception ex) {
/* 66 */       System.err.println("Exception during XACML Parsing ..." + ex.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/XACMLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */