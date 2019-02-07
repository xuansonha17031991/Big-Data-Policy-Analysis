package experiments;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class RequestConverter {

	public static void main(String[] args) {
		try {
			/* Read Domain Text File */
			//HashMap<String, String> mapDomain = DomainReader
			//		.readerDomain("C:\\Users\\DELL\\Desktop\\exampleRequest.txt");

			/* Read Request XML File */
			//HashMap<String, String> mapRequest = DomainReader.readerRequest("requests/request.permit.xml");
//
			// stringQuery = RequestConverter(mapDomain, mapRequest);
			//System.out.println(stringQuery);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static String RequestConverter(HashMap<String, String> mapDomain, HashMap<String, String> mapRequest) {
		HashMap<String, String> mapNegValue = new LinkedHashMap<>();

		for (Entry<String, String> domain : mapDomain.entrySet()) {
			for (Entry<String, String> request : mapRequest.entrySet()) {
				for (String domainValue : domain.getValue().trim().split(", ")) {
					if (domainValue.equals(request.getValue())) {
						String negValue = mapDomain.put(domain.getKey(), domain.getValue())
								.replaceAll(domainValue + ",", "");
						mapNegValue.put(domain.getKey(), negValue.trim());
					}
				}
			}
		}
		System.out.println("Map Domain: \n\t" + mapDomain);
		System.out.println("Map Request: \n\t" + mapRequest);
		System.out.println("------------------------------");
		String stringQuery = space(mapRequest, "P_1", mapNegValue);
		return stringQuery;
	}

	/**
	 * 
	 * @param mapRequest: Hashmap Request from Request File
	 * @param stringQuery: spaceValue (Ex: P_1, D_1,...)
	 * @param mapDomain: Hashmap Domain from Txt File
	 * @return
	 */
	private static String space(HashMap<String, String> mapRequest, String spaceValue,
			HashMap<String, String> mapDomain) {
		for (Entry<String, String> request : mapRequest.entrySet()) {
			for (Entry<String, String> domain : mapDomain.entrySet()) {
				if (request.getKey().equals(domain.getKey())) {
					String[] arrDomainValue = domain.getValue().trim().split("= ");
					for (String attri : arrDomainValue) {
						spaceValue = replaceOf(spaceValue, request.getKey(), request.getValue(),
								attri.trim().split(", "));
					}

				}
			}
		}
		return spaceValue;
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

}
