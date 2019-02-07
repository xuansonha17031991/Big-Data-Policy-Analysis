package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadAttributes {

	public static HashMap<String, String> readText(String textFile) throws Exception {
		// read file attribute domains
		File textF = new File(textFile);
		Scanner scanner = new Scanner(textF);
		HashMap<String, String> value = new LinkedHashMap<>();
		while (scanner.hasNextLine()) {
			String temp = scanner.nextLine().toString();
			String[] arrStringAtributeDomains = temp.split("=>");// split string temp
			for (String gValueattriDomains : arrStringAtributeDomains) {
				value.put(arrStringAtributeDomains[0].trim(), arrStringAtributeDomains[1].trim());
			}
		}
		return value;
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

}
