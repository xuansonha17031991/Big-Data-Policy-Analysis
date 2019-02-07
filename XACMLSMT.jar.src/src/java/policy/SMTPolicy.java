package policy;

import java.io.PrintStream;

import query.QueryRunner;
import utils.Pair;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import combiningalg.ISMTCombAlg;
import combiningalg.SMTAlgFactory;

public class SMTPolicy extends SMTPolicyElement {
	private static SMTAlgFactory algFac = new SMTAlgFactory();

	public ISMTCombAlg alg;
	public SMTPolicyElement[] members;
	int version;
	public Pair<BoolExpr, BoolExpr> targetExpressions;

	public SMTPolicy(String i, Pair policyTarget, SMTAlgFactory.SMTCombiningAlg algorithm, SMTPolicyElement[] elements, Context context, int v,
			PolicyElementType type) {
		this.ctx = context;
		this.setIdAndType(i, type);
		this.targetExpressions = policyTarget;
		this.members = elements;
		this.version = v;
		this.alg = algFac.getAlgorithm(algorithm, this.members, context, v);
	}

	@Override
	public void print(PrintStream out) {
		System.out.println("ID:" + this.getId() + " --> [" + this.alg.toString() + "]" + " Type:" + this.type);
		for (int i = 0; i < this.members.length; i++) {
			this.members[i].print(out);
		}
	}

	private SMTRule getTheRuleforPolicy(SMTRule combinedRule, boolean separateTargetOfPolicy) {
		BoolExpr targetFirst = this.targetExpressions.first;
		BoolExpr targetSecond = this.targetExpressions.second; // This is for cards and some additional constraints. 
		BoolExpr c_p = this.ctx.mkAnd(new BoolExpr[] { targetFirst, this.ctx.mkNot(targetSecond),
				combinedRule.getPermitDS() });
		BoolExpr c_p_str = this.ctx.mkAnd(new BoolExpr[] { targetFirst, this.ctx.mkNot(targetSecond), combinedRule.get(Decision.Permit, !separateTargetOfPolicy).second });
		BoolExpr c_d = this.ctx.mkAnd(new BoolExpr[] { targetFirst, this.ctx.mkNot(targetSecond),
				combinedRule.getDenyDS() });
		BoolExpr c_d_str = this.ctx.mkAnd(new BoolExpr[] { targetFirst, this.ctx.mkNot(targetSecond), combinedRule.get(Decision.Deny, !separateTargetOfPolicy).second });

		BoolExpr c_in_p = this.ctx.mkOr(new BoolExpr[] {
				this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }), combinedRule.getIndeterminateDS_P() }),
				this.ctx.mkAnd(new BoolExpr[] { targetSecond, combinedRule.getPermitDS() }) });
		BoolExpr c_in_p_str = this.ctx.mkOr(new BoolExpr[] {
				this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }), combinedRule.get(Decision.IndetP, !separateTargetOfPolicy).second }),
				this.ctx.mkAnd(new BoolExpr[] { targetSecond, combinedRule.get(Decision.Permit, !separateTargetOfPolicy).second }) });

		BoolExpr c_in_d = this.ctx.mkOr(new BoolExpr[] {
				this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }), combinedRule.getIndeterminateDS_D() }),
				this.ctx.mkAnd(new BoolExpr[] { targetSecond, combinedRule.getDenyDS() }) });
		BoolExpr c_in_d_str = this.ctx.mkOr(new BoolExpr[] {
				this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }), combinedRule.get(Decision.IndetD, !separateTargetOfPolicy).second }),
				this.ctx.mkAnd(new BoolExpr[] { targetSecond, combinedRule.get(Decision.Deny, !separateTargetOfPolicy).second }) });
		
		BoolExpr c_in_pd = this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }),
				combinedRule.getIndeterminateDS_PD() });
		BoolExpr c_in_pd_str = this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] { targetFirst, targetSecond }),
				combinedRule.get(Decision.IndetPD, !separateTargetOfPolicy).second });
		BoolExpr c_na = this.ctx.mkAnd(new BoolExpr[] { this.ctx.mkOr(new BoolExpr[] {this.ctx.mkNot(targetFirst), combinedRule.get(Decision.Na, !separateTargetOfPolicy).first}), this.ctx.mkNot(targetSecond) });
		BoolExpr c_na_str = this.ctx.mkAnd(new BoolExpr[] {
				this.ctx.mkOr(new BoolExpr[] { this.ctx.mkNot(targetFirst), combinedRule.get(Decision.Na, !separateTargetOfPolicy).second }), this.ctx.mkNot(targetSecond) });
		SMTRule smtRule = new SMTRule(this.getId(), this.type, this.ctx, c_p, c_d, c_in_pd, c_in_p, c_in_d, c_na);
		smtRule.setDSStr(c_p_str, c_d_str, c_in_pd_str, c_in_p_str, c_in_d_str, c_na_str);

		return smtRule;
	}

	@Override
	public SMTRule getFinalElement() {
		return getFinalElementByConfiguration();
	}

	private SMTRule getFinalElementByConfiguration() {
		if (QueryRunner.SHOW_AT_POLICY_ONLY) {
			if (this.type == PolicyElementType.Policy) {
				if (QueryRunner.debug)
					System.out.println(this.getId() + " -->" + this.alg.toString());
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, false).getFinalElement();
				combinedRule.convertToComibinationWithDSType(this);
				return getTheRuleforPolicy(combinedRule, true);
			}
			if (this.type == PolicyElementType.PolicySet) {
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, false).getFinalElement();
				return getTheRuleforPolicy(combinedRule, false);
			}

		}

		else if (QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION) {
			if (this.type == PolicyElementType.Policy) {
				if (QueryRunner.debug)
					System.out.println(this.getId() + " -->" + this.alg.toString());
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, false).getFinalElement();
				combinedRule.convertToComibinationWithDSType(this);
				return getTheRuleforPolicy(combinedRule, true);
			}
			if (this.type == PolicyElementType.PolicySet) {
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, true).getFinalElement();
				return getTheRuleforPolicy(combinedRule, true);
			}
		}

		else if (QueryRunner.SHOW_AT_RULE) {
			if (this.type == PolicyElementType.Policy) {
				if (QueryRunner.debug)
					System.out.println(this.getId() + " -->" + this.alg.toString());
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, false).getFinalElement();
				return getTheRuleforPolicy(combinedRule, false);
			}
			if (this.type == PolicyElementType.PolicySet) {
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, true).getFinalElement();
				return getTheRuleforPolicy(combinedRule, true);
			}

		}

		else {
			if (this.type == PolicyElementType.Policy) {
				if (QueryRunner.debug)
					System.out.println(this.getId() + " -->" + this.alg.toString());
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, true).getFinalElement();
				return getTheRuleforPolicy(combinedRule, true);
			}
			if (this.type == PolicyElementType.PolicySet) {
				SMTRule combinedRule = this.alg.executeAlgAllPairs(this.type, true).getFinalElement();
				return getTheRuleforPolicy(combinedRule, true);
			}
		}

		System.err.println("IS IT A POLICY OR A POLICYSET ???");
		return null;
	}

}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/policy/SMTPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */