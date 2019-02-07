/*     */ package utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ExpressionEvaluator {
/*   6 */   public static enum EXPRESSIONTYPE { Prefix,  Infix,  Postfix;
/*     */     
/*     */     private EXPRESSIONTYPE() {} }
/*   9 */   public static enum OPERATION { Subtraction,  Addition,  Modulus,  Multiplication,  Division,  Power,  LeftParenthesis,  RightParenthesis,  Equals, 
/*     */     
/*  11 */     And,  Or,  StringEqual,  NotIn,  IntegerAdd,  GreaterThan,  LessThan;
/*     */     
/*     */     private OPERATION() {} }
/*     */   
/*  15 */   private static int Precedence(String Operation) { switch (Operation.charAt(0))
/*     */     {
/*     */     case '(': 
/*     */     case ')': 
/*  19 */       return 4;
/*     */     case '^': 
/*  21 */       return 3;
/*     */     case '%': 
/*     */     case '*': 
/*     */     case '/': 
/*  25 */       return 2;
/*     */     case '+': 
/*     */     case '-': 
/*  28 */       return 1;
/*     */     }
/*     */     
/*  31 */     System.err.println("Invalid operation.");
/*  32 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   private static OPERATION GetOperation(int i)
/*     */   {
/*  38 */     switch (i)
/*     */     {
/*     */     case 0: 
/*  41 */       return OPERATION.Subtraction;
/*     */     case 1: 
/*  43 */       return OPERATION.Addition;
/*     */     case 2: 
/*  45 */       return OPERATION.Modulus;
/*     */     case 3: 
/*  47 */       return OPERATION.Multiplication;
/*     */     case 4: 
/*  49 */       return OPERATION.Division;
/*     */     case 5: 
/*  51 */       return OPERATION.Power;
/*     */     case 6: 
/*  53 */       return OPERATION.LeftParenthesis;
/*     */     case 7: 
/*  55 */       return OPERATION.RightParenthesis;
/*     */     }
/*  57 */     return OPERATION.Equals;
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
/* 101 */   public static String OPERATIONS = "\\MINUS \\PLUS \\MOD \\MULT \\DIV \\POW \\LEFTP \\RIGHTP";
/*     */   
/*     */   private ExpressionNode Root;
/*     */   
/*     */   public ExpressionEvaluator(String Expression, EXPRESSIONTYPE Type)
/*     */   {
/* 107 */     switch (Type)
/*     */     {
/*     */     case Prefix: 
/* 110 */       this.Root = ConstructPrefixExpressionTree(Expression);
/* 111 */       break;
/*     */     case Infix: 
/* 113 */       this.Root = ConstructInfixExpressionTree(Expression);
/* 114 */       break;
/*     */     case Postfix: 
/* 116 */       this.Root = ConstructPostfixExpressionTree(Expression);
/* 117 */       break;
/*     */     default: 
/* 119 */       System.err.println("Invalid expression type. Reverting to zero.");
/* 120 */       this.Root = new ExpressionNode(1, Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   private int findIndexOf(String[] strArray, String str) {
/* 125 */     for (int i = 0; i < strArray.length; i++)
/* 126 */       if (strArray[i].equals(str)) return i;
/* 127 */     return -1;
/*     */   }
/*     */   
/*     */   private ExpressionNode ConstructPrefixExpressionTree(String PrefixExpression)
/*     */   {
/* 132 */     String[] Terms = PrefixExpression.split(" ");
/* 133 */     ArrayList<ExpressionNode> Nodes = new ArrayList();
/*     */     
/*     */ 
/* 136 */     for (String Term : Terms)
/*     */     {
/*     */ 
/* 139 */       String[] operations = OPERATIONS.split(" ");
/* 140 */       if (findIndexOf(operations, Term) == -1) {
/*     */         try
/*     */         {
/* 143 */           Nodes.add(new ExpressionNode(1, Double.valueOf(Term)));
/*     */         }
/*     */         catch (NumberFormatException e) {
/* 146 */           System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
/* 147 */           Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */         }
/*     */       } else {
/* 150 */         Nodes.add(new ExpressionNode(GetOperation(
/* 151 */           findIndexOf(operations, Term))));
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 157 */       int Current = Nodes.size() - 3;
/* 158 */       while (Nodes.size() > 1)
/*     */       {
/* 160 */         if (((ExpressionNode)Nodes.get(Current)).IsOperation())
/*     */         {
/*     */ 
/* 163 */           ((ExpressionNode)Nodes.get(Current)).SetChildren((ExpressionNode)Nodes.remove(Current + 1), (ExpressionNode)Nodes.remove(Current + 1));
/* 164 */           Current = Nodes.size() - 3;
/*     */         }
/*     */         else
/*     */         {
/* 168 */           Current--;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 174 */       System.err.println("Too many/few operations. Reverting expression to zero.");
/* 175 */       Nodes.clear();
/* 176 */       Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 180 */       System.err.println(e);
/* 181 */       System.err.println("Reverting expression to zero.");
/* 182 */       Nodes.clear();
/* 183 */       Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     
/*     */ 
/* 187 */     return (ExpressionNode)Nodes.get(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ExpressionNode ConstructInfixExpressionTree(String InfixExpression)
/*     */   {
/* 195 */     int j = 0;
/* 196 */     for (int i = 0; i < InfixExpression.length(); i++)
/*     */     {
/*     */ 
/* 199 */       if (OPERATIONS.indexOf(InfixExpression.charAt(i)) != -1)
/*     */       {
/* 201 */         if (InfixExpression.charAt(i) == '(') j++;
/* 202 */         if (InfixExpression.charAt(i) == ')') j--;
/* 203 */         if (j < 0)
/*     */         {
/* 205 */           System.err.println("Opening parenthesis expected. Reverting expression to zero.");
/* 206 */           return new ExpressionNode(1, Integer.valueOf(0));
/*     */         }
/*     */         
/* 209 */         if ((i > 0) && (InfixExpression.charAt(i - 1) != ' '))
/*     */         {
/* 211 */           InfixExpression = InfixExpression.substring(0, i) + " " + InfixExpression.substring(i);
/* 212 */           i++;
/*     */         }
/* 214 */         if ((i < InfixExpression.length() - 1) && (InfixExpression.charAt(i + 1) != ' '))
/*     */         {
/* 216 */           InfixExpression = InfixExpression.substring(0, i + 1) + " " + InfixExpression.substring(i + 1);
/* 217 */           i++;
/*     */         }
/*     */       }
/*     */     }
/* 221 */     if (j > 0)
/*     */     {
/* 223 */       System.err.println("Closing parenthesis expected. Reverting expression to zero.");
/* 224 */       return new ExpressionNode(1, Integer.valueOf(0));
/*     */     }
/*     */     
/*     */ 
/* 228 */     String[] Terms = InfixExpression.split(" ");
/* 229 */     ArrayList<ExpressionNode> Nodes = new ArrayList();
/*     */     
/*     */ 
/* 232 */     for (String Term : Terms)
/*     */     {
/* 234 */       if (OPERATIONS.indexOf(Term) == -1)
/*     */       {
/*     */         try
/*     */         {
/* 238 */           Nodes.add(new ExpressionNode(1, Double.valueOf(Term)));
/*     */         }
/*     */         catch (NumberFormatException e)
/*     */         {
/* 242 */           System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
/* 243 */           Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 248 */         Nodes.add(new ExpressionNode(GetOperation(OPERATIONS.indexOf(Term))));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 253 */     Object OperationStack = new ArrayList();
/* 254 */     Object ValueStack = new ArrayList();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 259 */       for (int i = 0; i < Nodes.size(); i++)
/*     */       {
/* 261 */         switch (((ExpressionNode)Nodes.get(i)).toString().charAt(0))
/*     */         {
/*     */         case '(': 
/* 264 */           ((ArrayList)OperationStack).add(0, Nodes.get(i));
/* 265 */           break;
/*     */         
/*     */         case ')': 
/* 268 */           while (!((ExpressionNode)((ArrayList)OperationStack).get(0)).toString().equals("("))
/*     */           {
/* 270 */             ((ArrayList)ValueStack).add(0, ((ArrayList)OperationStack).remove(0));
/* 271 */             ((ExpressionNode)((ArrayList)ValueStack).get(0)).SetChildren((ExpressionNode)((ArrayList)ValueStack).remove(2), (ExpressionNode)((ArrayList)ValueStack).remove(1));
/*     */           }
/* 273 */           ((ArrayList)OperationStack).remove(0);
/* 274 */           break;
/*     */         
/*     */         case '%': 
/*     */         case '*': 
/*     */         case '+': 
/*     */         case '-': 
/*     */         case '/': 
/*     */         case '^': 
/* 282 */           while ((((ArrayList)OperationStack).size() > 0) && (!((ExpressionNode)((ArrayList)OperationStack).get(0)).toString().equals("(")) && (Precedence(((ExpressionNode)Nodes.get(i)).toString()) <= Precedence(((ExpressionNode)((ArrayList)OperationStack).get(0)).toString())))
/*     */           {
/* 284 */             ((ArrayList)ValueStack).add(0, ((ArrayList)OperationStack).remove(0));
/* 285 */             ((ExpressionNode)((ArrayList)ValueStack).get(0)).SetChildren((ExpressionNode)((ArrayList)ValueStack).remove(2), (ExpressionNode)((ArrayList)ValueStack).remove(1));
/*     */           }
/* 287 */           ((ArrayList)OperationStack).add(0, Nodes.get(i));
/* 288 */           break;
/*     */         default: 
/* 290 */           ((ArrayList)ValueStack).add(0, Nodes.get(i));
/*     */         }
/*     */         
/*     */       }
/* 294 */       while (((ArrayList)OperationStack).size() > 0)
/*     */       {
/* 296 */         ((ArrayList)ValueStack).add(0, ((ArrayList)OperationStack).remove(0));
/* 297 */         ((ExpressionNode)((ArrayList)ValueStack).get(0)).SetChildren((ExpressionNode)((ArrayList)ValueStack).remove(2), (ExpressionNode)((ArrayList)ValueStack).remove(1));
/*     */       }
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 302 */       System.err.println("Too many/few operations. Reverting expression to zero.");
/* 303 */       ((ArrayList)ValueStack).clear();
/* 304 */       ((ArrayList)ValueStack).add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 308 */       System.err.println(e);
/* 309 */       System.err.println("Reverting expression to zero.");
/* 310 */       ((ArrayList)ValueStack).clear();
/* 311 */       ((ArrayList)ValueStack).add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     
/*     */ 
/* 315 */     return (ExpressionNode)((ArrayList)ValueStack).get(0);
/*     */   }
/*     */   
/*     */   private ExpressionNode ConstructPostfixExpressionTree(String PostfixExpression) {
/* 319 */     String[] Terms = PostfixExpression.split(" ");
/* 320 */     ArrayList<ExpressionNode> Nodes = new ArrayList();
/*     */     
/*     */ 
/* 323 */     for (String Term : Terms)
/*     */     {
/* 325 */       String[] operations = OPERATIONS.split(" ");
/* 326 */       if (findIndexOf(operations, Term) == -1) {
/*     */         try {
/* 328 */           Nodes.add(new ExpressionNode(1, Double.valueOf(Term)));
/*     */         }
/*     */         catch (NumberFormatException e) {
/* 331 */           System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
/* 332 */           Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */         }
/*     */         
/*     */       } else {
/* 336 */         Nodes.add(new ExpressionNode(GetOperation(findIndexOf(operations, Term))));
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 342 */       int Current = 2;
/* 343 */       while (Nodes.size() > 1)
/*     */       {
/* 345 */         if (((ExpressionNode)Nodes.get(Current)).IsOperation())
/*     */         {
/*     */ 
/* 348 */           ((ExpressionNode)Nodes.get(Current)).SetChildren((ExpressionNode)Nodes.remove(Current - 2), (ExpressionNode)Nodes.remove(Current - 2));
/* 349 */           Current = 2;
/*     */         }
/*     */         else
/*     */         {
/* 353 */           Current++;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 359 */       System.err.println("Too many/few operations. Reverting expression to zero.");
/* 360 */       Nodes.clear();
/* 361 */       Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 365 */       System.err.println(e);
/* 366 */       System.err.println("Reverting expression to zero.");
/* 367 */       Nodes.clear();
/* 368 */       Nodes.add(new ExpressionNode(1, Integer.valueOf(0)));
/*     */     }
/*     */     
/*     */ 
/* 372 */     return (ExpressionNode)Nodes.get(0);
/*     */   }
/*     */   
/* 375 */   public String GetPrefixExpression() { return this.Root.GetPrefixExpression(); }
/* 376 */   public String GetInfixExpression() { return this.Root.GetInfixExpression(); }
/* 377 */   public String GetPostfixExpression() { return this.Root.GetPostfixExpression(); }
/* 378 */   public Pair<Integer, Object> GetValue() { return this.Root.GetValue(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 385 */     ExpressionEvaluator ev = new ExpressionEvaluator("3 \\PLUS 5 \\MINUS 5 \\MULT 2", EXPRESSIONTYPE.Infix);
/*     */     
/*     */ 
/* 388 */     System.out.println("Prefix:" + ev.GetPrefixExpression());
/*     */   }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/ExpressionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */