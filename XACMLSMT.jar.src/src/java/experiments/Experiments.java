package experiments;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

import policy.SMTPolicy;
import policy.SMTRule;
import query.QueryParser;
import query.QueryRunner;
import solver.Reasoner;
import translator.XACMLPQNormalizer;
import utils.Tree;
import utils.Triple;

public class Experiments {
	static String querySoDGradeMan = "( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = Assign ) ) ) ) )\\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = View ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = Assign ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = Receive ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = View ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = ExternalGrades ) ) ) \\WEDGE ( \\REST command ( P_1 \\WEDGE ( command = Receive ) ) ) ) ) )";

	static String querySoDPolicyAnomaly = "( ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Codes ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Change ) ) ) ) )\\VEE ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Reports ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Change ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Codes ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Change ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Codes ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Read ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Reports ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Change ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-id ( P_1 \\WEDGE ( resource-id = Codes ) ) ) \\WEDGE ( \\REST action-id ( P_1 \\WEDGE ( action-id = Read ) ) ) ) ) )";

	static String resId = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
	static String querySoDKMarket = "( ( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Medicine ) ) ) ) "
			+ "\\VEE " + "( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Liquor ) ) ) ) ) " + "\\WEDGE "
			+ "( ( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Medicine ) ) ) ) " + "\\VEE "
			+ "( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Drink ) ) ) ) ) " + "\\WEDGE "
			+ "( ( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Liquor ) ) ) ) " + "\\VEE "
			+ "( \\NEG ( \\REST " + resId + " ( P_1 \\WEDGE ( " + resId + " = Drink ) ) ) ) )";

	public static String querySoDContinue = "( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = create ) ) ) ) )\\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = write ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = create ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = delete ) ) ) ) ) ) \\WEDGE ( ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = write ) ) ) ) ) \\VEE ( \\NEG ( ( \\REST resource-class ( P_1 \\WEDGE ( resource-class = pcMember-info_rc ) ) ) \\WEDGE ( \\REST action-type ( P_1 \\WEDGE ( action-type = delete ) ) ) ) ) )";

	private static String resIdIn4Stars = "urn:oasis:names:tc:xacml:2.0:example:type";
	private static String actIdIn4Stars = "urn:oasis:names:tc:xacml:1.0:action:action-id";
	static String querySoDIn4Stars = "( ( \\NEG ( ( \\REST " + resIdIn4Stars + " ( P_1 \\WEDGE ( " + resIdIn4Stars
			+ " = omega ) ) ) \\WEDGE " + "( \\REST " + actIdIn4Stars + " ( P_1 \\WEDGE ( " + actIdIn4Stars
			+ " = read ) ) ) ) )" + "\\VEE " + "( \\NEG ( ( \\REST " + resIdIn4Stars + " ( P_1 \\WEDGE ( "
			+ resIdIn4Stars + " = omega ) ) ) \\WEDGE " + "( \\REST " + actIdIn4Stars + " ( P_1 \\WEDGE ( "
			+ actIdIn4Stars + " = delete ) ) ) ) ) ) " + "\\WEDGE " + "( ( \\NEG ( ( \\REST " + resIdIn4Stars
			+ " ( P_1 \\WEDGE ( " + resIdIn4Stars + " = omega ) ) ) \\WEDGE " + "( \\REST " + actIdIn4Stars
			+ " ( P_1 \\WEDGE ( " + actIdIn4Stars + " = read ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST "
			+ resIdIn4Stars + " ( P_1 \\WEDGE ( " + resIdIn4Stars + " = uavImagery ) ) ) \\WEDGE " + "( \\REST "
			+ actIdIn4Stars + " ( P_1 \\WEDGE ( " + actIdIn4Stars + " = read ) ) ) ) ) ) " + "\\WEDGE "
			+ "( ( \\NEG ( ( \\REST " + resIdIn4Stars + " ( P_1 \\WEDGE ( " + resIdIn4Stars + " = omega ) ) ) \\WEDGE "
			+ "( \\REST " + actIdIn4Stars + " ( P_1 \\WEDGE ( " + actIdIn4Stars + " = delete ) ) ) ) ) " + "\\VEE "
			+ "( \\NEG ( ( \\REST " + resIdIn4Stars + " ( P_1 \\WEDGE ( " + resIdIn4Stars
			+ " = uavImagery ) ) ) \\WEDGE " + "( \\REST " + actIdIn4Stars + " ( P_1 \\WEDGE ( " + actIdIn4Stars
			+ " = read ) ) ) ) ) )";

	private static String resIdTransPolExtended = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
	private static String actIdTransPolExtended = "urn:oasis:names:tc:xacml:1.0:action:action-id";
	static String querySoDTransPolExtended = "( ( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended
			+ " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = create ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST "
			+ resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE "
			+ "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( " + actIdTransPolExtended
			+ " = approve ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended
			+ " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = execute ) ) ) ) ) ) " + "\\WEDGE "
			+ "( ( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended
			+ " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ actIdTransPolExtended + " = create ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST " + resIdTransPolExtended
			+ " ( P_1 \\WEDGE ( " + resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE " + "( \\REST "
			+ actIdTransPolExtended + " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = approve ) ) ) ) ) " + "\\VEE "
			+ "( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended
			+ " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ actIdTransPolExtended + " = audit ) ) ) ) ) ) " + "\\WEDGE " + "( ( \\NEG ( ( \\REST "
			+ resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE "
			+ "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = create ) ) ) ) ) "
			+ "\\VEE " + "( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended
			+ " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ actIdTransPolExtended + " = execute ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST " + resIdTransPolExtended
			+ " ( P_1 \\WEDGE ( " + resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE " + "( \\REST "
			+ actIdTransPolExtended + " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = audit ) ) ) ) ) )" + "\\WEDGE "
			+ "( ( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended
			+ " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ actIdTransPolExtended + " = approve ) ) ) ) ) " + "\\VEE " + "( \\NEG ( ( \\REST " + resIdTransPolExtended
			+ " ( P_1 \\WEDGE ( " + resIdTransPolExtended + " = Transaction1 ) ) ) \\WEDGE " + "( \\REST "
			+ actIdTransPolExtended + " ( P_1 \\WEDGE ( " + actIdTransPolExtended + " = execute ) ) ) ) ) " + "\\VEE "
			+ "( \\NEG ( ( \\REST " + resIdTransPolExtended + " ( P_1 \\WEDGE ( " + resIdTransPolExtended
			+ " = Transaction1 ) ) ) \\WEDGE " + "( \\REST " + actIdTransPolExtended + " ( P_1 \\WEDGE ( "
			+ actIdTransPolExtended + " = audit ) ) ) ) ) )";

	static String queryRefinement = "\\NEG ( ( P_1 \\IMPLIES P_2 ) \\WEDGE ( D_1 \\IMPLIES D_2 ) )";
	static String querySubsumption = "\\NEG ( ( P_1 \\IMPLIES P_2 ) \\WEDGE ( D_1 \\IMPLIES D_2 ) \\WEDGE ( IN_1 \\IMPLIES IN_2 ) )";

	static String queryChangeImpact = " ( ( P_1 \\IMPLIES ( \\NEG P_2 ) ) \\VEE ( D_1 \\IMPLIES ( \\NEG D_2 ) ) \\VEE ( IN_1 \\IMPLIES ( \\NEG IN_2 ) ) \\VEE ( ( \\NEG ( P_1 \\VEE ( D_1 \\VEE IN_1 ) ) ) \\IMPLIES ( P_2 \\VEE ( D_2 \\VEE IN_2 ) ) ) )";

	static String queryScenarioFinding = "P_1";

	private static final long KILOBYTE = 1024L;

	public static Map<String, Object> performAttributeHidingCheck(SMTPolicy[] policies, QueryRunner qr, boolean partial,
			String att, String[] attValues, String partialValue) {
		String query = null;
		if (partial) {
			String attValuePairsNotIn = "";
			for (int i = 0; i < attValues.length; i++) {
				if (!attValues[i].equals(partialValue)) {
					attValuePairsNotIn = attValuePairsNotIn + "( " + att + " = " + attValues[i].toString() + " )"
							+ (attValues.length - 1 == i ? "" : " \\WEDGE ");
				}
			}

			query = "( \\NEG ( ( \\REST " + att + " ( D_1 \\WEDGE " + att + " = " + partialValue.toString() + " ) ) "
					+ "\\IMPLIES ( P_1 ) ) \\WEDGE ( " + attValuePairsNotIn + " ) )";
		} else {
			query = "( \\NEG ( ( \\REST " + att + " ( D_1 ) ) \\IMPLIES " + "( P_1 ) ) \\WEDGE " + att
					+ " = \\EMPTYSET )";
		}
		System.out.println(query);
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		return qr.executeQuery(policies, query, System.out);
	}

	public static Map<String, Object> performScenarioFinding(SMTPolicy policy, XACMLPQNormalizer pl) {
		SMTRule finalRule = policy.getFinalElement();
		Reasoner s = new Reasoner(pl.getContext());
		return s.solveSMT(finalRule.getPermitDS(), false, pl, 0, System.out);
	}

	private static void waitForInput() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long convertToBigger(int choice, double bytes) {
		double result = 0L;
		switch (choice) {
		case 1:
			result = bytes / 1048576D;
			break;
		case 2:
			result = bytes / 1024L;
		}
		return Double.valueOf(result).longValue();
	}

	private static void printResults(Map<String, Object> res) {
		System.out.println("Time : " + ((DescriptiveStatistics) res.get("time")).getSum() + " Memory :"
				+ convertToBigger(1, ((DescriptiveStatistics) res.get("memory")).getSum()) + " Status :"
				+ res.get("status"));
	}

	private static void generateSMTEncoding(String query, SMTPolicy[] policies, QueryRunner qr, boolean allModels) {
		Tree tree = new Tree();

		System.out.println("**********   QUERY   **********");
		System.out.println(query);

		QueryParser qp = new QueryParser(query, QueryParser.EXPRESSIONTYPE.Infix);

		if (QueryRunner.debug) {
			System.out.println("Postfix:" + qp.GetPostfixExpression());
		}
		tree.addNode((String) qp.Root.getIdentifier().first);
		QueryRunner.convertToCustomTree(qp.Root, null, tree);
		if (QueryRunner.debug) {
			System.out.println("Tree representation of the query:");
			tree.display((String) qp.Root.getIdentifier().first);
		}

		if (QueryRunner.verbose)
			System.out.println("\n***** Z3 NOTATION *****************************************");
		Expr smtRepresentation = qr.convert2Z3ExprStr(tree, (String) qp.Root.getIdentifier().first, 0, qp.getNodes(),
				policies, qr.getLoader().getContext());
		try {
			PrintStream outputSMT = new PrintStream(new FileOutputStream("SMT-Encoding/OutputSMT2.txt", false));
			outputSMT.print(qr.getLoader().getContext().benchmarkToSMTString(query, "", "sat", "",
					new BoolExpr[] { (BoolExpr) smtRepresentation }, qr

							.getLoader().getContext().mkTrue()));
			outputSMT.close();
		} catch (Z3Exception zex) {
			System.err.println("Error in conversion to SMTLib format in RealPolicyTest " + zex.getLocalizedMessage());
		} catch (IOException ioex) {
			System.err.println("Error occurred in generateSMTFile of RealPolicyTests" + ioex.getMessage());
		}
		QueryRunner.models = allModels;
		QueryRunner.threshold = 1;
		Map<String, Object> resultQuery = qr.executeQuery(policies, query, System.out);
	}

	private static void generateSMTForSATExperiments(PrintStream output) {
		QueryRunner qr = new QueryRunner();
		QueryRunner.verbose = false;
		QueryRunner.z3output = true;
		QueryRunner.debug = false;
		QueryRunner.xacmlVersion = 2;

		String[] v3Policies = { "policies/xacml/releaseTests/KMarketFINAL.xml",
				"policies/xacml/releaseTests/KMarketFINAL.xml" };

		String[] v2Policies = { "policies/xacml/releaseTests/AttributeHidingPolicy_v2.xml" };

		String queryPAH = "\\NEG ( ( ( \\REST employer ( D_1 \\WEDGE employer = B ) ) \\IMPLIES ( P_1 ) ) \\WEDGE employer = A )";
		String queryGAH = "\\NEG ( ( ( \\REST employer ( D_1 ) ) \\IMPLIES ( P_1 ) ) \\WEDGE employer = \\EMPTYSET )";
		String querySoDSMTSAT = querySoDTransPolExtended;

		String query = queryPAH;

		SMTPolicy[] policyFormulas = null;
		String policyName = null;

		if (QueryRunner.xacmlVersion == 3) {
			policyFormulas = qr.loadPolicies(v3Policies, true, 3, query);
			if (QueryRunner.verbose)
				QueryRunner.printSMTPolicies(policyFormulas, qr.getLoader().getContext(), output);
			policyName = v3Policies[0].substring(v3Policies[0].lastIndexOf("/") + 1, v3Policies[0].length());
		} else if (QueryRunner.xacmlVersion == 2) {
			policyFormulas = qr.loadPolicies(v2Policies, true, 2, query);
			if (QueryRunner.verbose)
				QueryRunner.printSMTPolicies(policyFormulas, qr.getLoader().getContext(), output);
			policyName = v2Policies[0].substring(v2Policies[0].lastIndexOf("/") + 1, v2Policies[0].length());
		}

		generateSMTEncoding(query, policyFormulas, qr, false);
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		Map<String, Object> resultSoD = qr.executeQuery(policyFormulas, query, System.out);
	}

	private static void runRealPolicyTests() {
		Map<String, Triple<String, String, String[]>> v2policyPermMap = new HashMap();
		Map<String, Triple<String, String, String[]>> v3policyPermMap = new HashMap();
		v2policyPermMap.put("Continue-a.xml",
				new Triple("role", "pc-member", new String[] { "pc-member", "pc-chair" }));
		v2policyPermMap.put("GradeMan.xml", new Triple("role", "Faculty", new String[] { "Faculty", "TA" }));
		v2policyPermMap.put("COMPLEX_XACML_POLICY_IN4Stars.xml",
				new Triple("urn:oasis:names:tc:xacml:1.0:subject:subject-id", "nl:brigadeGeneraal",
						new String[] { "nl:brigadeGeneraal", "nl-army" }));
		v2policyPermMap.put("PolicyAnomalyPaper-ExamPolicy.xml",
				new Triple("role", "Designer", new String[] { "Designer", "Manager" }));
		v2policyPermMap.put("TransactionPolicy.xml",
				new Triple("urn:oasis:names:tc:xacml:1.0:action:action-id", "create", new String[] { "create" }));
		v3policyPermMap.put("KMarketFINAL.xml",
				new Triple("http://kmarket.com/id/group", "gold", new String[] { "gold", "silver" }));

		String[] v3Policies = { "policies/xacml/releaseTests/KMarketFINAL.xml" };

		String[] v2Policies = { "policies/xacml/releaseTests/Continue-a.xml" };

		QueryRunner qr = new QueryRunner();
		QueryRunner.verbose = false;
		QueryRunner.z3output = true;
		QueryRunner.debug = false;
		QueryRunner.xacmlVersion = 2;

		PrintStream output = System.out;
		try {
			output = new PrintStream(new FileOutputStream("SMT-Encoding/OutputSMTPolicies.txt", false));
		} catch (IOException ioex) {
			System.err.println("Error occurred in main of RealPolicyTests" + ioex.getMessage());
		}

		SMTPolicy[] policyFormulas = null;
		String policyName = null;

		long timeBeginLoad = System.currentTimeMillis();
		if (QueryRunner.xacmlVersion == 3) {
			policyFormulas = qr.loadPolicies(v3Policies, true, 3, null);
			if (QueryRunner.verbose)
				QueryRunner.printSMTPolicies(policyFormulas, qr.getLoader().getContext(), output);
			policyName = v3Policies[0].substring(v3Policies[0].lastIndexOf("/") + 1, v3Policies[0].length());
		} else if (QueryRunner.xacmlVersion == 2) {
			policyFormulas = qr.loadPolicies(v2Policies, true, 2, null);
			if (QueryRunner.verbose)
				QueryRunner.printSMTPolicies(policyFormulas, qr.getLoader().getContext(), output);
			policyName = v2Policies[0].substring(v2Policies[0].lastIndexOf("/") + 1, v2Policies[0].length());
		}
		long timeEndLoad = System.currentTimeMillis();

		long timeBeginSolve = System.currentTimeMillis();
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		Map<String, Object> resultSoD = qr.executeQuery(policyFormulas, querySoDContinue, System.out);

		long timeEndSolve = System.currentTimeMillis();
		System.out.println(
				"LOAD TIME : " + (timeEndLoad - timeBeginLoad) + "  SOLVE TIME : " + (timeEndSolve - timeBeginSolve));

		printResults(resultSoD);
		System.exit(0);

		waitForInput();
		QueryRunner.models = true;
		QueryRunner.threshold = 100;
		Map<String, Object> resultCI = qr.executeQuery(policyFormulas, queryChangeImpact, System.out);
		waitForInput();
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		Map<String, Object> resultPR = qr.executeQuery(policyFormulas, queryRefinement, System.out);
		waitForInput();
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		Map<String, Object> resultPS = qr.executeQuery(policyFormulas, querySubsumption, System.out);
		waitForInput();
		QueryRunner.models = false;
		QueryRunner.threshold = 0;
		Map<String, Object> resultSF = qr.executeQuery(policyFormulas, queryScenarioFinding, System.out);
		waitForInput();

		System.out.println("Time : " + ((DescriptiveStatistics) resultCI.get("time")).getSum() + " Memory :"
				+ convertToBigger(2, ((DescriptiveStatistics) resultCI.get("memory")).getSum()) + " Status :"
				+ resultCI.get("status"));

		Triple tr = QueryRunner.xacmlVersion == 2 ? (Triple) v2policyPermMap.get(policyName)
				: (Triple) v3policyPermMap.get(policyName);
		Map<String, Object> resultPAH = performAttributeHidingCheck(policyFormulas, qr, true, tr.first.toString(),
				(String[]) tr.third, tr.second.toString());
		waitForInput();
		Map<String, Object> resultGAH = performAttributeHidingCheck(policyFormulas, qr, false, tr.first.toString(),
				(String[]) tr.third, null);

		waitForInput();

		printResults(resultPAH);
		printResults(resultGAH);
		printResults(resultPR);
		printResults(resultPS);
		printResults(resultSF);
	}

	public static void main(String[] args) {
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/experiments/Experiments.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */