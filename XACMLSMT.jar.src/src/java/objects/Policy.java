package objects;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import policy.SMTPolicyElement.PolicyElementType;
import utils.StringUtils;

public class Policy extends PolicyElement {
	private URI nameSpace;
	private String version;
	List<PolicyElement> polElements;
	private PolicyElementType type;
	List<Expression> variableDefinitions;

	public Expression createVariableDefinition(String Id) {
		Expression varDef = new Expression();
		varDef.setId(Id);
		return varDef;
	}

	public PolicyElementType getType() {
		return this.type;
	}

	public Policy() {
		this.polElements = new ArrayList();
		this.variableDefinitions = new ArrayList();
	}

	private PolicyElement.CombiningAlg findAlgorithm(String algName) {
		String algStr = StringUtils.getShortName(algName);
		PolicyElement.CombiningAlg result = null;
		if (algStr
				.replace("-", "")
				.toUpperCase()
				.equals(PolicyElement.CombiningAlg.DenyOverrides.toString()
						.toUpperCase())) {
			result = PolicyElement.CombiningAlg.DenyOverrides;
		} else if (algStr
				.replace("-", "")
				.toUpperCase()
				.equals(PolicyElement.CombiningAlg.PermitOverrides.toString()
						.toUpperCase())) {
			result = PolicyElement.CombiningAlg.PermitOverrides;
		} else if (algStr
				.replace("-", "")
				.toUpperCase()
				.equals(PolicyElement.CombiningAlg.FirstApplicable.toString()
						.toUpperCase())) {
			result = PolicyElement.CombiningAlg.FirstApplicable;
		} else if (algStr
				.replace("-", "")
				.toUpperCase()
				.equals(PolicyElement.CombiningAlg.PermitUnlessDeny.toString()
						.toUpperCase())) {
			result = PolicyElement.CombiningAlg.PermitUnlessDeny;
		} else if (algStr
				.replace("-", "")
				.toUpperCase()
				.equals(PolicyElement.CombiningAlg.DenyUnlessPermit.toString()
						.toUpperCase()))
			result = PolicyElement.CombiningAlg.DenyUnlessPermit;
		assert (result != null) : ("Combining Algorithm of a policy or a rule can not be " + algName);
		return result;
	}

	public List<Expression> getVariableDefinitions() {
		List<Expression> resultList = new ArrayList(this.variableDefinitions);
		if ((getParent() != null)
				&& (getParent().getVariableDefinitions() != null)
				&& (getParent().getVariableDefinitions().size() > 0)) {
			resultList.addAll(getParent().getVariableDefinitions());
		}
		return resultList;
	}

	public void addVariable(Expression vd) {
		this.variableDefinitions.add(vd);
	}

	public Expression findVariable(String id) {
		for (Iterator<Expression> it = this.variableDefinitions.iterator(); it
				.hasNext();) {
			Expression vd = it.next();
			if (vd.getId().equals(id))
				return vd;
		}
		return null;
	}

	public void setAlgorithm(PolicyElementType t, String algName) {
		this.type = t;
		PolicyElement.CombiningAlg alg = findAlgorithm(algName);

		if (this.type == PolicyElementType.Policy) {
			assert ((alg != PolicyElement.CombiningAlg.DenyOverrides)
					|| (alg != PolicyElement.CombiningAlg.PermitOverrides)
					|| (alg != PolicyElement.CombiningAlg.FirstApplicable)
					|| (alg != PolicyElement.CombiningAlg.PermitUnlessDeny) || (alg != PolicyElement.CombiningAlg.DenyUnlessPermit)) : ("Combining Algorithm of a policy can not be " + alg);

			this.comAlg = alg;
		} else if (this.type == PolicyElementType.PolicySet) {
			assert ((alg != PolicyElement.CombiningAlg.DenyOverrides)
					|| (alg != PolicyElement.CombiningAlg.PermitOverrides)
					|| (alg != PolicyElement.CombiningAlg.FirstApplicable)
					|| (alg != PolicyElement.CombiningAlg.PermitUnlessDeny) || (alg != PolicyElement.CombiningAlg.DenyUnlessPermit)) : ("Combining Algorithm of a policyset can not be " + alg);

			this.comAlg = alg;
		}
	}

	public PolicyElement.CombiningAlg getAlgorithm() {
		return this.comAlg;
	}

	public void addPolicyElement(PolicyElement r) {
		this.polElements.add(r);
	}

	public URI getURI() {
		return this.nameSpace;
	}

	public void setVersion(String v) {
		this.version = v;
	}

	public String getVersion() {
		return this.version;
	}

	public List<PolicyElement> getPolicyElements() {
		return this.polElements;
	}

	public String convert2String() {
		String str = new String();
		str = this.type == PolicyElementType.PolicySet ? "[PSetId=" : "[PolId=";

		str = str
				+ this.id
				+ " ComAlg:"
				+ getAlgorithm().toString()
				+ "]"
				+ (this.description != null ? "\nDesc:" + this.description : "")
				+ ((this.target != null) && (this.target.anyOfs.size() != 0) ? "\n[Target]"
						+ this.target.convert2String()
						: "\nTarget:EMPTY\n");
		Iterator<Expression> it;
		if (!this.variableDefinitions.isEmpty()) {
			str = str + "Variables...\n";
			for (it = this.variableDefinitions.iterator(); it.hasNext();) {
				Expression vd = it.next();
				str = str + "Variable:" + vd.getId() + "\n";
			}
		}
		if ((this.polElements.size() > 0) && (this.type == PolicyElementType.Policy))
			str = str + "\nRule of " + this.id;
		if ((this.polElements.size() > 0) && (this.type == PolicyElementType.PolicySet))
			str = str + "\nPolicy of " + this.id + "\n";
		for (Iterator<PolicyElement> itPol = this.polElements.iterator(); itPol
				.hasNext();) {
			if (this.type == PolicyElementType.PolicySet) {
				Policy pe = (Policy) itPol.next();
				str = str + pe.convert2String() + "\n";
			} else if (this.type == PolicyElementType.Policy) {
				Rule r = (Rule) itPol.next();
				str = str + r.convert2String() + "\n";
			}
		}

		return str;
	}
}

/*
 * Location: /Users/okielabackend/Downloads/XACMLSMT.jar!/objects/Policy.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */