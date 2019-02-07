/*     */ package parserv2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;

/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Target
/*     */ {
/*  20 */   public static boolean PRINT = false;
/*     */   
/*     */   ArrayList<MatchList> subjects;
/*     */   ArrayList<MatchList> resources;
/*     */   ArrayList<MatchList> actions;
/*     */   ArrayList<MatchList> environments;
/*     */   
/*     */   public Target(ArrayList<MatchList> subs, ArrayList<MatchList> ress, ArrayList<MatchList> acs, ArrayList<MatchList> ens)
/*     */   {
/*  29 */     this.subjects = subs;
/*  30 */     this.resources = ress;
/*  31 */     this.actions = acs;
/*  32 */     this.environments = ens;
/*     */   }
/*     */   
/*     */   public Target(Node target) throws URISyntaxException
/*     */   {
/*     */     try {
/*  38 */       NodeList nl = target.getChildNodes();
/*  39 */       this.subjects = new ArrayList();
/*  40 */       this.resources = new ArrayList();
/*  41 */       this.actions = new ArrayList();
/*  42 */       this.environments = new ArrayList();
/*     */       
/*     */ 
/*  45 */       for (int i = 0; i < nl.getLength(); i++) {
/*  46 */         Node nTargetElement = nl.item(i);
/*     */         
/*  48 */         if (nTargetElement.getNodeName().toLowerCase().equals("subjects")) {
/*  49 */           NodeList subjectList = nTargetElement.getChildNodes();
/*  50 */           for (int k = 0; k < subjectList.getLength(); k++) {
/*  51 */             Node subject = subjectList.item(k);
/*  52 */             if (subject.getNodeName().toLowerCase().equals("subject")) {
/*  53 */               MatchList sList = new MatchList(subject, "subject");
/*  54 */               this.subjects.add(sList);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  60 */         if (nTargetElement.getNodeName().toLowerCase().equals("resources")) {
/*  61 */           NodeList resourceList = nTargetElement.getChildNodes();
/*  62 */           for (int k = 0; k < resourceList.getLength(); k++) {
/*  63 */             Node resource = resourceList.item(k);
/*  64 */             if (resource.getNodeName().toLowerCase().equals("resource")) {
/*  65 */               MatchList rList = new MatchList(resource, "resource");
/*  66 */               this.resources.add(rList);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  71 */         if (nTargetElement.getNodeName().toLowerCase().equals("actions")) {
/*  72 */           NodeList actionList = nTargetElement.getChildNodes();
/*  73 */           for (int k = 0; k < actionList.getLength(); k++) {
/*  74 */             Node action = actionList.item(k);
/*  75 */             if (action.getNodeName().toLowerCase().equals("action")) {
/*  76 */               MatchList aList = new MatchList(action, "action");
/*  77 */               this.actions.add(aList);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  82 */         if (nTargetElement.getNodeName().toLowerCase().equals("environments")) {
/*  83 */           NodeList environmentList = nTargetElement.getChildNodes();
/*  84 */           for (int k = 0; k < environmentList.getLength(); k++) {
/*  85 */             Node environment = environmentList.item(k);
/*  86 */             if (environment.getNodeName().toLowerCase().equals("environment")) {
/*  87 */               MatchList eList = new MatchList(environment, "environment");
/*  88 */               this.environments.add(eList);
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 107 */       System.out.println("Exception in Target " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 112 */   public ArrayList<MatchList> getSubjects() { return this.subjects; }
/* 113 */   public ArrayList<MatchList> getResources() { return this.resources; }
/* 114 */   public ArrayList<MatchList> getActions() { return this.actions; }
/* 115 */   public ArrayList<MatchList> getEnvironments() { return this.environments; }
/*     */   
/*     */   public boolean isMatched(XACMLContext context) throws IOException, URISyntaxException
/*     */   {
/*     */     try {
/* 120 */       boolean resultSubject = false;
/* 121 */       boolean resultResource = false;
/* 122 */       boolean resultAction = false;
/* 123 */       boolean resultEnvironment = false;
/*     */       Iterator<MatchList> mListIt;
/* 125 */       if (this.subjects.size() == 0)
/*     */       {
/* 127 */         if (PRINT) System.out.println("0 Subject --> Subject Matched in Target of " + getClass().toString());
/* 128 */         resultSubject = true;
/*     */       } else {
/* 130 */         for (mListIt = this.subjects.iterator(); (!resultSubject) && (mListIt.hasNext());) {
/* 131 */           MatchList subject = (MatchList)mListIt.next();
/*     */           
/* 133 */           for (Iterator<Subject> reqSub = context.subjects.iterator(); (!resultSubject) && (reqSub.hasNext());) {
/* 134 */             Subject rSubjectAtts = (Subject)reqSub.next();
/* 135 */             Iterator<ContextAttribute> rAttIt = rSubjectAtts.attrs.iterator();
/* 136 */             while ((!resultSubject) && (rAttIt.hasNext())) {
/* 137 */               ContextAttribute ca = (ContextAttribute)rAttIt.next();
/* 138 */               resultSubject = subject.isMatched(ca);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       MatchList subject;
/*     */       
/*     */       Iterator<Subject> reqSub;
/* 148 */       if (this.resources.size() == 0) {
/* 149 */         if (PRINT) System.out.println("0 Resource --> Resource Matched in Target of " + getClass().toString());
/* 150 */         resultResource = true;
/*     */       }
/*     */       else {
/* 153 */         for (mListIt = this.resources.iterator(); (!resultResource) && (mListIt.hasNext());) {
/* 154 */           MatchList resource = (MatchList)mListIt.next();
/*     */           
/* 156 */           Iterator<Resource> reqRes = context.resources.iterator();
/* 157 */           while ((!resultResource) && (reqRes.hasNext())) {
/* 158 */             Resource rResourceAtts = (Resource)reqRes.next();
/* 159 */             Iterator<ContextAttribute> rAttIt = rResourceAtts.attrs.iterator();
/* 160 */             while ((!resultResource) && (rAttIt.hasNext())) {
/* 161 */               ContextAttribute ca = (ContextAttribute)rAttIt.next();
/* 162 */               resultResource = resource.isMatched(ca);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 169 */       if (this.actions.size() == 0) {
/* 170 */         if (PRINT) System.out.println("0 Action --> Action Matched in Target of " + getClass().toString());
/* 171 */         resultAction = true;
/*     */       }
/*     */       else {
/* 174 */         for (mListIt = this.actions.iterator(); (!resultAction) && (mListIt.hasNext());) {
/* 175 */           MatchList action = (MatchList)mListIt.next();
/*     */           
/* 177 */           for (Iterator<ContextAttribute> reqAct = context.action.iterator(); (!resultAction) && (reqAct.hasNext());) {
/* 178 */             ContextAttribute rActionAtts = (ContextAttribute)reqAct.next();
/* 179 */             resultAction = action.isMatched(rActionAtts);
/*     */           } } }
/*     */       MatchList action;
/*     */       Iterator<ContextAttribute> reqAct;
/* 184 */       if (this.environments.size() == 0) {
/* 185 */         if (PRINT) System.out.println("0 Environment --> Environment Matched in Target of " + getClass().toString());
/* 186 */         resultEnvironment = true;
/*     */       }
/*     */       else {
/* 189 */         for (mListIt = this.environments.iterator(); (!resultEnvironment) && (mListIt.hasNext());) {
/* 190 */           MatchList environment = (MatchList)mListIt.next();
/*     */           
/* 192 */           for (Iterator<ContextAttribute> reqEnv = context.environment.iterator(); (!resultEnvironment) && (reqEnv.hasNext());) {
/* 193 */             ContextAttribute rEnvironmentAtts = (ContextAttribute)reqEnv.next();
/* 194 */             resultEnvironment = environment.isMatched(rEnvironmentAtts);
/*     */           }
/*     */         } }
/*     */       MatchList environment;
/*     */       Iterator<ContextAttribute> reqEnv;
/* 199 */       if (PRINT) {
/* 200 */         if (resultSubject) System.out.println("Sub Yes");
/* 201 */         if (resultAction) System.out.println("Act Yes");
/* 202 */         if (resultResource) System.out.println("Res Yes");
/* 203 */         if (resultEnvironment) System.out.println("Env Yes");
/*     */       }
/* 205 */       return (resultSubject) && (resultResource) && (resultAction) && (resultEnvironment);
/*     */     }
/*     */     catch (Exception e) {
/* 208 */       System.out.println("Exception in isMatched of Target " + e.getMessage()); }
/* 209 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doLazyMatch() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print()
/*     */   {
/* 226 */     boolean emptyTarget = true;
/* 227 */     Iterator it; if ((this.subjects != null) && (this.subjects.size() > 0)) {
/* 228 */       System.out.println("\nSubjects ---->");
/* 229 */       emptyTarget = false;
/* 230 */       for (it = this.subjects.iterator(); it.hasNext();)
/* 231 */         ((MatchList)it.next()).print();
/*     */     }
/* 234 */     if ((this.resources != null) && (this.resources.size() > 0)) {
/* 235 */       System.out.println((emptyTarget ? "\n" : "") + "Resources ---->");
/* 236 */       emptyTarget = false;
/* 237 */       for (it = this.resources.iterator(); it.hasNext();)
/* 238 */         ((MatchList)it.next()).print();
/*     */     }
/* 241 */     if ((this.actions != null) && (this.actions.size() > 0)) {
/* 242 */       System.out.println((emptyTarget ? "\n" : "") + "Actions ---->");
/* 243 */       emptyTarget = false;
/* 244 */       for (it = this.actions.iterator(); it.hasNext();)
/* 245 */         ((MatchList)it.next()).print();
/*     */     }
/* 248 */     if ((this.environments != null) && (this.environments.size() > 0)) {
/* 249 */       System.out.println((emptyTarget ? "\n" : "") + "Environments ---->");
/* 250 */       emptyTarget = false;
/* 251 */       for (it = this.environments.iterator(); it.hasNext();) {
/* 252 */         ((MatchList)it.next()).print();
/*     */       }
/*     */     }
/* 255 */     if (emptyTarget) System.out.print("EMPTY");
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */