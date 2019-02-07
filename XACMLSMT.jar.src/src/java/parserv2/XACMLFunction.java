/*     */ package parserv2;
/*     */ 
/*     */ /*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;

/*     */ import org.w3c.dom.Node;

import utils.StringUtils;
/*     */ 
/*     */ 
/*     */ public class XACMLFunction
/*     */   extends XACMLExpression
/*     */ {
/*  12 */   public static boolean PRINT = false;
/*     */   
/*     */   public static enum Functions
/*     */   {
/*  16 */     string_equal,  boolean_equal,  integer_equal,  double_equal,  date_equal, 
/*  17 */     time_equal,  dateTime_equal,  dayTimeDuration_equal,  yearMonthDuration_equal, 
/*  18 */     anyURI_equal,  x500Name_equal,  rfc822Name_equal,  hexBinary_equal,  base64Binary_equal, 
/*     */     
/*  20 */     integer_greater_than,  double_greater_than,  string_greater_than,  time_greater_than, 
/*  21 */     dateTime_greater_than,  date_greater_than, 
/*     */     
/*  23 */     integer_greater_than_or_equal,  double_greater_than_or_equal,  string_greater_than_or_equal, 
/*  24 */     time_greater_than_or_equal,  dateTime_greater_than_or_equal,  date_greater_than_or_equal, 
/*     */     
/*  26 */     integer_less_than,  double_less_than,  string_less_than,  time_less_than, 
/*  27 */     date_less_than,  dateTime_less_than, 
/*     */     
/*  29 */     integer_less_than_or_equal,  double_less_than_or_equal,  string_less_than_or_equal, 
/*  30 */     time_less_than_or_equal,  dateTime_less_than_or_equal,  date_less_than_or_equal, 
/*     */     
/*  32 */     integer_add,  double_add,  integer_subtract,  double_subtract, 
/*  33 */     integer_multiply,  double_multiply,  integer_divide,  double_divide, 
/*  34 */     integer_mod,  integer_abs,  double_abs,  round,  floor, 
/*     */     
/*  36 */     string_normalize_space, 
/*  37 */     string_normalize_to_lower_case, 
/*     */     
/*  39 */     double_to_integer,  integer_to_double, 
/*     */     
/*  41 */     or,  and,  n_of,  not, 
/*     */     
/*     */ 
/*  44 */     dateTime_add_dayTimeDuration,  dateTime_add_yearMonthDuration,  dateTime_subtract_dayTimeDuration, 
/*  45 */     dateTime_subtract_yearMonthDuration,  date_add_yearMonthDuration,  date_subtract_yearMonthDuration, 
/*  46 */     time_in_range,  string_concatenate,  url_string_concatenate, 
/*     */     
/*  48 */     string_one_and_only,  integer_one_and_only,  date_one_and_only,  double_one_and_only,  time_one_and_only, 
/*  49 */     string_bag_size,  integer_bag_size,  date_bag_size,  double_bag_size,  time_bag_size, 
/*  50 */     string_is_in,  integer_is_in,  date_is_in,  double_is_in,  time_is_in, 
/*     */     
/*  52 */     string_bag,  integer_bag,  date_bag,  double_bag,  time_bag, 
/*  53 */     string_intersection,  integer_intersection,  date_intersection,  double_intersection,  time_intersection, 
/*  54 */     string_at_least_one_member_of,  integer_at_least_one_member_of,  date_at_least_one_member_of,  double_at_least_one_member_of,  time_at_least_one_member_of, 
/*  55 */     string_union,  integer_union,  date_union,  double_union,  time_union, 
/*  56 */     string_subset,  integer_subset,  date_subset,  double_subset,  time_subset, 
/*  57 */     string_set_equals,  integer_set_equals,  date_set_equals,  double_set_equals,  time_set_equals, 
/*     */     
/*  59 */     any_of,  all_of,  any_of_any,  all_of_any,  any_of_all,  all_of_all,  map, 
/*  60 */     string_regexp_match,  anyURI_regexp_match,  ipAddress_regexp_match,  dnsName_regexp_match, 
/*  61 */     rfc822Name_regexp_match,  x500Name_regexp_match,  x500Name_match, 
/*  62 */     rfc822Name_match,  xpath_node_count,  xpath_node_equal,  xpath_node_match;
/*     */     
/*     */     private Functions() {} }
/*  65 */   public Functions predefinedFunc = null;
/*  66 */   public String userDefinedFunc = null;
/*     */   
/*     */   public ConFunctionResult parameters;
/*     */   
/*     */   public XACMLFunction(String str)
/*     */   {
/*  72 */     boolean userDefined = false;
/*  73 */     for (Functions m : Functions.values()) {
/*  74 */       if (m.toString().equals(str)) {
/*  75 */         this.predefinedFunc = m;
/*  76 */         userDefined = true;
/*  77 */         break;
/*     */       }
/*     */     }
/*  80 */     if (!userDefined) this.userDefinedFunc = str;
/*     */   }
/*     */   
/*  83 */   public boolean isUserDefined() { return this.predefinedFunc == null; }
/*     */   
/*     */   public XACMLFunction(Node n)
/*     */   {
/*  87 */     String matchExpr = n.getNodeValue();
/*  88 */     boolean userDefined = false;
/*  89 */     String fnSTR = StringUtils.getShortName(matchExpr).replaceAll("-", "_");
/*  90 */     for (Functions m : Functions.values()) {
/*  91 */       if (m.toString().equals(fnSTR)) {
/*  92 */         this.predefinedFunc = m;
/*  93 */         userDefined = true;
/*  94 */         if (!PRINT) break; System.out.println("Function : " + m.toString() + " func: " + matchExpr); break;
/*     */       }
/*     */     }
/*     */     
/*  98 */     if (!userDefined) this.userDefinedFunc = fnSTR;
/*  99 */     if (PRINT) System.out.println("User Defined Function : " + this.userDefinedFunc + " userDefinedfunc: " + matchExpr);
/*     */   }
/*     */   
/*     */   public void setParams(ConFunctionResult par)
/*     */   {
/* 104 */     this.parameters = par;
/*     */   }
/*     */   
/*     */   public ConFunctionResult execute(XACMLContext context) throws URISyntaxException
/*     */   {
/*     */     try {
/* 110 */       ConFunctionResult cfr = null;
/* 111 */       if (PRINT) System.out.println("IN FUNCTION .... : " + this.predefinedFunc);
/* 112 */       switch (this.predefinedFunc) {
/*     */       case string_equal: 
/*     */       case boolean_equal: 
/*     */       case integer_equal: 
/*     */       case double_equal: 
/*     */       case date_equal: 
/*     */       case time_equal: 
/*     */       case dateTime_equal: 
/*     */       case dayTimeDuration_equal: 
/*     */       case yearMonthDuration_equal: 
/*     */       case anyURI_equal: 
/*     */       case x500Name_equal: 
/*     */       case rfc822Name_equal: 
/*     */       case hexBinary_equal: 
/*     */       case base64Binary_equal: 
/* 127 */         if (PRINT) System.out.println("EQUALITY FUNCTION ....");
/* 128 */         boolean resEQ = equal_or_related(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 129 */           .get(1), this.parameters.dataType.get(1), 1);
/* 130 */         ArrayList<Object> valuesEQ = new ArrayList();
/* 131 */         ArrayList<Object> typesEQ = new ArrayList();
/* 132 */         valuesEQ.add(Boolean.valueOf(resEQ));
/* 133 */         typesEQ.add("boolean");
/* 134 */         return new ConFunctionResult(valuesEQ, typesEQ);
/*     */       
/*     */       case integer_greater_than: 
/*     */       case double_greater_than: 
/*     */       case string_greater_than: 
/*     */       case time_greater_than: 
/*     */       case dateTime_greater_than: 
/*     */       case date_greater_than: 
/* 142 */         if (PRINT) System.out.println("GREATER THAN FUNCTION ....");
/* 143 */         boolean resGT = equal_or_related(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 144 */           .get(1), this.parameters.dataType.get(1), 2);
/* 145 */         ArrayList<Object> valuesGT = new ArrayList();
/* 146 */         ArrayList<Object> typesGT = new ArrayList();
/* 147 */         valuesGT.add(Boolean.valueOf(resGT));
/* 148 */         typesGT.add("boolean");
/* 149 */         return new ConFunctionResult(valuesGT, typesGT);
/*     */       
/*     */       case integer_greater_than_or_equal: 
/*     */       case double_greater_than_or_equal: 
/*     */       case string_greater_than_or_equal: 
/*     */       case time_greater_than_or_equal: 
/*     */       case dateTime_greater_than_or_equal: 
/*     */       case date_greater_than_or_equal: 
/* 157 */         if (PRINT) System.out.println("GREATER THAN OR EQUAL FUNCTION ...." + this.parameters.result.size());
/* 158 */         boolean resGTEQ = equal_or_related(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 159 */           .get(1), this.parameters.dataType.get(1), 4);
/* 160 */         ArrayList<Object> valuesGTEQ = new ArrayList();
/* 161 */         ArrayList<Object> typesGTEQ = new ArrayList();
/* 162 */         valuesGTEQ.add(Boolean.valueOf(resGTEQ));
/* 163 */         typesGTEQ.add("boolean");
/* 164 */         return new ConFunctionResult(valuesGTEQ, typesGTEQ);
/*     */       
/*     */       case integer_less_than: 
/*     */       case double_less_than: 
/*     */       case string_less_than: 
/*     */       case time_less_than: 
/*     */       case date_less_than: 
/*     */       case dateTime_less_than: 
/* 172 */         if (PRINT) System.out.println("LESS THAN FUNCTION ....");
/* 173 */         boolean resLT = equal_or_related(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 174 */           .get(1), this.parameters.dataType.get(1), 3);
/* 175 */         ArrayList<Object> valuesLT = new ArrayList();
/* 176 */         ArrayList<Object> typesLT = new ArrayList();
/* 177 */         valuesLT.add(Boolean.valueOf(resLT));
/* 178 */         typesLT.add("boolean");
/* 179 */         return new ConFunctionResult(valuesLT, typesLT);
/*     */       
/*     */       case integer_less_than_or_equal: 
/*     */       case double_less_than_or_equal: 
/*     */       case string_less_than_or_equal: 
/*     */       case time_less_than_or_equal: 
/*     */       case dateTime_less_than_or_equal: 
/*     */       case date_less_than_or_equal: 
/* 187 */         if (PRINT) System.out.println("LESS THAN OR EQUAL FUNCTION ....");
/* 188 */         boolean resLTEQ = equal_or_related(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 189 */           .get(1), this.parameters.dataType.get(1), 5);
/* 190 */         ArrayList<Object> valuesLTEQ = new ArrayList();
/* 191 */         ArrayList<Object> typesLTEQ = new ArrayList();
/* 192 */         valuesLTEQ.add(Boolean.valueOf(resLTEQ));
/* 193 */         typesLTEQ.add("boolean");
/* 194 */         return new ConFunctionResult(valuesLTEQ, typesLTEQ);
/*     */       
/*     */       case string_one_and_only: 
/*     */       case integer_one_and_only: 
/*     */       case date_one_and_only: 
/*     */       case double_one_and_only: 
/*     */       case time_one_and_only: 
/* 201 */         if (PRINT) System.out.println("ONE_AND_ONLY FUNCTION ....");
/* 202 */         ArrayList<Object> valuesTOO = new ArrayList();
/* 203 */         ArrayList<Object> typesTOO = new ArrayList();
/* 204 */         valuesTOO.add(type_one_and_only(this.parameters.result.get(0), this.parameters.dataType.get(0)));
/* 205 */         typesTOO.add(this.parameters.dataType.get(0));
/* 206 */         return new ConFunctionResult(valuesTOO, typesTOO);
/*     */       
/*     */       case integer_add: 
/*     */       case double_add: 
/* 210 */         if (PRINT) System.out.println("ADD FUNCTION ....");
/* 211 */         Object resADD = arithmetic(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 212 */           .get(1), this.parameters.dataType.get(1), 1);
/* 213 */         ArrayList<Object> valuesADD = new ArrayList();
/* 214 */         ArrayList<Object> typesADD = new ArrayList();
/* 215 */         valuesADD.add(resADD);
/* 216 */         typesADD.add(this.parameters.dataType.get(0));
/* 217 */         return new ConFunctionResult(valuesADD, typesADD);
/*     */       
/*     */       case integer_subtract: 
/*     */       case double_subtract: 
/* 221 */         if (PRINT) System.out.println("SUBTRACT FUNCTION ....");
/* 222 */         Object resSUB = arithmetic(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 223 */           .get(1), this.parameters.dataType.get(1), 2);
/* 224 */         System.out.println("RESULT FROM SUB " + resSUB);
/* 225 */         ArrayList<Object> valuesSUB = new ArrayList();
/* 226 */         ArrayList<Object> typesSUB = new ArrayList();
/* 227 */         valuesSUB.add(resSUB);
/* 228 */         typesSUB.add(this.parameters.dataType.get(0));
/* 229 */         return new ConFunctionResult(valuesSUB, typesSUB);
/*     */       
/*     */       case integer_multiply: 
/*     */       case double_multiply: 
/* 233 */         if (PRINT) System.out.println("MULTIPLY FUNCTION ....");
/* 234 */         Object resMUL = arithmetic(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 235 */           .get(1), this.parameters.dataType.get(1), 3);
/* 236 */         ArrayList<Object> valuesMUL = new ArrayList();
/* 237 */         ArrayList<Object> typesMUL = new ArrayList();
/* 238 */         valuesMUL.add(resMUL);
/* 239 */         typesMUL.add(this.parameters.dataType.get(0));
/* 240 */         return new ConFunctionResult(valuesMUL, typesMUL);
/*     */       
/*     */       case integer_divide: 
/*     */       case double_divide: 
/* 244 */         if (PRINT) System.out.println("DIVIDE FUNCTION ....");
/* 245 */         Object resDIV = arithmetic(this.parameters.result.get(0), this.parameters.dataType.get(0), this.parameters.result
/* 246 */           .get(1), this.parameters.dataType.get(1), 4);
/* 247 */         ArrayList<Object> valuesDIV = new ArrayList();
/* 248 */         ArrayList<Object> typesDIV = new ArrayList();
/* 249 */         valuesDIV.add(resDIV);
/* 250 */         typesDIV.add(this.parameters.dataType.get(0));
/* 251 */         return new ConFunctionResult(valuesDIV, typesDIV);
/*     */       }
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
/* 263 */       return cfr;
/*     */     } catch (Exception e) {
/* 265 */       System.out.println("Exception in execute of XACMLFunction: " + e.getMessage()); }
/* 266 */     return null;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean equal_or_related(Object param1, Object param1Type, Object param2, Object param2Type, int operation)
/*     */     throws URISyntaxException, NumberFormatException
/*     */   {
/*     */     try
/*     */     {
/* 299 */       switch (operation) {
/*     */       case 1: 
/* 301 */         if ((param1Type.equals(param2Type)) && (param1.equals(param2))) return true;
/*     */         break;
/*     */       case 2: 
/* 304 */         if ((param1Type.equals("integer")) && (param2Type.equals("integer")))
/* 305 */           return Integer.parseInt(param1.toString()) > Integer.parseInt(param2.toString());
/* 306 */         if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 307 */           return Double.parseDouble(param1.toString()) > Double.parseDouble(param2.toString());
/*     */         break;
/*     */       case 3: 
/* 310 */         if ((param1Type.equals("integer")) && (param2Type.equals("integer")))
/* 311 */           return Integer.parseInt(param1.toString()) < Integer.parseInt(param2.toString());
/* 312 */         if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 313 */           return Double.parseDouble(param1.toString()) < Double.parseDouble(param2.toString());
/*     */         break;
/*     */       case 4: 
/* 316 */         if ((param1Type.equals("integer")) && (param2Type.equals("integer")))
/* 317 */           return Integer.parseInt(param1.toString()) >= Integer.parseInt(param2.toString());
/* 318 */         if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 319 */           return Double.parseDouble(param1.toString()) >= Double.parseDouble(param2.toString());
/*     */         break;
/*     */       case 5: 
/* 322 */         if ((param1Type.equals("integer")) && (param2Type.equals("integer")))
/* 323 */           return Integer.parseInt(param1.toString()) <= Integer.parseInt(param2.toString());
/* 324 */         if ((param1Type.equals("double")) && (param2Type.equals("double"))) {
/* 325 */           return Double.parseDouble(param1.toString()) <= Double.parseDouble(param2.toString());
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/* 330 */       return false;
/*     */     } catch (Exception e) {
/* 332 */       System.out.println("Exception in equal_or_related Function " + e.getLocalizedMessage()); }
/* 333 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object type_one_and_only(Object param, Object type)
/*     */   {
/* 340 */     return param;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object arithmetic(Object param1, Object param1Type, Object param2, Object param2Type, int operation)
/*     */   {
/* 353 */     Object result = null;
/* 354 */     switch (operation) {
/* 355 */     case 1:  if ((param1Type.equals("integer")) && (param2Type.equals("integer"))) {
/* 356 */         result = Integer.valueOf(Integer.parseInt(param1.toString()) + Integer.parseInt(param2.toString()));
/* 357 */       } else if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 358 */         result = Double.valueOf(Double.parseDouble(param1.toString()) + Double.parseDouble(param2.toString()));
/*     */       break; case 2:  if ((param1Type.equals("integer")) && (param2Type.equals("integer"))) {
/* 360 */         result = Integer.valueOf(Integer.parseInt(param1.toString()) - Integer.parseInt(param2.toString()));
/* 361 */       } else if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 362 */         result = Double.valueOf(Double.parseDouble(param1.toString()) - Double.parseDouble(param2.toString()));
/*     */       break; case 3:  if ((param1Type.equals("integer")) && (param2Type.equals("integer"))) {
/* 364 */         result = Integer.valueOf(Integer.parseInt(param1.toString()) * Integer.parseInt(param2.toString()));
/* 365 */       } else if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 366 */         result = Double.valueOf(Double.parseDouble(param1.toString()) * Double.parseDouble(param2.toString()));
/*     */       break; case 4:  if ((param1Type.equals("integer")) && (param2Type.equals("integer"))) {
/* 368 */         result = Integer.valueOf(Integer.parseInt(param1.toString()) / Integer.parseInt(param2.toString()));
/* 369 */       } else if ((param1Type.equals("double")) && (param2Type.equals("double")))
/* 370 */         result = Double.valueOf(Double.parseDouble(param1.toString()) / Double.parseDouble(param2.toString()));
/*     */       break; case 5:  if ((param1Type.equals("integer")) && (param2Type.equals("integer")))
/* 372 */         result = Integer.valueOf(Integer.parseInt(param1.toString()) % Integer.parseInt(param2.toString()));
/*     */       break; case 6:  if (param1Type.equals("integer")) { result = Integer.valueOf(Math.abs(Integer.parseInt(param1.toString())));
/* 374 */       } else if (param1Type.equals("double")) result = Double.valueOf(Math.abs(Double.parseDouble(param1.toString())));
/*     */       break; case 7:  if (param1Type.equals("integer")) { result = Integer.valueOf(Math.round(Integer.parseInt(param1.toString())));
/* 376 */       } else if (param1Type.equals("double")) result = Long.valueOf(Math.round(Double.parseDouble(param1.toString())));
/*     */       break; case 8:  if (param1Type.equals("integer")) { result = Double.valueOf(Math.floor(Integer.parseInt(param1.toString())));
/* 378 */       } else if (param1Type.equals("double")) { result = Double.valueOf(Math.floor(Double.parseDouble(param1.toString())));
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/* 383 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv2/XACMLFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */