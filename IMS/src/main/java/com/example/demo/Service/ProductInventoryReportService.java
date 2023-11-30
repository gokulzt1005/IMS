package com.example.demo.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.OutputStream;
  

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.demo.Entity.ProductInventoryEntity;
import com.example.demo.Repository.ProductInventoryRepository;

@Service
public class ProductInventoryReportService  {

	
	@Autowired
	ProductInventoryRepository productInventoryRepository;
	
	public static void generatePdf(HttpServletResponse response, List<ProductInventoryEntity> findAll) throws IOException, DocumentException {
	    Document document = new Document();
	    PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

	    document.open();

	    // Add a title to the document
	    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
	    Paragraph title = new Paragraph("Impression Solutions – High End laptop store", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    document.add(title);

	    // Create a table with 7 columns (excluding Date)
	    PdfPTable table = new PdfPTable(12);
	    table.setWidthPercentage(110);
	    table.setSpacingBefore(10f);
	    table.setSpacingAfter(10f);

	    // Define column headers
	    String[] headers = {
		        "Serial Number", "Model Name","Model Type","Processor","Ram","Display size", "Customer Name",
		        "Out Date","In Date", "Customer Mobile", "Price","Status"
		    };

	    // Add headers to the table
	    for (String header : headers) {
	        PdfPCell headerCell = new PdfPCell();
	        headerCell.addElement(new Paragraph(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));
	        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        headerCell.setBackgroundColor(BaseColor.GREEN);
	        table.addCell(headerCell);
	    }

	    // SimpleDateFormat for formatting java.sql.Date to a string
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    // Populate data rows for "out" and "service" transactions
	    for (ProductInventoryEntity entity : findAll) {
	        LocalDate outDate = entity.getOutDate();
	        LocalDate InDate = entity.getInDate();

	        table.addCell(entity.getProductId() != null ? entity.getProductId() : "null");
	        table.addCell(entity.getProductName() != null ? entity.getProductName() : "null");
	        table.addCell(entity.getProductType() != null ? entity.getProductType() : "null");
	        table.addCell(entity.getProcessorName() != null ? entity.getProcessorName() : "null");
	        table.addCell(entity.getRam() != null ? entity.getRam() : "null");
	        table.addCell(entity.getExpandableMemory() != null ? entity.getExpandableMemory() : "null");
	        table.addCell(entity.getCustomerName() != null ? entity.getCustomerName() : "null");

	        String formattedOutDate = outDate != null ? outDate.format(dateFormatter) : "null";
	        table.addCell(formattedOutDate);

	        String formattedInDate = InDate != null ? InDate.format(dateFormatter) : "null";
	        table.addCell(formattedInDate);

//	        String transactionType = entity.getTransactionType();
	        	        
	        table.addCell(String.valueOf(entity.getCustomerMobile()));
	        table.addCell(String.valueOf(entity.getPrices()));
	        
	        String transactionType = entity.getTransactionType();
	        if ("out".equalsIgnoreCase(transactionType)) {
	            transactionType = "sales";
	        }
	        
	        table.addCell(transactionType != null ? transactionType : "null");

	    }

	    document.add(table);
	    document.close();
	    writer.close();
	}


	 
	 
	public static void generateExcel(HttpServletResponse response, List<ProductInventoryEntity> findAll) throws IOException {
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Product Inventory");

	    // Create a header row
	    Row headerRow = sheet.createRow(0);
	    String[] headers = {
		        "Serial Number", "Model Name","Model Type","Processor","Ram","Display size", "Customer Name",
		        "Out Date","In Date", "Customer Mobile", "Price","Status"
		    };

	    CellStyle headerCellStyle = workbook.createCellStyle();
	    headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerCellStyle.setFont(headerFont);

	    for (int i = 0; i < headers.length; i++) {
	        Cell cell = headerRow.createCell(i);
	        cell.setCellValue(headers[i]);
	        cell.setCellStyle(headerCellStyle);
	    }

	    // Populate data rows
	    int rowNum = 1;
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    for (ProductInventoryEntity entity : findAll) {
	        Row row = sheet.createRow(rowNum++);
	        LocalDate outDate = entity.getOutDate();
	        LocalDate InDate = entity.getInDate();

	        row.createCell(0).setCellValue(entity.getProductId() != null ? entity.getProductId() : "null");
	        row.createCell(1).setCellValue(entity.getProductName() != null ? entity.getProductName() : "null");
	        row.createCell(2).setCellValue(entity.getProductType() != null ? entity.getProductType() : "null");
	        row.createCell(3).setCellValue(entity.getProcessorName() != null ? entity.getProcessorName() : "null");
	        row.createCell(4).setCellValue(entity.getRam() != null ? entity.getRam() : "null");
	        row.createCell(5).setCellValue(entity.getExpandableMemory() != null ? entity.getExpandableMemory() : "null");
	        row.createCell(6).setCellValue(entity.getCustomerName() != null ? entity.getCustomerName() : "null");

	        String formattedOutDate = outDate != null ? dateFormatter.format(outDate) : "null";
	        row.createCell(7).setCellValue(formattedOutDate);
	        
	        String formattedInDate = InDate != null ? dateFormatter.format(InDate) : "null";
	        row.createCell(8).setCellValue(formattedInDate);

	        row.createCell(9).setCellValue(String.valueOf(entity.getCustomerMobile()));
	        row.createCell(10).setCellValue(String.valueOf(entity.getPrices()));

	        String transactionType = entity.getTransactionType();
	        if ("out".equalsIgnoreCase(transactionType)) {
	            transactionType = "sales";
	        }
	        row.createCell(11).setCellValue(transactionType != null ? transactionType : "null");
	    }

	    // Auto-size columns
	    for (int i = 0; i < headers.length; i++) {
	        sheet.autoSizeColumn(i);
	    }

	    // Set the content type and headers for the response
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=ProductInventoryReport.xlsx");

	    // Write the workbook to the response output stream
	    try (OutputStream outputStream = response.getOutputStream()) {
	        workbook.write(outputStream);
	        outputStream.close();
	    }
	}

//    public static void main(String[] args) throws IOException {
//        List<ProductInventoryEntity> findAll = getInventoryData();
//        generateExcel(null, findAll);
//    }
//
//    // Replace this with your actual data retrieval logic
//    private static List<ProductInventoryEntity> getInventoryData() {
//    	productInventoryRepository.findall();
//    }
	 
	
}