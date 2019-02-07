package smtfunctions;

import java.util.List;

import objects.Expression;
import translator.XACMLTranslator;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

public class SMTArithmetic extends SMTFunction {
	public SMTArithmetic(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = getSMTParameters().get(0).getPrincipalExpr().getSort();
			setSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTArithmetic " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
		}
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			Expr expr = null;
			switch (this.type) {
			case 1:
				Expr[] expArray = new Expr[this.SMTparameters.size()];
				for (int i = 0; i < this.SMTparameters.size(); i++)
					expArray[i] = this.SMTparameters.get(i).getPrincipalExpr();
				expr = this.context.mkAdd(XACMLTranslator.convertExprAr2ArithExprAr(expArray));
			case 2:
				expArray = new Expr[this.SMTparameters.size()];
				for (int i = 0; i < this.SMTparameters.size(); i++)
					expArray[i] = this.SMTparameters.get(i).getPrincipalExpr();
				expr = this.context.mkSub(XACMLTranslator.convertExprAr2ArithExprAr(expArray));
			}

			return new SMTFunction.Z3FuncExpr(expr, getAdditionalExprFromParams(), getCardExprFromParams());
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTArithmetic " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTArithmetic.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */