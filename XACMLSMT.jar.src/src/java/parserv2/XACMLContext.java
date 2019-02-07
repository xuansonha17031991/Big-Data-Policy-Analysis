/*     */ package parserv2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;

/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;

/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XACMLContext
/*     */ {
/*     */   public ArrayList<Subject> subjects;
/*     */   public ArrayList<Resource> resources;
/*     */   public ArrayList<ContextAttribute> action;
/*     */   public ArrayList<ContextAttribute> environment;
/*     */   Document dom;
/*     */   static Element docEle;
/*     */   
/*     */   public XACMLContext(String file)
/*     */     throws IOException, ParserConfigurationException, SAXException, URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  38 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*     */       
/*  40 */       DocumentBuilder db = dbf.newDocumentBuilder();
/*  41 */       this.dom = db.parse(file);
/*  42 */       docEle = this.dom.getDocumentElement();
/*     */       
/*     */ 
/*  45 */       this.subjects = new ArrayList();
/*  46 */       NodeList nlSub = docEle.getElementsByTagName("Subject");
/*  47 */       for (int i = 0; i < nlSub.getLength(); i++) {
/*  48 */         Subject s = new Subject(nlSub.item(i));
/*  49 */         this.subjects.add(s);
/*     */       }
/*     */       
/*     */ 
/*  53 */       this.resources = new ArrayList();
/*  54 */       NodeList nlRes = docEle.getElementsByTagName("Resource");
/*  55 */       for (int i = 0; i < nlRes.getLength(); i++) {
/*  56 */         Resource r = new Resource(nlRes.item(i));
/*  57 */         this.resources.add(r);
/*     */       }
/*     */       
/*  60 */       this.action = new ArrayList();
/*  61 */       NodeList nlAct = docEle.getElementsByTagName("Action");
/*  62 */       for (int k = 0; k < nlAct.getLength(); k++) {
/*  63 */         NodeList nl = nlAct.item(k).getChildNodes();
/*  64 */         for (int i = 0; i < nl.getLength(); i++) {
/*  65 */           Node att = nl.item(i);
/*  66 */           if (att.getNodeName().toLowerCase().equals("attribute")) {
/*  67 */             ContextAttribute ca = new ContextAttribute(att);
/*  68 */             this.action.add(ca);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  73 */       this.environment = new ArrayList();
/*  74 */       NodeList nlEnv = docEle.getElementsByTagName("Environment");
/*  75 */       for (int k = 0; k < nlEnv.getLength(); k++) {
/*  76 */         NodeList nl = nlEnv.item(k).getChildNodes();
/*  77 */         for (int i = 0; i < nl.getLength(); i++) {
/*  78 */           Node att = nl.item(i);
/*  79 */           if (att.getNodeName().toLowerCase().equals("attribute")) {
/*  80 */             ContextAttribute ca = new ContextAttribute(att);
/*  81 */             this.environment.add(ca);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  87 */       System.out.println("Exception in XACMLContext " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<Object> returnValues(URI attID, int type) throws URISyntaxException
/*     */   {
/*     */     try
/*     */     {
/*  95 */       ArrayList<Object> values = null;
/*  96 */       boolean notFound = true;

/*  98 */       boolean notFound2; Iterator<ContextAttribute> itAtt; 
 
				switch (type) {
/*     */       case 1: 
/* 100 */         for (Iterator<Subject> it = this.subjects.iterator(); (it.hasNext()) && (notFound);) {
/* 101 */           Subject s = (Subject)it.next();
/* 102 */           notFound2 = true;
/* 103 */           for (itAtt = s.attrs.iterator(); (itAtt.hasNext()) && (notFound2);) {
/* 104 */             ContextAttribute ca = (ContextAttribute)itAtt.next();
/* 105 */             if (ca.attributeId.equals(attID)) { values = ca.attValues;notFound = false;notFound2 = false;
/*     */             }
/*     */           } }
/* 108 */         break;
/* 109 */       case 2:  for (Iterator<Resource> it = this.resources.iterator(); (it.hasNext()) && (notFound);) {
/* 110 */           Resource r = (Resource)it.next();
/* 111 */           notFound2 = true;
/* 112 */           for (itAtt = r.attrs.iterator(); (itAtt.hasNext()) && (notFound2);) {
/* 113 */             ContextAttribute ca = (ContextAttribute)itAtt.next();
/* 114 */             if (ca.attributeId.equals(attID)) { values = ca.attValues;notFound = false;notFound2 = false;
/*     */             }
/*     */           } }
/* 117 */         break;
/* 118 */       case 3:  for (itAtt = this.action.iterator(); (itAtt.hasNext()) && (notFound);) {
/* 119 */           ContextAttribute ca = (ContextAttribute)itAtt.next();
/* 120 */           if (ca.attributeId.equals(attID)) { values = ca.attValues;notFound = false;
/*     */           } }
/* 122 */         break;
/* 123 */       case 4:  for (itAtt = this.environment.iterator(); (itAtt.hasNext()) && (notFound);) {
/* 124 */           ContextAttribute ca = (ContextAttribute)itAtt.next();
/* 125 */           if (ca.attributeId.equals(attID)) { values = ca.attValues;notFound = false;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       
/* 131 */       return values;
/*     */     } catch (Exception e) {
/* 133 */       System.out.println("Exception in returnValues of XACMLContext" + e.getLocalizedMessage()); }
/* 134 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/XACMLContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */