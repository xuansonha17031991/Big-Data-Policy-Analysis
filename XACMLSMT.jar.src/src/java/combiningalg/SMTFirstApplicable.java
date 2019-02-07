package combiningalg;

import static policy.SMTPolicyElement.Decision.Deny;
import static policy.SMTPolicyElement.Decision.Indet;
import static policy.SMTPolicyElement.Decision.Na;
import static policy.SMTPolicyElement.Decision.Permit;
import policy.SMTPolicyElement;
import policy.SMTRule;
import utils.Pair;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class SMTFirstApplicable extends SMTCombiningAlg implements ISMTCombAlg {
	public SMTFirstApplicable(SMTPolicyElement[] eList, Context con, int v) {
		this.elementList = eList;
		this.ctx = con;
		this.version = v;
	}

	@Override
	public void runv2Rule(String id, SMTRule r1, SMTRule r2, Context ctx) {
		try {
			BoolExpr c_d_temp = ctx.mkOr(new BoolExpr[] { r1.getDenyDS(), ctx.mkAnd(new BoolExpr[] { r1.getNA_DS(), r2.getDenyDS() }) });

			BoolExpr c_p_temp = ctx.mkOr(new BoolExpr[] { r1.getPermitDS(), ctx.mkAnd(new BoolExpr[] { r1.getNA_DS(), r2.getPermitDS() }) });

			BoolExpr c_in_pd_temp = ctx.mkOr(new BoolExpr[] { r1.getIndeterminateDS(ctx),
					ctx.mkAnd(new BoolExpr[] { r1.getNA_DS(), r2.getIndeterminateDS(ctx) }) });

			BoolExpr c_na = ctx.mkAnd(new BoolExpr[] { r1.getNA_DS(), r2.getNA_DS() });

			this.result = new SMTRule(id, ctx, c_p_temp, c_d_temp, c_in_pd_temp, null, null, c_na);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@Override
	public void runv2PSet(String id, SMTRule p1, SMTRule p2, Context ctx) {
		try {
			if (p1.equals(p2)) {

				this.result = p1;
				return;
			}

			BoolExpr c_d_temp = ctx.mkOr(new BoolExpr[] { p1.getDenyDS(), ctx.mkAnd(new BoolExpr[] { p1.getNA_DS(), p2.getDenyDS() }) });

			BoolExpr c_p_temp = ctx.mkOr(new BoolExpr[] { p1.getPermitDS(), ctx.mkAnd(new BoolExpr[] { p1.getNA_DS(), p2.getPermitDS() }) });

			BoolExpr c_in_pd_temp = ctx.mkOr(new BoolExpr[] { p1.getIndeterminateDS(ctx),
					ctx.mkAnd(new BoolExpr[] { p1.getNA_DS(), p2.getIndeterminateDS(ctx) }) });

			BoolExpr c_na = ctx.mkAnd(new BoolExpr[] { p1.getNA_DS(), p2.getNA_DS() });

			this.result = new SMTRule(id, ctx, c_p_temp, c_d_temp, c_in_pd_temp, null, null, c_na);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@Override
	public void runv3Rule(String id, SMTRule r1, SMTRule r2, Context ctx, boolean withDetails) {
		try {
			Pair<BoolExpr, BoolExpr> c_d_temp = or(r1.get(Deny, withDetails), and(r1.get(Na, withDetails), r2.get(Deny, withDetails)));

			Pair<BoolExpr, BoolExpr> c_p_temp = or(r1.get(Permit, withDetails), and(r1.get(Na, withDetails), r2.get(Permit, withDetails)));

			Pair<BoolExpr, BoolExpr> c_in_pd_temp = or(r1.get(Indet, withDetails), and(r1.get(Na, withDetails), r2.get(Indet, withDetails)));

			Pair<BoolExpr, BoolExpr> c_na = and(r1.get(Na, withDetails), r2.get(Na, withDetails));

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, c_in_pd_temp.first, null, null, c_na.first);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, c_in_pd_temp.second, null, null, c_na.second);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@Override
	public void runv3PSet(String id, SMTRule p1, SMTRule p2, Context ctx, boolean withDetails) {
		try {
			if (p1.equals(p2)) {

				this.result = p1;
				return;
			}

			Pair<BoolExpr, BoolExpr> c_d_temp = or(p1.get(Deny, withDetails), and(p1.get(Na, withDetails), p2.get(Deny, withDetails)));

			Pair<BoolExpr, BoolExpr> c_p_temp = or(p1.get(Permit, withDetails), and(p1.get(Na, withDetails), p2.get(Permit, withDetails)));

			Pair<BoolExpr, BoolExpr> c_in_pd_temp = or(p1.get(Indet, withDetails), and(p1.get(Na, withDetails), p2.get(Indet, withDetails)));

			Pair<BoolExpr, BoolExpr> c_na = and(p1.get(Na, withDetails), p2.get(Na, withDetails));

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, c_in_pd_temp.first, null, null, c_na.first);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, c_in_pd_temp.second, null, null, c_na.second);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/SMTFirstApplicable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */