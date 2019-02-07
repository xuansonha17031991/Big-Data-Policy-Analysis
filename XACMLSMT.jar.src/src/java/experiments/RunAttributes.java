package experiments;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.microsoft.z3.Context;

import experiments.QueryOnPolicy2.QueryMode;
import policy.SMTPolicy;
import policy.SMTRule;
import policy.SMTPolicyElement.Decision;
import query.QueryRunner;
import utils.PresentationUtils;

public class RunAttributes  extends QueryOnPolicy2  {
	private static Scanner scanner;

	public static void main(String[] args) throws Exception {
		// map read xml requets
		HashMap<String, String> mapReadAttributeRequetsXML = readerRequest("requests/request.permit.xml");
		// map read attribute domains
		ReadAttributes readTextT = new ReadAttributes();
		HashMap<String, String> mapReadAttributeDomains = readTextT.readText("E:/Source/source/XACMLSMT.jar.src/filetxt.txt");
		// map attributeDomains
		HashMap<String, String> mapAttributeNegValue = new LinkedHashMap<>();
	readAttributeDomains(mapReadAttributeRequetsXML, mapReadAttributeDomains, mapAttributeNegValue);
		// test tree
		//readerRequest("requests/request.permit.xml");
	}

	public static HashMap<String, String> readerRequest(String fXmlFile)
			throws ParserConfigurationException, SAXException, IOException {
		// read xml requets
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		NodeList rootNodeList = doc.getElementsByTagName("Attributes");
		HashMap<String, String> mapRequest = new LinkedHashMap<>();
		for (int i = 0; i < rootNodeList.getLength(); i++) {
			Node rootNode = rootNodeList.item(i);
			Element rootElement = (Element) rootNode;
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
		}

		return mapRequest;
	}
	
	/**
	 * 
	 * @param mapReadAttributeRequetsXML read attribute requets xml
	 * @param mapReadAttributeDomains    read attribute domains in file txt
	 * @param mapAttributeNegValue       map attribute NegValue
	 */
	private static void readAttributeDomains(HashMap<String, String> mapReadAttributeRequetsXML,
			HashMap<String, String> mapReadAttributeDomains, HashMap<String, String> mapAttributeNegValue) {
		for (Entry<String, String> entryAttributeDomains : mapReadAttributeDomains.entrySet()) {
			for (Entry<String, String> entryAttributeRequetsXML : mapReadAttributeRequetsXML.entrySet()) {
				for (String gValueAttributeDomains : entryAttributeDomains.getValue().trim().split(", ")) {
					if (gValueAttributeDomains.equals(entryAttributeRequetsXML.getValue())) {
						String gValueNeg = mapReadAttributeDomains
								.put(entryAttributeDomains.getKey(), entryAttributeDomains.getValue())
								.replaceAll(gValueAttributeDomains + ",", "");
						mapAttributeNegValue.put(entryAttributeDomains.getKey(), gValueNeg.trim());
					}
				}
			}
		}
		System.out.print("Nhap  ");
		scanner = new Scanner(System.in);
		String strQuery = scanner.nextLine();
		String stringQuery = space(mapReadAttributeRequetsXML, strQuery, mapAttributeNegValue);
		System.out.println(stringQuery);
		
		
		
		
		
		
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
					for (String attri : arr) {
						strQuery = replaceOf(strQuery, entryAttributeRequetsXML.getKey(),
								entryAttributeRequetsXML.getValue(), attri.trim().split(", "));
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

	private static String notOf(String expr) {
		return "(\\NEG (" + expr + "))";
	}

	private static String applyOn(String newExpr, String currentExpr) {
		return "(" + newExpr + "(" + currentExpr + ")" + ")";
	}

	private static String[] multipleExprOf(String var, String[] negValues) {
		String[] ret = new String[negValues.length];
		for (int idx = 0; idx < negValues.length; idx++) {
			ret[idx] = exprOf(var, negValues[idx]);
		}
		return ret;
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
/*
 * //File fXmlFile = new File("requests/request.permit.xml");
 * //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 * //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //Document doc =
 * dBuilder.parse(fXmlFile);
 * 
 * //System.out.println("Root element :" +
 * doc.getDocumentElement().getNodeName());
 * 
 * // NodeList nList = doc.getElementsByTagName("Attributes"); //PrintWriter
 * outputStream = new PrintWriter(fileName); for (int i = 0; i <
 * nList.getLength(); i++) {
 * 
 * Node nNode = nList.item(i); Element eElement = (Element) nNode; //String
 * category = eElement.getAttribute("Category");
 * //System.out.println("Current Element: " + nNode.getNodeName() +
 * " Category = " + category);
 * 
 * if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 * 
 * NodeList nameList = eElement.getChildNodes();
 * 
 * for (int j = 0; j < nameList.getLength(); j++) { Node nNode2 =
 * nameList.item(j);
 * 
 * if (nNode2.getNodeType() == Node.ELEMENT_NODE) { Element element = (Element)
 * nNode2;
 * 
 * String id = element.getAttribute("AttributeId"); // System.out.print("\t" +
 * nNode2.getNodeName() + " AttributeId = " + id); String value =
 * nNode2.getTextContent(); // System.out.println(nNode2.getTextContent());
 * 
 * attributeValueMap.put(id, value.trim()); } } } }
 */
//ArrayList  list = new ArrayList<>();

/*
 * for(Entry<String, String>a : stQ.entrySet())// duyet AttributeDomain {
 * for(Entry<String, String> b : attributeValueMap.entrySet()) {// duyet
 * Attribute xml for(String c : a.getValue().trim().split(", ")) { // duyet
 * AttributeDomain get value for(String d : b.getValue().trim().split("=")) {//
 * duyet Attribute xml getValue if(c.equals(d)) { // so sanh e =
 * stQ.put(a.getKey(), a.getValue()).replaceAll(c+",", ""); // em dung thay the
 * // em dung remove thi //e= stQ.remove(c+";"); xoa khong duoc thuoc
 * attributeDomains.put(a.getKey(), e.trim()); }
 * 
 * } } }
 * 
 * }
 */
//tui bo 2 for roi  space
/*
 * for(Entry<String, String> entry : attributeValueMap.entrySet()) { for(
 * Entry<String, String> entrys : attributeDomains2.entrySet()) { for(String c :
 * entry.getKey().trim().split("=")) { for(String d:
 * entrys.getKey().trim().split("=")) { if(c.equals(d)) { String[] arr =
 * entrys.getValue().trim().split("= "); for(String attri : arr) { strQuery =
 * replaceOf(strQuery,c,entry.getValue(),attri.trim().split(", ")); } } } } } }
 */
