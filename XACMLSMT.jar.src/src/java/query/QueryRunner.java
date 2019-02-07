package query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import policy.SMTPolicy;
import policy.SMTPolicyElement.Decision;
import policy.SMTRule;
import query.QueryParser.OPERATION;
import smtfunctions.SMTMatchEqual;
import solver.Reasoner;
import translator.XACMLPQNormalizer;
import utils.Pair;
import utils.Tree;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

import experiments.Experiments;

public class QueryRunner {
	public static boolean SHOW_AT_POLICY_ONLY = true;
	public static boolean SHOW_AT_POLICY_WITH_TARGET_COMBINATION = false;
	public static boolean SHOW_AT_RULE = false;
	// public static boolean SHOW_AT_CONSTRAINT = false;

	public static boolean verbose = false;
	public static boolean z3output = false;
	public static int xacmlVersion = 3;
	public static boolean printVarDomains = false;
	public static boolean debug = false;
	public static boolean printSMT = false;
	public static boolean models = false;
	public static boolean proofs = false;
	public static int threshold = 0;

	private static int substitutionIndex = 0;

	public XACMLPQNormalizer pl;

	public QueryRunner() {
		HashMap<String, String> cfg = new HashMap();

		cfg.put("model", "true");
		cfg.put("proof", "true");
	}

	public XACMLPQNormalizer getLoader() {
		return this.pl;
	}

	private Pair<SetSort, EnumSort> addNewValue2Sort(Pair<SetSort, EnumSort> sort, String val, Context con) {
		try {
			Symbol[] symbols = new Symbol[sort.second.getConsts().length + 1];
			int i = 0;
			for (; i < sort.second.getConsts().length; i++) {
				symbols[i] = con.mkSymbol(sort.second.getConsts()[i].toString());
			}
			symbols[i] = con.mkSymbol(val);
			EnumSort eSort = con.mkEnumSort(sort.second.getName(), symbols);
			SetSort eSetSort = con.mkSetSort(eSort);
			return new Pair(eSetSort, eSort);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception " + zex.getMessage());
		}
		return null;
	}

	public Expr convert2Z3ExprStr(Tree t, String element, int depth, HashMap<String, QueryNode> nodesOfOrgTree, SMTPolicy[] policies, Context ctx) {
		ArrayList<String> children = t.getNodes().get(element).getChildren();
		Iterator<String> childIT = null;
		Expr result = null;

		QueryNode qNode = nodesOfOrgTree.get(element);

		try {
			if (qNode.getOperation() != null) {
				switch (qNode.getOperation()) {
				case Equals:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters...");
					result = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					Expr second = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
					if (result == null) {
						QueryNode qTempNode = nodesOfOrgTree.get(children.get(0));
						String secondId = nodesOfOrgTree.get(children.get(1)).getIdentifier().second;

						if ((QueryParser.specialNodes.containsKey(qTempNode.getIdentifier().second.trim().toLowerCase()))
								&& (QueryParser.specialNodes.get(qTempNode.getIdentifier().second.trim().toLowerCase()).intValue() == 1)) {
							Expr emptySet = ctx.mkEmptySet((Sort) ((Pair) this.pl.getTranslator().getListOfsorts()
									.get(this.pl.getShortDName(this.pl.getPolicyVariables().get(secondId)) + "_sort")).second);
							return ctx.mkEq(emptySet, second);
						}
					} else if (second == null) {
						QueryNode qTempNode = nodesOfOrgTree.get(children.get(1));
						String resultId = nodesOfOrgTree.get(children.get(0)).getIdentifier().second;

						if ((QueryParser.specialNodes.containsKey(qTempNode.getIdentifier().second.trim().toLowerCase()))
								&& (QueryParser.specialNodes.get(qTempNode.getIdentifier().second.trim().toLowerCase()).intValue() == 1)) {

							Expr emptySet = ctx.mkEmptySet((Sort) ((Pair) this.pl.getTranslator().getListOfsorts()
									.get(this.pl.getShortDName(this.pl.getPolicyVariables().get(resultId)) + "_sort")).second);
							return ctx.mkEq(emptySet, result);
						}
					}
					result = SMTMatchEqual.createMatchEqExpr(ctx, result, second);
					break;
				case Replace:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters for REPLC...");

					QueryNode leftNode = nodesOfOrgTree.get(children.get(0));
					int fullExprIndex;
					int otherIndex;
					List<Expr> newTerms = new ArrayList<Expr>();

					if (leftNode.getOperation() == null || leftNode.getOperation() == OPERATION.Replace) {
						fullExprIndex = 0;
						otherIndex = 1;
					} else {
						fullExprIndex = 1;
						otherIndex = 0;
					}

					int subDepth = depth;
					Pair<Expr, Expr> newTermsAttrPair = new Pair<Expr, Expr>(null, null);
					apply(newTermsAttrPair, false, t, children.get(otherIndex), subDepth, nodesOfOrgTree, policies, ctx);

					// https://stackoverflow.com/questions/7740556/equivalent-of-define-fun-in-z3-api
					// mkForall(Sort[] sorts, Symbol[] names, Expr body,int weight,
					// Pattern[] patterns, Expr[] noPatterns,
					// Symbol quantifierID, Symbol skolemID
					// StringSymbol attrSymbol = ctx.mkSymbol("X");
					// EnumSort enumSort =
					// this.pl.getTranslator().getListOfsorts().get("string_sort").second;

//					Expr elseTerm = ctx.mkStore((ArrayExpr) attrExpr, ctx.mkConst(attrSymbol, enumSort), ctx.mkFalse());
//
//					Expr forAll = ctx.mkForall(new Sort[] {enumSort}, /* types of
//					// quantified variables */
//					 new Symbol[] {attrSymbol}, /* names of quantified variables */
//							null, 1, null, null, null, null);
//					elseTerm = ctx.mkStore((ArrayExpr) attrExpr, forAll, ctx.mkFalse());
//					Expr ite = ctx.mkITE(ctx.mkEq(ctx.mkConst(attrSymbol, enumSort), valExpr), newTerm, elseTerm);
					Expr getExpr = convert2Z3ExprStr(t, children.get(fullExprIndex), depth, nodesOfOrgTree, policies, ctx);
					result = getExpr.substitute(newTermsAttrPair.second, newTermsAttrPair.first);
					break;
				case Neg:
					if (children.size() != 1)
						System.err.println("There was an error here since we need only one parameter");
					Expr temp = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					result = ctx.mkNot((BoolExpr) temp);
					break;
				case Implies:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters...");
					result = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					temp = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
					result = ctx.mkImplies((BoolExpr) result, (BoolExpr) temp);
					break;
				case Or:
					if (children.size() > 0) {
						childIT = children.iterator();
						result = convert2Z3ExprStr(t, childIT.next(), depth, nodesOfOrgTree, policies, ctx);
						while (childIT.hasNext()) {
							temp = convert2Z3ExprStr(t, childIT.next(), depth, nodesOfOrgTree, policies, ctx);
							result = ctx.mkOr(new BoolExpr[] { (BoolExpr) result, (BoolExpr) temp });
						}
					}

					break;

				case Exists:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters for EXISTS...");
					Expr term = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					Sort sort = null;
					if ((this.pl.getEnumeratedDomains().get(term.getSort().toString() + "_sort") != null)
							&& (this.pl.getTranslator().getListOfsorts().get(term.getSort().toString() + "_sort") != null)) {
						sort = (Sort) ((Pair) this.pl.getTranslator().getListOfsorts().get(term.getSort().toString() + "_sort")).first;
					} else if (this.pl.getTranslator().getListOfOtherVars().get(term.getSExpr()) != null)
						sort = (Sort) ((Pair) this.pl.getTranslator().getListOfOtherVars().get(term.getSExpr())).second;
					else {
						System.err.println("There is an error in finding the sort of the var/attribute" + term.getSExpr() + "\n"
								+ "May be not supported see XACMLCustomizer ...");
					}
					Sort[] sorts = new Sort[] { sort };
					Symbol[] names = new Symbol[] { ctx.mkSymbol(term.toString()) };
					Expr q = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
					result = ctx.mkExists(sorts, names, q, 1, null, null, null, null);
					break;

				case ForAll:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters for FORALL...");
					term = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					sort = null;

					if ((this.pl.getEnumeratedDomains().get(term.getSort().toString() + "_sort") != null)
							&& (this.pl.getTranslator().getListOfsorts().get(term.getSort().toString() + "_sort") != null)) {
						sort = (Sort) ((Pair) this.pl.getTranslator().getListOfsorts().get(term.getSort().toString() + "_sort")).first;
					} else if (this.pl.getTranslator().getListOfOtherVars().get(term.getSExpr()) != null)
						sort = (Sort) ((Pair) this.pl.getTranslator().getListOfOtherVars().get(term.getSExpr())).second;
					else {
						System.err.println("There is an error in finding the sort of the var/attribute" + term.getSExpr() + "\n"
								+ "May be not supported see XACMLCustomizer ...");
					}
					sorts = new Sort[] { sort };
					names = new Symbol[] { ctx.mkSymbol(term.toString()) };
					q = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
					result = ctx.mkForall(sorts, names, q, 1, null, null, null, null);
					break;

				case Rest:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters for REST...");
					term = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);

					Expr newTerm = ctx.mkConst(term.toString() + "_new" + substitutionIndex, term.getSort());
					substitutionIndex += 1;
					result = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx).substitute(term, newTerm);
					break;
				case Ins:
					if (children.size() != 2)
						System.err.println("There was an error here since more (or less) than two parameters for INS...");
					term = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
					result = ctx
							.mkAnd(new BoolExpr[] { (BoolExpr) term, (BoolExpr) convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx) });
					break;

				case And:
					if (children.size() > 0) {
						childIT = children.iterator();
						result = convert2Z3ExprStr(t, childIT.next(), depth, nodesOfOrgTree, policies, ctx);

						while (childIT.hasNext()) {
							temp = convert2Z3ExprStr(t, childIT.next(), depth, nodesOfOrgTree, policies, ctx);
							if (temp.isBool()) {
								result = ctx.mkAnd(new BoolExpr[] { (BoolExpr) result, (BoolExpr) temp });
							}
						}
					}
					break;

				default:
					System.err.println("I couldnt recognize this operator ...");
				}
				depth++;
				return result;
			}

			if (qNode.getTypeAndValue() != null) {

				if ((element.toUpperCase().startsWith("P_")) || (element.toUpperCase().startsWith("D_")) || (element.toUpperCase().startsWith("IN_"))
						|| (element.toUpperCase().startsWith("NA_"))) {
					String policyElement = element;
					String extElement = null;
					int index;
					if (element.contains(".")) {
						policyElement = element.substring(0, element.indexOf("."));
						extElement = element.substring(element.indexOf(".") + 1, element.indexOf("_", element.indexOf("_") + 1));
						element = policyElement;
						index = Integer.parseInt(element.substring(element.indexOf("_") + 1));
					} else {
						index = Integer.parseInt(element.substring(element.indexOf("_") + 1, element.indexOf("_", element.indexOf("_") + 1)));
					}

					if (policies.length < index)
						System.err.println("This policy index is wrong : " + element);
					SMTPolicy pol = policies[(index - 1)];
					Decision decision = null;
					if (element.toUpperCase().startsWith("P_"))
						decision = Decision.Permit;
					if (element.toUpperCase().startsWith("D_"))
						decision = Decision.Deny;
					if (element.toUpperCase().startsWith("IN_"))
						decision = Decision.Indet;
					if (element.toUpperCase().startsWith("NA_")) {
						decision = Decision.Na;
						// TODO
						// return ctx.mkNot(ctx.mkOr(new BoolExpr[] {
						// pol.getFinalElement().getPermitDS(),
						// pol.getFinalElement().getDenyDS(),
						// pol.getFinalElement().getIndeterminateDS(ctx) }));

					}
					Pair<BoolExpr, BoolExpr> pair = pol.getFinalElement().get(decision);
					return "SHORT".equals(extElement) ? pair.second : pair.first;

				} else {

					String attId = qNode.getIdentifier().second;
					if ((this.pl.getPolicyVariables().get(attId) != null) || (this.pl.getQueryVariables().get(attId) != null)) {
						if (this.pl.getTranslator().getListOfOtherVars().get(attId) != null) {
							result = (Expr) ((Pair) this.pl.getTranslator().getListOfOtherVars().get(attId)).first;
						} else if (this.pl.getTranslator().getListOfEnumeratedVars().get(attId) != null)
							result = (Expr) ((Pair) this.pl.getTranslator().getListOfEnumeratedVars().get(attId)).first;
						else {
							System.err.println("Error: I could not find the Z3 Expression for the variable" + attId + " in Customizer...");
						}
					} else {
						result = checkForEnumeratedValue(attId);
						boolean success = result != null;

						if (!success) {
							try {
								boolean value = BooleanUtils.toBoolean(attId.toLowerCase(), "true", "false");
								result = ctx.mkBool(value);
								success = true;
							} catch (Exception e) {
								if (debug)
									System.out.println(attId + " is not a boolean");
							}
						}
						if (!success) {
							try {
								int val = Integer.parseInt(attId);
								result = ctx.mkInt(val);
								success = true;
							} catch (Exception e) {
								if (debug)
									System.out.println(attId + " is not an integer");
							}
						}
						if (!success) {
							try {
								Double.parseDouble(attId);
								result = ctx.mkReal(attId);
								success = true;
							} catch (Exception e) {
								if (debug)
									System.out.println(attId + " is neither a double");
							}
						}
						if ((!success) && (debug)) {
							System.err.println("Error : I couldnt infer the type/value for : " + attId);
							System.err.println("Creating a string symbol for it");
						}
					}
				}
				depth++;
				return result;
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception " + zex.getMessage());
		}
		return null;
	}

	private void apply(Pair<Expr, Expr> newTermsAttrPair, boolean negative, Tree t, String string, int depth,
			HashMap<String, QueryNode> nodesOfOrgTree,
			SMTPolicy[] policies,
			Context ctx) {
		ArrayList<String> children = t.getNodes().get(string).getChildren();

		QueryNode qNode = nodesOfOrgTree.get(string);

		if (qNode.getOperation() == OPERATION.Equals) {
			Expr attrExpr = convert2Z3ExprStr(t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
			Expr valExpr = convert2Z3ExprStr(t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
			if (newTermsAttrPair.second == null) {
				newTermsAttrPair.second = attrExpr;
			}
			if (newTermsAttrPair.first == null) {
				newTermsAttrPair.first = ctx.mkStore((ArrayExpr) attrExpr, valExpr, negative ? ctx.mkFalse() : ctx.mkTrue());
			} else {
				newTermsAttrPair.first = ctx.mkStore((ArrayExpr) newTermsAttrPair.first, valExpr, negative ? ctx.mkFalse() : ctx.mkTrue());
			}
		} else if (qNode.getOperation() == OPERATION.And) {
			apply(newTermsAttrPair, negative, t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
			apply(newTermsAttrPair, negative, t, children.get(1), depth, nodesOfOrgTree, policies, ctx);
		} else if (qNode.getOperation() == OPERATION.Neg) {
			apply(newTermsAttrPair, !negative, t, children.get(0), depth, nodesOfOrgTree, policies, ctx);
		}
		depth++;
	}

	private Expr checkForEnumeratedValue(String value) {
		Map<String, Pair<String, Vector<String>>> enumSorts = this.pl.getEnumeratedDomains();

		for (Map.Entry<String, Pair<String, Vector<String>>> entry : enumSorts.entrySet()) {
			Pair<String, Vector<String>> typeAndValues = entry.getValue();
			for (int i = 0; i < typeAndValues.second.size(); i++) {
				if (value.equals(typeAndValues.second.get(i))) {
					return this.pl.getTranslator().getValueFromEnumerateDomain(typeAndValues.first, value);
				}
			}
		}
		return null;
	}

	public static void convertToCustomTree(QueryNode node, QueryNode parent, Tree tree) {
		if (node.getOperation() == null) {
			if (parent == null)
				tree.addNode(node.getIdentifier().first);
			else
				tree.addNode(node.getIdentifier().first, parent.getIdentifier().first);
		} else {
			if (parent != null)
				tree.addNode(node.getIdentifier().first, parent.getIdentifier().first);
			convertToCustomTree(node.getLeft(), node, tree);
			if (!node.isUnary()) {
				convertToCustomTree(node.getRight(), node, tree);
			}
		}
	}

	public SMTPolicy[] loadPolicies(String[] policies, boolean printVarsDomains, int version, String query) {
		this.pl = new XACMLPQNormalizer();

		PrintStream output = System.out;

		SMTPolicy[] policyFormulas = this.pl.loadAndTranslatePolicies(policies, version, query);

		if (printVarsDomains) {
			System.out.println("********* VARIABLES/ATTRIBUTES and DOMAINS  ***********");
			this.pl.printVarsAndDomains();
		}

		if ((printSMT) || (verbose)) {
			System.out.println("************** SMT POLICIES *************************");
			printSMTPolicies(policyFormulas, this.pl.getContext(), output);
		}
		return policyFormulas;
	}

	public static void printSMTPolicies(SMTPolicy[] pols, Context con, PrintStream out) {
		for (int i = 0; i < pols.length; i++) {
			out.println("<---------- POLICY " + (i + 1) + " ----------> ");
			SMTRule finalElement = pols[i].getFinalElement();

			out.println("Spaces for policy" + pols[i].getId());
			finalElement.printAS(con, out);
		}
	}

	public Map<String, Object> executeQuery(SMTPolicy[] policies, String query, PrintStream out) {
		Expr ex = getQueryExpression(policies, query, out);

		return findApplicableModel(ex, out);
	}

	public Map<String, Object> findApplicableModel(Expr ex, PrintStream out) {
		Reasoner s = new Reasoner(this.pl.getContext());
		if (verbose) {
			out.print("PHASE 3 : Solving : ");
		}
		return s.solveSMT((BoolExpr) ex, models, this.pl, threshold, out);
	}

	public Expr getQueryExpression(SMTPolicy[] policies, String query, PrintStream out) {
		Tree tree = new Tree();

		QueryParser qp = new QueryParser(query, QueryParser.EXPRESSIONTYPE.Infix);

		if (debug) {
			System.out.println("Postfix:" + qp.GetPostfixExpression());
		}

		tree.addNode(qp.Root.getIdentifier().first);
		convertToCustomTree(qp.Root, null, tree);
		if ((debug) || (verbose)) {
			out.println("PHASE 1 : Qquery PROCESSING");
		}

		out.println("\n\n Tree representation of the query:");
		tree.display(qp.Root.getIdentifier().first);

		Expr ex = convert2Z3ExprStr(tree, qp.Root.getIdentifier().first, 0, qp.getNodes(), policies, this.pl.getContext());
		if (verbose) {
			out.println("\nPHASE 2 : SMT Transformation");
			out.println("***** Z3 NOTATION *****************************************");
			out.println(ex.simplify().toString());
		}
		return ex;
	}

	private static void printUsage(Options options) {
		String header = "XACML SMT Analysis Tool V2 \n\n";
		String footer = "\nPlease report issues to fturkmen(AT)gmail.com";
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("XACML-SMT", header, options, footer, true);
	}

	private static Pair parseConsoleParameters(String[] args) {
		Options options = new Options();
		Option policyOption = new Option("p", true, "list of policies (order is important)");
		policyOption.setArgs(10);
		options.addOption(policyOption);
		options.addOption("q", true, "query, [default `P_1']");
		options.addOption("m", true, "number of models to list, [default 0]");
		options.addOption("z3", false, "show Z3 output and statistics");
		options.addOption("u", false, "show proof for UnSAT");
		options.addOption("smt", false, "show SMT encoding of policies");
		options.addOption("d", false, "print policy variables and enum domains");
		options.addOption("v", false, "verbose (lots of console output)");
		options.addOption("h", false, "print the help");
		options.addOption("xv", true, "XACML version; 2 for v2, 3 for v3, [default v3]");

		CommandLineParser parser = new GnuParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			return new Pair(options, cmd);
		} catch (ParseException e) {
			System.err.println("Error in parameters .. " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static final Logger LOGGER = Logger.getLogger(QueryRunner.class.getName());

	public static void main(String[] args) {
		Handler fileHandler = null;
		Formatter simpleFormatter = new SimpleFormatter();
		try {
			fileHandler = new FileHandler("./XACMLSMT.log");
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(simpleFormatter);
			LOGGER.addHandler(fileHandler);
		} catch (IOException e) {
			System.err.println("IOException in creating the logger file");
		}

		String[] policies = null;
		String query = "P_1";

		Pair p = parseConsoleParameters(args);
		CommandLine cmd = (CommandLine) p.second;
		Options options = (Options) p.first;
		if (cmd.hasOption("h")) {
			printUsage(options);
			return;
		}

		if (cmd.hasOption("p")) {
			policies = cmd.getOptionValues("p");
		} else {
			System.err.println("No policy has been specified...");
			printUsage(options);
			return;
		}

		if (cmd.hasOption("xv"))
			xacmlVersion = Integer.parseInt(cmd.getOptionValue("xv"));
		if (cmd.hasOption("v"))
			verbose = true;
		if (cmd.hasOption("d"))
			printVarDomains = true;
		if (cmd.hasOption("smt"))
			printSMT = true;
		if (cmd.hasOption("u"))
			proofs = true;
		if (cmd.hasOption("q")) {
			query = cmd.getOptionValue("q");

		} else {

			System.out.println("Default query : " + query);
		}

		if (cmd.hasOption("z3"))
			z3output = true;
		if (cmd.hasOption("m")) {
			models = true;
			threshold = Integer.parseInt(cmd.getOptionValue("m"));
		}

		QueryRunner qt = new QueryRunner();

		long timeBeginLoad = System.currentTimeMillis();
		SMTPolicy[] policyFormulas = qt.loadPolicies(policies, printVarDomains, xacmlVersion, query);
		long timeEndLoad = System.currentTimeMillis();
		System.out.println("**********   QUERY   ********");
		System.out.println(query);
		System.out.println("********  RESULTS   *********");
		long timeBeginSolve = System.currentTimeMillis();
		Map<String, Object> result = qt.executeQuery(policyFormulas, query, System.out);
		long timeEndSolve = System.currentTimeMillis();

		System.out.println("Solving Time: " + ((DescriptiveStatistics) result.get("time")).getSum() + " \nMemory: "
				+ Experiments.convertToBigger(1, ((DescriptiveStatistics) result.get("memory")).getSum()) + "MB");
	}

	private static void waitForInput() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/query/QueryRunner.class Java
 * compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */