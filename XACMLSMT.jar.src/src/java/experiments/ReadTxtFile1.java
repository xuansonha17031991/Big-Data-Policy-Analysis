package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class ReadTxtFile1 {

	public static void main(String[] args) throws Exception {
		readText("E:/Source/source/XACMLSMT.jar.src/filetxt.txt");
	}
	public static HashMap<String, String> readText (String textFile) throws Exception
	{
		// TODO Auto-generated method stub
		File textF = new File(textFile);
		Scanner scanner = new Scanner(textF);
		
		HashMap<String, String> value = new LinkedHashMap<>();	
		while (scanner.hasNextLine()) {
			String temp = scanner.nextLine().toString();
			
			String[] string = temp.split("=>");
			for(String s : string ) {
				value.put(string[0].trim(), string[1].trim());					
			}
		}
		return value;
	}
	

}
