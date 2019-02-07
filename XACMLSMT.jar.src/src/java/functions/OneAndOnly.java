package functions;

import java.util.Map;

import objects.Expression;

public class OneAndOnly extends Function {
	Map<Expression, FunctionFactory.DataTypes> parameterMap;

	public OneAndOnly(String id) {
		setId(id);
	}

	@Override
	public Boolean evaluate() {
		return Boolean.valueOf(true);
	}

	public String getDataType() {
		return FunctionFactory.DataTypes.xboolean.toString();
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/functions/OneAndOnly.class Java
 * compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */