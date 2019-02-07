package utils;

public class StringUtils {

	public static String getShortName(String exId) {
		if (exId.lastIndexOf("#") == -1) {
		return exId
				.substring(exId.lastIndexOf(":") + 1, exId.length());
		}
		return exId.substring(exId.lastIndexOf("#") + 1, exId.length());
		
	}

}
