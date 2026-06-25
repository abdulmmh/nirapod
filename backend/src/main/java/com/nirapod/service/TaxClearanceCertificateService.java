package com.nirapod.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.nirapod.model.TaxClearance;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TaxClearanceCertificateService {

    @Autowired private TaxClearanceService taxClearanceService;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public void generateCertificate(Long clearanceId, HttpServletResponse response) throws Exception {
        TaxClearance tc = taxClearanceService.getById(clearanceId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
            "attachment; filename=TaxClearance_" + tc.getCertificateNo() + ".pdf");

        Font titleFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font subFont    = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font boldFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font smallFont  = FontFactory.getFont(FontFactory.HELVETICA, 8);

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        // Border
        Rectangle rect = new Rectangle(30, 30, 565, 812);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(1.5f);
        canvas.rectangle(rect);

        // Watermark
        try {
            Image wm = Image.getInstance(new ClassPathResource("images/watermark.png").getURL());
            wm.scaleToFit(250, 250);
            wm.setAbsolutePosition(
                (PageSize.A4.getWidth()  - wm.getScaledWidth())  / 2,
                (PageSize.A4.getHeight() - wm.getScaledHeight()) / 2);
            canvas.saveState();
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.12f);
            canvas.setGState(gs);
            canvas.addImage(wm);
            canvas.restoreState();
        } catch (Exception ignored) {}

        // Logo
        try {
            Image logo = Image.getInstance(new ClassPathResource("images/govt_logo.png").getURL());
            logo.scaleToFit(60, 60);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception ignored) {}

        // Header
        addCentered(document, "Government of the People's Republic of Bangladesh", titleFont);
        addCentered(document, "National Board of Revenue", titleFont);
        document.add(Chunk.NEWLINE);
        addCentered(document, "TAX CLEARANCE CERTIFICATE", subFont);
        addCentered(document, "Certificate No: " + tc.getCertificateNo(), boldFont);
        document.add(new Paragraph(
            "________________________________________________________________________"));
        document.add(Chunk.NEWLINE);

        // Body
        Paragraph body = new Paragraph();
        body.add(new Chunk("This is to Certify that ", normalFont));
        body.add(new Chunk(tc.getTaxpayerName(), boldFont));
        body.add(new Chunk(", TIN: ", normalFont));
        body.add(new Chunk(tc.getTinNumber(), boldFont));
        body.add(new Chunk(
            ", has no outstanding tax liability for the assessment year ",
            normalFont));
        body.add(new Chunk(tc.getAssessmentYear(), boldFont));
        body.add(new Chunk(
            " and is issued this Tax Clearance Certificate accordingly.",
            normalFont));
        body.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(body);
        document.add(Chunk.NEWLINE);

        // Details table
        PdfPTable table = new PdfPTable(new float[]{40f, 60f});
        table.setWidthPercentage(100);
        addRow(table, "Certificate No.",   tc.getCertificateNo(),   boldFont, normalFont);
        addRow(table, "Taxpayer Name",     tc.getTaxpayerName(),    boldFont, normalFont);
        addRow(table, "TIN Number",        tc.getTinNumber(),       boldFont, normalFont);
        addRow(table, "Assessment Year",   tc.getAssessmentYear(),  boldFont, normalFont);
        addRow(table, "Issue Date",        fmt(tc.getIssuedDate()), boldFont, normalFont);
        addRow(table, "Valid Until",       fmt(tc.getValidUntil()), boldFont, normalFont);
        addRow(table, "Issued By",         tc.getIssuedBy() != null ? tc.getIssuedBy() : "NBR", boldFont, normalFont);
        addRow(table, "Status",            tc.getStatus(),          boldFont, normalFont);
        document.add(table);
        document.add(Chunk.NEWLINE);

        // QR + footer
        BufferedImage qrBuf = generateQR(
            "CertNo:" + tc.getCertificateNo() + ";TIN:" + tc.getTinNumber());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrBuf, "png", baos);
        Image qrImg = Image.getInstance(baos.toByteArray());
        qrImg.scaleToFit(90, 90);

        PdfPCell qrCell  = new PdfPCell(qrImg, false);
        qrCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell noteCell = new PdfPCell();
        noteCell.setBorder(Rectangle.NO_BORDER);
        noteCell.addElement(new Paragraph("Deputy Commissioner of Taxes", boldFont));
        noteCell.addElement(new Paragraph("National Board of Revenue, Dhaka", smallFont));
        noteCell.addElement(new Paragraph("Date: " + fmt(LocalDate.now()), smallFont));

        PdfPTable footer = new PdfPTable(2);
        footer.setWidthPercentage(100);
        footer.setWidths(new float[]{3f, 1f});
        footer.addCell(noteCell);
        footer.addCell(qrCell);
        document.add(footer);

        Paragraph sysNote = new Paragraph(
            "N.B: This is a system generated certificate and requires no manual signature.",
            smallFont);
        sysNote.setAlignment(Element.ALIGN_CENTER);
        document.add(sysNote);

        document.close();
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void addCentered(Document doc, String text, Font font) throws DocumentException {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
    }

    private void addRow(PdfPTable t, String label, String value, Font lf, Font vf) {
        PdfPCell lc = new PdfPCell(new Phrase(label, lf));
        lc.setPadding(7);
        t.addCell(lc);
        PdfPCell vc = new PdfPCell(new Phrase(value != null ? value : "â€”", vf));
        vc.setPadding(7);
        t.addCell(vc);
    }

    private String fmt(LocalDate d) {
        return d != null ? d.format(FMT) : "â€”";
    }

    private BufferedImage generateQR(String text) throws Exception {
        QRCodeWriter w = new QRCodeWriter();
        BitMatrix m = w.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        BufferedImage img = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 150; x++)
            for (int y = 0; y < 150; y++)
                img.setRGB(x, y, m.get(x, y) ? 0x000000 : 0xFFFFFF);
        return img;
    }
}
