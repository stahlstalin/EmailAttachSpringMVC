package com.apple.ist.poc.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.apple.ist.poc.constant.ApplicationConstant;

/**
 * <pre>This controller file is responsible for:
 * <li> Generating the excel</li>
 * <li> Sending the mail</li>
 * </pre>
 * <p>An user interface is available for the user to mention the to-list, subject and attach a file and send it as an attachment in a mail.
 * If the user is not attaching any file, then a default excel file is generated at the server end and sent as an attachment.
 * The main intention here is to demonstrate and send the excel file as an attachment without browsing to the physical file for attachment. </p>
 * @author stalinpratap.s
 * @since 26/03/2016
 * @version 1.0
 */
@Controller
@RequestMapping(ApplicationConstant.SEND_MAIL_SERVICE)
public class SendEmailAttachController {
	
	/**
	 * Logger implementation 
	 * @see https://logging.apache.org/log4j/2.x/javadoc.html
	 */
	private static final Logger LOG = Logger.getLogger(SendEmailAttachController.class);

	/**
	 * <p>This bean is used for sending mail </p>
	 * @see {@link http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mail.html}
	 * {@linkplain org.springframework.mail.javamail.JavaMailSender} 
	 */
	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * This method is for excel creation and send file as attachments in the mail.  
	 * @param attachFile {@linkplain org.springframework.web.multipart.commons.CommonsMultipartFile}
	 * <p> This file <code>CommonsMultipartFile attachFile</code> is uploaded from the UI through browse button and form submitted </p>
	 * @param request Servlet request object
	 * @return String <p>This return string is used by the view resolver to redirect control to Result.jsp
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String sendEmail( @RequestParam final CommonsMultipartFile attachFile, HttpServletRequest request ) {

		// reads form input
		final String emailTo = request.getParameter("mailTo");
		final String subject = request.getParameter("subject");
		final String message = request.getParameter("message");

		// for logging
		LOG.info("emailTo: " + emailTo);
		LOG.info("subject: " + subject);
		LOG.info("message: " + message);
		LOG.info("attachFile: " + attachFile.getOriginalFilename());
		
		/**
		 * <p>This inner class extends MimeMessagePreparator and overwrites the @see org.springframework.mail.javamail.MimeMessagePreparator.prepare(MimeMessage mimeMessage) 
		 * for creating excel(if no attachment chosen from ui) and send the file in mail</p>
		 * @see http://docs.spring.io/autorepo/docs/spring-framework/2.5.x/api/org/springframework/mail/javamail/MimeMessagePreparator.html
		 * @author stalinpratap.s
		 */
		class MimeMessagePrep implements MimeMessagePreparator {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				/**
				 * @see org.springframework.mail.javamail.MimeMessageHelper
				 * @see http://docs.spring.io/autorepo/docs/spring-framework/2.5.x/api/org/springframework/mail/javamail/MimeMessageHelper.html
				 */
				MimeMessageHelper messageHelper = new MimeMessageHelper( mimeMessage, true, "UTF-8" );
				messageHelper.setFrom("XXXX@gmail.com");
				messageHelper.setValidateAddresses(true);
				messageHelper.setPriority(1); //Set the priority ("X-Priority" header) of the message. priority - the priority value; typically between 1 (highest) and 5 (lowest)
				messageHelper.setTo(emailTo);
				messageHelper.setSubject(subject);
				messageHelper.setText(message);
				
				String attachName = attachFile.getOriginalFilename();
				
				LOG.info(((attachFile != null) && (!attachName.isEmpty()))+" "+attachName);
				
				//Condition to check if there is an attachment chosen from UI as CommonsMultipartFile reference
				if ((attachFile != null) && (!attachName.isEmpty())) {
					
					LOG.info("File Item fieldname: "+attachFile.getFileItem().getFieldName());
					LOG.info("File Item ContentType: "+attachFile.getFileItem().getContentType());

					messageHelper.addAttachment(attachName, new InputStreamSource() {
						@Override
						public InputStream getInputStream() throws IOException {
							return attachFile.getInputStream();
						}
					});
				} else {
					//Thousand ways of Failure #Failed:1
					messageHelper.addAttachment("poi-test.xls", new InputStreamSource() {
						@SuppressWarnings("resource")
						@Override
						public InputStream getInputStream() throws IOException {
							HSSFWorkbook workbook = new HSSFWorkbook();
							HSSFSheet sheet = workbook.createSheet("Excel Report");
							HSSFRow rowhead = sheet.createRow((short)0);
							rowhead.createCell(0).setCellValue("No.");
							rowhead.createCell(1).setCellValue("Name");
							rowhead.createCell(2).setCellValue("Address");
							rowhead.createCell(3).setCellValue("Email");
							HSSFRow row1 = sheet.createRow((short)1);
							row1.createCell(0).setCellValue("1");
							row1.createCell(1).setCellValue("Stahl Stalin");
							row1.createCell(2).setCellValue("India");
							row1.createCell(3).setCellValue("stahl.stalin@gmail.com");
							
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							try {
							    workbook.write(bos);
							} finally {
							    bos.close();
							}
							byte[] content = bos.toByteArray();
//							byte[] content = workbook.getBytes(); //Invoking HSSFWorkbook.getBytes() does not return all of the data necessary to re- construct a complete Excel file.
							int size = content.length;
							InputStream is = null;
							byte[] b = new byte[size];
							try {
								is = new ByteArrayInputStream(content);
								is.read(b);
								LOG.info("Data Recovered: "+new String(b));
								LOG.info(is.available()+" InputStream toString"+is.toString());
							} catch (IOException e) {
								LOG.error(e.getMessage());
							} finally {
								try{
									if(is != null) {
										is.close();
									}
								} catch (IOException ex){
									LOG.error(ex.getMessage());
								}
							}
							return is;
						}
					},"application/vnd.ms-excel");
					
//					Thousand ways of Failure #Failed:1 (NOT RECOMMENDED)
					messageHelper.addAttachment("poi-test2.xls", new File("/Users/stalinpratap.s/Downloads/EmailAttachSpringMVC/poi-test.xls"));
					
//					Taste of Successs #Success:1 - @see https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/io/InputStreamSource.html
					ByteArrayResource resource = getExcelBytes();
					messageHelper.addAttachment("poi-test3.xls", resource,"application/vnd.ms-excel");
					
//					Thousand ways of Failure #Failed:2 
					ByteArrayResource resource2 = getExcelBytes2();
					messageHelper.addAttachment("poi-test4.xls", resource2,"application/vnd.ms-excel");
					
					messageHelper.addAttachment("poi-test5.xls", new InputStreamSource() {
						
						@Override
						public InputStream getInputStream() throws IOException {
							return getExcelBytes().getInputStream();
						}
					},"application/vnd.ms-excel");

				}
			}
		
		}
		
		MimeMessagePreparator mimeMessagePreparator = new MimeMessagePrep(); //Overloading OOPS concept

		mailSender.send(mimeMessagePreparator);

		return "Result";
	}
	
	/**
	 * This method creates a sample excel sheet using @see org.apache.poi.hssf.usermodel.HSSFWorkbook
	 * INFO:: This will successfully attach the excel sheet in the mail
	 * @return java.io.ByteArrayResource
	 */
	@SuppressWarnings("resource")
	private ByteArrayResource getExcelBytes() {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Excel Report");
		HSSFRow rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("No.");
		rowhead.createCell(1).setCellValue("Name");
		rowhead.createCell(2).setCellValue("Address");
		rowhead.createCell(3).setCellValue("Email");
		HSSFRow row1 = sheet.createRow((short)1);
		row1.createCell(0).setCellValue("1");
		row1.createCell(1).setCellValue("Stahl Stalin");
		row1.createCell(2).setCellValue("India");
		row1.createCell(3).setCellValue("stahl.stalin@gmail.com");
		HSSFRow row2 = sheet.createRow((short)2);
		row2.createCell(0).setCellValue("2");
		row2.createCell(1).setCellValue("Saubhagya Dey");
		row2.createCell(2).setCellValue("USA");
		row2.createCell(3).setCellValue("saubhagya.dey@gmail.com");
		HSSFRow row3 = sheet.createRow((short)3);
		row3.createCell(0).setCellValue("3");
		row3.createCell(1).setCellValue("Omkar Lenka");
		row3.createCell(2).setCellValue("India");
		row3.createCell(3).setCellValue("omkar.lenka@gmail.com");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
		    workbook.write(bos);
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		} finally {
		    try {
				bos.close();
			} catch (IOException ex) {
				LOG.error(ex.getMessage());
			}
		}
		byte[] content = bos.toByteArray();
//		byte[] content = workbook.getBytes(); //Invoking HSSFWorkbook.getBytes() does not return all of the data necessary to re- construct a complete Excel file.
		return new ByteArrayResource(content);
	
	}
	
	/**
	 * This method creates a sample excel sheet using @see org.apache.poi.hssf.usermodel.HSSFWorkbook
	 * INFO:: This will fail to attach the excel sheet
	 * @return ByteArrayResource
	 */
	@SuppressWarnings("resource")
	private ByteArrayResource getExcelBytes2() {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Excel Report");
		HSSFRow rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("No.");
		rowhead.createCell(1).setCellValue("Name");
		rowhead.createCell(2).setCellValue("Address");
		rowhead.createCell(3).setCellValue("Email");
		HSSFRow row1 = sheet.createRow((short)1);
		row1.createCell(0).setCellValue("1");
		row1.createCell(1).setCellValue("Stahl Stalin");
		row1.createCell(2).setCellValue("India");
		row1.createCell(3).setCellValue("stahl.stalin@gmail.com");
		
		byte[] content = workbook.getBytes(); 
		return new ByteArrayResource(content);
	
	}
	
}