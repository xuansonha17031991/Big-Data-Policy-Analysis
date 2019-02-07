package translator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import objects.Expression;
import objects.Policy;
import objects.PolicyElement;
import objects.Rule;
import policy.SMTPolicy;
import policy.SMTPolicyElement;
import policy.SMTPolicyElement.PolicyElementType;
import policy.SMTRule;
import query.QueryRunner;
import smtfunctions.SMTFunction;
import smtfunctions.SMTFunctionFactory;
import utils.Pair;
import utils.StringUtils;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

import functions.FunctionFactory;

public class XACMLTranslator {
	Context con;
	XACMLPQNormalizer pl;
	SMTFunctionFactory sff;
	int version;
	Map<String, Pair<SetSort, EnumSort>> listOfSorts;
	Map<String, Pair<Expr, String>> listOfEnumeratedVars;
	Map<String, Pair<Expr, Sort>> listOfOtherVars;
	Map<String, Expression> parentVariableReferences = new HashMap();

	public XACMLTranslator(XACMLPQNormalizer p, int v) {
		this.pl = p;
		this.version = v;
		this.con = this.pl.getContext();
		this.listOfSorts = new HashMap();
		this.listOfEnumeratedVars = new HashMap();
		this.listOfOtherVars = new HashMap();
		initialize();
		this.sff = new SMTFunctionFactory(this.version, this);
	}

	public Map<String, Pair<SetSort, EnumSort>> getListOfsorts() {
		return this.listOfSorts;
	}

	public Map<String, Pair<Expr, String>> getListOfEnumeratedVars() {
		return this.listOfEnumeratedVars;
	}

	public Map<String, Pair<Expr, Sort>> getListOfOtherVars() {
		return this.listOfOtherVars;
	}

	public Context getContext() {
		return this.con;
	}

	public int getVersion() {
		return this.version;
	}

	private boolean domainAddedAlready(String domainName) {
		for (Map.Entry<String, Pair<SetSort, EnumSort>> entry : this.listOfSorts.entrySet()) {
			if (entry.getKey().equals(domainName))
				return true;
		}
		return false;
	}

	private static void printEnumSortElements(EnumSort sort) {
		try {
			for (int i = 0; i < sort.getConsts().length; i++) {
				System.out.println(i + " " + sort.getConsts()[i].toString());
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception " + zex.getMessage() + zex.getStackTrace());
		}
	}

	public enum BoolExprType {
		Not, And, Or, Imply, False, True
	}

	private BoolExpr generateBoolExpr(BoolExprType type, Expr[] expressions) {
		try {
			switch (type) {
			case Not:
				return this.con.mkNot((BoolExpr) expressions[0]);
			case And:
				return this.con.mkAnd(convertExprAr2BoolExprAr(expressions));
			case Or:
				return this.con.mkOr(convertExprAr2BoolExprAr(expressions));
			case Imply:
				return this.con.mkImplies((BoolExpr) expressions[0], (BoolExpr) expressions[1]);
			case False:
				return this.con.mkFalse();
			case True:
				return this.con.mkTrue();
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in generateBoolExpr " + zex.getMessage());
		}
		return null;
	}

	public static BoolExpr[] convertExprAr2BoolExprAr(Expr[] expressions) {
		BoolExpr[] expr = new BoolExpr[expressions.length];
		for (int i = 0; i < expressions.length; i++)
			expr[i] = ((BoolExpr) expressions[i]);
		return expr;
	}

	public static ArithExpr[] convertExprAr2ArithExprAr(Expr[] expressions) {
		ArithExpr[] expr = new ArithExpr[expressions.length];
		for (int i = 0; i < expressions.length; i++)
			expr[i] = ((ArithExpr) expressions[i]);
		return expr;
	}

	public void initialize() {
		try {
			for (Map.Entry<String, Pair<String, Vector<String>>> entry : XACMLPQNormalizer.enumerateDomains.entrySet()) {
				String domainName = StringUtils.getShortName((String) ((Pair) entry.getValue()).first) + "_sort";

				if (!domainAddedAlready(domainName)) {
					Vector<String> values = (Vector) ((Pair) entry.getValue()).second;

					if (values.size() != 0) {
						Symbol eNames = this.con.mkSymbol(domainName);
						Symbol[] symbols = new Symbol[values.size()];
						for (int i = 0; i < values.size(); i++)
							symbols[i] = this.con.mkSymbol(values.get(i));
						EnumSort eSort = this.con.mkEnumSort(eNames, symbols);
						SetSort eSetSort = this.con.mkSetSort(eSort);
						this.listOfSorts.put(domainName.toLowerCase(), new Pair(eSetSort, eSort));

					}
				}
			}

			for (Map.Entry<String, String> entry : XACMLPQNormalizer.policyVariables.entrySet()) {
				Pair<String, Vector<String>> domain = XACMLPQNormalizer.enumerateDomains.get(StringUtils.getShortName(entry.getValue()));

				if (domain != null) {
					String domainName = StringUtils.getShortName(domain.first) + "_sort";
					Pair<SetSort, EnumSort> sort = this.listOfSorts.get(domainName);

					Expr ex = this.con.mkConst(entry.getKey(), sort.first);
					this.listOfEnumeratedVars.put(entry.getKey(), new Pair(ex, domainName));
				}
			}

			for (Map.Entry<String, String> entry : XACMLPQNormalizer.queryVariables.entrySet()) {
				Pair<String, Vector<String>> domain = XACMLPQNormalizer.enumerateDomains.get(StringUtils.getShortName(entry.getValue()));

				if (domain != null) {
					String domainName = StringUtils.getShortName(domain.first) + "_sort";
					Pair<SetSort, EnumSort> sort = this.listOfSorts.get(domainName);

					Expr ex = this.con.mkConst(entry.getKey(), sort.first);
					this.listOfEnumeratedVars.put(entry.getKey(), new Pair(ex, domainName));
				}
			}
		} catch (Z3Exception e) {
			System.err.println("Z3Exception in initialize " + e.getMessage());
			e.printStackTrace();
		}
	}

	public SMTPolicy translatePolicy(PolicyElementType pType, Policy p) {
		try {
			if (pType == PolicyElementType.Policy) {
				SMTRule[] rules = new SMTRule[p.getPolicyElements().size()];
				for (int i = 0; i < p.getPolicyElements().size(); i++) {
					rules[i] = translateRule((Rule) p.getPolicyElements().get(i));
				}
				Pair policyTarget = translateTarget(p);

				return new SMTPolicy(p.getId(), policyTarget, SMTPolicyElement.convertCombiningAlg(p.getAlgorithm()), rules, this.con, this.version, pType);
			}

			if (pType == PolicyElementType.PolicySet) {
				SMTPolicy[] policies = new SMTPolicy[p.getPolicyElements().size()];
				for (int i = 0; i < p.getPolicyElements().size(); i++) {
					Policy pol = (Policy) p.getPolicyElements().get(i);
					policies[i] = translatePolicy(pol.getType(), pol);
				}
				Pair policysetTarget = translateTarget(p);
				return new SMTPolicy(p.getId(), policysetTarget, SMTPolicyElement.convertCombiningAlg(p.getAlgorithm()), policies, this.con, this.version,
						pType);
			}

			if (QueryRunner.verbose)
				System.err.println("Not a Policy nor a PolicySet!!!");
		} catch (Exception ex) {
			System.err.println("Exception in translatePolicy " + ex.getMessage());
		}
		return null;
	}

	Map<PolicyElement, Pair<BoolExpr, BoolExpr>> translatedList = new HashMap();

	public Pair<BoolExpr, BoolExpr> translateTarget(PolicyElement p) {
		try {
			if (this.translatedList.containsKey(p)) {
				return this.translatedList.get(p);
			}
			if ((p.getTarget() == null) || (p.getTarget().getAnyOfs().size() == 0)) {
				return new Pair(generateBoolExpr(BoolExprType.True, null), generateBoolExpr(BoolExprType.False, null));
			}
			Vector<Expr> cardConstraints = new Vector();

			List<List<List<Expression>>> target = p.getTarget().getAnyOfs();
			BoolExpr[] anyOfBoolExArray = new BoolExpr[target.size()];
			int i = 0;

			for (Iterator<List<List<Expression>>> oneAnyOfIt = target.iterator(); oneAnyOfIt.hasNext();) {
				List<List<Expression>> oneAnyOf = oneAnyOfIt.next();
				BoolExpr[] allOfBoolExArr = new BoolExpr[oneAnyOf.size()];
				int j = 0;

				for (Iterator<List<Expression>> oneAllOfIt = oneAnyOf.iterator(); oneAllOfIt.hasNext();) {
					List<Expression> oneAllOf = oneAllOfIt.next();
					BoolExpr[] oneAllOfBoolEx = new BoolExpr[oneAllOf.size()];
					int k = 0;
					for (Iterator<Expression> expIt = oneAllOf.iterator(); expIt.hasNext();) {
						Expression exp = expIt.next();

						if (QueryRunner.debug)
							System.out.println("Target Expr : " + exp.getId());
						SMTFunction.Z3FuncExpr res = generateFuncExpr(exp, this.con.getBoolSort());
						List<Expr> tarCardConstraints = res.getCardExpr();
						if (tarCardConstraints != null)
							cardConstraints.addAll(res.getCardExpr());
						oneAllOfBoolEx[k] = ((BoolExpr) res.getPrincipalExpr());
						k++;
					}

					allOfBoolExArr[j] = generateBoolExpr(BoolExprType.And, oneAllOfBoolEx);
					j++;
				}
				anyOfBoolExArray[i] = generateBoolExpr(BoolExprType.Or, allOfBoolExArr);
				i++;
			}

			BoolExpr indet = null;
			if ((cardConstraints == null) || (cardConstraints.size() == 0)) {
				indet = generateBoolExpr(BoolExprType.False, null);
			} else {
				indet = cardConstraints.size() == 1 ? (BoolExpr) cardConstraints.get(0) : generateBoolExpr(BoolExprType.Or,
						cardConstraints.toArray(new BoolExpr[cardConstraints.size()]));
			}
			Pair<BoolExpr, BoolExpr> translated = new Pair(generateBoolExpr(BoolExprType.And, anyOfBoolExArray), indet);
			this.translatedList.put(p, translated);
			return translated;
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in translateTarget " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Exception in translateTarget " + ex.getMessage());
		}
		return null;
	}

	public SMTRule translateRule(Rule r) {
		try {
			String id = r.getId();
			SMTRule.Effects e = null;
			if (r.getEfect() == PolicyElement.Effect.Permit) {
				e = SMTRule.Effects.Permit;
			} else if (r.getEfect() == PolicyElement.Effect.Deny) {
				e = SMTRule.Effects.Deny;
			}

			Iterator<Expression> itEx;
			if ((r.getParent().getVariableDefinitions() != null) && (r.getParent().getVariableDefinitions().size() > 0)) {
				for (itEx = r.getParent().getVariableDefinitions().iterator(); itEx.hasNext();) {
					Expression varDec = itEx.next();

					this.parentVariableReferences.put(varDec.getId(), varDec);
				}
			}

			Pair ruleTarget = translateTarget(r);

			Pair<Expr, Expr> conditionEx;

			if (r.getCondition() != null) {
				Expression condition = r.getCondition();
				SMTFunction.Z3FuncExpr conEx = generateFuncExpr(condition.getParameters().get(0), this.con.getBoolSort());
				List<Expr> conCardConstraints = conEx.getCardExpr();
				Expr conExprs = conEx.getPrincipalExpr();
				Expr conCardExprs = null;
				if (conCardConstraints != null)
					conCardExprs = generateBoolExpr(BoolExprType.Or, conCardConstraints.toArray(new Expr[conCardConstraints.size()]));
				else
					conCardExprs = generateBoolExpr(BoolExprType.False, null);
				conditionEx = new Pair(conExprs, conCardExprs);
				if (QueryRunner.debug)
					System.out.println("Condition Exp : " + condition.convert2String());
			} else {
				conditionEx = new Pair(generateBoolExpr(BoolExprType.True, null), generateBoolExpr(BoolExprType.False, null));
			}

			BoolExpr ruleApplicable = this.con.mkAnd(new BoolExpr[] { (BoolExpr) ruleTarget.first, this.con.mkNot((BoolExpr) ruleTarget.second) });
			BoolExpr conApplicable = this.con.mkAnd(new BoolExpr[] { (BoolExpr) conditionEx.first, this.con.mkNot((BoolExpr) conditionEx.second) });

			BoolExpr ruleNA = this.con.mkAnd(new BoolExpr[] { this.con.mkNot((BoolExpr) ruleTarget.first), this.con.mkNot((BoolExpr) ruleTarget.second) });
			BoolExpr conNA = this.con.mkAnd(new BoolExpr[] { this.con.mkNot((BoolExpr) conditionEx.first), this.con.mkNot((BoolExpr) conditionEx.second) });

			return new SMTRule(r.getId(), ruleApplicable, (BoolExpr) ruleTarget.second, ruleNA, conApplicable,
					(BoolExpr) conditionEx.second, conNA, e, this.con);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in translateRule " + zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Exception in translateRule " + ex.getMessage());
		}
		return null;
	}

	private SMTFunction.Z3FuncExpr generateReferenceEx(Expression ex, Sort expectedSort) {
		if (this.parentVariableReferences.size() > 0) {
			for (Map.Entry<String, Expression> entry : this.parentVariableReferences.entrySet()) {
				if (ex.getId().equals(entry.getKey())) {
					Expression varDecEx = entry.getValue();
					SMTFunction.Z3FuncExpr parentExpr = generateFuncExpr(varDecEx, expectedSort);
					return parentExpr;
				}
			}
		}
		return null;
	}

	public SMTFunction.Z3FuncExpr generateFuncExpr(Expression ex, Sort expectedSort) {
		SMTFunction.Z3FuncExpr exTemp = generateReferenceEx(ex, expectedSort);
		if (exTemp != null) {
			return exTemp;
		}

		Expr att = generateAttributeExpressions(ex);
		if (att != null)
			return new SMTFunction.Z3FuncExpr(att, null, null);
		// If not AttributeValue or AttributeVar => create function.
		SMTFunction.Z3FuncExpr funExpr = this.sff.generate(ex, expectedSort);
		if (funExpr != null)
			return funExpr;
		return null;
	}

	private int findTimeIntValue(String val) {
		String[] strList = val.split("(:)|(\\+)");
		String hour = strList[0];
		String minute = strList[1];
		String second = strList[2];
		int valNum = Integer.parseInt(hour) * 60 * 60 + Integer.parseInt(minute) * 60 + Integer.parseInt(second);
		if (QueryRunner.debug)
			System.out.println("Time Value " + val + " --> " + valNum);
		return valNum;
	}

	public enum ValueOrVariable {
		Value, Variable
	}

	public Expr getAttributeExpression(String dataType, String value, ValueOrVariable valOrVar, String varId) {
		FunctionFactory.DataTypes typeName = FunctionFactory.typeNames.get(dataType);
		try {
			switch (typeName) {
			case xstring:
			case xrfc822name:
			case xanyuri:
			case xpathexpression:
				if (valOrVar == ValueOrVariable.Value)
					return getValueFromEnumerateDomain(dataType, value);
				if (valOrVar == ValueOrVariable.Variable)
					return getVariable(varId, dataType, EnumerateOrSingle.Enumerate);
				break;
			case xinteger:
				if (valOrVar == ValueOrVariable.Value)
					return this.con.mkInt(Integer.parseInt(value));
				if (valOrVar == ValueOrVariable.Variable)
					return getVariable(varId, dataType, EnumerateOrSingle.Single);
				break;
			case xdouble:
				if (valOrVar == ValueOrVariable.Value)
					return this.con.mkReal(value);
				if (valOrVar == ValueOrVariable.Variable)
					return getVariable(varId, dataType, EnumerateOrSingle.Single);
				break;
			case xboolean:
				if (valOrVar == ValueOrVariable.Value) {
					if (value.toLowerCase().equals("false"))
						return this.con.mkFalse();
					if (value.toLowerCase().equals("true"))
						return this.con.mkTrue();
				} else if (valOrVar == ValueOrVariable.Variable) {
					return getVariable(varId, dataType, EnumerateOrSingle.Single);
				}
				break;
			case xtime:
				if (valOrVar == ValueOrVariable.Value) {
					return this.con.mkInt(findTimeIntValue(value));
				}
				if (valOrVar == ValueOrVariable.Variable) {
					return getVariable(varId, dataType, EnumerateOrSingle.Single);
				}
				break;
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in generateFuncExpr " + zex.getMessage());
		}
		if (QueryRunner.debug)
			System.err.println("I don't recognize this type " + typeName);
		return null;
	}

	public enum EnumerateOrSingle {
		Enumerate, Single
	}

	private Expr getVariable(String id, String dataType, EnumerateOrSingle enumOrSingleType) {
		try {
			Pair p = this.listOfEnumeratedVars.get(id);
			if (p != null) {
				return (Expr) p.first;
			}
			if (QueryRunner.debug)
				System.out.println("Strange could not find the variable so will create...");
			if (enumOrSingleType == EnumerateOrSingle.Enumerate) {
				Pair<SetSort, EnumSort> sort = this.listOfSorts.get(dataType.toLowerCase() + "_sort");
				if (sort == null)
					System.err.println("Could not find the sort, perhaps not supported... ");
				return this.con.mkConst(id, sort.first);
			}
			if (enumOrSingleType == EnumerateOrSingle.Single) {
				Expr ex = null;
				FunctionFactory.DataTypes typeName = FunctionFactory.typeNames.get(dataType);
				switch (typeName) {
				case xboolean:
					ex = this.con.mkBoolConst(id);
					this.listOfOtherVars.put(id, new Pair(ex, ex.getSort()));
					return ex;
				case xinteger:
					ex = this.con.mkIntConst(id);
					this.listOfOtherVars.put(id, new Pair(ex, ex.getSort()));
					return ex;
				case xdouble:
					ex = this.con.mkRealConst(id);
					this.listOfOtherVars.put(id, new Pair(ex, ex.getSort()));
					return ex;
				case xtime:
					ex = this.con.mkIntConst(id);
					this.listOfOtherVars.put(id, new Pair(ex, ex.getSort()));
					return ex;
				}
				if (QueryRunner.verbose) {
					System.err.println("I don't recognize this type ...");
				}

			}

		} catch (Z3Exception zex) {

			System.err.println("Z3Exception in getVariable " + zex.getMessage());
		}

		return null;
	}

	public Expr generateAttributeExpressions(Expression ex) {
		Expr expr = null;
		if ((ex instanceof Expression.AttributeValue)) {
			Expression.AttributeValue av = (Expression.AttributeValue) ex;
			String dataType = StringUtils.getShortName(av.getDataType());
			expr = getAttributeExpression(dataType, av.getValue(), ValueOrVariable.Value, null);
		}

		if (ex.getClass().getCanonicalName().endsWith("AttributeSelector")) {
			Expression.AttributeSelector as = (Expression.AttributeSelector) ex;
			String dataType = StringUtils.getShortName(as.getDataType());
			expr = getAttributeExpression(dataType, as.getPath(), ValueOrVariable.Value, null);
		}

		if (ex.getClass().getCanonicalName().endsWith("AttributeDesignator")) {
			Expression.AttributeDesignator ad = (Expression.AttributeDesignator) ex;
			String dataType = StringUtils.getShortName(ad.getDataType());
			expr = getAttributeExpression(dataType, null, ValueOrVariable.Variable, ex.getId());
		}

		return expr;
	}

	public Expr getValueFromEnumerateDomain(String dataType, String value) {
		Pair<SetSort, EnumSort> sort = this.listOfSorts.get(StringUtils.getShortName((String) ((Pair) XACMLPQNormalizer.enumerateDomains.get(dataType
				.toLowerCase())).first) + "_sort");
		try {
			for (int i = 0; i < sort.second.getConsts().length; i++) {
				String str = sort.second.getConsts()[i].toString();
				if ((str.startsWith("|")) && (str.endsWith("|")))
					str = str.substring(str.indexOf("|") + 1, str.lastIndexOf("|"));
				if (str.equals(value))
					return sort.second.getConsts()[i];
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in getVariable " + zex.getMessage());
		}
		return null;
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/translator/XACMLTranslator.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */