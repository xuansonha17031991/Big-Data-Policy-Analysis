package pn_xacm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class read_Request {
	

	public static HashMap<String, String> readRequestTXT() throws FileNotFoundException {
		File textF = new File("E:/Source/source/XACMLSMT.jar.src/fileDomain.txt");
		Scanner scanner = new Scanner(textF);
		HashMap<String, String> value = new LinkedHashMap<>();
		while (scanner.hasNextLine()) {
			String temp = scanner.nextLine().toString();
			String[] arrStringAtributeDomains = temp.split("=>");// split string temp
			String getValueArry = arrStringAtributeDomains[1];
			String deletelast = getValueArry.substring(0, getValueArry.length() - 1);
			String deleteFirstly = deletelast.substring(1, deletelast.length());
			value.put(arrStringAtributeDomains[0].trim(), deleteFirstly.trim());
		}
		return value;
	}

	public static HashMap<String, String> readerRequestXML()
			throws ParserConfigurationException, SAXException, IOException {
		// read xml requets
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse("requests\\KMarket2_request.xml");
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
						mapRequest.put(idChildElement.trim(), valueChildElement.trim());
					}
				}

			}

		}
		
		return mapRequest;
	}

	/*
	 * private static List<String> createSetNew(Node rootNode, Element rootElement)
	 * { List<String> setNew = new ArrayList<String>(); if (rootNode.getNodeType()
	 * == Node.ELEMENT_NODE) { NodeList childNodeList = rootElement.getChildNodes();
	 * for (int j = 0; j < childNodeList.getLength(); j++) { Node childNode =
	 * childNodeList.item(j);
	 * 
	 * if (childNode.getNodeType() == Node.ELEMENT_NODE) { Element childElement =
	 * (Element) childNode; String idChildElement =
	 * childElement.getAttribute("AttributeId"); setNew.add(valueChildElement); } }
	 * 
	 * } return setNew; }
	 */

}
