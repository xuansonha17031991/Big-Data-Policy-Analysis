/*    */ package parserv2;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ 
/*    */ 
/*    */ public class Rule
/*    */   extends XACMLContainer
/*    */ {
/*    */   public XACMLContainer.Result effect;
/* 21 */   public Condition condition = null;
/*    */   
/*    */   public Rule(XACMLContainer.Result ef, Condition con) {
/* 24 */     this.effect = ef;
/* 25 */     this.condition = con;
/*    */   }
/*    */   
/*    */   public Rule(Node rule) throws URISyntaxException {
/*    */     try {
/* 30 */       this.winner = this;
/* 31 */       NamedNodeMap nnm = rule.getAttributes();
/* 32 */       this.effect = (nnm.getNamedItem("Effect").getNodeValue().toLowerCase().indexOf("permit") != -1 ? XACMLContainer.Result.Permit : XACMLContainer.Result.Deny);
/*    */       
/* 34 */       this.Id = nnm.getNamedItem("RuleId").getNodeValue();
/* 35 */       if (PRINT) System.out.println("ID:" + this.Id);
/* 36 */       NodeList nl = rule.getChildNodes();
/* 37 */       for (int i = 0; i < nl.getLength(); i++) {
/* 38 */         Node item = nl.item(i);
/*    */         
/* 40 */         if (item.getNodeName().toLowerCase().equals("target")) {
/* 41 */           this.target = new Target(item);
/*    */         }
/*    */         
/*    */ 
/* 45 */         if (item.getNodeName().toLowerCase().equals("description")) {
/* 46 */           this.description = item.getTextContent().trim();
/* 47 */           if (PRINT) { System.out.println("Description " + this.description);
/*    */           }
/*    */         }
/*    */         
/* 51 */         if (item.getNodeName().toLowerCase().equals("condition")) {
/* 52 */           this.condition = new Condition(item);
/* 53 */           if (PRINT) System.out.println("HERE WE HAVE A CONDITION");
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 57 */       System.out.println("Exception in Rule " + e.getMessage());
/*    */     }
/*    */   }
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
/*    */ 
/*    */   public void print()
/*    */   {
/* 83 */     System.out.print("ID:" + this.Id + " -- TARGET[");
/* 84 */     if (this.target != null) this.target.print();
/* 85 */     System.out.println("]");
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Rule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */