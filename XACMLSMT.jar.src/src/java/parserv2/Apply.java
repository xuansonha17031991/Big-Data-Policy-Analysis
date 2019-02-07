/*     */ package parserv2;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Apply
/*     */   extends XACMLExpression
/*     */ {
/*  16 */   public static boolean PRINT = false;
/*     */   public ArrayList<XACMLExpression> exList;
/*     */   public XACMLFunction fn;
/*     */   
/*     */   public Apply() {}
/*     */   
/*     */   public Apply(Node appElement)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  27 */       NamedNodeMap nnm = appElement.getAttributes();
/*  28 */       Node n = nnm.getNamedItem("FunctionId");
/*     */       
/*  30 */       this.fn = new XACMLFunction(n);
/*  31 */       if (PRINT) { System.out.println("APPLY Function : " + n.getNodeValue().toString());
/*     */       }
/*  33 */       NodeList nl = appElement.getChildNodes();
/*  34 */       this.exList = new ArrayList();
/*  35 */       for (int i = 0; i < nl.getLength(); i++) {
/*  36 */         Node appE = nl.item(i);
/*  37 */         if (appE.getNodeName().toLowerCase().equals("apply")) {
/*  38 */           XACMLExpression ex = new XACMLExpression();
/*  39 */           ex.apply = new Apply(appE);
/*  40 */           ex.type = 2;
/*  41 */           this.exList.add(ex);
/*     */         }
/*     */         
/*  44 */         if (appE.getNodeName().toLowerCase().equals("attributeselector")) {
/*  45 */           XACMLExpression ex = new XACMLExpression();
/*  46 */           ex.atts = new AttributeSelector(appE);
/*  47 */           ex.type = 3;
/*  48 */           this.exList.add(ex);
/*     */         }
/*     */         
/*  51 */         if (appE.getNodeName().toLowerCase().equals("attributevalue")) {
/*  52 */           XACMLExpression ex = new XACMLExpression();
/*  53 */           ex.att = new Attribute(appE);
/*  54 */           ex.type = 4;
/*  55 */           this.exList.add(ex);
/*     */         }
/*     */         
/*  58 */         if (appE.getNodeName().toLowerCase().equals("variablereference")) {
/*  59 */           XACMLExpression ex = new XACMLExpression();
/*  60 */           ex.variableId = new String(appE.getNodeValue());
/*  61 */           ex.type = 5;
/*  62 */           this.exList.add(ex);
/*     */         }
/*     */         
/*  65 */         if (appE.getNodeName().toLowerCase().indexOf("attributedesignator") != -1) {
/*  66 */           XACMLExpression ex = new XACMLExpression();
/*  67 */           ex.ad = new AttributeDesignator(appE);
/*  68 */           ex.type = 6;
/*  69 */           this.exList.add(ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  74 */       System.out.println("Exception in Apply " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public ConFunctionResult executeFunction(XACMLContext context) throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  82 */       ConFunctionResult result = null;
/*  83 */       ConFunctionResult params = resolveExpression(context);
/*  84 */       if (PRINT) System.out.println("PARAMETERS SIZE " + params.result.size());
/*  85 */       this.fn.setParams(params);
/*  86 */       return this.fn.execute(context);
/*     */     }
/*     */     catch (Exception e) {
/*  89 */       System.out.println("Exception in executeFunction of Apply" + e.getMessage()); }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public ConFunctionResult resolveExpression(XACMLContext context)
/*     */     throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  98 */       ConFunctionResult parameters = null;
/*     */       
/* 100 */       for (int i = 0; i < this.exList.size(); i++) {
/* 101 */         XACMLExpression ex = (XACMLExpression)this.exList.get(i);
/* 102 */         if (PRINT) System.out.println("ResolveExpression ex type " + ex.type);
/* 103 */         AttributeSelector as; switch (ex.type) {
/*     */         case 2: 
/* 105 */           Apply ap = ex.apply;
/* 106 */           parameters = mergeParameters(parameters, ap.executeFunction(context));
/* 107 */           break;
/* 108 */         case 3:  as = ex.atts;
/*     */           
/* 110 */           break;
/* 111 */         case 4:  if (PRINT) System.out.println("ATTRIBUTEVALUE OF RESOLVEEXPRESSION");
/* 112 */           Attribute av = ex.att;
/* 113 */           ArrayList<Object> values = new ArrayList();
/* 114 */           ArrayList<Object> types = new ArrayList();
/* 115 */           if (PRINT) System.out.println("Attribute Type :" + av.dataType + " Attribute value " + av.value);
/* 116 */           values.add(av.value);
/* 117 */           types.add(ConFunctionResult.findType(av.dataType));
/* 118 */           parameters = mergeParameters(parameters, new ConFunctionResult(values, types));
/* 119 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         case 6: 
/* 125 */           AttributeDesignator ad = ex.ad;
/* 126 */           ContextHandler ch = new ContextHandler(ad.attributeId);
/*     */           
/*     */ 
/* 129 */           ArrayList<Object> valueList = ch.retrieve(context, ad.designatorType);
/* 130 */           if (PRINT) for (int k = 0; k < valueList.size(); k++)
/* 131 */               System.out.println("Values returned from context" + valueList.get(k).toString());
/* 132 */           ArrayList<Object> typeList = new ArrayList();
/*     */           
/* 134 */           typeList.add(ConFunctionResult.findType(ad.dataType));
/*     */           
/* 136 */           parameters = new ConFunctionResult(valueList, typeList);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 141 */       return parameters;
/*     */     } catch (Exception e) {
/* 143 */       System.out.println("Exception in resolveExpression of Apply" + e.getMessage()); }
/* 144 */     return null;
/*     */   }
/*     */   
/*     */   private ConFunctionResult mergeParameters(ConFunctionResult existing, ConFunctionResult newOne)
/*     */   {
/* 149 */     ConFunctionResult cfr = existing;
/*     */     
/* 151 */     if (cfr == null) { cfr = newOne;
/* 152 */     } else if (newOne != null) {
/* 153 */       for (int i = 0; i < newOne.result.size(); i++) {
/* 154 */         cfr.result.add(newOne.result.get(i));
/* 155 */         cfr.dataType.add(newOne.dataType.get(i));
/*     */       }
/*     */     }
/*     */     
/* 159 */     return cfr;
/*     */   }
/*     */   
/*     */   public void printParameters(ConFunctionResult params) {
/* 163 */     for (int i = 0; (params != null) && (i < params.result.size()); i++) {
/* 164 */       System.out.println("Value in params " + params.result.get(i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Apply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */