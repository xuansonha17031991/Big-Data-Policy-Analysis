package smtfunctions;

import java.util.List;
import java.util.Map;

import objects.Expression;
import utils.Pair;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Z3Exception;

public class SMTAtLeastOneMemberOf extends SMTFunction {
	Sort enumSort;

	public SMTAtLeastOneMemberOf(String funcName, Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t,
			Map<String, Pair<SetSort, EnumSort>> listOfSorts) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = this.context.getBoolSort();
			this.enumSort = (findSort(funcName.substring(0, funcName.indexOf("-")), listOfSorts, con).second);
			setSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTAtLeastOneMemberOf " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
		}
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			Expr intersect = this.context.mkSetIntersection(new ArrayExpr[] { (ArrayExpr) this.SMTparameters.get(0).getPrincipalExpr(),
					(ArrayExpr) this.SMTparameters.get(1).getPrincipalExpr() });
			Expr emptySet = this.context.mkEmptySet(this.enumSort);
			Expr expr = this.context.mkDistinct(new Expr[] { intersect, emptySet });

			return new SMTFunction.Z3FuncExpr(expr, getAdditionalExprFromParams(), getCardExprFromParams());
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTAtLeastOneMemberOf " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTAtLeastOneMemberOf
 * .class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */