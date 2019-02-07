/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdReference
/*    */   extends XACMLContainer
/*    */ {
/* 22 */   public static boolean PRINT = false;
/* 23 */   public static HashMap<String, URL> refList = new HashMap();
/*    */   
/*    */   public String earliestVersion;
/*    */   
/*    */   public String latestVersion;
/* 28 */   public final String DIRECTORY = "";
/*    */   public String version;
/*    */   public XACMLContainer reference;
/*    */   
/*    */   public IdReference(Node id, String parentId) throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 36 */       NamedNodeMap nnm = id.getAttributes();
/*    */       
/* 38 */       this.earliestVersion = (nnm.getNamedItem("Version") != null ? nnm.getNamedItem("Version").getNodeValue() : null);
/*    */       
/*    */ 
/* 41 */       this.version = (nnm.getNamedItem("EarliestVersion") != null ? nnm.getNamedItem("EarliestVersion").getNodeValue() : null);
/*    */       
/*    */ 
/* 44 */       this.latestVersion = (nnm.getNamedItem("LatestVersion") != null ? nnm.getNamedItem("LatestVersion").getNodeValue() : null);
/*    */       
/* 46 */       URL ref_string = new URL(id.getTextContent());
/* 47 */       if (PRINT) System.out.println("HERE WE ARE ..." + ref_string.toString());
/* 48 */       URI ref = new URI(ref_string.toString());
/*    */       
/*    */ 
/* 51 */       int type = 2;
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 56 */       if (id.getNodeName().toLowerCase().indexOf("policyset") != -1) type = 1;
/* 57 */       this.reference = retrieveURIReference(ref, type);
/*    */ 
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 62 */       System.out.println("We have a file in the system?? TRY MORE");
/* 63 */       int type = 2;
/* 64 */       if (id.getNodeName().toLowerCase().indexOf("policyset") != -1) type = 1;
/* 65 */       this.reference = retrieveFileReference(id.getTextContent(), type);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public XACMLContainer retrieveFileReference(String fileSystemPolicy, int type)
/*    */     throws URISyntaxException
/*    */   {
/* 73 */     XACMLContainer ref = null;
/* 74 */     if (!fileSystemPolicy.equals(null)) {
/* 75 */       if (PRINT) System.out.println("File Path : " + fileSystemPolicy);
/* 76 */       XACMLParser px = new XACMLParser("" + fileSystemPolicy);
/* 77 */       ref = px.container;
/*    */     }
/* 79 */     return ref;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public XACMLContainer retrieveURIReference(URI URIPolicy, int type)
/*    */     throws URISyntaxException
/*    */   {
/* 88 */     return retrieveFileReference(URIPolicy.toString(), type);
/*    */   }
/*    */   
/*    */   public void print()
/*    */   {
/* 93 */     this.reference.print();
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/IdReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */