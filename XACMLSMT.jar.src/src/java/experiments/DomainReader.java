package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class DomainReader {

	private static Scanner scanner;

	public static void main(String[] args) throws Exception {
		// HashMap<String, String> mapDomain =
		// DomainReader.readerDomain("C:\\Users\\DELL\\Desktop\\exampleRequest.txt");
	}

//	public static ArrayList<String> getDomainValue(HashMap<String, String> mapDomain) {
//		ArrayList<String> listDomainValue = new ArrayList<>();
//		/* Get ID, Value from HashMap */
//
//		return listDomainValue;
//	}

	public static HashMap<String, String> readerDomain(String textFileLocation) throws FileNotFoundException {
		File textFile = new File(textFileLocation);
		scanner = new Scanner(textFile);

		HashMap<String, String> mapDomain = new LinkedHashMap<>();

		while (scanner.hasNextLine()) {

			/* temp: save all value from text file */
			String temp = scanner.nextLine().toString();

			/* Read value */
			String[] arrayDomain = temp.split("=>");

			for (String string : arrayDomain) {
				mapDomain.put(arrayDomain[0].trim(), arrayDomain[1].trim());
			}
		}
		return mapDomain;
	}

//	public static ArrayList<String> getRequestValue(HashMap<String, String> mapRequest) {
//		ArrayList<String> listRequestValue = new ArrayList<String>();
//
//		/* Get Value from HashMap Request */
//		for (String valueRequest : mapRequest.values()) {
//			listRequestValue.add(valueRequest);
//		}
//		return listRequestValue;
//	}

	public static HashMap<String, String> readerRequest(String fXmlFile)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// System.out.println("Root element :" +
		// doc.getDocumentElement().getNodeName());

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

						/* Add to HashMap */
						mapRequest.put(idChildElement, valueChildElement);
					}
				}
			}
		}
		return mapRequest;
	}
}
