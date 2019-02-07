/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class CombinerParameters
/*    */ {
/*    */   ArrayList<CombinerParameter> comParams;
/*    */   
/*    */   public CombinerParameters(Node com)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 19 */       NodeList nl = com.getChildNodes();
/* 20 */       for (int i = 0; i < nl.getLength(); i++) {
/* 21 */         Node combPa = nl.item(i);
/* 22 */         if (combPa.getNodeName().toLowerCase().equals("combinerparameter")) {
/* 23 */           NodeList ndList = combPa.getChildNodes();
/* 24 */           NamedNodeMap nnm = combPa.getAttributes();
/* 25 */           String par = nnm.getNamedItem("parameterName").getNodeValue();
/* 26 */           this.comParams = new ArrayList();
/* 27 */           for (int k = 0; k < ndList.getLength(); k++) {
/* 28 */             Node paras = ndList.item(k);
/* 29 */             if (paras.getNodeName().toLowerCase().equals("attribute")) {
/* 30 */               Attribute att = new Attribute(paras);
/* 31 */               this.comParams.add(new CombinerParameter(par, att));
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 38 */       System.out.println("Exception in Constructor of CombinerParameters " + e.getLocalizedMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/CombinerParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */