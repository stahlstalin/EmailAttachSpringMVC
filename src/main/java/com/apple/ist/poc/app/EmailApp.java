package com.apple.ist.poc.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
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
	 * Logger implementation 
	 * @see https://logging.apache.org/log4j/2.x/javadoc.html
	 */
	private static final Logger LOG = Logger.getLogger(EmailApp.class);
	
	/**
	 * Static inner class to hold the EmailApp instance
	 * @author stalinpratap.s
	 *
	 */
	private static class EmailAppSingletonHelper {
		
		private static final EmailApp INSTANCE = new EmailApp();

		private EmailAppSingletonHelper() {}
		
	}
	
	/**
	 * private constructor
	 */
	private EmailApp() {}
	
	/**
	 * Bill Pugh Singleton Implementation Design Pattern
	 * @return
	 */
	public static EmailApp getInstance() {
		return EmailAppSingletonHelper.INSTANCE;
	}

	/**
	 * This method creates an HSSFWorkbook instance
	 * @return
	 */
	private HSSFWorkbook createWorkbook() {
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
		
		return workbook;
	}
	
	/**
	 * main method - starting point of application execution
	 * @param args 
	 */
	public static void main(String[] args) {
		HSSFWorkbook workbook = EmailApp.getInstance().createWorkbook();
		
		try( FileOutputStream out =  new FileOutputStream(new File("poi-test.xls"));) {
	        workbook.write(out);
	        LOG.info("Excel written successfully..");
	         
	    } catch (IOException e1 ) {
	        LOG.error("Exception Occurred: "+e1.getCause(),e1);
	    }

	}

}
