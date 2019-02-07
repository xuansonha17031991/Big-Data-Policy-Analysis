package smtfunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.Expression;
import translator.XACMLTranslator;
import utils.StringUtils;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Z3Exception;

import functions.FunctionFactory;

public class SMTFunctionFactory {
	int version;
	static Map<String, FuncDecl> userDefinedFunctions;
	XACMLTranslator custObj;

	public SMTFunctionFactory(int v, XACMLTranslator c) {
		this.version = v;
		userDefinedFunctions = new HashMap();
		this.custObj = c;
	}

	public static Map<String, FuncDecl> getUserDefinedFunctions() {
		return userDefinedFunctions;
	}

	private SMTFunction.Z3FuncExpr[] generateParamFuncExpr(Expression ex, Sort userDefinedFunSort) {
		List<Expression> params = ex.getParameters();
		List<SMTFunction.Z3FuncExpr> allExpressions = new ArrayList();

		for (int i = 0; i < params.size(); i++) {
			Sort s = null;
			if (userDefinedFunSort != null)
				s = userDefinedFunSort;
			s = SMTFunction.findParameterSort(StringUtils.getShortName(ex.getId()), this.custObj.getListOfsorts(), this.custObj.getContext());

			SMTFunction.Z3FuncExpr expr = this.custObj.generateFuncExpr(params.get(i), s);

			allExpressions.add(expr);
		}

		if (allExpressions.size() > 0)
			return allExpressions.toArray(new SMTFunction.Z3FuncExpr[allExpressions.size()]);
		return null;
	}

	private Sort[] findSorts(Expr[] expressions) {
		try {
			Sort[] parameterSorts = new Sort[expressions.length];
			for (int i = 0; i < expressions.length; i++) {
				parameterSorts[i] = expressions[i].getSort();
			}
			return parameterSorts;
		} catch (Z3Exception zex) {
			System.err.println("Exception in findSorts of SMTFunctionFactory" + zex.getMessage());
		}
		return null;
	}

	private List<SMTFunction.Z3FuncExpr> findParams(Expression ex, Sort expectedSort) {
		SMTFunction.Z3FuncExpr[] params = generateParamFuncExpr(ex, null);
		List<SMTFunction.Z3FuncExpr> parameters = new ArrayList();
		for (int i = 0; i < params.length; i++)
			parameters.add(params[i]);
		return parameters;
	}

	public SMTFunction.Z3FuncExpr generate(Expression ex, Sort expectedSort) {
		String tempId = StringUtils.getShortName(ex.getId());

		FunctionFactory.FunctionTypes funcName = FunctionFactory.functionNames.get(tempId);
		if (funcName != null) {
			switch (funcName) {

			case OneAndOnly:
				SMTFunction fun = new SMTOneAndOnly(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 0);

				return fun.generateExpr();

			case Equal:
			case Match:
				fun = new SMTMatchEqual(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 0);

				return fun.generateExpr();

			case AtLeastOneMemberOf:
				fun = new SMTAtLeastOneMemberOf(tempId, this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 0, this.custObj.getListOfsorts());

				SMTFunction.Z3FuncExpr res = fun.generateExpr();
				BoolExpr addEx = this.custObj.getContext().mkTrue();
				for (int i = 0; i < res.getAdditionalExpr().size(); i++) {
					addEx = this.custObj.getContext().mkAnd(new BoolExpr[] { (BoolExpr) res.getAdditionalExpr().get(i), addEx });
				}

				return new SMTFunction.Z3FuncExpr(this.custObj.getContext().mkAnd(new BoolExpr[] { addEx, (BoolExpr) res.getPrincipalExpr() }),
						res.getAdditionalExpr(), res.getCardExpr());

			case Not:
				fun = new SMTPropLogic(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 1);

				return fun.generateExpr();
			case And:
				fun = new SMTPropLogic(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 2);

				return fun.generateExpr();
			case Or:
				fun = new SMTPropLogic(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 3);

				return fun.generateExpr();

			case Bag:
				fun = new SMTBagFunction(tempId, this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 0, this.custObj.getListOfsorts());

				return fun.generateExpr();

			case IsIn:
				fun = new SMTIsIn(tempId, this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 0, this.custObj.getListOfsorts());
				return fun.generateExpr();

			case GreaterThan:
				fun = new SMTArithmeticINEq(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 1);

				return fun.generateExpr();
			case LessThan:
				fun = new SMTArithmeticINEq(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 2);

				return fun.generateExpr();
			case GreaterOrEqual:
				fun = new SMTArithmeticINEq(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 3);

				return fun.generateExpr();
			case LessOrEqual:
				fun = new SMTArithmeticINEq(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 4);

				return fun.generateExpr();

			case Add:
				fun = new SMTArithmetic(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 1);
				return fun.generateExpr();
			case Subtract:
				fun = new SMTArithmetic(this.custObj.getContext(), ex.getParameters(), findParams(ex, null), 2);
				return fun.generateExpr();
			case TimeInRange:
				fun = new SMTTimeInRange(this.custObj.getContext(), ex.getParameters(), findParams(ex, null));
				return fun.generateExpr();
			}

		}

		FuncDecl g = userDefinedFunctions.get(tempId.toLowerCase());

		SMTFunction.Z3FuncExpr[] z3funcParams = generateParamFuncExpr(ex, expectedSort);
		Expr[] params = new Expr[z3funcParams.length];
		for (int i = 0; i < z3funcParams.length; i++)
			params[i] = z3funcParams[i].getPrincipalExpr();
		try {
			if (g == null) {
				g = this.custObj.getContext().mkFuncDecl(tempId.toLowerCase(), findSorts(params), expectedSort);
				userDefinedFunctions.put(tempId.toLowerCase(), g);
			}
			return new SMTFunction.Z3FuncExpr(this.custObj.getContext().mkApp(g, params), null, null);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in generate of SMTFunctionFactory" + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTFunctionFactory
 * .class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */