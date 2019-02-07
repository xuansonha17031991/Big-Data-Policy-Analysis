package smtfunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Z3Exception;
import com.microsoft.z3.enumerations.Z3_sort_kind;

import objects.Expression;
import query.QueryRunner;
import utils.Pair;

public class SMTMatchEqual extends SMTFunction {
	static Map<String, Pair<SetSort, EnumSort>> z3AvSorts;

	public SMTMatchEqual(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = this.context.getBoolSort();
			setSorts();
			setZ3AvailableSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTMatchEqual " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
		}
	}

	private void setZ3AvailableSorts() {
		z3AvSorts = new HashMap();
		try {
			z3AvSorts.put("double", new Pair(this.context.mkSetSort(this.context.getRealSort()), null));
			z3AvSorts.put("boolean", new Pair(this.context.mkSetSort(this.context.getBoolSort()), null));
			z3AvSorts.put("integer", new Pair(this.context.mkSetSort(this.context.getIntSort()), null));
		} catch (Z3Exception zex) {
			System.err.println("Exception in setZ3AvailableSorts of SMT (Match-Equal) " + zex.getMessage());
		}
	}

	public static Expr createMatchEqExpr(Context context, Expr p0, Expr p1) {
		try {
			if ((p0.getSort().getSortKind() != Z3_sort_kind.Z3_ARRAY_SORT)
					&& (p1.getSort().getSortKind() != Z3_sort_kind.Z3_ARRAY_SORT)) {
				return context.mkEq(p0, p1);
			}
			if (p0.getSort().getSortKind() != Z3_sort_kind.Z3_ARRAY_SORT) {
				if (p0.isBool()) {
					p0 = context.mkConst(p0.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("boolean")).first);
				} else if (p0.isReal()) {
					p0 = context.mkConst(p0.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("double")).first);
				} else if (p0.isInt()) {
					p0 = context.mkConst(p0.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("integer")).first);
				}
			} else if (p1.getSort().getSortKind() != Z3_sort_kind.Z3_ARRAY_SORT) {
				if (p1.isBool()) {
					p1 = context.mkConst(p1.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("boolean")).first);
				} else if (p1.isReal()) {
					p1 = context.mkConst(p1.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("double")).first);
				} else if (p1.isInt())
					p1 = context.mkConst(p1.getSExpr(), (SetSort) ((Pair) z3AvSorts.get("integer")).first);
			}
			Expr expr;
			if (p0.isArray() && p1.isArray()) {
				expr = context.mkEq(p0, p1);
			} else if (p0.isArray())
				expr = context.mkSetMembership(p1, (ArrayExpr) p0);
			else
				expr = context.mkSetMembership(p0, (ArrayExpr) p1);
			return (com.microsoft.z3.BoolExpr) expr;
		} catch (Z3Exception zex) {
			System.err.println("Exception in createMatchEqExpr of SMT (Match-Equal) " + zex.getMessage());
		}
		return null;
	}

	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			Expr expr = null;
			if (QueryRunner.debug) {
				System.out.println("MatchEq Exp Param Sort 0:"
						+ ((SMTFunction.Z3FuncExpr) this.SMTparameters.get(0)).getPrincipalExpr().getSort()
						+ " MatchEq Exp Param Sort 1:"
						+ ((SMTFunction.Z3FuncExpr) this.SMTparameters.get(1)).getPrincipalExpr().getSort());
			}
			Expr p0 = ((SMTFunction.Z3FuncExpr) this.SMTparameters.get(0)).getPrincipalExpr();
			Expr p1 = ((SMTFunction.Z3FuncExpr) this.SMTparameters.get(1)).getPrincipalExpr();
			expr = createMatchEqExpr(this.context, p0, p1);
			return new SMTFunction.Z3FuncExpr(expr, getAdditionalExprFromParams(), getCardExprFromParams());
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMT (Match-Equal) " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTMatchEqual.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */