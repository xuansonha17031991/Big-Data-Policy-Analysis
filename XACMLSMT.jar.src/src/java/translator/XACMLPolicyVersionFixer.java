package translator;

import java.util.ArrayList;
import java.util.List;

import objects.Expression;
import parserv2.Match;
import parserv2.PolicySet;
import parserv2.XACMLExpression;
import policy.SMTPolicyElement.PolicyElementType;

public class XACMLPolicyVersionFixer {
	public objects.PolicyElement translateModel(parserv2.XACMLContainer xcon) {
		try {
			if ((xcon instanceof PolicySet)) {
				PolicySet pSet = (PolicySet) xcon;
				objects.Policy newpSet = new objects.Policy();
				newpSet.setId(pSet.Id);
				newpSet.setDescription(pSet.description);

				newpSet.setAlgorithm(PolicyElementType.PolicySet, pSet.getCombiningAlg().getAlgorithmText(2));
				newpSet.setTarget(translateTarget(pSet.target));
				for (int i = 0; i < pSet.getPolicySets().size(); i++) {
					PolicySet p = pSet.getPolicySets().get(i);
					objects.PolicyElement newPElem = translateModel(p);
					newpSet.addPolicyElement(newPElem);
					newPElem.setParent(newpSet);
				}

				for (int i = 0; i < pSet.getPolicies().size(); i++) {
					parserv2.Policy p = pSet.getPolicies().get(i);
					objects.PolicyElement newPElem = translateModel(p);
					newpSet.addPolicyElement(newPElem);
					newPElem.setParent(newpSet);
				}

				return newpSet;
			}
			if ((xcon instanceof parserv2.Policy)) {
				parserv2.Policy p = (parserv2.Policy) xcon;
				objects.Policy newP = new objects.Policy();
				newP.setAlgorithm(PolicyElementType.Policy, p.getCombiningAlg().getAlgorithmText(1));
				newP.setId(p.Id);
				newP.setDescription(p.description);

				newP.setTarget(translateTarget(p.target));

				for (int i = 0; i < p.getRules().size(); i++) {
					parserv2.Rule r = p.getRules().get(i);
					objects.Rule rNew = new objects.Rule();
					rNew.setId(r.Id);
					rNew.setEffect(r.effect.toString().toLowerCase().equals("permit") ? "permit" : "deny");
					rNew.setTarget(translateTarget(r.target));
					rNew.setParent(newP);

					if (r.condition != null) {
						assert (r.condition.con.apply != null);
						Expression newCon = new Expression();
						newCon.setId("Condition of Rule:" + r.Id);

						translateExpression(r.condition.con, newCon);
						newCon.setOwner(rNew);
						rNew.setCondition(newCon);
					}
					newP.addPolicyElement(rNew);
				}
				return newP;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void translateExpression(XACMLExpression expression, Expression parentEx) {
		switch (expression.type) {

		case 2:
			String fnStr = expression.apply.fn.isUserDefined() ? expression.apply.fn.userDefinedFunc : expression.apply.fn.predefinedFunc.toString();
			Expression temp = this.funcFactor.create(fnStr.replace("_", "-"));
			for (int i = 0; i < expression.apply.exList.size(); i++) {
				XACMLExpression ex = expression.apply.exList.get(i);
				translateExpression(ex, temp);
			}
			parentEx.addExpression(temp);
			break;
		case 3:
			temp = parentEx.createAttributeSelector(expression.atts.mustBePresent, "UNKNOWN-ATTRIBUTE-SELECTOR-V2-DOES-NOT-HAVE", expression.atts.dataType

			.toString(), expression.atts.requestContextPath);

			parentEx.addExpression(temp);
			break;
		case 4:
			temp = parentEx.createAttributeValue(expression.att.dataType.toString(), expression.att.value);

			parentEx.addExpression(temp);
			break;
		case 5:
			break;

		case 6:
			temp = parentEx.createAttributeDesignator(expression.ad.mustBePresent, findCategory(expression.ad.designatorType),
					expression.ad.attributeId.toString(), expression.ad.dataType.toString());
			parentEx.addExpression(temp);
		}

	}

	private List<List<Expression>> addOneAnyOf(List<parserv2.MatchList> elements, String cat) {
		try {
			List<List<Expression>> oneAnyOf = new ArrayList();
			for (int i = 0; i < elements.size(); i++) {
				List<Expression> allOfs = new ArrayList();
				parserv2.MatchList mList = elements.get(i);
				ArrayList<Match> listOfMatches = mList.getMatchList();
				for (int j = 0; j < listOfMatches.size(); j++) {
					Match m = listOfMatches.get(j);

					String fnStr = m.matchID.isUserDefined() ? m.matchID.userDefinedFunc : m.matchID.predefinedFunc.toString();
					functions.Function f = this.funcFactor.create(fnStr.replace("_", "-"));
					if (m.ad != null) {
						f.addExpression(f.createAttributeDesignator(m.ad.mustBePresent, cat, m.ad.attributeId

						.toString(), m.ad.dataType.toString()));
					}
					if (m.as != null) {
						f.addExpression(f.createAttributeSelector(m.as.mustBePresent, cat, m.as.dataType

						.toString(), m.as.requestContextPath));
					}

					if (m.attribute != null) {
						f.addExpression(f.createAttributeValue(m.attribute.dataType.toString(), m.attribute.value));
					}

					allOfs.add(f);
				}
				oneAnyOf.add(allOfs);
			}
			return oneAnyOf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	functions.FunctionFactory funcFactor = new functions.FunctionFactory();

	private objects.Target translateTarget(parserv2.Target t) {
		try {
			objects.Target newT = new objects.Target();

			if (t != null) {
				List<parserv2.MatchList> elements = t.getSubjects();
				if ((elements != null) && (elements.size() > 0)) {
					newT.addAnyOf(addOneAnyOf(elements, "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));
				}
				elements = t.getResources();
				if ((elements != null) && (elements.size() > 0)) {
					newT.addAnyOf(addOneAnyOf(elements, "urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));
				}
				elements = t.getActions();
				if ((elements != null) && (elements.size() > 0)) {
					newT.addAnyOf(addOneAnyOf(elements, "urn:oasis:names:tc:xacml:3.0:attribute-category:action"));
				}
				elements = t.getEnvironments();
				if ((elements != null) && (elements.size() > 0)) {
					newT.addAnyOf(addOneAnyOf(elements, "urn:oasis:names:tc:xacml:3.0:attribute-category:environment"));
				}
				return newT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String findCategory(int type) {
		switch (type) {
		case 1:
			return "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
		case 2:
			return "urn:oasis:names:tc:xacml:3.0:attribute-category:resource";
		case 3:
			return "urn:oasis:names:tc:xacml:3.0:attribute-category:action";
		case 4:
			return "urn:oasis:names:tc:xacml:3.0:attribute-category:environment";
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			XACMLPolicyVersionFixer pvf = new XACMLPolicyVersionFixer();
			parserv3.DOMParser dp = new parserv3.DOMParser();
			objects.Policy p = dp.parse("../XACMLEngine/policies/v3/Pol-XU09_Example.xacml");

			parserv2.XACMLParser xp = new parserv2.XACMLParser("../XACMLEngine/policies/v2/GradeSheetDPolicy.xml");
			objects.Policy pol = (objects.Policy) pvf.translateModel(xp.container);

			System.out.println(pol.convert2String());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/translator/XACMLPolicyVersionFixer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */