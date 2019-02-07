package experiments;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import parserv3.DOMParser;
import policy.SMTPolicy;
import policy.SMTPolicyElement.Decision;
import policy.SMTRule;
import query.QueryNode;
import query.QueryParser;
import query.QueryRunner;
import utils.PresentationUtils;
import utils.Tree;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

public class QueryOnPolicy2 extends AbstractExperiments {
	static long start = System.currentTimeMillis();
	public enum QueryMode {
		ShowAtPolicyOnly, ShowAtPolicyWithTargetCombination, ShowAtRule, ShowAtConstraint
	}

	public static void main(String[] args) throws Exception {
		
		 
		//testWithContinueAPolicy2();

		 //testWithContinueAPolicy1();
	//testParseXMLRequest();
	//	testWithSyntheticPolicy();
	
	//testWithKMarket2Policy();

//	testWithKMarketPolicy();
//	testWithContinueAPolicy();
//		testWithGEYSERS2Policy();

//		testWithGEYSERSPolicy4();

//		testWithGEYSERSPolicy3();

		//testWithGEYSERSPolicy2();

//		 testWithGEYSERSPolicy();
//	testWithInferencePolicy2();

//		testWithInferencePolicy1();

//		 testWithNormalHealthcare3Policy();
		testWithSyntheticPolicy();
	}

	private static void testWithSyntheticPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\itrust3-10.xml";
		String analyzeQuery = "(\\REPLC (urn:oasis:names:tc:xacml:1.0:action:action-id = View \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:action:action-id = Add \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Edit3 \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Monitor \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Report \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Edit \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Show \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Manage \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Change \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = Send)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Messages \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Status1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Access log8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ER9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = All Patients5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHA \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Adverse Events \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Satisfaction Survey Results2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Personnel8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Diagnoses \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAPs \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Report Requests1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital7 2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital6 3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital4 1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital2 6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital1 7 4 5 8 9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient Information \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = CPT ProcedureCodes \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Emergency Patient Report8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Prescription Records \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Office Visit Reminders8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = PHR Information3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Records8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Consultations \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Chronic Disease Risks5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Basic Health Information \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Expired Prescription Reports9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Pending Consultations5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Demographics \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Potential Prescription-Renewals9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Hospital Listing \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = LOINC Codes9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patient List \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = UAP \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ND Codes \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Global Session Timeout1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = ICD Codes8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = My Providers \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Patients \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Document Office Visit \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Representatives9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Laboratory Procedures1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = HCP Assignment to Hospital \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Message \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results2 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results3 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results1 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results6 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results7 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results4 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results5 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results8 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = test results9 \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Email History)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:subject:subject-id = Patient \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP7 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER1 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER7 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA7 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA1 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = ER9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP1 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = HCP3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = PHA \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP1 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP7 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = UAP4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester7 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Tester1 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin 4 3 2 1 8 7 6 5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin8 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin7 9 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin6 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin5 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin4 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin3 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin2 \\WEDGE urn:oasis:names:tc:xacml:1.0:subject:subject-id = Admin1)))(P_1))))))";
		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithKMarket2Policy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\KMarket2.xml";
		//String analyzeQuery = replaceOf(
			//replaceOf("P_1", "http://kmarket.com/id/role", "kmarket-blue", "kmarket-gold", "kmarket-silver"),
			//	"urn:oasis:names:tc:xacml:1.0:resource:resource-id", "Drink");
		String analyzeQuery = "(\\REPLC( http://kmarket.com/id/totalAmount= 500 )((\\REPLC (http://kmarket.com/id/role = kmarket-gold \\WEDGE (\\NEG (http://kmarket.com/id/role = kmarket-blue \\WEDGE http://kmarket.com/id/role = kmarket-silver))) ((((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Medicine \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Liquor \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Drink)))(P_1))))))))";
		
		
		//String analyzeQuery = "(\\REPLC (http://kmarket.com/id/totalAmount = 500 \\WEDGE (\\NEG (http://kmarket.com/id/totalAmount = 100 \\WEDGE http://kmarket.com/id/totalAmount = 1000)))((\\REPLC (http://kmarket.com/id/amount = 50 \\WEDGE (\\NEG (http://kmarket.com/id/amount = 5 \\WEDGE http://kmarket.com/id/amount = 10)))((\\REPLC (http://kmarket.com/id/role = kmarket-silver \\WEDGE (\\NEG (http://kmarket.com/id/role = kmarket-blue \\WEDGE http://kmarket.com/id/role = kmarket-gold)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Drink \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Medicine \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Liquor)))(P_1))))))))";
	
				
				
		analyze(queryMode, policyPath, analyzeQuery, false);
	}
	private static void testWithKMarketPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;

		String policyPath = "policies\\KMarket2.xml";
			String analyzeQuery ="(\\REPLC (http://kmarket.com/id/role = kmarket-gold \\WEDGE (\\NEG (http://kmarket.com/id/role = kmarket-blue \\WEDGE http://kmarket.com/id/role = kmarket-silver)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Medicine \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Liquor \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Drink)))(P_1))))";
			
			analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithGEYSERSPolicy4() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = replaceOf("P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type",
				"http://geysers.eu/upperlicl/resource/resource-type/VNode-Info",
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
		String analyzeQuery = replaceOf("P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type",
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

	private static void testWithGEYSERSPolicy2() {
		QueryMode queryMode = QueryMode.ShowAtRule;
		String policyPath = "policies\\GEYSERS.xml";
		String analyzeQuery = replaceOf("P_1", "http://authz-interop.org/AAA/xacml/resource/resource-type",
				"http://geysers.eu/upperlicl/resource/resource-type/VLink",
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
		String analyzeQuery = replaceOf(
				replaceOf(replaceOf("P_1", "role", "admin", "pc-chair", "pc-member", "subreviewer"),
						"urn:oasis:names:tc:xacml:1.0:action:action-id", "delete", "read", "create", "write"),
				"urn:oasis:names:tc:xacml:1.0:resource:resource-id", "conference_rc");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithContinueAPolicy1() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-xacml3.xml";
		String analyzeQuery = replaceOf(replaceOf("P_1", "role", "admin", "pc-chair", "pc-member", "subreviewer"),
				"urn:oasis:names:tc:xacml:1.0:action:action-id", "delete", "read", "create", "write");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithContinueAPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyOnly;
		String policyPath = "policies\\continue-a-xacml3.xml";
		//String analyzeQuery = "(\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = conference_rc) (\\REPLC ((role = admin) \\WEDGE (\\NEG (role = pc-chair))) (\\REPLC (urn:oasis:names:tc:xacml:1.0:action:action-id = read) P_1)))";
		//"(\\REPLC (urn:oasis:names:tc:xacml:1.0:action:action-id = write) \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:action:action-id = read) \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = create) \\WEDGE (urn:oasis:names:tc:xacml:1.0:action:action-id =  delete)))((\\REPLC (role = admin \\WEDGE (\\NEG (role = pc-chair \\WEDGE role =  pc-member \\WEDGE role = subreviewer)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = conference_rc \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-decision_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = conferenceInfo_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-assignments_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-content-rating_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-info-reviewer_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-conflicts_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id =  isMeetingFlag_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-assignments_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-content-commentsAll_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-submission_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-info-isChairFlag_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-submission-file_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-info-password_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-conflicts_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-assignmentCount_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-content_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = pcMember-info_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-submission-info_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-info_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-content-commentsPc_rc \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id = paper-review-info-submissionStatus_rc)))(P-1))))))";		
		String analyzeQuery="(\\REPLC (urn:oasis:names:tc:xacml:1.0:action:action-id = delete \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:action:action-id = read \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = create \\WEDGE urn:oasis:names:tc:xacml:1.0:action:action-id = write)))((\\REPLC (role = subreviewer \\WEDGE (\\NEG (role = pc-chair \\WEDGE role = admin \\WEDGE role = pc-member)))(P_1))))";
		
		
		
		
		
		//System.out.println(System.getProperty("java.library.path"));
		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithSimpleContinueAPolicy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\continue-a-test.xml";
		String analyzeQuery = "(\\REPLC ((role = admin) \\WEDGE (\\NEG (role = pc-chair))) P_1)";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithInferencePolicy2() {
		QueryMode queryMode = QueryMode.ShowAtRule;
		String policyPath = "E:\\Workspace\\Thesis\\Research\\alfa-project\\alfa-project\\src-gen\\health_care_inference.HealthCarePsPatientRecord.xml";
		String analyzeQuery = replaceOf(
				replaceOf("P_1", "resource-path", "Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis",
						"Patient.Record.PrimateLentivirusGroup"),
				"role", "Nurse", "Doctor", "External");
//		String analyzeQuery = "(\\REPLC (patient:management-info:status = AvailableNurseDoctor) (\\REPLC (action-type = Read) (\\REPLC ((role = Nurse) \\WEDGE (\\NEG (role = Doctor \\WEDGE role = External)) ) (\\REPLC (resource-type = Patient.Record) (\\REPLC (resource-path = Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis) P_1)))))";

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithInferencePolicy1() {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "E:\\Workspace\\Thesis\\Research\\alfa-project\\alfa-project\\src-gen\\health_care_inference.HealthCarePsPatientRecord.xml";
		String analyzeQuery = replaceOf(replaceOf(
				replaceOf("P_1", "resource-path", "Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis",
						"Patient.Record.Medicines", "Patient.Record.Billing"),
				"role", "Nurse", "Doctor", "External"), "patient:management-info:status", "AvailableNurseDoctor");

		analyze(queryMode, policyPath, analyzeQuery, false);
	}

	private static void testWithNormalHealthcare3Policy() {
		QueryMode queryMode = QueryMode.ShowAtPolicyWithTargetCombination;
		String policyPath = "policies\\health-care3.xml";
		String analyzeQuery = replaceOf(replaceOf("P_1.SHORT", "party-type", "Insurance"), "role", "External", "Doctor",
				"Nurse");

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
										replaceOf(replaceOf(replaceOf("P_1", "user-id", "Doctor-Patient"), "role",
												"Doctor"), "resource-type", "Patient.Record"),
										"patient:management-info:status", "AvailableNurseDoctor"),
								"patient:record:department", "Heart"),
						"patient:record:assigned-doctor-id", "Doctor-Patient"),
				"action-type", "Read");

		/*
		 * if(analyzeQuery.equals(parseXMLRequest("requests/request.permit.xml"))) {
		 * System.out.println("true"); } else { System.out.println("flase"); }
		 */
		Assert.assertTrue(analyzeQuery.equals(parseXMLRequest("requests/request.permit.xml")));
	}

	private static String a = null;
	private static String acsx = null;

	private static String parseXMLRequest(String fXmlFile)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		NodeList rootNodeList = doc.getElementsByTagName("Attributes");

		
		HashMap<String, String> mapRequestClone = new LinkedHashMap<>();
		HashMap<String, HashMap<String, String>> mapTong = new LinkedHashMap<>();
		List<String> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();

		String idChildElement = "";
		String valueChildElement = "";
		for (int i = 0; i < rootNodeList.getLength(); i++) {
			Node rootNode = rootNodeList.item(i);
			Element rootElement = (Element) rootNode;
			a = rootElement.getAttribute("Category");			
			mapTong.put(a, mapHash(rootNode, rootElement));
		}
		System.out.println(mapTong);

		
		
		return null;
	}

	private static HashMap<String, String> mapHash( Node rootNode,
			Element rootElement) {
		HashMap<String, String> mapRequest = new LinkedHashMap<>();
		if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
			NodeList childNodeList = rootElement.getChildNodes();
			for (int j = 0; j < childNodeList.getLength(); j++) {
				Node childNode = childNodeList.item(j);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					Element childElement = (Element) childNode;
					String idChildElement = childElement.getAttribute("AttributeId");
					String valueChildElement = childNode.getTextContent().trim();
					mapRequest.put(idChildElement, valueChildElement);
				}
			}
		}
		return mapRequest;
	}

	private static String space(HashMap<String, String[]> mapReadAttributeRequetsXML, String strQuery) {
		for (Entry<String, String[]> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
			String[] a = entryAttributeRequetsXML.getValue();
			for (String string : a) {
				System.out.println(a);
			}

		}
		return strQuery;
	}
	
	
	private static void analyze(QueryMode queryMode, String policyPath, String analyzeQuery,
			boolean findApplicableModel) {
		QueryOnPolicy2 qt = new QueryOnPolicy2();
		QueryRunner qr = qt.getQueryRunner();

		QueryRunner.SHOW_AT_POLICY_ONLY = queryMode == QueryMode.ShowAtPolicyOnly;
		QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION = queryMode == QueryMode.ShowAtPolicyWithTargetCombination;
		QueryRunner.SHOW_AT_RULE = queryMode == QueryMode.ShowAtRule;

		SMTPolicy[] policyFormulas = qt.apply(qr, !(QueryRunner.SHOW_AT_POLICY_ONLY
				|| QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION || QueryRunner.SHOW_AT_RULE), policyPath);

		long end = System.currentTimeMillis();
	    long t = end - start;
	    System.out.println("Tong thoi gi: " + t + " millisecond");
	    
		if (analyzeQuery != null) {
			Expr queryExpression = qr.getQueryExpression(policyFormulas, analyzeQuery, System.out);// z3

			System.out
					.println("\n\n-------------------- Normal Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.normalUniform(queryExpression));

			System.out
					.println("\n\n-------------------- Solver Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.uniform(qr.getLoader().getContext(), queryExpression));

			if (findApplicableModel) {
				Map<String, Object> result = qr.findApplicableModel(queryExpression, System.out);
			}
		}

	}

	public SMTPolicy[] apply(QueryRunner qr, boolean showAtConstraint, String policyPath) {
		SMTPolicy[] policyFormulas = qr.loadPolicies(new String[] { policyPath }, true, QueryRunner.xacmlVersion,
				DOMAIN);

		System.out.println(
				"\n\n--------------------------------------- EXPERIMENTS -----------------------------------------");
		SMTRule smtRule = policyFormulas[0].getFinalElement();

		Context ctx = qr.getLoader().getContext();
		if (showAtConstraint) {
			System.out.println(Decision.Permit + " " + PresentationUtils.normalUniform(smtRule.getPermitDS()));
			System.out.println(Decision.Deny + " " + PresentationUtils.normalUniform(smtRule.getDenyDS()));
			System.out.println(Decision.Indet + " " + PresentationUtils.normalUniform(smtRule.getIndeterminateDS(ctx)));// ctx
																														// here
			System.out.println(Decision.Na + " " + PresentationUtils.normalUniform(smtRule.getNA_DS()));
		} else {
			System.out.println(
					Decision.Permit + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Permit, true)));
			System.out.println(
					Decision.Deny + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Deny, true)));
			System.out.println(
					Decision.Indet + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Indet, true)));
			System.out
					.println(Decision.Na + " " + PresentationUtils.normalUniform(smtRule.getDSStr(Decision.Na, true)));
		}

		return policyFormulas;
	}

	private static String exprOf(String var, String value) {
		return var + " = " + value;
	}

	private static String replaceOf(String currentExpr, String var, String posValue, String... negValues) {
		String posExpr = exprOf(var, posValue);
		String internalExpr;
		if (negValues != null && negValues.length > 0) {
			internalExpr = andOf(posExpr, notOf(andOf(multipleExprOf(var, negValues))));
		} else {
			internalExpr = exprOf(var, posValue);
		}
		return applyOn("\\REPLC " + "(" + internalExpr + ")", currentExpr);
	}

	private static String[] multipleExprOf(String var, String[] negValues) {
		String[] ret = new String[negValues.length];
		for (int idx = 0; idx < negValues.length; idx++) {
			ret[idx] = exprOf(var, negValues[idx]);
		}
		return ret;
	}

	private static String andOf(String... exprs) {
		return StringUtils.join(exprs, " \\WEDGE ");
	}

	private static String notOf(String expr) {
		return "(\\NEG (" + expr + "))";
	}

	private static String applyOn(String newExpr, String currentExpr) {
		return "(" + newExpr + "(" + currentExpr + ")" + ")";
	}

}
