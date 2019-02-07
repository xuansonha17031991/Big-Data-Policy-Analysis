package policy;

import utils.Pair;

import com.microsoft.z3.BoolExpr;

public class SMTRule extends SMTPolicyElement {
	BoolExpr c_p;
	BoolExpr c_d;
	BoolExpr c_in_pd;
	BoolExpr c_in_p;
	BoolExpr c_in_d;
	BoolExpr c_na;

	public static enum Effects {
		Permit, Deny;

		private Effects() {
		}
	}

	private void initialize() {
		try {
			this.c_p = this.ctx.mkFalse();
			this.c_d = this.ctx.mkFalse();
			this.c_in_pd = this.ctx.mkFalse();
			this.c_in_p = this.ctx.mkFalse();
			this.c_in_d = this.ctx.mkFalse();
			this.c_na = this.ctx.mkFalse();
		} catch (com.microsoft.z3.Z3Exception zex) {
			System.err.println("Z3Exception in initialize of Rule " + zex.getMessage());
		}
	}

	public SMTRule(String i, BoolExpr t_applicable, BoolExpr t_indeterminate, BoolExpr t_na, BoolExpr c_applicable, BoolExpr c_indeterminate,
			BoolExpr c_na, Effects e, com.microsoft.z3.Context con) {
		this.ctx = con;
		setIdAndType(i, PolicyElementType.Rule);
		initialize();
		if (e == Effects.Permit)
			this.c_p = con.mkAnd(new BoolExpr[] { t_applicable, c_applicable });
		if (e == Effects.Deny)
			this.c_d = con.mkAnd(new BoolExpr[] { t_applicable, c_applicable });
		if (e == Effects.Permit)
			this.c_in_p = con.mkOr(new BoolExpr[] { t_indeterminate, c_indeterminate });
		if (e == Effects.Deny)
			this.c_in_d = con.mkOr(new BoolExpr[] { t_indeterminate, c_indeterminate });
		this.c_na = con.mkOr(new BoolExpr[] { t_na, con.mkAnd(new BoolExpr[] { t_applicable, c_na }) });
	}

	public SMTRule(String i, com.microsoft.z3.Context con, BoolExpr c_permit, BoolExpr c_deny, BoolExpr c_pd_indet, BoolExpr c_p_indet,
			BoolExpr c_d_indet, BoolExpr na) {
		this(i, PolicyElementType.CombinationNoDS, con, c_permit, c_deny, c_pd_indet, c_p_indet, c_d_indet, na);
	}

	public SMTRule(String i, PolicyElementType type, com.microsoft.z3.Context con, BoolExpr c_permit, BoolExpr c_deny, BoolExpr c_pd_indet,
			BoolExpr c_p_indet,
			BoolExpr c_d_indet, BoolExpr na) {
		this.ctx = con;
		setIdAndType(i, type);
		initialize();
		if (c_permit != null)
			this.c_p = c_permit;
		if (c_deny != null)
			this.c_d = c_deny;
		if (c_pd_indet != null)
			this.c_in_pd = c_pd_indet;
		if (c_p_indet != null)
			this.c_in_p = c_p_indet;
		if (c_d_indet != null)
			this.c_in_d = c_d_indet;
		this.c_na = na;
	}

	@Override
	public void setDSStr(BoolExpr permitDSStr, BoolExpr denyDSStr, BoolExpr indeterminateDS_PDStr, BoolExpr indeterminateDS_PStr,
			BoolExpr indeterminateDS_DStr, BoolExpr naDSStr) {
		super.setDSStr(permitDSStr, denyDSStr, indeterminateDS_PDStr, indeterminateDS_PStr, indeterminateDS_DStr, naDSStr);

		if (type == PolicyElementType.CombinationNoDS) {
			setOriginalDecisionMap(this.permitDSStr, this.denyDSStr, this.indeterminateDS_PDStr, this.indeterminateDS_PStr, this.indeterminateDS_DStr,
					this.naDSStr);
		}
	}

	public Pair<BoolExpr, BoolExpr> get(Decision decision) {
		return get(decision, true);
	}

	public Pair<BoolExpr, BoolExpr> get(Decision decision, boolean withDetails) {
		BoolExpr ret = null;
		switch (decision) {
		case Deny:
			ret = c_d;
			break;
		case IndetD:
			ret = c_in_d;
			break;
		case IndetP:
			ret = c_in_p;
			break;
		case IndetPD:
			ret = c_in_pd;
			break;
		case Na:
			ret = c_na;
			break;
		case Permit:
			ret = c_p;
			break;
		case Indet:
			ret = getIndeterminateDS(ctx);
			break;
		}

		return new Pair<BoolExpr, BoolExpr>(ret, getDSStr(decision, withDetails));
	}

	public BoolExpr getPermitDS() {
		return this.c_p;
	}

	public BoolExpr getDenyDS() {
		return this.c_d;
	}

	public BoolExpr getIndeterminateDS_PD() {
		return this.c_in_pd;
	}

	public BoolExpr getIndeterminateDS_P() {
		return this.c_in_p;
	}

	public BoolExpr getIndeterminateDS_D() {
		return this.c_in_d;
	}

	public BoolExpr getNA_DS() {
		return this.c_na;
	}

	public BoolExpr getIndeterminateDS(com.microsoft.z3.Context context) {
		try {
			return context.mkOr(new BoolExpr[] { this.c_in_pd, this.c_in_p, this.c_in_d });
		} catch (com.microsoft.z3.Z3Exception zex) {
			System.err.println("Z3Exception in getIndeterminateDS " + zex.getMessage());
		}
		return null;
	}

	@Override
	public void print(java.io.PrintStream out) {
		printAS(this.ctx, out);
	}

	public void printAS(com.microsoft.z3.Context ctx, java.io.PrintStream out) {
		System.out.println(this.getId());
		try {
			out.println("C_p :" + this.c_p.simplify().toString());
			out.println("C_d :" + this.c_d.simplify().toString());
			out.println("C_in :" + getIndeterminateDS(ctx).simplify());

		} catch (com.microsoft.z3.Z3Exception zex) {
			System.err.println("Z3Exception in printAS of Rule " + zex.getMessage());
		}
	}

	public void printASSMTLib(com.microsoft.z3.Context ctx, boolean check, java.io.PrintStream out) {
		System.out.println(this.getId());
		try {
			BoolExpr last = ctx.mkAnd(new BoolExpr[] { (BoolExpr) this.c_p.simplify(), (BoolExpr) this.c_d.simplify(),
					(BoolExpr) getIndeterminateDS(ctx).simplify() });
			printBoolExprInSMTLib(ctx, last, check, out);
		} catch (com.microsoft.z3.Z3Exception zex) {
			System.err.println("Z3Exception in printASSMTLib of Rule " + zex.getMessage());
		}
	}

	public static void printBoolExprInSMTLib(com.microsoft.z3.Context ctx, BoolExpr last, boolean check, java.io.PrintStream out) {
		try {
			out.println(ctx.benchmarkToSMTString("", "", "sat", "", new BoolExpr[] { last }, check ? ctx

			.mkTrue() : ctx.mkFalse()));
		} catch (com.microsoft.z3.Z3Exception zex) {
			System.err.println("Z3Exception in printBoolExprInSMTLib of Rule " + zex.getMessage());
		}
	}

	@Override
	public SMTRule getFinalElement() {
		return this;
	}
}

/*
 * Location: /Users/okielabackend/Downloads/XACMLSMT.jar!/policy/SMTRule.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */