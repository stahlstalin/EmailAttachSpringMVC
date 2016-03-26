# EmailAttachSpringMVC
***
##Synopsis##
The main intention of the project is to generate excel file and send it as an attachment in a mail. 
The excel file thus attached in the mail, may or may not be present in the client/server file system.
This project also contains a user interface where user can type to-list, subject, body and browse for attachment for mail sending.
If the user chooses no attachment, then a sample excel sheet will be sent to the to-list.
The entire project is for educational purpose. One is free to make quality changes.

##Code Example##
###Excel Creation###
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

###Excel as attachment###
	MimeMessageHelper messageHelper = new MimeMessageHelper( mimeMessage, true, "UTF-8" );
	messageHelper.addAttachment("poi-test5.xls", new InputStreamSource() {
		@Override
		public InputStream getInputStream() throws IOException {
				return getExcelBytes().getInputStream();
			}
		},"application/vnd.ms-excel");
	}

###Mail Sending###
	MimeMessagePreparator mimeMessagePreparator = new MimeMessagePrep(); //Overloading OOPS concept
	mailSender.send(mimeMessagePreparator);
---	
##Motivation##
As part of a recent project requirement, a mailing action to occur where the filtered contents of the grid from dashboard to be sent as an attachment. The excel should be generated on the fly with the json content. The excel should not be downloaded in client file system prior to the mail send. This means browsing for the excel file to send the mail is not allowed because of infosec issue.

---
##Technology Stack##
- Spring MVC 4.2.1.RELEASE
- Apache POI 3.14
- JavaMail 1.4.7
- Log4j

##System Requirement##
* Java 1.7
* Maven 3.3.3
* Tomcat 8

---
##Installation##
https://github.com/stahlstalin/EmailAttachSpringMVC.git

cd EmailAttachSpringMVC

mvn clean install

Deploy the war file in the web container. (Tested in Tomcat)

---