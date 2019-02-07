
package experiments;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import objects.Policy;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import parserv3.DOMParser;
import policy.SMTPolicy;
import policy.SMTPolicyElement.Decision;
import policy.SMTRule;
import query.QueryRunner;
import utils.PresentationUtils;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

public class QueryOnPolicy extends AbstractExperiments {
	private static final String SIMPLE_HEALTH_CARE_POLICY = "policies\\simple-health-care.xml";

	public enum QueryMode {
		ShowAtPolicyOnly, ShowAtPolicyWithTargetCombination, ShowAtRule, ShowAtConstraint
	}

	public static void main(String[] args) throws Exception {
		//testParseXMLRequest();
		testWithKMarketPolicy();
		//testEvaluateXMLRequestDeny();
		//testEvaluateXMLRequestPermit();
		//testEvaluateXMLRequestNA();
		//testWithSyntheticPolicy();
	}

	private static void testWithSyntheticPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\synthetic360.xml";
		String analyzeQuery = null;

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithKMarket2Policy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\KMarket2.xml";
		String analyzeQuery = replaceOf(
				replaceOf("P_1", "http://kmarket.com/id/role", "kmarket-blue", "kmarket-gold", "kmarket-silver"),
				"urn:oasis:names:tc:xacml:1.0:resource:resource-id", "Drink");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithKMarketPolicy() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\KMarket.xml";
		String analyzeQuery = "(\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Drink) P_1)";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithGEYSERSPolicy4() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = replaceOf(
				"P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type", "http://geysers.eu/upperlicl/resource/resource-type/VNode-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VI", 
				"http://geysers.eu/upperlicl/resource/resource-type/VLink",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-IT",
				"http://geysers.eu/upperlicl/resource/resource-type/VI-Request",
				"http://geysers.eu/upperlicl/resource/resource-type/VR",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Mon-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-State-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Power-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Status-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Sync-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Operation-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/RP-Operation-Info");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithGEYSERSPolicy3() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = replaceOf(
				"P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type", "http://geysers.eu/upperlicl/resource/resource-type/VI", 
				"http://geysers.eu/upperlicl/resource/resource-type/VLink",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-IT",
				"http://geysers.eu/upperlicl/resource/resource-type/VI-Request",
				"http://geysers.eu/upperlicl/resource/resource-type/VR",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Mon-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-State-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Power-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Status-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Sync-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Operation-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/RP-Operation-Info");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithGEYSERSPolicy2() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = replaceOf(
				"P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type", "http://geysers.eu/upperlicl/resource/resource-type/VLink", 
				"http://geysers.eu/upperlicl/resource/resource-type/VI", 
				"http://geysers.eu/upperlicl/resource/resource-type/VR-IT",
				"http://geysers.eu/upperlicl/resource/resource-type/VI-Request",
				"http://geysers.eu/upperlicl/resource/resource-type/VR",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Mon-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-State-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Power-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Status-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Sync-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-Operation-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP",
				"http://geysers.eu/upperlicl/resource/resource-type/VR-RP-Info",
				"http://geysers.eu/upperlicl/resource/resource-type/RP-Operation-Info");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithGEYSERSPolicy() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = null;

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithGEYSERS2Policy() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS-2.xml";
		String analyzeQuery = null;

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithContinueAPolicy2() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-xacml3.xml";
		String analyzeQuery = 
				replaceOf(
					replaceOf(
						replaceOf(
							"P_1", 
							"role", "admin", "pc-chair", "pc-member", "subreviewer"),
						"urn:oasis:names:tc:xacml:1.0:action:action-id", "delete", "read", "create", "write"),
					"urn:oasis:names:tc:xacml:1.0:resource:resource-id", "conference_rc");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithContinueAPolicy1() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-xacml3.xml";
		String analyzeQuery = 
				replaceOf(
					replaceOf(
						"P_1", 
						"role", "admin", "pc-chair", "pc-member", "subreviewer"),
					"urn:oasis:names:tc:xacml:1.0:action:action-id", "delete", "read", "create", "write");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithContinueAPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-xacml3.xml";
		String analyzeQuery = "(\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = conference_rc) (\\REPLC ((role = admin) \\WEDGE (\\NEG (role = pc-chair))) P_1))";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithSimpleContinueAPolicy1() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-test.xml";
		String analyzeQuery = replaceOf("P_1", "role", "admin", "pc-chair", "pc-member");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithSimpleContinueAPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-test.xml";
		String analyzeQuery = "(\\REPLC ((role = admin) \\WEDGE (\\NEG (role = pc-chair))) P_1)";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithInferencePolicy2() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "E:\\Workspace\\Thesis\\Research\\alfa-project\\alfa-project\\src-gen\\health_care_inference.HealthCarePsPatientRecord.xml";
		String analyzeQuery = 
				replaceOf(
				replaceOf("P_1", "resource-path", "Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis", "Patient.Record.PrimateLentivirusGroup"),
				"role", "Nurse", "Doctor", "External");
//		String analyzeQuery = "(\\REPLC (patient:management-info:status = AvailableNurseDoctor) (\\REPLC (action-type = Read) (\\REPLC ((role = Nurse) \\WEDGE (\\NEG (role = Doctor \\WEDGE role = External)) ) (\\REPLC (resource-type = Patient.Record) (\\REPLC (resource-path = Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis) P_1)))))";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithInferencePolicy1() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "E:\\Workspace\\Thesis\\Research\\alfa-project\\alfa-project\\src-gen\\health_care_inference.HealthCarePsPatientRecord.xml";
		String analyzeQuery =
				replaceOf(
						replaceOf(
							replaceOf("P_1", "resource-path", "Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis", 
												"Patient.Record.Medicines", "Patient.Record.Billing"),
							"role", "Nurse", "Doctor", "External"),
					"patient:management-info:status", "AvailableNurseDoctor");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	
	private static void testWithNormalHealthcare3Policy1() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\health-care3.xml";
			String analyzeQuery = replaceOf(
									replaceOf(
										replaceOf(
											replaceOf(
												replaceOf(
													replaceOf(
														replaceOf("P_1", "user-id", "Doctor-Patient"), 
														"role", "Doctor", "Nurse"),
													"resource-type", "Patient.Record"),
												"patient:management-info:status", "AvailableNurseDoctor"),
											"patient:record:department", "Heart"),
										"patient:record:assigned-doctor-id", "Doctor-Patient"),
									"action-type", "Read");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithNormalHealthcare3Policy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\health-care3.xml";
		String analyzeQuery = replaceOf(replaceOf("P_1.SHORT", "party-type", "Insurance"), "role",
				"External", "Doctor", "Nurse");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithNormalHealthcarePolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyOnly;
		String policyPath = "policies\\health-care.xml";
		String analyzeQuery = null;

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testParseXMLRequest() throws Exception {
		String analyzeQuery = replaceOf(
				replaceOf(
					replaceOf(
						replaceOf(
							replaceOf(
								replaceOf(
									replaceOf("P_1", "user-id", "Doctor-Patient"), 
									"role", "Doctor"),
								"resource-type", "Patient.Record"),
							"patient:management-info:status", "AvailableNurseDoctor"),
						"patient:record:department", "Heart"),
					"patient:record:assigned-doctor-id", "Doctor-Patient"),
				"action-type", "Read");
		
		//Assert.assertTrue(analyzeQuery.equals(parseXMLRequest("requests/request.permit.xml")));
	}

	private static void testEvaluateXMLRequestPermit() throws ParserConfigurationException, SAXException, IOException  {
		String policyPath = SIMPLE_HEALTH_CARE_POLICY;
		//String request = DomainReader.readerRequest("requests/request-external.permit.xml").toString(); 
		//evaluate(policyPath, request);
	}
	
	private static void testEvaluateXMLRequestDeny() throws ParserConfigurationException, SAXException, IOException {
		String policyPath = SIMPLE_HEALTH_CARE_POLICY;
		//String request = DomainReader.readerRequest("requests/request-external.deny.xml").toString(); 

		//evaluate(policyPath,request);
	}
	
	private static void testEvaluateXMLRequestNA() throws ParserConfigurationException, SAXException, IOException {
		String policyPath = SIMPLE_HEALTH_CARE_POLICY;
		//String request = DomainReader.readerRequest("requests/request-external.na.xml").toString(); 

	//	evaluate(policyPath, request);
	}

	
	

	private static void parsePolicyElement(NodeList childNodes, Policy result, boolean b) {
		
	}
	
	public static void evaluate(String policyPath, String analyzeQuery) {
		QueryOnPolicy qt = new QueryOnPolicy();
		QueryRunner qr = qt.getQueryRunner();
		
 		
	
	}

	
	
	public static void analyze(QueryMode queryMode, String policyPath, String analyzeQuery, boolean findApplicableModel) {
		QueryOnPolicy qt = new QueryOnPolicy();
		QueryRunner qr = qt.getQueryRunner();

		QueryRunner.SHOW_AT_POLICY_ONLY = queryMode == QueryMode.ShowAtPolicyOnly;
		QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION = queryMode == QueryMode.ShowAtPolicyWithTargetCombination;
		QueryRunner.SHOW_AT_RULE = queryMode == QueryMode.ShowAtRule;

		SMTPolicy[] policyFormulas = qt.apply(qr,
				!(QueryRunner.SHOW_AT_POLICY_ONLY || QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION || QueryRunner.SHOW_AT_RULE), policyPath);

		if (analyzeQuery != null) {
			Expr queryExpression = qr.getQueryExpression(policyFormulas, analyzeQuery, System.out);

			System.out.println("\n\n-------------------- Normal Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.normalUniform(queryExpression));

			System.out.println("\n\n-------------------- Solver Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.uniform(qr.getLoader().getContext(), queryExpression));

			if (findApplicableModel) {
				Map<String, Object> result = qr.findApplicableModel(queryExpression, System.out);
			}
		}

	}

	private SMTPolicy[] apply(QueryRunner qr, boolean showAtConstraint, String policyPath) {
		SMTPolicy[] policyFormulas = qr.loadPolicies(new String[] { policyPath }, true,
				QueryRunner.xacmlVersion, DOMAIN);

		System.out.println("\n\n--------------------------------------- EXPERIMENTS -----------------------------------------");
		SMTRule smtRule = policyFormulas[0].getFinalElement();

		Context ctx = qr.getLoader().getContext();
		if (showAtConstraint) {
			System.out.println(Decision.Permit + " " + PresentationUtils.normalUniform(smtRule.getPermitDS()));
			System.out.println(Decision.Deny + " " + PresentationUtils.normalUniform(smtRule.getDenyDS()));
			System.out.println(Decision.Indet + " " + PresentationUtils.normalUniform(smtRule.getIndeterminateDS(ctx)));
			System.out.println(Decision.Na + " " + PresentationUtils.normalUniform(smtRule.getNA_DS()));
		} else {
			System.out.println(Decision.Permit + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Permit, true)));
			System.out.println(Decision.Deny + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Deny, true)));
			System.out.println(Decision.Indet + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Indet, true)));
			System.out.println(Decision.Na + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Na, true)));
		}

		return policyFormulas;
	}
	
	
	public static String exprOf(String var, String value) {
		return var + " = " + value;
	}
	
	
	/**
	 * 
	 * @param currentExpr The current expression
	 * @param var AttributeType
	 * @param posValue Value assigned to the AttributeType
	 * @param negValues Values that are not assigned to the AttributeType
	 * @return
	 */
	public static String replaceOf(String currentExpr, String var, String posValue, String... negValues) {
		String posExpr = exprOf(var, posValue);
		String internalExpr;
		if (negValues != null && negValues.length > 0) {
			internalExpr = andOf(posExpr, notOf(andOf(multipleExprOf(var, negValues))));
		} else {
			internalExpr = exprOf(var, posValue);
		}
		return applyOn("\\REPLC " + "(" + internalExpr + ")", currentExpr);
	}

	public static String[] multipleExprOf(String var, String[] negValues) {
		String[] ret = new String[negValues.length];
		for (int idx = 0; idx < negValues.length; idx++) {
			ret[idx] = exprOf(var, negValues[idx]);
		}
		return ret;
	}

	public static String andOf(String... exprs) {
		return StringUtils.join(exprs, " \\WEDGE ");
	}

	public static String notOf(String expr) {
		return "(\\NEG (" + expr + "))";
	}

	public static String applyOn(String newExpr, String currentExpr) {
		return "(" + newExpr + "(" + currentExpr + ")" + ")";
	}

}