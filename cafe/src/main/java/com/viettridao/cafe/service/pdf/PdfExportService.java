package com.viettridao.cafe.service.pdf;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfExportService implements IPdfExportService{

    public byte[] exportInvoiceToPdf(InvoiceEntity invoice) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Header
        document.add(new Paragraph("🍽 CAFE MANAGEMENT")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph("🔖 HÓA ĐƠN THANH TOÁN").setBold().setFontSize(16).setMarginTop(10));
        document.add(new Paragraph("Mã hóa đơn: " + invoice.getId()));
        document.add(new Paragraph("Ngày tạo: " +
                invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        document.add(new Paragraph("\n"));

        // Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}))
                .useAllAvailableWidth();

        table.addHeaderCell("Tên món");
        table.addHeaderCell("Số lượng");
        table.addHeaderCell("Đơn giá (VNĐ)");
        table.addHeaderCell("Thành tiền (VNĐ)");

        DecimalFormat df = new DecimalFormat("#,###");

        for (InvoiceDetailEntity item : invoice.getInvoiceDetails()) {
            table.addCell(item.getMenuItem().getItemName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(df.format(item.getPrice()));
            table.addCell(df.format(item.getQuantity() * item.getPrice()));
        }

        document.add(table);
        document.add(new Paragraph("\nTổng cộng: " + df.format(invoice.getTotalAmount()) + " VNĐ").setBold());
        document.close();

        return out.toByteArray();
    }
}
