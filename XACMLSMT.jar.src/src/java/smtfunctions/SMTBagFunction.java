package smtfunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import objects.Expression;
import utils.Pair;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Z3Exception;

public class SMTBagFunction extends SMTFunction {
	static int index = 0;

	List<Expr> additionalExpressions;

	public SMTBagFunction(String funcName, Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t,
			Map<String, Pair<SetSort, EnumSort>> listOfSorts) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;

		this.returnSort = (findSort(funcName.substring(0, funcName.indexOf("-")), listOfSorts, con).first);
		setSorts();
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			Expr bagVar = this.context.mkConst("bagVar" + index, this.returnSort);
			index += 1;
			List<Expr> additionalExpressions = new ArrayList();
			for (int i = 0; i < this.SMTparameters.size(); i++) {
				additionalExpressions.add(this.context.mkSetMembership(this.SMTparameters.get(i).getPrincipalExpr(), (ArrayExpr) bagVar));
			}
			List<Expr> addExs = getAdditionalExprFromParams();
			if ((addExs != null) && (addExs.size() > 0))
				additionalExpressions.addAll(addExs);
			return new SMTFunction.Z3FuncExpr(bagVar, additionalExpressions, getCardExprFromParams());
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTBagFunction " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTBagFunction
 * .class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */