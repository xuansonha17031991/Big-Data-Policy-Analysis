package smtfunctions;

import java.util.List;
import java.util.Vector;

import objects.Expression;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

public class SMTOneAndOnly extends SMTFunction {
	public SMTOneAndOnly(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = getSMTParameters().get(0).getPrincipalExpr().getSort();
			setSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTOneAndOnly " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTOneAndOnly " + ex.getMessage());
		}
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			Vector<Expr> cardConstraints = new Vector();
			BoolExpr expr = this.context.mkBoolConst("card_" + getSMTParameters().get(0).getPrincipalExpr().getSExpr());
			cardConstraints.add(expr);
			List<Expr> cardEx = getCardExprFromParams();
			if ((cardEx != null) && (cardEx.size() > 0))
				cardConstraints.addAll(cardEx);
			return new SMTFunction.Z3FuncExpr(getSMTParameters().get(0).getPrincipalExpr(), getAdditionalExprFromParams(), cardConstraints);
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTOneAndOnly " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTOneAndOnly.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */