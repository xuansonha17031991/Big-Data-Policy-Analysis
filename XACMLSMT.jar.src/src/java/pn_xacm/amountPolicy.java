package pn_xacm;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class amountPolicy {
	static int DenPolicyset = 1;


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		getPolicy();
	}

	public static void getPolicy() throws ParserConfigurationException, SAXException, IOException {
		int level = 1;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse("policies\\itrust3.xml");
		NodeList rootNodePolicySet = doc.getElementsByTagName("PolicySet");
		NodeList rootNodePolicy = doc.getElementsByTagName("Policy");
		NodeList rootNodeRule = doc.getElementsByTagName("Rule");
		Element elements = doc.getDocumentElement();
		System.out.println("Total of PolicySet : " + rootNodePolicySet.getLength());
		System.out.println("Total of Policy : " + rootNodePolicy.getLength());
		System.out.println("Total of Rule : " + rootNodeRule.getLength());

		NodeList nodeList = elements.getChildNodes();
		printNode(nodeList,level);
		System.out.println("The deepest level is : " + DenPolicyset);
	
	}

	private static void printNode(NodeList nodeList,int level) {

		level++;
		if (nodeList != null && nodeList.getLength() > 0) {

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if(node.getNodeName().equals("Policy")) {
						if (level > DenPolicyset) {
							DenPolicyset = level;
						}
					
					}
					if(node.getNodeName().equals("PolicySet")) {
						//level++;
						
						printNode(node.getChildNodes(),level);

					// how depth is it?
					if (level > DenPolicyset) {
						DenPolicyset = level;
					}
					
					}
					
					
				}
			}

		}

	}

}
