package functions;

import java.util.HashMap;
import java.util.Map;

import utils.StringUtils;

public class FunctionFactory {
	public static enum FunctionTypes {
		OneAndOnly, Equal, Add, Subtract, LessOrEqual, GreaterOrEqual, Match, And, LessThan, Or, Not, IsIn, GreaterThan, Bag, Intersection, AtLeastOneMemberOf, TimeInRange;
		private FunctionTypes() {
		}
	}

	public static enum DataTypes {
		xstring, xboolean, xinteger, xdouble, xdate, xtime, xrfc822name, xanyuri, xpathexpression;

		private DataTypes() {
		}
	}

	public static final Map<String, FunctionTypes> functionNames = new HashMap();
	public static final Map<String, DataTypes> typeNames = new HashMap();

	static {
		typeNames.put("string", DataTypes.xstring);
		typeNames.put("boolean", DataTypes.xboolean);
		typeNames.put("integer", DataTypes.xinteger);
		typeNames.put("double", DataTypes.xdouble);
		typeNames.put("date", DataTypes.xdate);
		typeNames.put("time", DataTypes.xtime);
		typeNames.put("rfc822Name", DataTypes.xrfc822name);
		typeNames.put("anyURI", DataTypes.xanyuri);
		typeNames.put("xpathExpression", DataTypes.xpathexpression);

		functionNames.put("string-one-and-only", FunctionTypes.OneAndOnly);
		functionNames.put("integer-one-and-only", FunctionTypes.OneAndOnly);
		functionNames.put("date-one-and-only", FunctionTypes.OneAndOnly);
		functionNames.put("double-one-and-only", FunctionTypes.OneAndOnly);

		functionNames.put("date-less-than-or-equal", FunctionTypes.LessOrEqual);
		functionNames.put("integer-less-than-or-equal", FunctionTypes.LessOrEqual);
		functionNames.put("double-less-than-or-equal", FunctionTypes.LessOrEqual);
		functionNames.put("date-greater-than-or-equal", FunctionTypes.GreaterOrEqual);
		functionNames.put("integer-greater-than-or-equal", FunctionTypes.GreaterOrEqual);
		functionNames.put("double-greater-than-or-equal", FunctionTypes.GreaterOrEqual);
		functionNames.put("double-less-than", FunctionTypes.LessThan);
		functionNames.put("integer-less-than", FunctionTypes.LessThan);
		functionNames.put("integer-greater-than", FunctionTypes.GreaterThan);
		functionNames.put("double-greater-than", FunctionTypes.GreaterThan);

		functionNames.put("boolean-equal", FunctionTypes.Equal);
		functionNames.put("string-equal", FunctionTypes.Equal);

		functionNames.put("double-add", FunctionTypes.Add);
		functionNames.put("integer-add", FunctionTypes.Add);
		functionNames.put("double-subtract", FunctionTypes.Subtract);
		functionNames.put("integer-subtract", FunctionTypes.Subtract);

		functionNames.put("date-add-yearMonthDuration", FunctionTypes.Add);
		functionNames.put("anyURI-equal", FunctionTypes.Equal);
		functionNames.put("xpath-node-match", FunctionTypes.Match);
		functionNames.put("rfc822Name-match", FunctionTypes.Match);

		functionNames.put("string-is-in", FunctionTypes.IsIn);
		functionNames.put("string-bag", FunctionTypes.Bag);
		functionNames.put("and", FunctionTypes.And);
		functionNames.put("or", FunctionTypes.Or);
		functionNames.put("not", FunctionTypes.Not);

		functionNames.put("string-at-least-one-member-of", FunctionTypes.AtLeastOneMemberOf);

		functionNames.put("time-in-range", FunctionTypes.TimeInRange);
	}

	public static String findTypeName(DataTypes dt) {
		String typeName = null;
		for (Map.Entry<String, DataTypes> entry : typeNames.entrySet()) {
			if (entry.getValue() == dt) {
				typeName = entry.getKey();
				break;
			}
		}
		return typeName;
	}

	public Function create(String id) {
		FunctionTypes funcName = functionNames.get(StringUtils.getShortName(id));
		Function result = null;
		if (funcName == null) {
			result = new DummyFunction(id, true);
			return result;
		}

		switch (funcName) {
		case OneAndOnly:
			result = new OneAndOnly(id);
			return result;
		case Equal:
			result = new Equal(id);
			return result;
		case Add:
			result = new DummyFunction(id, false);
			return result;
		case LessOrEqual:
			result = new DummyFunction(id, false);
			return result;
		case GreaterOrEqual:
			result = new DummyFunction(id, false);
			return result;
		case Match:
			result = new DummyFunction(id, false);
			return result;
		case LessThan:
			result = new DummyFunction(id, false);
			return result;
		case And:
			result = new DummyFunction(id, false);
			return result;
		case Or:
			result = new DummyFunction(id, false);
			return result;
		case Not:
			result = new DummyFunction(id, false);
			return result;
		case IsIn:
			result = new DummyFunction(id, false);
			return result;
		case GreaterThan:
			result = new DummyFunction(id, false);
			return result;
		case Bag:
			result = new DummyFunction(id, false);
			return result;
		case AtLeastOneMemberOf:
			result = new DummyFunction(id, false);
			return result;
		case TimeInRange:
			result = new DummyFunction(id, false);
			return result;
		case Intersection:
			result = new DummyFunction(id, false);
			return result;
		}

		System.err.println("This function is not supported yet");
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/functions/FunctionFactory.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */