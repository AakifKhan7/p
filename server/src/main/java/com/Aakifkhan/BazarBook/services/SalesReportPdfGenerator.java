package com.Aakifkhan.BazarBook.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.Aakifkhan.BazarBook.dto.sales.SalesResponse;

@Service
public class SalesReportPdfGenerator {

    private final TemplateEngine templateEngine;

    public SalesReportPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generate(List<SalesResponse> sales, LocalDate start, LocalDate end) {
        try {
            
            
            // Build context for Thymeleaf
            Context ctx = new Context();
            ctx.setVariable("sales", sales);
            ctx.setVariable("range", (start == null && end == null) ? "All Time" : String.format("%s – %s", start, end));
            ctx.setVariable("generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")));
            double grandTotal = 0;
            for (SalesResponse s : sales) {
                grandTotal += s.getPrice();
            }
            ctx.setVariable("grandTotal", String.format("%.2f", grandTotal));
            // Add shop name if sales exist
            String shopName = sales.isEmpty() ? "No Shop" : sales.get(0).getShopName();
            ctx.setVariable("shopName", shopName);
            
            
            String html = templateEngine.process("sales-report.html", ctx);
            
            
            
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(html, null);
                builder.toStream(baos);
                builder.run();
                byte[] result = baos.toByteArray();
                
                return result;
            } catch (IOException e) {
                System.err.println("PDF conversion failed: " + e.getMessage());
                throw new IllegalStateException("Failed to generate PDF", e);
            }
        } catch (Exception e) {
            System.err.println("PDF generation error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
