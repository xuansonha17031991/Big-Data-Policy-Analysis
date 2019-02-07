package parserv3;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import objects.Expression;
import objects.Policy;
import objects.PolicyElement;
import objects.Rule;
import objects.Target;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import policy.SMTPolicyElement.PolicyElementType;
import functions.Function;
import functions.FunctionFactory;

public class PullParser implements IParser {
	private final XMLInputFactory2 inputFac;
	FunctionFactory ff;

	public PullParser() throws Exception {
		this.inputFac = ((XMLInputFactory2) XMLInputFactory2.newInstance());
		this.ff = new FunctionFactory();
	}

	public String getParserName() {
		return "Pull-Parser";
	}

	@Override
	public Policy parse(String fileName) {
		try {
			InputStream xmlInputStream = new FileInputStream(fileName);
			XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) this.inputFac.createXMLStreamReader(xmlInputStream);

			return parseDocument(xmlStreamReader, null);

		} catch (Exception e) {
			System.err.println("Exception in parse of PullParser " + e.getMessage());
		}
		return null;
	}

	private void parseAnyOf(XMLStreamReader2 aParser, Target t) {
		List<List<Expression>> allOfs = new ArrayList();
		t.addAnyOf(allOfs);
	}

	private void parseTarget(XMLStreamReader2 aParser, PolicyElement p) {
		try {
			Target t = new Target();
			boolean endReached = false;

			while ((!endReached) && (aParser.hasNext())) {
				int eventType = aParser.next();
				switch (eventType) {
				case 1:
					String aName = aParser.getName().getLocalPart().trim();
					if ("anyof".equalsIgnoreCase(aName)) {
						parseAnyOf(aParser, t);
					}
					break;
				case 4:
					break;
				case 2:
					aName = aParser.getName().getLocalPart().trim();
					if ("target".equalsIgnoreCase(aName)) {
						endReached = true;
						p.setTarget(t);
					}
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in read of SAXParser " + e.getMessage());
		}
	}

	private Policy parseDocument(XMLStreamReader2 aParser, Policy parentPSet) {
		try {
			Policy pSet = null;
			boolean endReached = false;
			while ((!endReached) && (aParser.hasNext())) {
				int eventType = aParser.next();
				switch (eventType) {
				case 1:
					String aName = aParser.getName().getLocalPart().trim();
					if ("policyset".equalsIgnoreCase(aName)) {
						pSet = new Policy();
						pSet.setAlgorithm(PolicyElementType.Policy, aParser.getAttributeValue(null, "PolicyCombiningAlgId"));
						pSet.setId(aParser.getAttributeValue(null, "PolicySetId"));
						if (parentPSet != null) {
							parentPSet.addPolicyElement(pSet);
							pSet.setParent(parentPSet);
						}
						parentPSet = pSet;
						pSet = parseDocument(aParser, pSet);
					} else if ("target".equalsIgnoreCase(aName)) {
						parseTarget(aParser, parentPSet);
					} else if ("policy".equalsIgnoreCase(aName)) {
						Policy p = parsePolicy(aParser, parentPSet);
						if (pSet == null)
							pSet = p;
					}
					break;
				case 4:
					break;

				case 2:
					aName = aParser.getName().getLocalPart().trim();
					if ("policyset".equalsIgnoreCase(aName)) {
						endReached = true;
					}
					break;
				}
			}
			return pSet;
		} catch (Exception e) {
			System.err.println("Exception in read of SAXParser " + e.getMessage());
		}
		return null;
	}

	private void parseExpression(XMLStreamReader2 aParser, Rule r, Expression parentEx) {
		try {
			boolean endReached = false;
			while ((!endReached) && (aParser.hasNext())) {
				int eventType = aParser.next();
				switch (eventType) {
				case 1:
					String aName = aParser.getName().getLocalPart().trim();
					if ("apply".equalsIgnoreCase(aName)) {
						Function aMatch = this.ff.create(aParser.getAttributeValue(null, "FunctionId"));
						aMatch.setOwner(parentEx.getOwnerPolicy());
						parseExpression(aParser, r, aMatch);
					} else if ("attributevalue".equalsIgnoreCase(aName)) {
						String dataType = aParser.getAttributeValue(null, "DataType");
						aParser.next();
						String value = aParser.getText();
						Expression av = parentEx.createAttributeValue(dataType, value);
						parentEx.addExpression(av);
					} else if ("attributedesignator".equalsIgnoreCase(aName)) {
						Expression ad = parentEx.createAttributeDesignator(Boolean.valueOf(aParser.getAttributeValue(null, "MustBePresent")).booleanValue(),
								aParser.getAttributeValue(null, "Category"), aParser.getAttributeValue(null, "AttributeId"),
								aParser.getAttributeValue(null, "DataType"));
						parentEx.addExpression(ad);
					} else if ("attributeselector".equalsIgnoreCase(aName)) {
						Expression as = parentEx.createAttributeSelector(Boolean.valueOf(aParser.getAttributeValue(null, "MustBePresent")).booleanValue(),
								aParser.getAttributeValue(null, "Category"), aParser.getAttributeValue(null, "DataType"), aParser.getAttributeValue(null, "Path"));
						parentEx.addExpression(as);
					} else if ("variablereference".equalsIgnoreCase(aName)) {
						Expression ex = ((Policy) parentEx.getOwnerPolicy()).findVariable(aParser.getAttributeValue(null, "VariableId"));
						assert (ex != null) : "Variable Reference couldn't be found!!!";
						parentEx.addExpression(ex);
					}
					break;

				case 2:
					aName = aParser.getName().getLocalPart().trim();
					if ("condition".equalsIgnoreCase(aName)) {
						endReached = true;
					}
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in read of SAXParser " + e.getMessage());
		}
	}

	private Rule parseRule(XMLStreamReader2 aParser, Policy p) {
		try {
			boolean endReached = false;
			Rule r = new Rule();
			r.setParent(p);
			while ((!endReached) && (aParser.hasNext())) {
				int eventType = aParser.next();
				switch (eventType) {
				case 1:
					String aName = aParser.getName().getLocalPart().trim();
					if ("target".equalsIgnoreCase(aName)) {
						parseTarget(aParser, r);
					} else if ("condition".equalsIgnoreCase(aName)) {
						Expression c = new Expression();
						c.setOwner(r.getParent());
						c.setId("Condition of Rule:" + r.getId());
						parseExpression(aParser, r, c);
					} else if ("description".equalsIgnoreCase(aName)) {
						aParser.next();
						String desc = aParser.getText();
						r.setDescription(desc);
					}
					break;

				case 2:
					aName = aParser.getName().getLocalPart().trim();
					if ("rule".equalsIgnoreCase(aName)) {
						endReached = true;
					}
					break;
				}
			}
			return r;
		} catch (Exception e) {
			System.err.println("Exception in read of SAXParser " + e.getMessage());
		}
		return null;
	}

	private Policy parsePolicy(XMLStreamReader2 aParser, Policy pSet) {
		try {
			boolean endReached = false;
			Policy p = new Policy();
			p.setAlgorithm(PolicyElementType.Policy, aParser.getAttributeValue(null, "RuleCombiningAlgId"));
			p.setId(aParser.getAttributeValue(null, "PolicyId"));
			while ((!endReached) && (aParser.hasNext())) {
				int eventType = aParser.next();
				switch (eventType) {
				case 1:
					String aName = aParser.getName().getLocalPart().trim();
					if ("target".equalsIgnoreCase(aName)) {
						parseTarget(aParser, p);
					} else if ("rule".equalsIgnoreCase(aName)) {
						parseRule(aParser, p);
					} else if ("description".equalsIgnoreCase(aName)) {
						aParser.next();
						String desc = aParser.getText();
						p.setDescription(desc);
					}
					break;

				case 2:
					aName = aParser.getName().getLocalPart().trim();

					if ("policy".equalsIgnoreCase(aName)) {
						endReached = true;
						if (pSet != null) {
							pSet.addPolicyElement(p);
						}
					}
					break;
				}
			}
			return p;
		} catch (Exception e) {
			System.err.println("Exception in read of SAXParser " + e.getMessage());
		}

		return null;
	}

	public static void main(String[] args) {
		try {
			PullParser dp = new PullParser();

			Policy p = dp.parse("policies//Pol-ExampleFromSpec1.xacml");

			System.out.println(p.convert2String());
		} catch (Exception e) {
			System.err.println("Exception in main DomParser " + e.getMessage());
			e.printStackTrace();
		}
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/parserv3/PullParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */