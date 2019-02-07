package combiningalg;

import policy.SMTPolicyElement;
import policy.SMTPolicyElement.PolicyElementType;
import policy.SMTRule;
import utils.Pair;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

public abstract class SMTCombiningAlg implements ISMTCombAlg {
	protected SMTRule result = null;

	protected SMTPolicyElement[] elementList;
	protected Context ctx;
	protected int version;

	private void executeAlgOnePair(PolicyElementType type, SMTPolicyElement tempPelement, int index, boolean withDetails) {
		if (this.version == 2) {
			if (type == PolicyElementType.Policy) {
				runv2Rule(tempPelement.getId() + "_" + index, (SMTRule) tempPelement,
						this.elementList[index].getFinalElement(), this.ctx);
			} else if (type == PolicyElementType.PolicySet) {

				runv2PSet(tempPelement.getId() + "_" + index, (SMTRule) tempPelement,
						this.elementList[index].getFinalElement(), this.ctx);
			}
		} else if (this.version == 3) {
			if (type == PolicyElementType.Policy) {
				runv3Rule(tempPelement.getId() + "_" + index, (SMTRule) tempPelement,
						this.elementList[index].getFinalElement(), this.ctx, withDetails);
			} else if (type == PolicyElementType.PolicySet)
				runv3PSet(tempPelement.getId() + "_" + index, (SMTRule) tempPelement,
						this.elementList[index].getFinalElement(), this.ctx, withDetails);
		}
	}

	@Override
	public SMTPolicyElement executeAlgAllPairs(PolicyElementType type, boolean withDetails) {
		SMTRule tempPelement = this.elementList[0].getFinalElement();//
		if (this.elementList.length == 1) {
			executeAlgOnePair(type, tempPelement, 0, withDetails);
			tempPelement = getCombinedElement(type);
		} else {
			for (int i = 1; i < this.elementList.length; i++) {
				executeAlgOnePair(type, tempPelement, i, withDetails);
				tempPelement = getCombinedElement(type);
			}
		}
		return tempPelement;
	}

	public SMTRule getCombinedElement(PolicyElementType type) {
		return this.result;
	}

	protected abstract void runv2Rule(String paramString, SMTRule paramSMTRule1, SMTRule paramSMTRule2,
			Context paramContext);

	protected abstract void runv2PSet(String paramString, SMTRule paramSMTRule1, SMTRule paramSMTRule2,
			Context paramContext);

	protected abstract void runv3Rule(String paramString, SMTRule paramSMTRule1, SMTRule paramSMTRule2,
			Context paramContext, boolean withDetails);

	protected abstract void runv3PSet(String paramString, SMTRule paramSMTRule1, SMTRule paramSMTRule2,
			Context paramContext, boolean withDetails);

	protected Pair<BoolExpr, BoolExpr> and(Pair<BoolExpr, BoolExpr>... args) {
		Pair<BoolExpr[], BoolExpr[]> convert = convert(args);
	
		BoolExpr expr = ctx.mkAnd(convert.first);
		BoolExpr str = ctx.mkAnd(convert.second);
		return new Pair<BoolExpr, BoolExpr>(expr, str);
	}

	private Pair<BoolExpr[], BoolExpr[]> convert(Pair<BoolExpr, BoolExpr>... args) {
		BoolExpr[] arrayStr = new BoolExpr[args.length];
		BoolExpr[] arrayExpr = new BoolExpr[args.length];
	
		for (int idx = 0; idx < args.length; idx++) {
			arrayExpr[idx] = args[idx].first;
			arrayStr[idx] = args[idx].second;
		}
		Pair<BoolExpr[], BoolExpr[]> convert = new Pair<BoolExpr[], BoolExpr[]>(arrayExpr, arrayStr);
		return convert;
	}

	protected Pair<BoolExpr, BoolExpr> or(Pair<BoolExpr, BoolExpr>... args) {
		Pair<BoolExpr[], BoolExpr[]> convert = convert(args);
	
		BoolExpr expr = ctx.mkOr(convert.first);
		BoolExpr str = ctx.mkOr(convert.second);
		return new Pair<BoolExpr, BoolExpr>(expr, str);
	}

	protected Pair<BoolExpr, BoolExpr> not(Pair<BoolExpr, BoolExpr> arg) {
		BoolExpr expr = ctx.mkNot(arg.first);
		BoolExpr str = ctx.mkNot(arg.second);
		return new Pair<BoolExpr, BoolExpr>(expr, str);
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/SMTCombiningAlg.
 * class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */