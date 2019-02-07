package smtfunctions;

import java.util.List;

import objects.Expression;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class SMTArithmeticINEq extends SMTFunction {
	public SMTArithmeticINEq(Context con, List<Expression> paramExpressions, List<SMTFunction.Z3FuncExpr> params, int t) {
		this.SMTparameters = params;
		this.parameters = paramExpressions;
		this.type = t;
		this.context = con;
		try {
			this.returnSort = this.context.getBoolSort();
			setSorts();
		} catch (Z3Exception zex) {
			System.err.println("Z3 error in SMTLessOrEqual " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Z3 error in SMTPropLogic " + ex.getMessage());
		}
	}

	@Override
	public SMTFunction.Z3FuncExpr generateExpr() {
		try {
			switch (this.type) {
			case 1:
				return new SMTFunction.Z3FuncExpr(this.context.mkGt((ArithExpr) this.SMTparameters.get(0).getPrincipalExpr(), (ArithExpr) this.SMTparameters
						.get(1).getPrincipalExpr()), getAdditionalExprFromParams(), getCardExprFromParams());

			case 2:
				return new SMTFunction.Z3FuncExpr(this.context.mkLt((ArithExpr) this.SMTparameters.get(0).getPrincipalExpr(), (ArithExpr) this.SMTparameters
						.get(1).getPrincipalExpr()), getAdditionalExprFromParams(), getCardExprFromParams());

			case 3:
				return new SMTFunction.Z3FuncExpr(this.context.mkGe((ArithExpr) this.SMTparameters.get(0).getPrincipalExpr(), (ArithExpr) this.SMTparameters
						.get(1).getPrincipalExpr()), getAdditionalExprFromParams(), getCardExprFromParams());

			case 4:
				return new SMTFunction.Z3FuncExpr(this.context.mkLe((ArithExpr) this.SMTparameters.get(0).getPrincipalExpr(), (ArithExpr) this.SMTparameters
						.get(1).getPrincipalExpr()), getAdditionalExprFromParams(), getCardExprFromParams());
			}
		} catch (Z3Exception zex) {
			System.err.println("Exception in generateExpr of SMTArithmeticINEq " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTArithmeticINEq
 * .class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */