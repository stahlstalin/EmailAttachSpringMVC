package com.apple.ist.poc.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This is a test application to create a sample excel sheet using apache poi api
 * {@link https://poi.apache.org/apidocs/}
 * @author stalinpratap.s
 * @version 1.0
 * @since 26/03/2016
 *
 */
public class EmailApp {
	
	/**
	 * main method - starting point of application execution
	 * @param args 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Excel Report");
		HSSFRow rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("No.");
		rowhead.createCell(1).setCellValue("Name");
		rowhead.createCell(2).setCellValue("Address");
		rowhead.createCell(3).setCellValue("Email");
		HSSFRow row = sheet.createRow((short)1);
		row.createCell(0).setCellValue("1");
		row.createCell(1).setCellValue("Stahl Stalin");
		row.createCell(2).setCellValue("India");
		row.createCell(3).setCellValue("stahl.stalin@gmail.com");
		
		try {
	        FileOutputStream out =  new FileOutputStream(new File("poi-test.xls"));
	        workbook.write(out);
	        out.close();
	        System.out.println("Excel written successfully..");
	         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}

}
