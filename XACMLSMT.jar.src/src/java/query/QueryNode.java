package query;

import utils.Pair;
import functions.FunctionFactory;

public class QueryNode {
	private Pair<FunctionFactory.DataTypes, Object> typeAndValue;
	private final QueryParser.OPERATION Operation;
	private QueryNode Left = null;
	private QueryNode Right = null;

	private final Pair<String, String> identifier;

	private static int uniqueCounter;

	public QueryNode(FunctionFactory.DataTypes type, Object value) {
		this.typeAndValue = new Pair(type, value);

		this.identifier = new Pair(value.toString() + "_" + uniqueCounter, value.toString());
		uniqueCounter += 1;

		this.Operation = null;
	}

	public QueryNode(QueryParser.OPERATION Operation) {
		this.typeAndValue = null;

		if ((Operation == QueryParser.OPERATION.Equals) && (QueryRunner.debug)) {
			System.err.println("We have Equality : (THINK in Case Exception: You cannot explicitly define an Equals node.)");
		}

		this.identifier = new Pair(Operation.toString() + "_" + uniqueCounter, Operation.toString());
		uniqueCounter += 1;

		this.Operation = Operation;
	}

	public Pair<String, String> getIdentifier() {
		return this.identifier;
	}

	public QueryParser.OPERATION getOperation() {
		return this.Operation;
	}

	public Pair getTypeAndValue() {
		return this.typeAndValue;
	}

	public boolean isUnary() {
		if (this.Operation == null) {
			System.err.println("Not an Operation so not UNARY");
			return false;
		}

		if (this.Operation == QueryParser.OPERATION.Neg)
			return true;
		return false;
	}

	@Override
	public String toString() {
		if (this.Operation != null)
			return getOperationString();
		if (this.typeAndValue != null) {
			return "(" + this.typeAndValue.second.toString() + this.typeAndValue.first.name() + ")";
		}
		return null;
	}

	public QueryNode getLeft() {
		return this.Left;
	}

	public QueryNode getRight() {
		return this.Right;
	}

	private String getOperationString() {
		switch (this.Operation) {
		case LeftParenthesis:
			return "(";
		case RightParenthesis:
			return ")";
		case Subtraction:
			return "-";
		case Addition:
			return "+";
		case Modulus:
			return "%";
		case Multiplication:
			return "*";
		case Division:
			return "/";
		case Power:
			return "^";
		case Equals:
			return "=";
		case Replace:
			return "\\REPLC";
		case And:
			return "\\WEDGE";
		case Or:
			return "\\VEE";
		case Implies:
			return "\\IMPLIES";
		case ForAll:
			return "\\FORALL";
		case Exists:
			return "\\EXISTS";
		case Rest:
			return "\\REST";
		case Ins:
			return "\\INS";
		case Neg:
			return "\\NEG";
		}
		System.out.println("Unknown operation");
		return null;
	}

	public void SetChildren(QueryNode Left, QueryNode Right) {
		this.Left = Left;
		this.Right = Right;
	}

	public Pair<FunctionFactory.DataTypes, Object> convert2JavaObjects() {
		switch (this.Operation) {
		case Equals:
		case Replace:
			break;

		case Power:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Double.valueOf(Math.pow(
					Double.parseDouble(this.Left.convert2JavaObjects().second.toString()),
					Double.parseDouble(this.Right.convert2JavaObjects().second.toString()))));
			break;

		case Division:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Double.valueOf(Double.parseDouble(this.Left.convert2JavaObjects().second
					.toString()) / Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
			break;

		case Multiplication:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Double.valueOf(Double.parseDouble(this.Left.convert2JavaObjects().second
					.toString()) * Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
			break;

		case Modulus:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Integer.valueOf((int) Double.parseDouble(this.Left.convert2JavaObjects().second
					.toString()) % (int) Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
			break;

		case Addition:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Double.valueOf(Double.parseDouble(this.Left.convert2JavaObjects().second
					.toString()) + Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
			break;

		case Subtraction:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Double.valueOf(Double.parseDouble(this.Left.convert2JavaObjects().second
					.toString()) - Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
			break;

		case And:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xboolean, Boolean.valueOf((Boolean.parseBoolean(this.Left.convert2JavaObjects().second
					.toString())) && (Boolean.parseBoolean(this.Right.convert2JavaObjects().second.toString()))));
			break;

		case Or:
			this.typeAndValue = new Pair(FunctionFactory.DataTypes.xdouble, Boolean.valueOf((Boolean.parseBoolean(this.Left.convert2JavaObjects().second
					.toString())) || (Boolean.parseBoolean(this.Right.convert2JavaObjects().second.toString()))));
			break;
		case StringEqual:
			break;
		case NotIn:
			break;
		case IntegerAdd:
			break;
		case GreaterThan:
			break;
		case LessThan:
			break;
		case Implies:
		case ForAll:
		case Exists:
		case Rest:
		case Ins:
		case Neg:
		default:
			this.typeAndValue = null;
		}

		return this.typeAndValue;
	}

	public String GetPrefixExpression() {
		if (this.Operation == null) {
			return this.typeAndValue.second.toString();
		}

		return getOperationString() + " " + this.Left.GetPrefixExpression() + " " + this.Right.GetPrefixExpression();
	}

	public String GetInfixExpression() {
		if (this.Operation == null) {
			return this.typeAndValue.second.toString();
		}

		return "(" + this.Left.GetInfixExpression() + " " + getOperationString() + " " + this.Right.GetInfixExpression() + ")";
	}

	public String GetPostfixExpression() {
		if (this.Operation == null) {
			return this.typeAndValue.second.toString();
		}

		if (isUnary())
			return this.Left.GetPostfixExpression() + " " + getOperationString();
		return this.Left.GetPostfixExpression() + " " + this.Right.GetPostfixExpression() + " " + getOperationString();
	}

	public boolean IsOperation() {
		return (this.Operation != QueryParser.OPERATION.Equals) && (this.Left == null);
	}
}

/*
 * Location: /Users/okielabackend/Downloads/XACMLSMT.jar!/query/QueryNode.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */