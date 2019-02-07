/*     */ package utils;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpressionNode
/*     */ {
/*     */   private Pair<Integer, Object> valueAndType;
/*     */   private ExpressionEvaluator.OPERATION Operation;
/*     */   private ExpressionNode Left;
/*     */   private ExpressionNode Right;
/*     */   
/*     */   public ExpressionNode(int type, Object value)
/*     */   {
/*  16 */     this.Left = null;
/*  17 */     this.Right = null;
/*  18 */     this.valueAndType = new Pair(Integer.valueOf(type), value);
/*     */     
/*     */ 
/*  21 */     this.Operation = ExpressionEvaluator.OPERATION.Equals;
/*     */   }
/*     */   
/*     */   public ExpressionNode(ExpressionEvaluator.OPERATION Operation) {
/*  25 */     this(Operation, null, null);
/*     */   }
/*     */   
/*     */   public ExpressionNode(ExpressionEvaluator.OPERATION Operation, ExpressionNode Left, ExpressionNode Right) {
/*  29 */     this.valueAndType = null;
/*  30 */     if (Operation == ExpressionEvaluator.OPERATION.Equals) {
/*  31 */       System.err.println("You cannot explicitly define an Equals node.");
/*  32 */       System.exit(1);
/*     */     }
/*     */     
/*  35 */     this.Operation = Operation;
/*  36 */     this.Left = Left;
/*  37 */     this.Right = Right;
/*     */   }
/*     */   
/*     */   public void SetChildren(ExpressionNode Left, ExpressionNode Right) {
/*  41 */     this.Left = Left;
/*  42 */     this.Right = Right;
/*     */   }
/*     */   
/*     */   public Pair<Integer, Object> GetValue()
/*     */   {
/*  47 */     switch (this.Operation) {
/*     */     case Equals: 
/*     */       break;
/*  50 */     case Power:  this.valueAndType = new Pair(Integer.valueOf(1), Double.valueOf(Math.pow(Double.parseDouble(this.Left.GetValue().second.toString()), 
/*  51 */         Double.parseDouble(this.Right.GetValue().second.toString()))));
/*  52 */       break;
/*     */     case Division: 
/*  54 */       this.valueAndType = new Pair(Integer.valueOf(1), Double.valueOf(Double.parseDouble(this.Left.GetValue().second.toString()) / 
/*  55 */         Double.parseDouble(this.Right.GetValue().second.toString())));
/*  56 */       break;
/*     */     case Multiplication: 
/*  58 */       this.valueAndType = new Pair(Integer.valueOf(1), Double.valueOf(Double.parseDouble(this.Left.GetValue().second.toString()) * 
/*  59 */         Double.parseDouble(this.Right.GetValue().second.toString())));
/*  60 */       break;
/*     */     case Modulus: 
/*  62 */       this.valueAndType = new Pair(Integer.valueOf(1), Integer.valueOf((int)Double.parseDouble(this.Left.GetValue().second.toString()) % 
/*  63 */         (int)Double.parseDouble(this.Right.GetValue().second.toString())));
/*  64 */       break;
/*     */     case Addition: 
/*  66 */       this.valueAndType = new Pair(Integer.valueOf(1), Double.valueOf(Double.parseDouble(this.Left.GetValue().second.toString()) + 
/*  67 */         Double.parseDouble(this.Right.GetValue().second.toString())));
/*  68 */       break;
/*     */     case Subtraction: 
/*  70 */       this.valueAndType = new Pair(Integer.valueOf(1), Double.valueOf(Double.parseDouble(this.Left.GetValue().second.toString()) - 
/*  71 */         Double.parseDouble(this.Right.GetValue().second.toString())));
/*  72 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case And: 
/*  77 */       this.valueAndType = new Pair(Integer.valueOf(4), Boolean.valueOf((Boolean.parseBoolean(this.Left.GetValue().second.toString())) && 
/*  78 */         (Boolean.parseBoolean(this.Right.GetValue().second.toString()))));
/*  79 */       break;
/*     */     
/*     */     case Or: 
/*  82 */       this.valueAndType = new Pair(Integer.valueOf(4), Boolean.valueOf((Boolean.parseBoolean(this.Left.GetValue().second.toString())) || 
/*  83 */         (Boolean.parseBoolean(this.Right.GetValue().second.toString()))));
/*  84 */       break;
/*     */     case StringEqual: 
/*     */       break;
/*     */     case NotIn: 
/*     */       break;
/*     */     case IntegerAdd: 
/*     */       break;
/*     */     case GreaterThan: 
/*     */       break;
/*     */     case LessThan:  break; default:  this.valueAndType = null;
/*     */     }
/*     */     
/*  96 */     return this.valueAndType;
/*     */   }
/*     */   
/*     */ 
/*     */   private String GetOperation()
/*     */   {
/* 102 */     String[] operations = ExpressionEvaluator.OPERATIONS.split(" ");
/* 103 */     String op = operations[this.Operation.ordinal()];
/* 104 */     return op.substring(op.indexOf("\\") + 2, op.length()).trim();
/*     */   }
/*     */   
/*     */   public String GetPrefixExpression()
/*     */   {
/* 109 */     if (this.Operation == ExpressionEvaluator.OPERATION.Equals) {
/* 110 */       return this.valueAndType.second.toString();
/*     */     }
/*     */     
/* 113 */     return GetOperation() + " " + this.Left.GetPrefixExpression() + " " + this.Right.GetPrefixExpression();
/*     */   }
/*     */   
/*     */   public String GetInfixExpression()
/*     */   {
/* 118 */     if (this.Operation == ExpressionEvaluator.OPERATION.Equals) {
/* 119 */       return this.valueAndType.second.toString();
/*     */     }
/*     */     
/*     */ 
/* 123 */     return "(" + this.Left.GetInfixExpression() + " " + GetOperation() + " " + this.Right.GetInfixExpression() + ")";
/*     */   }
/*     */   
/*     */   public String GetPostfixExpression() {
/* 127 */     if (this.Operation == ExpressionEvaluator.OPERATION.Equals) {
/* 128 */       return this.valueAndType.second.toString();
/*     */     }
/* 130 */     return this.Left.GetPostfixExpression() + " " + this.Right.GetPostfixExpression() + " " + GetOperation();
/*     */   }
/*     */   
/*     */   public boolean IsOperation()
/*     */   {
/* 135 */     return (this.Operation != ExpressionEvaluator.OPERATION.Equals) && (this.Left == null);
/*     */   }
/*     */   
/* 138 */   public String toString() { return this.Operation != ExpressionEvaluator.OPERATION.Equals ? GetOperation() : this.valueAndType.second.toString(); }
/*     */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/ExpressionNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */