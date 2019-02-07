package policy;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import objects.PolicyElement;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import combiningalg.SMTAlgFactory;

public abstract class SMTPolicyElement {
	private final static Map<String, String> shortIdMap = new HashMap<String, String>();
	private static int nextPS = 1;
	private static int nextP = 1;
	private static int nextR = 1;
	private String id;
	PolicyElementType type;
	Context ctx;
	private final Map<Decision, BoolExpr> decisionDSMap = new HashMap<SMTPolicyElement.Decision, BoolExpr>();

	public enum PolicyElementType {
		PolicySet, Policy, Rule, CombinationWithDS, CombinationNoDS // DS: DecisionSpace
	}

	public enum Decision {
		Permit, Deny, IndetP, IndetD, IndetPD, Indet, Na
	}

	public static SMTAlgFactory.SMTCombiningAlg convertCombiningAlg(PolicyElement.CombiningAlg alg) {
		switch (alg) {
		case DenyOverrides:
			return SMTAlgFactory.SMTCombiningAlg.DenyOverrides;
		case PermitOverrides:
			return SMTAlgFactory.SMTCombiningAlg.PermitOverrides;
		case FirstApplicable:
			return SMTAlgFactory.SMTCombiningAlg.FirstApplicable;
		case OnlyOneApplicable:
			return SMTAlgFactory.SMTCombiningAlg.OnlyOneApplicable;
		case DenyUnlessPermit:
			return SMTAlgFactory.SMTCombiningAlg.DenyUnlessPermit;
		case PermitUnlessDeny:
			return SMTAlgFactory.SMTCombiningAlg.PermitUnlessDeny;
		}
		System.err.println("Could not find the combining algorithm ...");
		return null;
	}

	public abstract void print(PrintStream paramPrintStream);

	public abstract SMTRule getFinalElement();

	public PolicyElementType getType() {
		return this.type;
	}

	protected BoolExpr permitDSStr;
	protected BoolExpr denyDSStr;
	protected BoolExpr indeterminateDS_PDStr;
	protected BoolExpr indeterminateDS_PStr;
	protected BoolExpr indeterminateDS_DStr;
	protected BoolExpr naDSStr;

	public BoolExpr getIndeterminateDSStr() {
		return ctx.mkOr(getIndeterminateDS_PDStr(), getIndeterminateDS_PStr(), getIndeterminateDS_DStr());
	}

	protected void convertToComibinationWithDSType(SMTPolicy ownerPolicy) {
		this.id = ownerPolicy.getId() + ".Combination";
		this.type = PolicyElementType.CombinationWithDS;
		String shortId = shortIdMap.get(ownerPolicy.getId()) + ".Comb";
		shortIdMap.put(this.id, shortId);

		prepareOriginalMap(shortId);
	}

	private void prepareOriginalMap(String shortId) {
		BoolExpr permitDSStr = ctx.mkBoolConst(shortId + ".P");
		BoolExpr denyDSStr = ctx.mkBoolConst(shortId + ".D");
		BoolExpr naDSStr = ctx.mkBoolConst(shortId + ".NA");
		BoolExpr indeterminateDS_DStr = ctx.mkBoolConst(shortId + ".IN_D");
		BoolExpr indeterminateDS_PDStr = ctx.mkBoolConst(shortId + ".IN_PD");
		BoolExpr indeterminateDS_PStr = ctx.mkBoolConst(shortId + ".IN_P");

		setOriginalDecisionMap(permitDSStr, denyDSStr, indeterminateDS_PDStr, indeterminateDS_PStr, indeterminateDS_DStr, naDSStr);
	}

	protected void setIdAndType(String id, PolicyElementType type) {
		this.id = id;
		this.type = type;
		if (type != PolicyElementType.CombinationNoDS && type != PolicyElementType.CombinationWithDS) {
			String shortId = shortIdMap.get(id);
			this.type = type;
			if (shortId == null) {
				String typeStr;
				String idx;
				if (this.type == PolicyElementType.Policy) {
					typeStr = "POL";
					idx = nextPS + "." + nextP++;
					nextR = 1;
				} else if (this.type == PolicyElementType.PolicySet) {
					typeStr = "PS";
					idx = String.valueOf(nextPS++);
					nextP = 1;
				} else {
					typeStr = "R";
					idx = nextPS + "." + nextP + "." + nextR++;
				}
				shortId = typeStr + "_" + idx;
				shortIdMap.put(this.id, shortId);
			}

			prepareOriginalMap(shortId);

			setDSStr(permitDSStr, denyDSStr, indeterminateDS_PDStr, indeterminateDS_PStr, indeterminateDS_DStr, naDSStr);
		}
	}

	public void setDSStr(BoolExpr permitDSStr, BoolExpr denyDSStr, BoolExpr indeterminateDS_PDStr, BoolExpr indeterminateDS_PStr,
			BoolExpr indeterminateDS_DStr, BoolExpr naDSStr) {
		this.permitDSStr = apply(permitDSStr);
		this.denyDSStr = apply(denyDSStr);
		this.indeterminateDS_DStr = apply(indeterminateDS_DStr);
		this.indeterminateDS_PDStr = apply(indeterminateDS_PDStr);
		this.indeterminateDS_PStr = apply(indeterminateDS_PStr);
		this.naDSStr = apply(naDSStr);
	}

	private BoolExpr apply(BoolExpr ds) {
		return ds == null ? ctx.mkFalse() : ds;
	}

	protected void setOriginalDecisionMap(BoolExpr permitDSStr, BoolExpr denyDSStr, BoolExpr indeterminateDS_PDStr, BoolExpr indeterminateDS_PStr,
			BoolExpr indeterminateDS_DStr, BoolExpr naDSStr) {
		decisionDSMap.put(Decision.Permit, permitDSStr);
		decisionDSMap.put(Decision.Deny, denyDSStr);
		decisionDSMap.put(Decision.Na, naDSStr);
		decisionDSMap.put(Decision.IndetPD, indeterminateDS_PDStr);
		decisionDSMap.put(Decision.IndetP, indeterminateDS_PStr);
		decisionDSMap.put(Decision.IndetD, indeterminateDS_DStr);
		decisionDSMap.put(Decision.Indet,
				ctx.mkOr(decisionDSMap.get(Decision.IndetPD), decisionDSMap.get(Decision.IndetP), decisionDSMap.get(Decision.IndetD)));
	}

	public BoolExpr getDSStr(Decision decision, boolean withDetails) {
		if (!withDetails) {
			return decisionDSMap.get(decision);
		}

		switch (decision) {
		case Permit:
			return permitDSStr;
		case Deny:
			return denyDSStr;
		case IndetD:
			return indeterminateDS_DStr;
		case IndetP:
			return indeterminateDS_PStr;
		case IndetPD:
			return indeterminateDS_PDStr;
		case Indet:
			return getIndeterminateDSStr();
		case Na:
			return naDSStr;
		default:
			return null;
		}
	}

	public BoolExpr getPermitDSStr() {
		return permitDSStr;
	}

	public BoolExpr getDenyDSStr() {
		return denyDSStr;
	}

	public BoolExpr getIndeterminateDS_PDStr() {
		return indeterminateDS_PDStr;
	}

	public BoolExpr getIndeterminateDS_PStr() {
		return indeterminateDS_PStr;
	}

	public BoolExpr getIndeterminateDS_DStr() {
		return indeterminateDS_DStr;
	}

	public BoolExpr getNaDSStr() {
		return naDSStr;
	}

	public String getId() {
		return id;
	}
}