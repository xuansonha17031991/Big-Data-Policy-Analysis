package parserv3;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import objects.Expression;
import objects.Policy;
import objects.Rule;
import objects.Target;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import policy.SMTPolicyElement.PolicyElementType;
import functions.Function;
import functions.FunctionFactory;

public class DOMParser implements IParser {
	private final DocumentBuilder builder;
	FunctionFactory ff;

	public DOMParser() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		this.builder = factory.newDocumentBuilder();
		this.ff = new FunctionFactory();
	}

	public String getParserName() {
		return "W3C DOM";
	}

	@Override
	public Policy parse(String fileName) {
		try {
			InputStream anInputStream = new FileInputStream(fileName);
			Document d = this.builder.parse(anInputStream, "utf-8");
			Policy result = new Policy();
			parsePolicyElement(d.getChildNodes(), result, false);

			return result;
		} catch (Exception e) {
			System.err.println("Exception in read of DOMParser " + e.getMessage());
		}
		return null;
	}

	public void parsePolicyElement(NodeList nodes, Policy aTo, boolean createInstance) {
		try {
			for (int j = 0; j < nodes.getLength(); j++) {
				Node n = nodes.item(j);
				if ((n.getNodeType() == 1) && ("Description".equals(getTag(n)))) {
					String desc = n.getTextContent();
					aTo.setDescription(desc);
				} else if ((n.getNodeType() == 1) && ("Target".equals(getTag(n)))) {
					Target t = new Target();
					parseTarget((Element) n, t);
					aTo.setTarget(t);
				} else if ((n.getNodeType() == 1) && ("Rule".equals(getTag(n)))) {
					Rule r = new Rule();
					r.setParent(aTo);
					parseRule((Element) n, r);
					aTo.addPolicyElement(r);
				} else if ((n.getNodeType() != 1) || (!"PolicyIdReference".equals(getTag(n)))) {

					if ((n.getNodeType() == 1) && ("VariableDefinition".equals(getTag(n)))) {
						Expression c = aTo
								.createVariableDefinition(n.getAttributes().getNamedItem("VariableId").getNodeValue());
						c.setOwner(aTo);

						parseExpression((Element) n, c);

						aTo.addVariable(c);
					} else if ((n.getNodeType() == 1) && ("Policy".equals(getTag(n)))) {
						Policy childP = null;
						parsePolicyElement(n.getChildNodes(), createInstance ? (childP = new Policy()) : aTo, false);
						if (createInstance) {
							aTo.addPolicyElement(childP);

							childP.setAlgorithm(PolicyElementType.Policy, n.getAttributes().getNamedItem("RuleCombiningAlgId").getNodeValue());
							childP.setId(n.getAttributes().getNamedItem("PolicyId").getNodeValue());
							childP.setParent(aTo);
						} else {
							aTo.setAlgorithm(PolicyElementType.Policy, n.getAttributes().getNamedItem("RuleCombiningAlgId").getNodeValue());
							aTo.setId(n.getAttributes().getNamedItem("PolicyId").getNodeValue());
						}
					} else if ((n.getNodeType() == 1) && ("PolicySet".equals(getTag(n)))) {
						Policy childPset = null;
						parsePolicyElement(n.getChildNodes(), createInstance ? (childPset = new Policy()) : aTo, true);
						if (createInstance) {
							aTo.addPolicyElement(childPset);
							childPset.setAlgorithm(PolicyElementType.PolicySet,
									n.getAttributes().getNamedItem("PolicyCombiningAlgId").getNodeValue());
							childPset.setId(n.getAttributes().getNamedItem("PolicySetId").getNodeValue());
							childPset.setParent(aTo);
						} else {
							aTo.setAlgorithm(PolicyElementType.PolicySet, n.getAttributes().getNamedItem("PolicyCombiningAlgId").getNodeValue());
							aTo.setId(n.getAttributes().getNamedItem("PolicySetId").getNodeValue());
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in  parsePolicyElement " + e.getMessage());
		}
	}

	private String getTag(Node n) {
		final String nodeName = n.getNodeName();
		if (nodeName.contains(":")) {
			String[] split = nodeName.split(":");
			return split[split.length-1];
		}
		return nodeName;
	}

	public void parseTarget(Element aTarget, Target aTo) {
		NodeList nodes = aTarget.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if ((n.getNodeType() == 1) && ("AnyOf".equals(getTag(n)))) {
				NodeList allOfNodes = n.getChildNodes();
				List<List<Expression>> allOfs = new ArrayList();
				for (int j = 0; j < allOfNodes.getLength(); j++) {
					Node n1 = allOfNodes.item(j);
					if ((n1.getNodeType() == 1) && ("AllOf".equals(getTag(n1)))) {
						List<Expression> allOf = new ArrayList();
						NodeList attMatchElems = n1.getChildNodes();
						for (int k = 0; k < attMatchElems.getLength(); k++) {
							Node n2 = attMatchElems.item(k);
							if ((n2.getNodeType() == 1) && ("Match".equals(getTag(n2)))) {
								Function a = this.ff.create(n2.getAttributes().getNamedItem("MatchId").getNodeValue());

								parseExpression((Element) n2, a);
								a.setId(n2.getAttributes().getNamedItem("MatchId").getNodeValue());
								allOf.add(a);
							}
						}
						allOfs.add(allOf);
					}
				}
				aTo.addAnyOf(allOfs);
			}
		}
	}

	public void parseExpression(Element anExpression, Expression aTo) {
		try {
			NodeList nodes = anExpression.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if ((n.getNodeType() == 1) && ("Apply".equals(getTag(n)))) {
					Function aMatch = this.ff.create(n.getAttributes().getNamedItem("FunctionId").getNodeValue());
					aMatch.setOwner(aTo.getOwnerPolicy());
					aMatch.setParent(aTo);
					parseExpression((Element) n, aMatch);

					aTo.addExpression(aMatch);
				} else if ((n.getNodeType() == 1) && ("AttributeValue".equals(getTag(n)))) {
					Expression av = aTo.createAttributeValue(n.getAttributes().getNamedItem("DataType").getNodeValue(),
							n.getTextContent());
					av.setParent(aTo);

					aTo.addExpression(av);
				} else {
					if ((n.getNodeType() == 1) && ("AttributeDesignator".equals(getTag(n)))) {
						Expression ad = aTo.createAttributeDesignator(
								getMBP(n),
								getCategory(n),
								n.getAttributes().getNamedItem("AttributeId").getNodeValue(),
								n.getAttributes().getNamedItem("DataType").getNodeValue());
						ad.setParent(aTo);
						aTo.addExpression(ad);
					} else if ((n.getNodeType() == 1) && ("AttributeSelector".equals(getTag(n)))) {
						Expression as = aTo.createAttributeSelector(
								getMBP(n),
								getCategory(n),
								n.getAttributes().getNamedItem("DataType").getNodeValue(),
								n.getAttributes().getNamedItem("Path").getNodeValue());
						as.setParent(aTo);
						aTo.addExpression(as);
					} else if ((n.getNodeType() == 1) && ("VariableReference".equals(getTag(n)))) {
						Expression ex = ((Policy) aTo.getOwnerPolicy())
								.findVariable(n.getAttributes().getNamedItem("VariableId").getNodeValue());
						ex.setParent(aTo);
						assert (ex != null) : "Variable Reference couldn't be found!!!";

						aTo.addExpression(ex);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in  parseExpression " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String getCategory(Node n) {
		Node namedItem = n.getAttributes().getNamedItem("Category");
		if (namedItem != null) {
			return namedItem.getNodeValue();
		}
		return null;
	}

	private boolean getMBP(Node n) {
		Node nodeValue = n.getAttributes().getNamedItem("MustBePresent");
		boolean mbp = false;
		if (nodeValue != null) {
			mbp = Boolean.valueOf(nodeValue.getNodeValue()).booleanValue();
		}
		return mbp;
	}

	private void parseRule(Element aRule, Rule aTo) {
		try {
			NodeList nodes = aRule.getChildNodes();
			aTo.setId(aRule.getAttributes().getNamedItem("RuleId").getNodeValue());
			aTo.setEffect(aRule.getAttributes().getNamedItem("Effect").getNodeValue());
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if ((n.getNodeType() == 1) && ("Description".equals(getTag(n)))) {
					String desc = n.getTextContent();
					aTo.setDescription(desc);
				} else if ((n.getNodeType() == 1) && ("Target".equals(getTag(n)))) {
					Target t = new Target();
					parseTarget((Element) n, t);
					aTo.setTarget(t);
				} else if ((n.getNodeType() == 1) && ("Condition".equals(getTag(n)))) {
					Expression c = new Expression();
					c.setOwner(aTo.getParent());
					parseExpression((Element) n, c);
					c.setId("Condition of Rule:" + aTo.getId());
					aTo.setCondition(c);
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in parseRule " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		/*try {
			DOMParser dp = new DOMParser();

			Policy p = dp.parse(
					"/Users/fturkmen/Desktop/Work_On_And_Copy/Papers/CYCLONE-Papers/paper-cloudspd2016/experiments/continue-av3.xml");

			System.out.println(p.convert2String());
		} catch (Exception e) {
			System.err.println("Exception in main DomParser" + e.getMessage());
			e.printStackTrace();
		}*/
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv3/DOMParser.class Java
 * compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */