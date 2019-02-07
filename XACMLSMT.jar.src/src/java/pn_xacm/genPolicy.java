package pn_xacm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.math3.geometry.partitioning.utilities.AVLTree.Node;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class genPolicy {
	 



	    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException, TransformerException {

	    	name2();
	    	

	    }
	    
	    public static HashMap<String, ArrayList<String>>  name() {
	    	 ArrayList<manager> listOfCustomer = new ArrayList<manager>();
	    	 HashMap<String, ArrayList<String>> map = new HashMap<>();
	    	try {
		    	 FileInputStream excelFile = new FileInputStream(new File("E:/Source/source/XACMLSMT.jar.src/policy.xlsx"));
		       
		    	 XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		      
		    	 Sheet datatypeSheet = workbook.getSheetAt(0);
		    	 DataFormatter fmt = new DataFormatter();
		         Iterator<Row> iterator = datatypeSheet.iterator();
		        
		         while (iterator.hasNext()) {
		           Row currentRow = iterator.next();
		           manager customer = new manager();
		           customer.setResouce1(currentRow.getCell(0).getStringCellValue());
		           customer.setResouce2(currentRow.getCell(1).getStringCellValue());

		           customer.setAcction3(currentRow.getCell(2).getStringCellValue());
		          customer.setReSource4(currentRow.getCell(3).getStringCellValue());
		           listOfCustomer.add(customer);
		         }
		         //
		         workbook.close();
		     
		      
		      ArrayList<String> list = new ArrayList<>();
		    
		      
		      for (manager manager : listOfCustomer) {
				
		    	  list.add(manager.getResouce2());
		    	  list.add(manager.getAcction3());
		    	  list.add(manager.getReSource4());
		    	  map.put(manager.getResouce1(), list);
			}
		    
		      
		       } catch (FileNotFoundException e) {
		         e.printStackTrace();
		       } catch (IOException e) {
		         e.printStackTrace();
		       }
			return map;
	    	
		     
		}

		
	    private static void name2() throws IOException, ParserConfigurationException, SAXException, TransformerException {
	    	System.out.print("Nhap  extend :");
			
	    	Scanner scanner2 = new Scanner(System.in);
	    	  HashMap<String, ArrayList<String>> mapRequest = name();
			 int n = scanner2.nextInt();
			for (int i = 0; i < n ; i++) {
				File textF = new File("E:\\Source\\source\\XACMLSMT.jar.src\\policies\\manage_teacher.xml");
				 Scanner scanner = new Scanner(textF);
				 String name = "E:\\Source\\source\\XACMLSMT.jar.src\\policies\\policyManager"+i+".xml";
				File file = new File(name);
		         
		         
		         PrintWriter outputStream = new PrintWriter(file);
		      
		         while (scanner.hasNextLine()) {
		         
		        	 String values = scanner.nextLine().trim();
		        	 
		        	 outputStream.println(values);
		         }
		         
		        
		         outputStream.close();
		       
		         updataPolicy.readerRequestXML(name,mapRequest);
			}
	    	 
			
		}

		
}
	
	 
