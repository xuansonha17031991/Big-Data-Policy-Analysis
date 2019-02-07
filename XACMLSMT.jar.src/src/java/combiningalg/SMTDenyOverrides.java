package combiningalg;

import static policy.SMTPolicyElement.Decision.Deny;
import static policy.SMTPolicyElement.Decision.IndetD;
import static policy.SMTPolicyElement.Decision.IndetP;
import static policy.SMTPolicyElement.Decision.IndetPD;
import static policy.SMTPolicyElement.Decision.Na;
import static policy.SMTPolicyElement.Decision.Permit;
import policy.SMTPolicyElement;
import policy.SMTRule;
import query.QueryRunner;
import utils.Pair;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class SMTDenyOverrides extends SMTCombiningAlg implements ISMTCombAlg {
	public SMTDenyOverrides(SMTPolicyElement[] eList, Context con, int v) {
		this.elementList = eList;
		this.ctx = con;
		this.version = v;
	}

	@Override
	public void runv2Rule(String id, SMTRule r1, SMTRule r2, Context ctx) {
		try {
			BoolExpr c_d_temp = ctx.mkOr(new BoolExpr[] { r1.getDenyDS(), r2.getDenyDS() });

			BoolExpr c_in_d_temp = ctx.mkAnd(new BoolExpr[] { ctx.mkOr(new BoolExpr[] { r1.getIndeterminateDS_D(), r2.getIndeterminateDS_D() }),
					ctx.mkNot(c_d_temp) });

			BoolExpr c_p_temp = ctx.mkAnd(new BoolExpr[] { ctx.mkOr(new BoolExpr[] { r1.getPermitDS(), r2.getPermitDS() }),
					ctx.mkNot(ctx.mkOr(new BoolExpr[] { c_d_temp, c_in_d_temp })) });

			BoolExpr c_in_p_temp = ctx.mkAnd(new BoolExpr[] { ctx.mkOr(new BoolExpr[] { r1.getIndeterminateDS_P(), r2.getIndeterminateDS_P() }),
					ctx.mkNot(ctx.mkOr(new BoolExpr[] { c_d_temp, c_in_d_temp, c_p_temp })) });

			BoolExpr c_na = ctx.mkAnd(new BoolExpr[] { r1.getNA_DS(), r2.getNA_DS() });

			this.result = new SMTRule(id, ctx, c_p_temp, c_d_temp, null, c_in_p_temp, c_in_d_temp, c_na);
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

			if (QueryRunner.debug)
				System.out.println("--------- POLICY 1 -------> ");
			if (QueryRunner.debug)
				p1.printAS(ctx, System.out);
			if (QueryRunner.debug)
				System.out.println("--------- POLICY 2 -------> ");
			if (QueryRunner.debug) {
				p2.printAS(ctx, System.out);
			}
			BoolExpr c_d_temp = ctx.mkOr(new BoolExpr[] { p1.getDenyDS(), p2.getDenyDS(), p1.getIndeterminateDS(ctx), p2.getIndeterminateDS(ctx) });
			BoolExpr c_p_temp = ctx.mkAnd(new BoolExpr[] { ctx.mkOr(new BoolExpr[] { p1.getPermitDS(), p2.getPermitDS() }), ctx.mkNot(c_d_temp) });
			BoolExpr c_na = ctx.mkAnd(new BoolExpr[] { p1.getNA_DS(), p2.getNA_DS() });

			this.result = new SMTRule(id, ctx, c_p_temp, c_d_temp, null, null, null, c_na);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void runv3Rule(String id, SMTRule r1, SMTRule r2, Context ctx, boolean withDetails) {
		try {
			Pair<BoolExpr, BoolExpr> c_d_temp = or(r1.get(Deny, withDetails), r2.get(Deny, withDetails));

			Pair<BoolExpr, BoolExpr> c_in_pd_temp = and(
					or(or(r1.get(IndetPD, withDetails), r2.get(IndetPD, withDetails)), and(r1.get(IndetD, withDetails), or(r2.get(IndetP, withDetails), r2.get(Permit, withDetails))),
							and(r2.get(IndetD, withDetails), or(r1.get(IndetP, withDetails), r1.get(Permit, withDetails)))), not(c_d_temp));

			Pair<BoolExpr, BoolExpr> c_in_d_temp = and(or(r1.get(IndetD, withDetails), r2.get(IndetD, withDetails)), and(not(c_d_temp), not(c_in_pd_temp)));

			Pair<BoolExpr, BoolExpr> c_p_temp = and(or(r1.get(Permit, withDetails), r2.get(Permit, withDetails)), and(not(c_d_temp), not(c_in_pd_temp), not(c_in_d_temp)));

			Pair<BoolExpr, BoolExpr> c_in_p_temp = and(or(r1.get(IndetP, withDetails), r2.get(IndetP, withDetails)),
					and(not(c_d_temp), not(c_in_pd_temp), not(c_in_d_temp), not(c_p_temp)));

			Pair<BoolExpr, BoolExpr> c_na = and(r1.get(Na, withDetails), r2.get(Na, withDetails));

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, c_in_pd_temp.first, c_in_p_temp.first, c_in_d_temp.first, c_na.first);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, c_in_pd_temp.second, c_in_p_temp.second, c_in_d_temp.second, c_na.second);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void runv3PSet(String id, SMTRule p1, SMTRule p2, Context ctx, boolean withDetails) {
		try {
			if (p1.equals(p2)) {

				this.result = p1;
				return;
			}

			Pair<BoolExpr, BoolExpr> c_d_temp = or( p1.get(Deny, withDetails), p2.get(Deny, withDetails) );

			Pair<BoolExpr, BoolExpr> c_in_pd_temp = and(
					or( or( p1.get(IndetPD, withDetails), p2.get(IndetPD, withDetails) ),
							and( p1.get(IndetD, withDetails), or( p2.get(IndetP, withDetails), p2.get(Permit, withDetails) ) ),
							and( p2.get(IndetD, withDetails), or( p1.get(IndetP, withDetails), p1.get(Permit, withDetails) ) ) ),
					not(c_d_temp) );

			Pair<BoolExpr, BoolExpr> c_in_d_temp = and( or( p1.get(IndetD, withDetails), p2.get(IndetD, withDetails) ),
					and( not(c_d_temp), not(c_in_pd_temp) ) );

			Pair<BoolExpr, BoolExpr> c_p_temp = and( or( p1.get(Permit, withDetails), p2.get(Permit, withDetails) ),
					and( not(c_d_temp), not(c_in_pd_temp), not(c_in_d_temp) ) );

			Pair<BoolExpr, BoolExpr> c_in_p_temp = and( or( p1.get(IndetP, withDetails), p2.get(IndetP, withDetails) ),
					and( not(c_d_temp), not(c_in_pd_temp), not(c_in_d_temp), not(c_p_temp) ) );

			Pair<BoolExpr, BoolExpr> c_na = and( p1.get(Na, withDetails), p2.get(Na, withDetails) );

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, c_in_pd_temp.first, c_in_p_temp.first, c_in_d_temp.first, c_na.first);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, c_in_pd_temp.second, c_in_p_temp.second, c_in_d_temp.second, c_na.second);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/SMTDenyOverrides.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */