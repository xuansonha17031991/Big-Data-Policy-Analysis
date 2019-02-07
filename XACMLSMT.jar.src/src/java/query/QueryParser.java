package query;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.math.NumberUtils;

import utils.Pair;
import functions.FunctionFactory;

public class QueryParser {
	public static enum EXPRESSIONTYPE {
		Prefix, Infix, Postfix;

		private EXPRESSIONTYPE() {
		}
	}

	public static enum OPERATION {
		Subtraction, Addition, Modulus, Multiplication, Division, Power, LeftParenthesis, RightParenthesis, Equals, Replace,

		And, Or, Implies, Neg, ForAll, Exists, Rest, Ins,

		StringEqual, NotIn, IntegerAdd, GreaterThan, LessThan;

		private OPERATION() {
		}
	}

	public static java.util.Map<String, Integer> specialNodes = new HashMap();

	private static int Precedence(OPERATION Operation) {
		switch (Operation) {
		case LeftParenthesis:
		case RightParenthesis:
			return 10;
		case Equals:
			return 9;
		case Power:
			return 8;
		case Division:
		case Multiplication:
		case Modulus:
			return 7;
		case Addition:
		case Subtraction:
			return 6;
		case Neg:
		case Rest:
		case Replace:
		case Ins:
			return 5;
		case ForAll:
		case Exists:
			return 4;
		case And:
			return 3;
		case Or:
			return 2;
		case Implies:
			return 1;
		}

		System.err.println("Invalid operation.");
		return -1;
	}

	private static OPERATION GetOperation(int i) {
		switch (i) {
		case 0:
			return OPERATION.Subtraction;
		case 1:
			return OPERATION.Addition;
		case 2:
			return OPERATION.Modulus;
		case 3:
			return OPERATION.Multiplication;
		case 4:
			return OPERATION.Division;
		case 5:
			return OPERATION.Power;
		case 6:
			return OPERATION.LeftParenthesis;
		case 7:
			return OPERATION.RightParenthesis;
		case 8:
			return OPERATION.Equals;
		case 9:
			return OPERATION.And;
		case 10:
			return OPERATION.Or;
		case 11:
			return OPERATION.Implies;
		case 12:
			return OPERATION.Neg;
		case 13:
			return OPERATION.ForAll;
		case 14:
			return OPERATION.Exists;
		case 15:
			return OPERATION.Rest;
		case 16:
			return OPERATION.Ins;
		case 17:
			return OPERATION.Replace;
		}

		return null;
	}

	public static String[] OPERATIONS = { "-", "+", "%", "*", "/", "^", "(", ")", "=", "\\WEDGE", "\\VEE", "\\IMPLIES", "\\NEG", "\\FORALL",
			"\\EXISTS", "\\REST", "\\INS", "\\REPLC" };

	public QueryNode Root;

	private final HashMap<String, QueryNode> nodes;

	public QueryParser(String Expression, EXPRESSIONTYPE Type) {
		this.nodes = new HashMap();
		specialNodes.put("\\emptyset", Integer.valueOf(1));

		switch (Type) {
		case Prefix:
			this.Root = ConstructPrefixExpressionTree(Expression);
			break;
		case Infix:
			this.Root = ConstructInfixExpressionTree(Expression);
			break;
		case Postfix:
			this.Root = ConstructPostfixExpressionTree(Expression);
			break;
		default:
			System.err.println("Invalid expression type. Reverting to zero.");
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			this.Root = qNode;
		}
	}

	public HashMap<String, QueryNode> getNodes() {
		return this.nodes;
	}

	private static int findIndexOf(String str, String before, String after) {
		for (int i = 0; i < OPERATIONS.length; i++)
			if (OPERATIONS[i].equals(str)) {
				if (((str.equals("-")) || (str.equals("+")) || (str.equals("*")) || (str.equals("%")) || (str.equals("/")) || (str.equals("^")))
						&& ((!before.equals(" ")) || (!after.equals(" ")))) {
					return -1;
				}
				return i;
			}
		return -1;
	}

	private QueryNode ConstructPrefixExpressionTree(String PrefixExpression) {
		String[] Terms = PrefixExpression.split(" ");
		ArrayList<QueryNode> Nodes = new ArrayList();

		for (int i = 0; i < Terms.length; i++) {
			String Term = Terms[i];
			String before = i == 0 ? Term : Terms[(i - 1)];
			String after = i + 1 >= Terms.length ? Term : Terms[(i + 1)];

			if (findIndexOf(Term, before, after) == -1) {
				try {
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Double.valueOf(Term));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				} catch (NumberFormatException e) {
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			} else {
				QueryNode qNode = new QueryNode(GetOperation(findIndexOf(Term, before, after)));
				this.nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}

		try {
			int Current = Nodes.size() - 3;
			while (Nodes.size() > 1) {
				if (Nodes.get(Current).IsOperation()) {

					Nodes.get(Current).SetChildren(Nodes.remove(Current + 1), Nodes.remove(Current + 1));
					Current = Nodes.size() - 3;
				} else {
					Current--;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		}

		return Nodes.get(0);
	}

	private QueryNode ConstructInfixExpressionTree(String InfixExpression) {
		int j = 0;
		int i = 0;
		while (i < InfixExpression.length()) {

			String str = String.valueOf(InfixExpression.charAt(i));
			String before = i == 0 ? str : String.valueOf(InfixExpression.charAt(i - 1));
			String after = i + 1 >= InfixExpression.length() ? str : String.valueOf(InfixExpression.charAt(i + 1));
			boolean specialOp = false;
			if (str.equals("\\")) {
				i++;
				while (InfixExpression.charAt(i) != ' ') {
					str = str + InfixExpression.charAt(i);
					i++;
				}
				specialOp = true;
			}

			if (findIndexOf(str, before, after) != -1) {
				if (str.equals("("))
					j++;
				if (str.equals(")"))
					j--;
				if (j < 0) {
					System.err.println("Opening parenthesis expected. Reverting expression to zero.");
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					return qNode;
				}

				if ((!specialOp) && (i > 0) && (InfixExpression.charAt(i - 1) != ' ')) {

					InfixExpression = InfixExpression.substring(0, i) + " " + InfixExpression.substring(i);
					i++;
				}

				if ((!specialOp) && (i < InfixExpression.length() - 1) && (InfixExpression.charAt(i + 1) != ' ')) {
					InfixExpression = InfixExpression.substring(0, i + 1) + " " + InfixExpression.substring(i + 1);
					i++;
				}
			}
			i++;
		}
		if (j > 0) {
			System.err.println("Closing parenthesis expected. Reverting expression to zero.");
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			return qNode;
		}

		String[] Terms = InfixExpression.split(" +");
		if (QueryRunner.debug) {
			System.out.println("INFIX (QUERY) : " + InfixExpression);
			for (int z = 0; z < Terms.length; z++)
				System.out.print(Terms[z]);
		}
		ArrayList<QueryNode> Nodes = new ArrayList();

		for (int k = 0; k < Terms.length; k++) {
			String Term = Terms[k];
			String before = k == 0 ? Term : Terms[(k - 1)];
			String after = k + 1 >= Terms.length ? Term : Terms[(k + 1)];
			if (findIndexOf(Term, before, after) == -1) {
				try {
					QueryNode qNode;
					if (NumberUtils.isNumber(Term)) {
						qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Term.trim());
					} else {
						qNode = new QueryNode(FunctionFactory.DataTypes.xstring, Term.trim());
					}
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				} catch (NumberFormatException e) {
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			} else {
				QueryNode qNode = new QueryNode(GetOperation(findIndexOf(Term.trim(), before, after)));
				this.nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}

		ArrayList<QueryNode> OperationStack = new ArrayList();
		ArrayList<QueryNode> ValueStack = new ArrayList();

		try {
			for (i = 0; i < Nodes.size(); i++) {
				if (Nodes.get(i).getOperation() != null) {
					switch (Nodes.get(i).getOperation()) {
					case LeftParenthesis:
						OperationStack.add(0, Nodes.get(i));
						break;

					case RightParenthesis:
						while (!OperationStack.get(0).getOperation().toString().equals("LeftParenthesis")) {
							QueryNode op = OperationStack.remove(0);
							ValueStack.add(0, op);
							if (op.isUnary()) {
								ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
							} else
								ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
						}
						OperationStack.remove(0);
						break;

					case Equals:
					case Replace:
					case Power:
					case Division:
					case Multiplication:
					case Modulus:
					case Addition:
					case Subtraction:
					case Rest:
					case Ins:
					case ForAll:
					case Exists:
					case And:
					case Or:
					case Implies:
						while ((OperationStack.size() > 0) && (!OperationStack.get(0).getOperation().toString().equals("LeftParenthesis"))
								&& (Precedence(Nodes.get(i).getOperation()) <= Precedence(OperationStack.get(0).getOperation()))) {
							QueryNode op = OperationStack.remove(0);
							ValueStack.add(0, op);
							if (op.isUnary())
								ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
							else
								ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
						}
						OperationStack.add(0, Nodes.get(i));
						break;

					case Neg:
						while ((OperationStack.size() > 0) && (!OperationStack.get(0).getOperation().toString().equals("LeftParenthesis"))
								&& (Precedence(Nodes.get(i).getOperation()) <= Precedence(OperationStack.get(0).getOperation()))) {
							ValueStack.add(0, OperationStack.remove(0));
							ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
						}
						OperationStack.add(0, Nodes.get(i));
						break;

					}

				} else {
					ValueStack.add(0, Nodes.get(i));
				}
			}

			while (OperationStack.size() > 0) {
				QueryNode op = OperationStack.remove(0);
				ValueStack.add(0, op);
				if (op.isUnary())
					ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
				else {
					ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));

				}

			}

		} catch (IndexOutOfBoundsException e) {

			System.err.println("Too many/few operations. Reverting expression to zero." + e.getLocalizedMessage());
			ValueStack.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			ValueStack.add(qNode);
			return null;
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Reverting expression to zero." + e.getLocalizedMessage());
			ValueStack.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			ValueStack.add(qNode);
			return null;
		}

		return ValueStack.get(0);
	}

	private QueryNode ConstructPostfixExpressionTree(String PostfixExpression) {
		String[] Terms = PostfixExpression.split(" ");
		ArrayList<QueryNode> Nodes = new ArrayList();

		for (int i = 0; i < Terms.length; i++) {
			String Term = Terms[i];
			String before = i == 0 ? Term : Terms[(i - 1)];
			String after = i + 1 >= Terms.length ? Term : Terms[(i + 1)];

			if (findIndexOf(Term, before, after) == -1) {
				try {
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Double.valueOf(Term));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				} catch (NumberFormatException e) {
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
					this.nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			} else {
				QueryNode qNode = new QueryNode(GetOperation(findIndexOf(Term, before, after)));
				this.nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}

		try {
			int Current = 2;
			while (Nodes.size() > 1) {
				if (Nodes.get(Current).IsOperation()) {

					Nodes.get(Current).SetChildren(Nodes.remove(Current - 2), Nodes.remove(Current - 2));
					Current = 2;
				} else {
					Current++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
			return null;
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(FunctionFactory.DataTypes.xdouble, Integer.valueOf(0));
			this.nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
			return null;
		}

		return Nodes.get(0);
	}

	public String GetPrefixExpression() {
		return this.Root.GetPrefixExpression();
	}

	public String GetInfixExpression() {
		return this.Root.GetInfixExpression();
	}

	public String GetPostfixExpression() {
		return this.Root.GetPostfixExpression();
	}

	public Pair<FunctionFactory.DataTypes, Object> GetValue() {
		return this.Root.convert2JavaObjects();
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/query/QueryParser.class Java
 * compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */