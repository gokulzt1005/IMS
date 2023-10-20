package com.example.demo.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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

@Service
public class ProductInventoryReportService  {

	
	
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
	    PdfPTable table = new PdfPTable(7);
	    table.setWidthPercentage(100);
	    table.setSpacingBefore(10f);
	    table.setSpacingAfter(10f);

	    // Define column headers
	    String[] headers = {
	        "Product ID", "Product Name", "Customer Name",
	        "Out Date", "Status", "Customer Mobile", "Prices"
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
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    // Populate data rows for "out" and "service" transactions
	    for (ProductInventoryEntity entity : findAll) {
	        java.util.Date outDate = entity.getOutDate();

	        table.addCell(entity.getProductId() != null ? entity.getProductId() : "null");
	        table.addCell(entity.getProductName() != null ? entity.getProductName() : "null");
	        table.addCell(entity.getCustomerName() != null ? entity.getCustomerName() : "null");

	        // Format outDate as a string using SimpleDateFormat
	        String formattedOutDate = outDate != null ? dateFormat.format(outDate) : "null";
	        table.addCell(formattedOutDate);

	        String transactionType = entity.getTransactionType();
	        
	        // Replace "out" with "sales" in the transaction type column
	        if ("out".equalsIgnoreCase(transactionType)) {
	            transactionType = "sales";
	        }
	        
	        table.addCell(transactionType != null ? transactionType : "null");
	        table.addCell(String.valueOf(entity.getCustomerMobile()));
	        table.addCell(String.valueOf(entity.getPrices()));
	    }

	    document.add(table);
	    document.close();
	    writer.close();
	}


	 
	 
	public static void generateExcel(HttpServletResponse response, List<ProductInventoryEntity> findAll) throws IOException {
        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet within the workbook
        Sheet sheet = workbook.createSheet("Inventory Report");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Product ID", "Product Name", "Customer Name",
            "Out Date", "Transaction Type", "Customer Mobile", "Prices", "Status"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // SimpleDateFormat for formatting java.sql.Date to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Populate data rows for "out" transactions only
        int rowNum = 1;
        for (ProductInventoryEntity entity : findAll) {
            if ("out".equalsIgnoreCase(entity.getTransactionType())) {
                java.util.Date outDate = entity.getOutDate();

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(entity.getProductId() != null ? entity.getProductId() : "null");
                row.createCell(1).setCellValue(entity.getProductName() != null ? entity.getProductName() : "null");
                row.createCell(2).setCellValue(entity.getCustomerName() != null ? entity.getCustomerName() : "null");

                // Format outDate as a string using SimpleDateFormat
                String formattedOutDate = outDate != null ? dateFormat.format(outDate) : "null";
                row.createCell(3).setCellValue(formattedOutDate);

                row.createCell(4).setCellValue(entity.getTransactionType() != null ? entity.getTransactionType() : "null");
                row.createCell(5).setCellValue(entity.getCustomerMobile());
                row.createCell(6).setCellValue(entity.getPrices());

                String saleStatus = entity.isIsdeleted() ? "Sale" : "Unsale";
                row.createCell(7).setCellValue(saleStatus);
            }
        }

        // Set response headers for Excel download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=inventory_report.xlsx");

        // Write the workbook to the response output stream
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        }
    }

    public static void main(String[] args) throws IOException {
        List<ProductInventoryEntity> findAll = getInventoryData();
        generateExcel(null, findAll);
    }

    // Replace this with your actual data retrieval logic
    private static List<ProductInventoryEntity> getInventoryData() {
        return List.of(
            new ProductInventoryEntity(),
            new ProductInventoryEntity()
        );
    }
	 
	
}