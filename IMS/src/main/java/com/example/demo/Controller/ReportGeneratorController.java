package com.example.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.ProductInventoryEntity;
import com.example.demo.Repository.ProductInventoryRepository;
import com.example.demo.Service.ProductInventoryReportService;
import com.itextpdf.text.DocumentException;
import java.time.Month;

@RestController
@RequestMapping("/v1/api")
public class ReportGeneratorController {
	
	
	private final ProductInventoryReportService ReportService;
	
    private final ProductInventoryRepository productRepository;

    @Autowired
    public ReportGeneratorController(ProductInventoryReportService ReportService, ProductInventoryRepository productRepository) {
        this.ReportService = ReportService;
        this.productRepository = productRepository;
    }

  
    @GetMapping("/pdf")
    public void generateProductInventoryPdf(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            HttpServletResponse response) throws IOException, DocumentException {
    	
        Month reportMonth = Month.of(month);
        List<ProductInventoryEntity> filteredData = productRepository.findByYearAndMonth(year, month);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        String filename = "monthly_inventory_report_" + reportMonth + "_" + year + ".pdf";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        ReportService.generatePdf(response, filteredData);
    }
    

    @GetMapping("/excel")
    public void generateProductInventoryExcel(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            HttpServletResponse response) throws IOException {
        List<ProductInventoryEntity> filteredData = productRepository.findByYearAndMonth(year, month);
        ReportService.generateExcel(response, filteredData);
    }

	
}	
	
	