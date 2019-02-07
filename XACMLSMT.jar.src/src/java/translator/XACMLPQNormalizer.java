package translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import objects.Expression;
import objects.Policy;
import objects.PolicyElement;
import objects.Rule;
import objects.Target;
import parserv2.XACMLParser;
import parserv3.DOMParser;
import policy.SMTPolicy;
import policy.SMTPolicyElement.PolicyElementType;
import query.QueryNode;
import query.QueryParser;
import query.QueryRunner;
import utils.Pair;
import utils.StringUtils;
import utils.Tree;

import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

import functions.FunctionFactory;

public class XACMLPQNormalizer {
	Context ctx = null;

	XACMLTranslator xc;

	static Map<String, String> policyVariables;

	static Map<String, String> queryVariables;
	static Map<String, Pair<String, Vector<String>>> enumerateDomains;

	public XACMLPQNormalizer() {
		initialize();
		HashMap<String, String> cfg = new HashMap();

		cfg.put("model", "true");
		cfg.put("proof", "true");
		try {
			this.ctx = new Context(cfg);
			// this.ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_COMPLIANT);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception " + zex.getMessage());
		}
	}

	public Context getContext() {
		return this.ctx;
	}

	public XACMLTranslator getTranslator() {
		return this.xc;
	}

	public Map<String, Pair<String, Vector<String>>> getEnumeratedDomains() {
		return enumerateDomains;
	}

	public Map<String, String> getPolicyVariables() {
		return policyVariables;
	}

	public Map<String, String> getQueryVariables() {
		return queryVariables;
	}

	public void printVarsAndDomains() {
		for (Map.Entry<String, String> var : policyVariables.entrySet()) {
			System.out.print(var.getKey() + " : [");
			if (enumerateDomains.get(var.getValue().toLowerCase()) != null) {
				Vector<String> values = (Vector) ((Pair) enumerateDomains
						.get(var.getValue().toLowerCase())).second;
				for (int i = 0; i < values.size(); i++) {
					String value = values.get(i);
					System.out.print(value + (i < values.size() - 1 ? "," : ""));
				}
				System.out.println("]");
			} else {
				System.out.println(var.getValue().toLowerCase() + "]");
			}
		}
	}

	private void initialize() {
		policyVariables = new HashMap();
		queryVariables = new HashMap();
		setEnumerateDomains();
	}

	private void setEnumerateDomains() {
		enumerateDomains = new HashMap();
		Vector<String> stringVector = new Vector();

		enumerateDomains.put("string", new Pair("string", stringVector));

		enumerateDomains.put("rfc822name", new Pair("string", stringVector));

		enumerateDomains.put("anyuri", new Pair("string", stringVector));

		enumerateDomains.put("date", new Pair("date", new Vector()));

		enumerateDomains.put("datetime", new Pair("datetime", new Vector()));

		enumerateDomains.put("yearmonthduration", new Pair("yearmonthduration", new Vector()));

		enumerateDomains.put("xpathexpression", new Pair("xpathexpression", new Vector()));
	}

	public String getShortDName(String longName) {
		if (longName.lastIndexOf("#") == -1) {
			return StringUtils.getShortName(longName);
		}
		return longName.substring(longName.lastIndexOf("#") + 1, longName.length());
	}

	private void checkForVariable(Expression ex) {
		if ((ex instanceof Expression.AttributeValue)) {
			Pair<String, Vector<String>> entry = enumerateDomains
					.get(getShortDName(((Expression.AttributeValue) ex).getDataType().toLowerCase()));
			if (entry != null) {
				Vector<String> values = entry.second;
				String value = ((Expression.AttributeValue) ex).getValue();
				if (!values.contains(value))
					values.add(value);
			}
		} else if ((ex instanceof Expression.AttributeSelector)) {
			Pair<String, Vector<String>> entry = enumerateDomains
					.get(getShortDName(((Expression.AttributeSelector) ex).getDataType().toLowerCase()));
			if (entry != null) {
				Vector<String> values = entry.second;
				String value = ((Expression.AttributeSelector) ex).getPath();
				if (!values.contains(value))
					values.add(value);
			}
		} else {
			// For variable
			String type;
			if ((ex instanceof Expression.AttributeDesignator)) {
				String var = ((Expression.AttributeDesignator) ex).getId();
				type = ((Expression.AttributeDesignator) ex).getDataType();
				if (!policyVariables.containsKey(var))
					policyVariables.put(var, type.toLowerCase());
			} else {
				List<Expression> parameters = ex.getParameters();
				if ((parameters == null) || (parameters.size() == 0))
					return;
				Expression param;
				for (Iterator<Expression> typeIt = parameters.iterator(); typeIt.hasNext(); checkForVariable(param))
					param = typeIt.next();
			}
		}
	}

	private void getVarsTarget(Target t) {
		if ((t == null) || (t.getAnyOfs().size() == 0))
			return;
		List<List<List<Expression>>> target = t.getAnyOfs();

		for (Iterator<List<List<Expression>>> oneAnyOfIt = target.iterator(); oneAnyOfIt.hasNext();) {
			List<List<Expression>> oneAnyOf = oneAnyOfIt.next();

			for (Iterator<List<Expression>> oneAllOfIt = oneAnyOf.iterator(); oneAllOfIt.hasNext();) {
				List<Expression> oneAllOf = oneAllOfIt.next();
				for (Iterator<Expression> expIt = oneAllOf.iterator(); expIt.hasNext();)
					checkForVariable(expIt.next());
			}
		}
	}

	int numberOfPolicies = 0;
	int numberOfPolicySets = 0;
	int numberOfRules = 0;

	private void findNumberOfElements(PolicyElement p) {
		if ((p instanceof Policy)) {
			Policy pTemp = (Policy) p;
			if (pTemp.getType() == PolicyElementType.Policy) {
				this.numberOfPolicies += 1;
			} else if (pTemp.getType() == PolicyElementType.PolicySet)
				this.numberOfPolicySets += 1;
			for (int i = 0; i < pTemp.getPolicyElements().size(); i++) {
				findNumberOfElements(pTemp.getPolicyElements().get(i));
			}
		}
		if ((p instanceof Rule)) {
			this.numberOfRules += 1;
		}
	}

	private void setEnumeratedDomainsFromPolicy(Policy p) {
		getVarsTarget(p.getTarget());

		List<Expression> variables = p.getVariableDefinitions();
		Iterator<Expression> itVar;
		if (variables != null) {
			for (itVar = variables.iterator(); itVar.hasNext();) {
				checkForVariable(itVar.next());
			}
		}

		List<PolicyElement> eList = p.getPolicyElements();
		for (Iterator<PolicyElement> itP = eList.iterator(); itP.hasNext();) {
			PolicyElement pElem = itP.next();

			if ((pElem instanceof Policy)) {
				setEnumeratedDomainsFromPolicy((Policy) pElem);
			} else if ((pElem instanceof Rule)) {
				getVarsTarget(((Rule) pElem).getTarget());
				if (((Rule) pElem).getCondition() != null) {
					checkForVariable(((Rule) pElem).getCondition());
				}
			}
		}
	}

	public Policy parseXMLDOM(String fileName, int version) {
		try {
			if (version == 3) {
				DOMParser dm = new DOMParser();
				Policy p = dm.parse(fileName);
				if (QueryRunner.debug) {
					System.out.println(p.convert2String());
				}
				return p;
			}
			if (version == 2) {
				XACMLPolicyVersionFixer pvf = new XACMLPolicyVersionFixer();
				XACMLParser xp = new XACMLParser(fileName);
				return (Policy) pvf.translateModel(xp.container);
			}
		} catch (Exception e) {
			System.err.println("Exception in read of DOMParser " + e.getMessage());
		}
		return null;
	}

	private Pair<Boolean, Boolean> checkAndAddQueryValueToEnumeratedDomains(QueryNode firstChild, QueryNode secondChild) {
		boolean varNotFound = true;
		boolean valueNotFound = true;
		for (Map.Entry<String, String> entry : policyVariables.entrySet()) {
			// Firstly check from enumerated Domains.
			Pair<String, Vector<String>> varSort = enumerateDomains.get(getShortDName(entry.getValue()));

			if (varSort == null) {
				continue;
			}

			boolean firstChildPolicyVar = entry.getKey().equals(firstChild.getIdentifier().second);
			boolean secondChildPolicyVar = entry.getKey().equals(secondChild.getIdentifier().second);

			if ((firstChildPolicyVar || secondChildPolicyVar) && (varNotFound)) {
				varNotFound = false;
			}

			// FirstChild is variable, SecondChild is value
			if ((firstChildPolicyVar) && (!((Vector) varSort.second).contains(secondChild.getIdentifier().second))
					&& (!QueryParser.specialNodes
							.containsKey(secondChild.getIdentifier().second.toLowerCase()))) {
				((Vector) varSort.second).addElement(secondChild.getIdentifier().second); // Add value to the EnumeratedDomain
				valueNotFound = false;
				break;
			}

			// FirstChild is value, SecondChild is variable
			if ((secondChildPolicyVar) && (!((Vector) varSort.second).contains(firstChild.getIdentifier().second))
					&& (!QueryParser.specialNodes
							.containsKey(firstChild.getIdentifier().second.toLowerCase()))) {
				((Vector) varSort.second).addElement(firstChild.getIdentifier().second); // Add value to the EnumeratedDomain
				valueNotFound = false;
				break;
			}
		}
		return new Pair(Boolean.valueOf(varNotFound), Boolean.valueOf(valueNotFound));
	}

	private QueryNode checkQueryNodes(Tree t, String element, int depth, HashMap<String, QueryNode> nodesOfOrgTree) {
		ArrayList<String> children = t.getNodes().get(element).getChildren();

		QueryNode qNode = nodesOfOrgTree.get(element);
		if (qNode.getOperation() != null) {
			switch (qNode.getOperation()) {
			case Equals:
				if (children.size() != 2)
					System.err.println("There was an error here since more (or less) than two parameters...");
				QueryNode firstChild = checkQueryNodes(t, children.get(0), depth, nodesOfOrgTree);
				QueryNode secondChild = checkQueryNodes(t, children.get(1), depth, nodesOfOrgTree);

				Pair<Boolean, Boolean> varValueNotFound = checkAndAddQueryValueToEnumeratedDomains(firstChild, secondChild);

				if (varValueNotFound.first.booleanValue()) {// Variable is not found.
					String typeName = FunctionFactory.findTypeName((FunctionFactory.DataTypes) firstChild.getTypeAndValue().first);

					if (!policyVariables.containsKey(firstChild.getIdentifier().second)) {
						queryVariables.put(firstChild.getIdentifier().second, typeName);
					}
				}
				if (varValueNotFound.second.booleanValue()) {// If the value is not found yet.
					String typeName = FunctionFactory.findTypeName((FunctionFactory.DataTypes) secondChild.getTypeAndValue().first);

					Pair<String, Vector<String>> varSort = enumerateDomains.get(typeName);
					if (varSort != null) {
						varSort.second.addElement(secondChild.getIdentifier().second);// Value from query is added to the enumerateDomains.
					}
				}

				// How about the single value domain?
				return null;
			}

			if (QueryRunner.debug) {
				System.err.println(
						"This operator is not supported yet in the query !!! " + qNode.getIdentifier().first);
			}
		} else if (qNode.getTypeAndValue() != null) {
			return qNode;
		}

		for (int i = 0; i < children.size(); i++) {
			checkQueryNodes(t, children.get(i), depth, nodesOfOrgTree);
		}

		return null;
	}

	private void setDomainsFromQuery(String query) {
		Tree tree = new Tree();
		QueryParser qp = new QueryParser(query, QueryParser.EXPRESSIONTYPE.Infix);
		tree.addNode(qp.Root.getIdentifier().first);
		QueryRunner.convertToCustomTree(qp.Root, null, tree);

		checkQueryNodes(tree, qp.Root.getIdentifier().first, 0, qp.getNodes());
	}

	public SMTPolicy[] loadAndTranslatePolicies(String[] policyFiles, int version, String query) {
		Policy[] policies = new Policy[policyFiles.length];
		SMTPolicy[] smtPolicies = new SMTPolicy[policyFiles.length];
		for (int i = 0; i < policyFiles.length; i++) {
			Policy p = parseXMLDOM(policyFiles[i], version);

			findNumberOfElements(p);

			setEnumeratedDomainsFromPolicy(p);
			policies[i] = p;
		}

		if (query != null) {
			setDomainsFromQuery(query);
		}
		this.xc = new XACMLTranslator(this, version);
		long timeBeginLoad = System.currentTimeMillis();
		for (int i = 0; i < policyFiles.length; i++) {

			try {

				SMTPolicy smtPol = this.xc.translatePolicy(policies[i].getType(), policies[i]);

				smtPolicies[i] = smtPol;
			} catch (Exception e) {
				System.err.println("Error loading the policy in (loadAndTranslatePolicies)" + e.getMessage());
			}
		}

		long timeEndLoad = System.currentTimeMillis();

		return smtPolicies;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/translator/XACMLPQNormalizer.
 * class Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */