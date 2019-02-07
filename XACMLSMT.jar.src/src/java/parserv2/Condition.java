/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class Condition
/*    */ {
/* 11 */   public static boolean PRINT = false;
/*    */   public XACMLExpression con;
/*    */   
/*    */   public Condition(Node condition) throws URISyntaxException {
/*    */     try {
/* 16 */       NodeList nl = condition.getChildNodes();
/* 17 */       this.con = new XACMLExpression();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 22 */       for (int i = 0; i < nl.getLength(); i++) {
/* 23 */         Node nConditionElement = nl.item(i);
/*    */         
/* 25 */         if (nConditionElement.getNodeName().toLowerCase().equals("apply")) {
/* 26 */           this.con.apply = new Apply(nConditionElement);
/* 27 */           this.con.type = 2;
/*    */           
/*    */ 
/* 30 */           if (PRINT) { System.out.println("Apply in Condition: ");
/*    */           }
/*    */         }
/*    */         
/*    */ 
/* 35 */         if (nConditionElement.getNodeName().toLowerCase().equals("attributeselector")) {
/* 36 */           this.con.atts = new AttributeSelector(nConditionElement);
/* 37 */           this.con.type = 3;
/* 38 */           if (PRINT) { System.out.println("AttSelector in Condition: " + this.con.atts.requestContextPath);
/*    */           }
/*    */         }
/* 41 */         if (nConditionElement.getNodeName().toLowerCase().equals("attributevalue")) {
/* 42 */           this.con.att = new Attribute(nConditionElement);
/* 43 */           this.con.type = 4;
/* 44 */           if (PRINT) { System.out.println("Attribute Value in Condition: " + this.con.att.value);
/*    */           }
/*    */         }
/*    */         
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 54 */         if (nConditionElement.getNodeName().toLowerCase().equals("variablereference")) {
/* 55 */           this.con.type = 5;
/* 56 */           this.con.variableId = nConditionElement.getNodeValue();
/* 57 */           if (PRINT) { System.out.println("Variable Reference in Condition: " + this.con.variableId);
/*    */           }
/*    */         }
/* 60 */         if (nConditionElement.getNodeName().toLowerCase().indexOf("attributedesignator") != -1) {
/* 61 */           this.con.ad = new AttributeDesignator(nConditionElement);
/* 62 */           this.con.type = 6;
/* 63 */           if (PRINT) System.out.println("AttributeDesignator in Condition: " + this.con.ad.attributeId.toString());
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 68 */       System.out.println("Exception in Condition " + e.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean evaluate(XACMLContext cont)
/*    */     throws URISyntaxException
/*    */   {
/*    */     try
/*    */     {
/* 77 */       if (PRINT) System.out.println("CONDITION EVALUATE ...");
/* 78 */       ConFunctionResult conRes = this.con.evaluate(cont);
/* 79 */       if ((conRes != null) && (Boolean.parseBoolean(conRes.result.get(0).toString()))) {
/* 80 */         if (PRINT) System.out.println("Condition Holds ...");
/* 81 */         return true;
/*    */       }
/*    */       
/* 84 */       return false;
/*    */     }
/*    */     catch (Exception e) {
/* 87 */       System.out.println("Exception on evaluate of Condition" + e.getMessage()); }
/* 88 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Condition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */