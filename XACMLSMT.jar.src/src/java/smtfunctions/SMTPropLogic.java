package smtfunctions;

import java.util.List;

import objects.Expression;
import translator.XACMLTranslator;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

public class SMTPropLogic extends SMTFunction {
	public SMTPropLogic(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = this.context.getBoolSort();
			setSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTPropLogic " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
		}
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			switch (this.type) {
			case 1:
				return new SMTFunction.Z3FuncExpr(this.context.mkNot((com.microsoft.z3.BoolExpr) this.SMTparameters.get(0).getPrincipalExpr()),
						getAdditionalExprFromParams(), getCardExprFromParams());
			case 2:
				Expr[] expArray = new Expr[this.SMTparameters.size()];
				for (int i = 0; i < this.SMTparameters.size(); i++)
					expArray[i] = this.SMTparameters.get(i).getPrincipalExpr();
				return new SMTFunction.Z3FuncExpr(this.context.mkAnd(XACMLTranslator.convertExprAr2BoolExprAr(expArray)), getAdditionalExprFromParams(),
						getCardExprFromParams());
			case 3:
				expArray = new Expr[this.SMTparameters.size()];
				for (int i = 0; i < this.SMTparameters.size(); i++)
					expArray[i] = this.SMTparameters.get(i).getPrincipalExpr();
				return new SMTFunction.Z3FuncExpr(this.context.mkOr(XACMLTranslator.convertExprAr2BoolExprAr(expArray)), getAdditionalExprFromParams(),
						getCardExprFromParams());
			}
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTPropLogic " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTPropLogic.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */