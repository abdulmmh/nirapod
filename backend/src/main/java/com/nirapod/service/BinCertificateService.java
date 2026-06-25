package com.nirapod.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.nirapod.model.VatRegistration;
import com.nirapod.dao.VatRegistrationDAO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class BinCertificateService {

    @Autowired
    private VatRegistrationDAO vatRegistrationDAO;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd MMMM yyyy");

    // â”€â”€ Colours â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private static final Color NBR_BLUE   = new Color(26,  63,  143);
    private static final Color NBR_GREEN  = new Color(14,  120,  71);
    private static final Color LIGHT_GRAY = new Color(248, 250, 253);
    private static final Color BORDER     = new Color(232, 236, 240);

    // â”€â”€ Fonts â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private static final Font TITLE_FONT  = new Font(Font.HELVETICA, 18, Font.BOLD,   NBR_BLUE);
    private static final Font SUB_FONT    = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
    private static final Font LABEL_FONT  = new Font(Font.HELVETICA,  9, Font.BOLD,   Color.GRAY);
    private static final Font VALUE_FONT  = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);
    private static final Font BIN_FONT    = new Font(Font.HELVETICA, 22, Font.BOLD,   NBR_GREEN);
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA,  8, Font.ITALIC, Color.GRAY);


    public byte[] generateCertificate(Long vatRegistrationId) {
        VatRegistration vat = vatRegistrationDAO.findById(vatRegistrationId)
            .orElseThrow(() -> new RuntimeException(
                "VAT Registration not found: " + vatRegistrationId));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, out);
            doc.open();

            addHeader(doc);
            addTitle(doc);
            addBinHighlight(doc, vat);
            addDetailsTable(doc, vat);
            addQrCode(writer, vat);
            addFooter(doc);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("BIN certificate generation failed", e);
        }
    }

    // â”€â”€ Sections â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void addHeader(Document doc) throws DocumentException {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(NBR_BLUE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(16);

        Paragraph govLine = new Paragraph("People's Republic of Bangladesh",
            new Font(Font.HELVETICA, 10, Font.NORMAL, Color.WHITE));
        govLine.setAlignment(Element.ALIGN_CENTER);

        Paragraph nbrLine = new Paragraph("National Board of Revenue (NBR)",
            new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE));
        nbrLine.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(govLine);
        cell.addElement(nbrLine);
        header.addCell(cell);
        doc.add(header);
        doc.add(Chunk.NEWLINE);
    }

    private void addTitle(Document doc) throws DocumentException {
        Paragraph title = new Paragraph("BUSINESS IDENTIFICATION NUMBER (BIN) CERTIFICATE", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        doc.add(title);

        Paragraph sub = new Paragraph("VAT Registration Certificate issued under the VAT & SD Act", SUB_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        doc.add(sub);
    }

    private void addBinHighlight(Document doc, VatRegistration vat) throws DocumentException {
        PdfPTable box = new PdfPTable(1);
        box.setWidthPercentage(60);
        box.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(LIGHT_GRAY);
        cell.setBorderColor(NBR_GREEN);
        cell.setBorderWidth(2f);
        cell.setPadding(14);

        Paragraph lbl = new Paragraph("BIN NUMBER", LABEL_FONT);
        lbl.setAlignment(Element.ALIGN_CENTER);

        Paragraph bin = new Paragraph(vat.getBinNo(), BIN_FONT);
        bin.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(lbl);
        cell.addElement(bin);
        box.addCell(cell);

        doc.add(box);
        doc.add(Chunk.NEWLINE);
    }

    private void addDetailsTable(Document doc, VatRegistration vat) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{35f, 65f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setSpacingAfter(16);

        addRow(table, "Business Name",     vat.getBusinessName());
        addRow(table, "Owner / Rep. Name", vat.getOwnerName());
        addRow(table, "TIN Number",        vat.getTinNumber());
        addRow(table, "Trade License No.", vat.getTradeLicenseNo());
        addRow(table, "VAT Category",      vat.getVatCategory());
        addRow(table, "Registration Date", format(vat.getRegistrationDate()));
        addRow(table, "Effective Date",    format(vat.getEffectiveDate()));
        addRow(table, "Expiry Date", format(vat.getExpiryDate()));
        addRow(table, "VAT Zone",          vat.getVatZone());
        addRow(table, "VAT Circle",        vat.getVatCircle());
        addRow(table, "Address",           vat.getAddress());
        addRow(table, "Status",            vat.getStatus());
        addRow(table, "Issue Date",        LocalDate.now().format(FMT));

        doc.add(table);
    }

    private void addRow(PdfPTable table, String label, String value) {
        PdfPCell lbl = new PdfPCell(new Phrase(label, LABEL_FONT));
        lbl.setBackgroundColor(LIGHT_GRAY);
        lbl.setBorderColor(BORDER);
        lbl.setPadding(8);
        table.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value != null ? value : "â€”", VALUE_FONT));
        val.setBorderColor(BORDER);
        val.setPadding(8);
        table.addCell(val);
    }

    private void addQrCode(PdfWriter writer, VatRegistration vat) {
        try {
            com.google.zxing.qrcode.QRCodeWriter qrWriter =
                new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix matrix = qrWriter.encode(
                "BIN:" + vat.getBinNo(),
                com.google.zxing.BarcodeFormat.QR_CODE, 100, 100
            );

            java.awt.image.BufferedImage buffered =
                com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(matrix);

            com.lowagie.text.Image qrImg =
                com.lowagie.text.Image.getInstance(writer, buffered, 1f);
            qrImg.setAbsolutePosition(460, 60);
            qrImg.scaleToFit(80, 80);
            writer.getDirectContent().addImage(qrImg);

        } catch (Exception ignored) {
        }
    }

    private void addFooter(Document doc) throws DocumentException {
        Paragraph p = new Paragraph(
            "N.B: This is a system generated certificate and requires no manual signature.",
            FOOTER_FONT);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(10);
        doc.add(p);
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private String format(LocalDate date) {
        return date != null ? date.format(FMT) : "â€”";
    }
}
