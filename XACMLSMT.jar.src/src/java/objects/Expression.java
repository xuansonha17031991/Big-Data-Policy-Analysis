/*     */ package objects;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class Expression
/*     */ {
/*     */   String id;
/*     */   protected List<Expression> parameters;
/*     */   private PolicyElement owner;
/*  12 */   private Expression parent = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addExpression(Expression ex)
/*     */   {
/*  33 */     if (this.parameters == null) this.parameters = new ArrayList();
/*  34 */     this.parameters.add(ex);
/*     */   }
/*     */   
/*     */   public void setId(String str)
/*     */   {
/*  39 */     this.id = str;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  43 */     return this.id;
/*     */   }
/*     */   
/*  46 */   public List<Expression> getParameters() { return this.parameters; }
/*  47 */   public void setParameters(List<Expression> params) { this.parameters = params; }
/*     */   
/*  49 */   public PolicyElement getOwnerPolicy() { return this.owner; }
/*  50 */   public void setOwner(PolicyElement ow) { this.owner = ow; }
/*     */   
/*     */   public String convert2String() {
/*  53 */     String str = "Id:" + this.id;
/*  54 */     assert (this.parameters != null) : "Parameters is null in Expression";
/*  55 */     if (this.parameters != null) {
/*  56 */       for (int i = 0; i < this.parameters.size(); i++) {
/*  57 */         Expression ex = (Expression)this.parameters.get(i);
/*     */         
/*  59 */         str = str + "\n" + (ex.getParent() != null ? "Parent-->[" + ex.getParent().getId() + "]" : "") + ex.convert2String();
/*     */       }
/*     */     }
/*     */     
/*  63 */     return str;
/*     */   }
/*     */   
/*     */ 
/*  67 */   public Expression getParent() { return this.parent; }
/*     */   
/*  69 */   public void setParent(Expression ex) { this.parent = ex; }
/*     */   
/*     */ 
/*  72 */   public AttributeValue createAttributeValue(String dType, String v) { return new AttributeValue(dType, v); }
/*     */   
/*  74 */   public AttributeDesignator createAttributeDesignator(boolean mbp, String cat, String Id, String dType) { return new AttributeDesignator(mbp, cat, Id, dType); }
/*     */   
/*     */   public AttributeSelector createAttributeSelector(boolean mbp, String cat, String dType, String path) {
/*  77 */     return new AttributeSelector(mbp, cat, dType, path);
/*     */   }
/*     */   
/*     */   public class AttributeValue extends Expression
/*     */   {
/*     */     String dataType;
/*     */     String value;
/*     */     String adId;
/*     */     
/*     */     public AttributeValue(String dType, String v)
/*     */     {
/*  88 */       this.dataType = dType;
/*  89 */       this.value = v; }
/*     */     
/*  91 */     public void setAD(String a) { this.adId = a; }
/*  92 */     public String getAD() { return this.adId; }
/*  93 */     public String getDataType() { return this.dataType; }
/*  94 */     public void setDataType(String dType) { this.dataType = dType; }
/*  95 */     public String getValue() { return this.value; }
/*  96 */     public void setId(String v) { this.value = v; }
/*     */     
/*  98 */     public String convert2String() { String str = new String(this.value + "(Type=" + this.dataType + ") " + (this.adId != null ? " -AD-> " + this.adId : ""));
/*  99 */       return str;
/*     */     }
/*     */   }
/*     */   
/*     */   public class AttributeSelector extends Expression
/*     */   {
/*     */     String category;
/*     */     String dataType;
/*     */     String path;
/*     */     boolean mustBePresent;
/*     */     
/*     */     public AttributeSelector(boolean mbp, String cat, String dType, String p) {
/* 111 */       this.mustBePresent = mbp;
/* 112 */       this.category = cat;
/* 113 */       this.dataType = dType;
/* 114 */       this.path = p;
/*     */       
/* 116 */       this.id = p;
/*     */     }
/*     */     
/* 119 */     public boolean mustBePresent() { return this.mustBePresent; }
/* 120 */     public void setMustBePresent(boolean mbp) { this.mustBePresent = mbp; }
/* 121 */     public String getCategory() { return this.category; }
/* 122 */     public void setCategory(String cat) { this.category = cat; }
/* 123 */     public String getDataType() { return this.dataType; }
/* 124 */     public void setDataType(String dType) { this.dataType = dType; }
/* 125 */     public String getPath() { return this.path; }
/* 126 */     public void setPath(String p) { this.path = p; }
/*     */     
/*     */     public String convert2String()
/*     */     {
/* 130 */       String str = new String(this.path + "(Type=" + this.dataType + ") --> (Cat=" + this.category + ")\nmustBePresent =" + this.mustBePresent);
/* 131 */       return str;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public class AttributeDesignator
/*     */     extends Expression
/*     */   {
/*     */     boolean mustBePresent;
/*     */     String category;
/*     */     String dataType;
/*     */     
/*     */     public AttributeDesignator(boolean mbp, String cat, String Id, String dType)
/*     */     {
/* 145 */       this.mustBePresent = mbp;
/* 146 */       this.category = cat;
/* 147 */       this.id = Id;
/* 148 */       this.dataType = dType;
/*     */     }
/*     */     
/* 151 */     public boolean mustBePresent() { return this.mustBePresent; }
/* 152 */     public void setMustBePresent(boolean mbp) { this.mustBePresent = mbp; }
/* 153 */     public String getCategory() { return this.category; }
/* 154 */     public void setCategory(String cat) { this.category = cat; }
/* 155 */     public String getId() { return this.id; }
/* 156 */     public void setId(String Id) { this.id = Id; }
/* 157 */     public String getDataType() { return this.dataType; }
/* 158 */     public void setDataType(String dType) { this.dataType = dType; }
/*     */     
/*     */     public String convert2String() {
/* 161 */       String str = new String(this.id + "(Type=" + this.dataType + ") --> (Cat=" + this.category + ")\nmustBePresent =" + this.mustBePresent);
/* 162 */       return str;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/objects/Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */