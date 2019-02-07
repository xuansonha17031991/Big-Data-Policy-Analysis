package pn_xacm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class updataPolicy {
	public static void readerRequestXML(String a,HashMap<String, ArrayList<String>> mapRequest)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// read xml requets
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(a);
		NodeList rootNodeList = doc.getElementsByTagName("xacml3:Match");

		
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

						gen(mapRequest, childElement, valueChildElement);

					}

				}

			}
			

			// write the content in xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(a));
			transformer.transform(source, result);
		}

	}

	private static void gen(HashMap<String, ArrayList<String>> mapRequest, Element childElement,
			String valueChildElement) {
		for (Entry<String, ArrayList<String>> iterable_element : mapRequest.entrySet()) {
			
			if (valueChildElement.equals("AccessControl.Student_data_layer.idStu")) {
				for (String iterable_element2 : iterable_element.getValue()) {

					childElement.setTextContent(iterable_element2);

					iterable_element.getValue().remove(0);
					break;
				}
				break;
			} else if (valueChildElement.equals("AccessControl.Lecturer_data_layer.idLec")) {
				for (String iterable_element2 : iterable_element.getValue()) {

					childElement.setTextContent(iterable_element2);

					iterable_element.getValue().remove(0);
					break;
				}
				break;
			} else if (valueChildElement.equals("AccessControl.Classes_data_layer.idClasses") ) {
				for (String iterable_element2 : iterable_element.getValue()) {
					
					childElement.setTextContent(iterable_element2);
					
					iterable_element.getValue().remove(0);
					break;
				}
				break;
			}

		}
	}
}
