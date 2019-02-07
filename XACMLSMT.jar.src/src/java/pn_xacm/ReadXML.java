package pn_xacm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.SystemOutLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import experiments.AbstractExperiments;
import policy.SMTPolicy;
import policy.SMTPolicyElement.Decision;
import policy.SMTRule;
import query.QueryRunner;
import utils.PresentationUtils;


public class ReadXML extends AbstractExperiments {
	static long start= System.currentTimeMillis();
	static long start1;
	static long start2;
	static long start3;
	public enum QueryMode {
		ShowAtPolicyOnly, ShowAtPolicyWithTargetCombination, ShowAtRule, ShowAtConstraint
	}
	
	private static Scanner scanner;
	private static Scanner scanner2;
	private static HashMap<String, String> mapAttributeNegValue2 ;
	public static void main(String[] args) throws Exception {
		
		System.out.println(start);
		Attribute.readerRequest();
		
		Attribute.readText();
		Attribute.creatListAttributeValue("E:/Source/source/XACMLSMT.jar.src/fileAttributes2.txt");
		Attribute.createMapTotalValue();
		long end2 = 	System.currentTimeMillis();
		start2 = (end2 -start); 
		System.out.println("ket thuc domain "+start2);
		HashMap<String, String> mapReadAttributeDomains = read_Request.readRequestTXT();
		HashMap<String, String> mapReadAttributeRequetsXML = read_Request.readerRequestXML();
		HashMap<String, String> mapAttributeNegValue = new LinkedHashMap<>();
		long end = 	System.currentTimeMillis();
		start1 = (end-start); 
		System.out.println("ket thuc parse map "+start1);
		start3 = start1+start2;
		System.out.println("Thoi gian "+start3);
		String queryAnaly= readAttributeDomains(mapReadAttributeRequetsXML, mapReadAttributeDomains, mapAttributeNegValue);
		
		testWithKMarket2Policy(queryAnaly);
//		menu();
//		 Scanner scanner = new Scanner(System.in);
//		    int choice = scanner.nextInt();
//		
//		    switch (choice) {
//	        case 1:
//	        	 extend(queryAnaly);
//	     		WriteTxt();// read txtValue and format .
//	            break;
//	        case 2:
//	        	extend2(queryAnaly);
//	        	WriteTxt();// read txtValue and format .
//	            break;
//	        case 3:
//	            // Perform "decrypt number" case.
//	            break;
//	        case 4:
//	            // Perform "quit" case.
//	            break;
//	        default:
//	            // The user input an unexpected choice.
//	    }
	
		   
	
	}
	
	public static int menu() {

        int selection;
        Scanner input = new Scanner(System.in);

        /***************************************************/

        System.out.println("Choose from these choices");
        System.out.println("-------------------------\n");
        System.out.println("1 - Arraylec,idClass,ArrayList_student");
        System.out.println("2 - idLec,department,ArrayList_student");
        System.out.println("3 - Decrypt a number");
        System.out.println("4 - Quit");

        selection = input.nextInt();
        return selection;    
    }
	/***
	 * read txtValue
	 * @return list2 
	 * @throws FileNotFoundException
	 */
private static List<String> scannerPhay() throws FileNotFoundException {
	File textF = new File("E:/Source/source/XACMLSMT.jar.src/txtvalue.txt");
	Scanner scanner = new Scanner(textF);
	List<String> list = new ArrayList<>();
	List<String> list2 = new ArrayList<>();
	while (scanner.hasNextLine()) {
		String temp = scanner.nextLine().toString();
		list.add(temp);
	}
	for (String string : list) {
		
		if (string.endsWith(",")) { 
			string = string.substring(0, string.length() - 1) + " "; 
			list2.add(string);
		}else {
			list2.add(string);
		}
	}
	return list2;
}
/***
 *  write txtValue lan 2
 * @throws FileNotFoundException
 */
	private static void WriteTxt() throws FileNotFoundException {
		List<String> list = scannerPhay();
		String fileName = "txtvalue.txt";
		PrintWriter outputStream = new PrintWriter(fileName);
		for (String string : list) {
			outputStream.println(string);
		}
		outputStream.close();
	}
	private static int[] anArray;
	private static void extend(String queryAnaly) throws FileNotFoundException {
		String getvaule = testWithKMarket2Policy(queryAnaly);
		String fileName = "txtvalue.txt";
		PrintWriter outputStream = new PrintWriter(fileName);
		for (Entry<String, String> array : mapAttributeNegValue2.entrySet()) {
			outputStream.println(array.getKey()+"="+array.getValue());
		}
		if(!getvaule.equals("true")) {
			outputStream.println("result"+"="+"IND");
		}else if (getvaule.equals("false")) {
			outputStream.println("result"+"="+getvaule);
		}else {
			outputStream.println("result"+"="+getvaule);
		}	
		Random random = new Random();
		scan2(outputStream, random);
		outputStream.close();
	}
	private static void extend2(String queryAnaly) throws FileNotFoundException {
		String getvaule = testWithKMarket2Policy(queryAnaly);
		String fileName = "txtvalue.txt";
		PrintWriter outputStream = new PrintWriter(fileName);
		for (Entry<String, String> array : mapAttributeNegValue2.entrySet()) {
			outputStream.println(array.getKey()+"="+array.getValue());
		}
		if(!getvaule.equals("true")) {
			outputStream.println("result"+"="+"IND");
		}else if (getvaule.equals("false")) {
			outputStream.println("result"+"="+getvaule);
		}else {
			outputStream.println("result"+"="+getvaule);
		}
		Random random = new Random();
		scan3(outputStream, random);
		outputStream.close();
	}

	private static void scan3(PrintWriter outputStream, Random random) {
		System.out.print("Nhap  extend :");
		scanner2 = new Scanner(System.in);
		String strQuery = scanner2.nextLine();
		System.out.print("Nhap  extend:");
		String strQuery3 = scanner2.nextLine();
		System.out.print("Nhap  extend:");
		String strQuery2 = scanner2.nextLine();
		
		anArray = new int[4];
	    Random rand = new Random();
	    for (int i = 0; i < 4; i++) {
	      anArray[i] = rand.nextInt(1500);
	    }
	    String[] fruits = {"Khoa ky thuat phan mem","He thong thong tin","Khoa Hoc may tinh"};
	   int idx = new Random().nextInt(fruits.length);
	    String random2 = (fruits[idx]);
	    outputStream.print(strQuery+"="+random.nextInt(1500));
	   outputStream.println();
	   outputStream.println(strQuery3+"="+random2);
	    outputStream.print(strQuery2+"=");
	    for (int i : anArray) {
			outputStream.print(i+",");
		}
		
	}

	private static void scan2(PrintWriter outputStream, Random random) {
		System.out.print("Nhap  extend :");
		scanner2 = new Scanner(System.in);
		String strQuery = scanner2.nextLine();
		System.out.print("Nhap  extend:");
		String strQuery3 = scanner2.nextLine();
		System.out.print("Nhap  extend:");
		String strQuery2 = scanner2.nextLine();
		
		anArray = new int[4];
	    Random rand = new Random();
	    for (int i = 0; i < 4; i++) {
	      anArray[i] = rand.nextInt(1500);
	    }
	  //  String[] fruits = {"Khoa ky thuat phan mem","He thong thong tin","Khoa Hoc may tinh"};
	  // int idx = new Random().nextInt(fruits.length);
	  //  String random2 = (fruits[idx]);
	    outputStream.print(strQuery+"=");
	    for (int i : anArray) {
			outputStream.print(i+",");
		}
	   outputStream.println();
	   outputStream.println(strQuery3+"="+random.nextInt(1500));
	    outputStream.print(strQuery2+"=");
	    for (int i : anArray) {
			outputStream.print(i+",");
		}
		
	}

	
	private static String testWithKMarket2Policy(String queryAnaly) {
		QueryMode queryMode = QueryMode.ShowAtConstraint;
		String policyPath = "policies\\SupportingOurApproach.xml";
		//String analyzeQuery = "(\\REPLC (http://kmarket.com/id/role = kmarket-gold \\WEDGE (\\NEG (http://kmarket.com/id/role = kmarket-blue \\WEDGE http://kmarket.com/id/role =  kmarket-silver)))((\\REPLC (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Liquor \\WEDGE (\\NEG (urn:oasis:names:tc:xacml:1.0:resource:resource-id = Medicine \\WEDGE urn:oasis:names:tc:xacml:1.0:resource:resource-id =  Drink)))(P_1))))";
	// target 1818   1785  3355  1157
		// policy 1809   millisecond  2554    1626   1497
		// rule 2004 millisecond   1683 1712
// constraint 1951    3069   3296  
  
		return analyze(queryMode, policyPath, queryAnaly, false);
		
	}

	private static String analyze(QueryMode queryMode, String policyPath, String analyzeQuery, boolean findApplicableModel) {
		long start= System.currentTimeMillis();
		ReadXML qt = new ReadXML();
		QueryRunner qr = qt.getQueryRunner();
		String value=null;
		QueryRunner.SHOW_AT_POLICY_ONLY = queryMode == QueryMode.ShowAtPolicyOnly;
		QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION = queryMode == QueryMode.ShowAtPolicyWithTargetCombination;
		QueryRunner.SHOW_AT_RULE = queryMode == QueryMode.ShowAtRule;

		SMTPolicy[] policyFormulas = qt.apply(qr, !(QueryRunner.SHOW_AT_POLICY_ONLY
				|| QueryRunner.SHOW_AT_POLICY_WITH_TARGET_COMBINATION || QueryRunner.SHOW_AT_RULE), policyPath);
		long end = System.currentTimeMillis();
		long t2 = end-start;
		System.out.println(t2);
	    long t = t2-start3;
	    System.out.println("Tong thoi gi: " + t + " millisecond");
	
		if (analyzeQuery != null) {
			Expr queryExpression = qr.getQueryExpression(policyFormulas, analyzeQuery, System.out);// z3
			
			System.out
					.println("\n\n-------------------- Normal Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.normalUniform(queryExpression));
			value = PresentationUtils.normalUniform(queryExpression);
			System.out
					.println("\n\n-------------------- Solver Simplification of Query Expression --------------------");
			System.out.println(PresentationUtils.uniform(qr.getLoader().getContext(), queryExpression));

			if (findApplicableModel) {
				Map<String, Object> result = qr.findApplicableModel(queryExpression, System.out);
			}
		}
		return value;

	}


	private SMTPolicy[] apply(QueryRunner qr, boolean showAtConstraint, String policyPath) {
		SMTPolicy[] policyFormulas = qr.loadPolicies(new String[] { policyPath }, true, QueryRunner.xacmlVersion,
				domainTest);

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








	/**
	 * 
	 * @param mapReadAttributeRequetsXML read attribute requets xml
	 * @param mapReadAttributeDomains    read attribute domains in file txt
	 * @param mapAttributeNegValue       map attribute NegValue
	 */
	public static String domainTest;
	private static String readAttributeDomains(HashMap<String, String> mapReadAttributeRequetsXML,
			HashMap<String, String> mapReadAttributeDomains, HashMap<String, String> mapAttributeNegValue) throws FileNotFoundException {
		HashMap<String, String> mapAttributegValue = new HashMap<>();
		for (Entry<String, String> entryAttributeDomains : mapReadAttributeDomains.entrySet()) {
			mapDomainNeg(mapReadAttributeRequetsXML, mapReadAttributeDomains, mapAttributeNegValue,
					entryAttributeDomains);
			mapValue(mapReadAttributeRequetsXML, mapReadAttributeDomains, mapAttributegValue,
					entryAttributeDomains);
		}
		//System.out.print("Nhap  ");
		//scanner = new Scanner(System.in);
		String strQuery = "P_1";

		mapAttributeNegValue2 =	mapAttributegValue;
		
		String stringQuery = space(mapReadAttributeRequetsXML, strQuery, mapAttributeNegValue);
		System.out.println(stringQuery);
		String stringQuery2=space2(mapReadAttributeRequetsXML, mapAttributeNegValue);
		domainTest=stringQuery2.replace("\\WEDGE ()", "");
		System.out.println(domainTest);
		return stringQuery;

	}

	private static void mapValue(HashMap<String, String> mapReadAttributeRequetsXML,
			HashMap<String, String> mapReadAttributeDomains, HashMap<String, String> mapAttributegValue,
			Entry<String, String> entryAttributeDomains) {
		for (Entry<String, String> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
				if (entryAttributeDomains.getKey().equals(entryAttributeRequetsXML.getKey())) {
					 mapAttributegValue.put(entryAttributeDomains.getKey(), entryAttributeRequetsXML.getValue());
					}
				}
		
		
	}



	private static void mapDomainNeg(HashMap<String, String> mapReadAttributeRequetsXML,
			HashMap<String, String> mapReadAttributeDomains, HashMap<String, String> mapAttributeNegValue,
			Entry<String, String> entryAttributeDomains) {
		for (Entry<String, String> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
			String[] arrayXml = entryAttributeDomains.getValue().trim().split(", ");
			for (String gValueAttributeDomains : arrayXml) {
				if (gValueAttributeDomains.trim().equals(entryAttributeRequetsXML.getValue())) {
					String gValueNeg = mapReadAttributeDomains
							.put(entryAttributeDomains.getKey(), entryAttributeDomains.getValue().trim())
							.replaceAll(entryAttributeRequetsXML.getValue() + ", ", "")
							.replaceAll(", " + entryAttributeRequetsXML.getValue().trim(), " ");
					mapAttributeNegValue.put(entryAttributeDomains.getKey().trim(), gValueNeg.trim());
				}
			}

		}
	}

	/**
	 * 
	 * @param mapReadAttributeRequetsXML read attribute requets xml
	 * @param strQuery                   replaceOf(String currentExpr, String var,
	 *                                   String posValue, String... negValues,)
	 * @param mapAttributeNegValue       map attribute NegValue
	 * @return
	 */
	private static String space(HashMap<String, String> mapReadAttributeRequetsXML, String strQuery,
			HashMap<String, String> mapAttributeNegValue) {
		for (Entry<String, String> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
			for (Entry<String, String> entryAttributeNegValue : mapAttributeNegValue.entrySet()) {
				if (entryAttributeRequetsXML.getKey().equals(entryAttributeNegValue.getKey())) {
					String[] arr = entryAttributeNegValue.getValue().trim().split("= ");
					strQuery = stringLastArry(strQuery, entryAttributeRequetsXML, arr);
				}
			}
		}
		return strQuery;
	}

	private static String stringLastArry(String strQuery, Entry<String, String> entryAttributeRequetsXML,
			String[] arr) {
		for (String attri : arr) {
			strQuery = replaceOf(strQuery, entryAttributeRequetsXML.getKey(),
					entryAttributeRequetsXML.getValue().trim(), attri.trim().split(", "));
		}
		return strQuery;
	}
	
	

	private static String space2(HashMap<String, String> mapReadAttributeRequetsXML, 
			HashMap<String, String> mapAttributeNegValue) {
		String strQuery=null;
		for (Entry<String, String> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
			for (Entry<String, String> entryAttributeNegValue : mapAttributeNegValue.entrySet()) {
				if (entryAttributeRequetsXML.getKey().equals(entryAttributeNegValue.getKey())) {
					String[] arr = entryAttributeNegValue.getValue().trim().split("= ");
					for (String attri : arr) {
						strQuery = replaceOfDomain(strQuery,entryAttributeRequetsXML.getKey(),
								entryAttributeRequetsXML.getValue().trim(), attri.trim().split(", "));
					}
				}
			}
		}
		return strQuery;
	}

	

	private static String exprOf(String var, String value) {
		return var + " = " + value;
	}

	private static String andOf(String... exprs) {
		return StringUtils.join(exprs, " \\WEDGE ");
	}
	private static String orOf(String... exprs) {
		return StringUtils.join(exprs, " \\VEE ");
	}

	private static String notOf(String expr) {
		return "(\\NEG (" + expr + "))";
	}

	private static String applyOn(String newExpr, String currentExpr) {
		return "(" + newExpr + "(" + currentExpr + ")" + ")";
	}
	private static String applyOn2(String newExpr,String currentExpr) {
		return "(" + newExpr +" \\WEDGE "+"(" + andOf(currentExpr) + ")" + ")";
	}
	private static String[] multipleExprOf(String var, String[] negValues) {
		String[] ret = new String[negValues.length];
		for (int idx = 0; idx < negValues.length; idx++) {
			ret[idx] = exprOf(var, negValues[idx]);
		}
		return ret;
	}
	private static String replaceOfDomain(String currentExpr,String var, String posValue,String... negValues) {
		String posExpr = exprOf(var, posValue);

		String internalExpr;
		if (negValues != null && negValues.length > 0) {
			internalExpr = orOf(posExpr, orOf(multipleExprOf(var, negValues)));
		} else {
			internalExpr = exprOf(var, posValue);
		}	
		return applyOn2("(" + internalExpr + ")",currentExpr);
		
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
}
