package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.SalesReport;
import java.io.FileOutputStream;

public class PDFReportGenerator {
    public static String generateReport(SalesReport report,
                                     String fromDate, String toDate,
                                     String savePath) {
    try {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(savePath));
        document.open();

        // ─── Fonts ───────────────────────────────────────
        Font titleFont   = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(0, 51, 153));
        Font headerFont  = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(58, 125, 68));
        Font normalFont  = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont    = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font smallFont   = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

        // ─── Title ───────────────────────────────────────
        Paragraph title = new Paragraph("RE-WEAR THRIFT STORE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("SALES REPORT", headerFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // ─── Date Range ──────────────────────────────────
        Paragraph dateRange = new Paragraph(
            "Report Period: " + fromDate + " to " + toDate, normalFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a");
        Paragraph generated = new Paragraph(
            "Generated on: " + sdf.format(new java.util.Date()), smallFont);
        generated.setAlignment(Element.ALIGN_CENTER);
        generated.setSpacingAfter(30);
        document.add(generated);

        // ─── Separator ───────────────────────────────────
        document.add(new Paragraph("─────────────────────────────────────────────────"));

        // ─── Revenue Summary ─────────────────────────────
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("REVENUE SUMMARY", headerFont));
        document.add(new Paragraph("\n"));

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("en", "IN"));

        PdfPTable revenueTable = new PdfPTable(2);
        revenueTable.setWidthPercentage(80);
        revenueTable.setWidths(new float[]{2, 1});

        addRow(revenueTable, "Total Sales Revenue", "Rs " + nf.format(report.getTotalSales()), boldFont, normalFont);
        addRow(revenueTable, "Total Orders", String.valueOf(report.getTotalOrders()), boldFont, normalFont);
        addRow(revenueTable, "Average Order Value", "Rs " + nf.format(Math.round(report.getAverageOrderValue())), boldFont, normalFont);

        document.add(revenueTable);

        // ─── Separator ───────────────────────────────────
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("─────────────────────────────────────────────────"));

        // ─── Order Status Breakdown ──────────────────────
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("ORDER STATUS BREAKDOWN", headerFont));
        document.add(new Paragraph("\n"));

        PdfPTable statusTable = new PdfPTable(2);
        statusTable.setWidthPercentage(80);
        statusTable.setWidths(new float[]{2, 1});

        addRow(statusTable, "Delivered", String.valueOf(report.getDeliveredOrders()), normalFont, normalFont);
        addRow(statusTable, "Pending", String.valueOf(report.getPendingOrders()), normalFont, normalFont);
        addRow(statusTable, "Cancelled", String.valueOf(report.getCancelledOrders()), normalFont, normalFont);

        document.add(statusTable);

        // ─── Separator ───────────────────────────────────
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("─────────────────────────────────────────────────"));

        // ─── Top Insights ────────────────────────────────
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("TOP INSIGHTS", headerFont));
        document.add(new Paragraph("\n"));

        PdfPTable insightsTable = new PdfPTable(2);
        insightsTable.setWidthPercentage(80);
        insightsTable.setWidths(new float[]{2, 1});

        addRow(insightsTable, "Top Selling Product", report.getTopSellingProduct(), boldFont, normalFont);
        addRow(insightsTable, "Total Registered Users", String.valueOf(report.getTotalUsers()), normalFont, normalFont);

        document.add(insightsTable);

        // ─── Footer ─────────────────────────────────────
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("─────────────────────────────────────────────────"));
        Paragraph footer1 = new Paragraph("Generated by ReWear Admin Dashboard", smallFont);
        footer1.setAlignment(Element.ALIGN_CENTER);
        document.add(footer1);

        Paragraph footer2 = new Paragraph("Confidential - For internal use only", smallFont);
        footer2.setAlignment(Element.ALIGN_CENTER);
        document.add(footer2);

        document.close();
        return null; // success

    } catch (Exception e) {
        return "Failed to generate PDF: " + e.getMessage();
    }
}

private static void addRow(PdfPTable table, String label, String value,
                            Font labelFont, Font valueFont) {
    PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
    labelCell.setBorder(Rectangle.NO_BORDER);
    labelCell.setPadding(8);

    PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
    valueCell.setBorder(Rectangle.NO_BORDER);
    valueCell.setPadding(8);
    valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

    table.addCell(labelCell);
    table.addCell(valueCell);
}
}