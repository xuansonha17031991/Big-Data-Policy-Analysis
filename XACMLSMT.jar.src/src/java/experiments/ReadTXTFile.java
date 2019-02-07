package experiments;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import functions.Equal;

public class ReadTXTFile extends ReadTxtFile1 {
	private static Scanner scanner;
	private static String temp;
	private static String e;
	public static void main(String[] args) throws Exception {
		String fileName ="fileAttributes.txt";
		scanner = new Scanner(System.in);
		try {
			File fXmlFile = new File("requests/request.permit.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Attributes");
			PrintWriter outputStream = new PrintWriter(fileName);
			
			HashMap<String, String> attributeValueMap = new LinkedHashMap<>();// parse xml de lay k,v cua attribute xml
			HashMap<String, String> stQ =readText("E:/Source/source/XACMLSMT.jar.src/filetxt.txt");// Doc file attriButeDomain 
			HashMap<String, String> attributeDomains = new LinkedHashMap<>();// tao hashMap AttributeDomain de tra ve negValue
			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);
				Element eElement = (Element) nNode;
				//String category = eElement.getAttribute("Category");
				//System.out.println("Current Element: " + nNode.getNodeName() + " Category = " + category);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList nameList = eElement.getChildNodes();
					
					for (int j = 0; j < nameList.getLength(); j++) {
						Node nNode2 = nameList.item(j);

						if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode2;

							String id = element.getAttribute("AttributeId");
//							System.out.print("\t" + nNode2.getNodeName() + " AttributeId = " + id);
							String value = nNode2.getTextContent();
//							System.out.println(nNode2.getTextContent());
							
							attributeValueMap.put(id, value.trim());
						}		
					}
				}
			}
			System.out.print("Nhap  ");
			String strQuery = scanner.nextLine();	
		/*	for(Entry<String, String>a : stQ.entrySet())// duyet AttributeDomain
			{
				for(Entry<String, String> b : attributeValueMap.entrySet()) {// duyet Attribute xml
					for(String c : a.getValue().trim().split(", ")) { // duyet AttributeDomain get value
							for(String d : b.getValue().trim().split("=")) {// duyet Attribute xml getValue
								if(c.equals(d)) { // so sanh						
									e =	stQ.put(a.getKey(), a.getValue()).replaceAll(c+",", ""); // em dung thay the 
									// em dung remove thi  
									//e= stQ.remove(c+";"); xoa khong duoc thuoc 										
									attributeDomains.put(a.getKey(), e.trim());	
							}
								
						}
					}
				}
				
			}	*/
			// con 3 vong. tui bo nhung vong khong xai toi roi
			for(Entry<String, String>a : stQ.entrySet()){
				for(Entry<String, String> b : attributeValueMap.entrySet()) {
					for(String c : a.getValue().trim().split(", ")) { 			
								if(c.equals(b.getValue())) { 					
									e =	stQ.put(a.getKey(), a.getValue()).replaceAll(c+",", ""); 
						}
					}
				}	
			}
			
			String stringQuery = space(attributeValueMap,strQuery,attributeDomains);
			System.out.println(stringQuery);
			outputStream.print(stringQuery);		
			outputStream.close();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String space2(HashMap<String, String> attributeValueMap, String strQuery2) {
		for(Entry<String, String> entry : attributeValueMap.entrySet()) {
			strQuery2 = replaceOf(strQuery2,entry.getKey(), entry.getValue());
		}
		return strQuery2;
	}
	private static String space(HashMap<String, String> attributeValueMap, String strQuery,HashMap<String, String> attributeDomains2) {
			
		/*for(Entry<String, String> entry : attributeValueMap.entrySet()) {
				for( Entry<String, String> entrys : attributeDomains2.entrySet())	 	{	
					for(String c : entry.getKey().trim().split("=")) {
						for(String d: entrys.getKey().trim().split("=")) {							
								if(c.equals(d)) {
									String[] arr = entrys.getValue().trim().split("= ");
										for(String attri : arr) {								
												strQuery = replaceOf(strQuery,c,entry.getValue(),attri.trim().split(", "));	
											}
										}
									}				
								}
							}				
			 }*/
		// tui bo 2 for roi 
		for(Entry<String, String> entry : attributeValueMap.entrySet()) {
			for( Entry<String, String> entrys : attributeDomains2.entrySet())	 	{			
							if(entry.getKey().equals(entrys.getKey())) {
								String[] arr = entrys.getValue().trim().split("= ");
									for(String attri : arr) {								
											strQuery = replaceOf(strQuery,entry.getKey(),entry.getValue(),attri.trim().split(", "));	
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
