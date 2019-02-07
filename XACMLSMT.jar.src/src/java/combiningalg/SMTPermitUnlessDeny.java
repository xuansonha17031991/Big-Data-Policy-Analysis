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

public class SMTPermitUnlessDeny extends SMTCombiningAlg implements ISMTCombAlg {
	public SMTPermitUnlessDeny(SMTPolicyElement[] eList, Context con, int v) {
		this.elementList = eList;
		this.ctx = con;
		this.version = v;
	}

	@Override
	public void runv2Rule(String id, SMTRule r1, SMTRule r2, Context ctx) {
	}

	@Override
	public void runv2PSet(String id, SMTRule p1, SMTRule p2, Context ctx) {
	}

	@Override
	public void runv3Rule(String id, SMTRule r1, SMTRule r2, Context ctx, boolean withDetails) {
		try {
			Pair<BoolExpr, BoolExpr> allRequests = or(r1.get(Permit, withDetails), r2.get(Permit, withDetails), r1.get(Deny, withDetails), r2.get(Deny, withDetails), r1.get(Indet, withDetails), r2.get(Indet, withDetails), r1.get(Na, withDetails),
					r2.get(Na, withDetails));

			Pair<BoolExpr, BoolExpr> c_d_temp = or(r1.get(Deny, withDetails), r2.get(Deny, withDetails));

			Pair<BoolExpr, BoolExpr> c_p_temp = and(allRequests, not(c_d_temp));

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, null, null, null, null);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, null, null, null, null);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}

	@Override
	public void runv3PSet(String id, SMTRule p1, SMTRule p2, Context ctx, boolean withDetails) {
		try {
			if (p1.equals(p2)) {
				this.result = p1;
				System.err.println("Why do we have equal rules runv2PSet() in SMTNotApplicable");
				return;
			}

			Pair<BoolExpr, BoolExpr> allRequests = or(p1.get(Permit, withDetails), p2.get(Permit, withDetails), p1.get(Deny, withDetails), p2.get(Deny, withDetails), p1.get(Indet, withDetails), p2.get(Indet, withDetails), p1.get(Na, withDetails),
					p2.get(Na, withDetails));

			Pair<BoolExpr, BoolExpr> c_d_temp = or(p1.get(Deny, withDetails), p2.get(Deny, withDetails));

			Pair<BoolExpr, BoolExpr> c_p_temp = and(allRequests, not(c_d_temp));

			this.result = new SMTRule(id, ctx, c_p_temp.first, c_d_temp.first, null, null, null, null);
			this.result.setDSStr(c_p_temp.second, c_d_temp.second, null, null, null, null);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in DenyOverrides " + zex.getMessage());
		}
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/SMTPermitUnlessDeny.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */