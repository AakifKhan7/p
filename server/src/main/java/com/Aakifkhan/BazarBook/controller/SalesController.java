package com.Aakifkhan.BazarBook.controller;

import java.util.List;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import com.Aakifkhan.BazarBook.dto.sales.SalesCreateRequest;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;
import com.Aakifkhan.BazarBook.services.SalesService;
import com.Aakifkhan.BazarBook.services.SalesReportPdfGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales")
@Validated
public class SalesController {

    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesReportPdfGenerator pdfGenerator;

    @PostMapping("/create")
    public ResponseEntity<SalesResponse> createSale(@Valid @RequestBody SalesCreateRequest request) {
        return ResponseEntity.ok(salesService.createSale(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesResponse>> listSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(salesService.listSales(startDate, endDate));
    }

    @GetMapping("/report/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint works!");
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<byte[]> downloadSalesPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            // Get real sales data for current user
            List<SalesResponse> data = salesService.listSales(startDate, endDate);
            byte[] pdf = pdfGenerator.generate(data, startDate, endDate);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("sales_report.pdf")
                    .build());
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("PDF generation failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PDF generation failed".getBytes());
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSale(id);
        return ResponseEntity.ok().build();
    }
}
