package com.nirapod.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.nirapod.dao.AitRecordDAO;
import com.nirapod.dao.AitCreditLedgerDAO;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.AitRecord;
import com.nirapod.model.AitCreditLedger;
import com.nirapod.model.Taxpayer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class AitCertificateService {

  @Autowired
  private AitRecordDAO aitRecordDAO;
  @Autowired
  private AitCreditLedgerDAO creditLedgerDAO;

  // â”€â”€ Brand colors (NBR green palette) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  private static final Color NBR_DARK = new Color(0, 77, 45); // #004D2D
  private static final Color NBR_MID = new Color(0, 120, 68); // #007844
  private static final Color NBR_LIGHT = new Color(232, 247, 239); // #E8F7EF
  private static final Color GOLD = new Color(180, 140, 0); // #B48C00
  private static final Color BORDER_GRAY = new Color(200, 210, 205);

  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

  // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public void generateCertificate(Long aitRecordId, HttpServletResponse response)
      throws Exception {

    // 1. Load AIT record
    AitRecord record = aitRecordDAO.getById(aitRecordId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "AIT record not found: " + aitRecordId));

    if (!"CREDITED".equals(record.getStatus())) {
      throw new IllegalStateException(
          "Certificate can only be issued for CREDITED records. " +
              "Current status: " + record.getStatus());
    }

    // 2. Load credit ledger entry (for approved amount)
    Optional<AitCreditLedger> ledgerOpt = creditLedgerDAO
        .getAvailableByTaxpayerId(record.getTaxpayer().getId())
        .stream()
        .filter(l -> l.getAitRecord().getId().equals(aitRecordId))
        .findFirst();

    // Fall back to all ledger entries if not in available list
    BigDecimal creditedAmount = ledgerOpt
        .map(AitCreditLedger::getCreditedAmount)
        .orElse(record.getApprovedAitAmount() != null
            ? record.getApprovedAitAmount()
            : record.getCalculatedAitAmount());

    // 3. Taxpayer info
    Taxpayer tp = record.getTaxpayer();
    String name = resolveDisplayName(tp);
    String tin = tp.getTinNumber() != null ? tp.getTinNumber() : "N/A";

    // 4. Fiscal year & source
    String fiscalYear = record.getFiscalYear() != null
        ? record.getFiscalYear().getYearName()
        : "N/A";
    String sourceType = formatSourceType(record.getSourceType());
    String refNo = record.getAitReferenceNo();
    String challanNo = record.getChallanNumber() != null
        ? record.getChallanNumber()
        : "N/A";
    String bankName = record.getBankName() != null
        ? record.getBankName()
        : "N/A";
    String taxableVal = formatBDT(record.getTaxableValue());
    String aitRate = record.getAitRate() != null
        ? record.getAitRate().stripTrailingZeros().toPlainString() + "%"
        : "N/A";
    String creditedAmt = formatBDT(creditedAmount);
    String officerName = record.getAssignedOfficer() != null
        ? record.getAssignedOfficer().getFullName()
        : "Deputy Commissioner of Taxes";
    String issueDate = LocalDate.now().format(DATE_FMT);

    // 5. Set HTTP response headers
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition",
        "inline; filename=\"AIT-Certificate-" + refNo + ".pdf\"");

    // 6. Build PDF
    Document document = new Document(PageSize.A4, 40, 40, 35, 35);
    PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
    document.open();
    PdfContentByte canvas = writer.getDirectContent();

    // â”€â”€ Outer border â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    drawOuterBorder(canvas);

    // â”€â”€ Watermark â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addWatermark(canvas);

    // â”€â”€ Header band (green) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addHeaderBand(document, canvas, writer);

    // â”€â”€ Government logo + title block â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addTitleBlock(document);

    // â”€â”€ Gold divider line â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addGoldDivider(document, canvas, writer);

    // â”€â”€ Certificate heading â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addCertificateHeading(document);

    // â”€â”€ Reference & date bar â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addRefBar(document, refNo, issueDate);

    // â”€â”€ Certified text paragraph â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addCertifiedParagraph(document, name, fiscalYear, sourceType, creditedAmt);

    document.add(Chunk.NEWLINE);

    // â”€â”€ Details table â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addDetailsTable(document, name, tin, fiscalYear, sourceType,
        taxableVal, aitRate, creditedAmt, challanNo, bankName, issueDate);

    document.add(Chunk.NEWLINE);

    // â”€â”€ Footer: notes | signature | QR â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addFooter(document, refNo, tin, name, creditedAmt, fiscalYear,
        officerName, record);

    // â”€â”€ System note â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    addSystemNote(document);

    document.close();
  }

  // =========================================================================
  // PRIVATE DRAWING METHODS
  // =========================================================================

  private void drawOuterBorder(PdfContentByte canvas) {
    // Double-border effect: outer thin + inner thick
    canvas.setColorStroke(new Color(NBR_DARK.getRed(),
        NBR_DARK.getGreen(), NBR_DARK.getBlue()));
    canvas.setLineWidth(1f);
    canvas.rectangle(25, 25, 545, 792);
    canvas.stroke();

    canvas.setColorStroke(GOLD);
    canvas.setLineWidth(2.5f);
    canvas.rectangle(32, 32, 531, 778);
    canvas.stroke();
  }

  private void addWatermark(PdfContentByte canvas) {
    try {
      Image watermark = Image.getInstance(
          new ClassPathResource("images/watermark.png").getURL());
      watermark.scaleToFit(260, 260);
      float x = (PageSize.A4.getWidth() - watermark.getScaledWidth()) / 2;
      float y = (PageSize.A4.getHeight() - watermark.getScaledHeight()) / 2;
      watermark.setAbsolutePosition(x, y);
      canvas.saveState();
      PdfGState gs = new PdfGState();
      gs.setFillOpacity(0.07f);
      canvas.setGState(gs);
      canvas.addImage(watermark);
      canvas.restoreState();
    } catch (Exception ignored) {
    }
  }

  private void addHeaderBand(Document document, PdfContentByte canvas, PdfWriter writer)
      throws Exception {
    // Full-width green band at the top (absolute position)
    canvas.saveState();
    canvas.setColorFill(NBR_DARK);
    canvas.rectangle(32, 780, 531, 30);
    canvas.fill();
    canvas.restoreState();

    // "NATIONAL BOARD OF REVENUE" text on the band
    Font bandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
        new Phrase("NATIONAL BOARD OF REVENUE â€” GOVERNMENT OF THE PEOPLE'S REPUBLIC OF BANGLADESH", bandFont),
        PageSize.A4.getWidth() / 2, 789, 0);
  }

  private void addTitleBlock(Document document) throws Exception {
    document.add(new Paragraph(" ")); // space below header band

    Font govFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, NBR_DARK);
    Font nbrFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, NBR_DARK);
    Font deptFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(80, 80, 80));

    try {
      Image logo = Image.getInstance(
          new ClassPathResource("images/govt_logo.png").getURL());
      logo.scaleToFit(53, 53);
      logo.setAlignment(Element.ALIGN_CENTER);
      document.add(logo);
    } catch (Exception ignored) {
    }

    Paragraph gov = new Paragraph("Government of the People's Republic of Bangladesh", govFont);
    gov.setAlignment(Element.ALIGN_CENTER);
    gov.setSpacingBefore(4);
    document.add(gov);

    Paragraph nbr = new Paragraph("National Board of Revenue", nbrFont);
    nbr.setAlignment(Element.ALIGN_CENTER);
    document.add(nbr);

    Paragraph dept = new Paragraph("Taxes Division, Segunbagicha, Dhaka-1000", deptFont);
    dept.setAlignment(Element.ALIGN_CENTER);
    document.add(dept);
  }

  private void addGoldDivider(Document document, PdfContentByte canvas, PdfWriter writer)
      throws Exception {
    document.add(new Paragraph(" "));
    // We use a thin colored table row as divider (works with document flow)
    PdfPTable divider = new PdfPTable(1);
    divider.setWidthPercentage(90);
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(GOLD);
    cell.setFixedHeight(2f);
    cell.setBorder(Rectangle.NO_BORDER);
    divider.addCell(cell);
    document.add(divider);
    document.add(new Paragraph(" "));
  }

  private void addCertificateHeading(Document document) throws DocumentException {
    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, NBR_DARK);
    Font subHead = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(60, 60, 60));

    Paragraph head = new Paragraph("ADVANCE INCOME TAX (AIT) CREDIT CERTIFICATE", headFont);
    head.setAlignment(Element.ALIGN_CENTER);
    head.setSpacingAfter(4);
    document.add(head);

    Paragraph sub = new Paragraph(
        "Issued under the Income Tax Ordinance, 1984 (Section 82C)", subHead);
    sub.setAlignment(Element.ALIGN_CENTER);
    document.add(sub);

    document.add(new Paragraph(" "));
  }

  private void addRefBar(Document document, String refNo, String issueDate)
      throws DocumentException {
    Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
    Font valFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

    PdfPTable bar = new PdfPTable(new float[] { 1f, 1f });
    bar.setWidthPercentage(90);
    bar.setSpacingAfter(10);

    PdfPCell leftCell = new PdfPCell();
    leftCell.setBackgroundColor(NBR_MID);
    leftCell.setBorder(Rectangle.NO_BORDER);
    leftCell.setPadding(8);
    leftCell.addElement(new Phrase("Certificate Reference No.", labelFont));
    leftCell.addElement(new Phrase(refNo, valFont));
    bar.addCell(leftCell);

    PdfPCell rightCell = new PdfPCell();
    rightCell.setBackgroundColor(NBR_DARK);
    rightCell.setBorder(Rectangle.NO_BORDER);
    rightCell.setPadding(8);
    rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    rightCell.addElement(new Phrase("Date of Issue", labelFont));
    rightCell.addElement(new Phrase(issueDate, valFont));
    bar.addCell(rightCell);

    document.add(bar);
  }

  private void addCertifiedParagraph(Document document, String name,
      String fiscalYear, String sourceType, String creditedAmt)
      throws DocumentException {

    Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, NBR_DARK);

    Paragraph cert = new Paragraph();
    cert.setAlignment(Element.ALIGN_JUSTIFIED);
    cert.setSpacingBefore(4);
    cert.setSpacingAfter(4);
    cert.add(new Chunk("This is to certify that ", normalFont));
    cert.add(new Chunk(name, boldFont));
    cert.add(new Chunk(
        " has paid Advance Income Tax (AIT) at source during the fiscal year ", normalFont));
    cert.add(new Chunk(fiscalYear, boldFont));
    cert.add(new Chunk(" under the category of ", normalFont));
    cert.add(new Chunk(sourceType, boldFont));
    cert.add(new Chunk(
        ". The said AIT has been verified and credited by the National Board of Revenue. " +
            "The taxpayer is entitled to claim a credit of ",
        normalFont));
    cert.add(new Chunk("Tk. " + creditedAmt, boldFont));
    cert.add(new Chunk(
        " against their income tax liability for the relevant assessment year.", normalFont));
    document.add(cert);
  }

  private void addDetailsTable(Document document, String name, String tin,
      String fiscalYear, String sourceType, String taxableVal,
      String aitRate, String creditedAmt, String challanNo,
      String bankName, String issueDate) throws DocumentException {

    Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(50, 50, 50));
    Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    Font monoFont = FontFactory.getFont(FontFactory.COURIER, 10, NBR_DARK);

    PdfPTable table = new PdfPTable(new float[] { 2.2f, 3f, 2.2f, 3f });
    table.setWidthPercentage(92);
    table.setSpacingBefore(8);
    table.setSpacingAfter(8);

    // Table header row
    PdfPCell headerCell = new PdfPCell(
        new Phrase("Particulars of Advance Income Tax", labelFont));
    headerCell.setColspan(4);
    headerCell.setBackgroundColor(NBR_LIGHT);
    headerCell.setBorderColor(BORDER_GRAY);
    headerCell.setPadding(8);
    table.addCell(headerCell);

    // Rows
    addTableRow(table, "Taxpayer Name", name, "TIN Number", tin,
        labelFont, valueFont, monoFont, true);
    addTableRow(table, "Fiscal Year", fiscalYear, "Source Type", sourceType,
        labelFont, valueFont, monoFont, false);
    addTableRow(table, "Taxable Value", "Tk. " + taxableVal,
        "AIT Rate", aitRate,
        labelFont, valueFont, monoFont, true);
    addTableRow(table, "Challan Number", challanNo, "Bank Name", bankName,
        labelFont, valueFont, monoFont, false);

    // Credit amount â€” full-width highlighted row
    PdfPCell creditLabelCell = new PdfPCell(
        new Phrase("Credited AIT Amount (à§³)", labelFont));
    creditLabelCell.setColspan(2);
    creditLabelCell.setBackgroundColor(NBR_LIGHT);
    creditLabelCell.setBorderColor(BORDER_GRAY);
    creditLabelCell.setPadding(8);
    table.addCell(creditLabelCell);

    Font bigAmtFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, NBR_DARK);
    PdfPCell creditValCell = new PdfPCell(new Phrase("Tk. " + creditedAmt, bigAmtFont));
    creditValCell.setColspan(2);
    creditValCell.setBorderColor(BORDER_GRAY);
    creditValCell.setBackgroundColor(new Color(220, 245, 230));
    creditValCell.setPadding(8);
    table.addCell(creditValCell);

    document.add(table);
  }

  private void addTableRow(PdfPTable table, String l1, String v1,
      String l2, String v2, Font labelFont, Font valueFont,
      Font monoFont, boolean shaded) {
    Color bg = shaded ? new Color(248, 252, 250) : Color.WHITE;

    PdfPCell c1 = new PdfPCell(new Phrase(l1, labelFont));
    c1.setBackgroundColor(bg);
    c1.setBorderColor(BORDER_GRAY);
    c1.setPadding(7);
    table.addCell(c1);

    boolean isMono = v1.startsWith("TIN-") || v1.startsWith("AIT-")
        || v1.startsWith("CHAL") || v1.startsWith("CH-");
    PdfPCell c2 = new PdfPCell(new Phrase(v1, isMono ? monoFont : valueFont));
    c2.setBackgroundColor(bg);
    c2.setBorderColor(BORDER_GRAY);
    c2.setPadding(7);
    table.addCell(c2);

    PdfPCell c3 = new PdfPCell(new Phrase(l2, labelFont));
    c3.setBackgroundColor(bg);
    c3.setBorderColor(BORDER_GRAY);
    c3.setPadding(7);
    table.addCell(c3);

    boolean isMono2 = v2.startsWith("TIN-") || v2.startsWith("AIT-");
    PdfPCell c4 = new PdfPCell(new Phrase(v2, isMono2 ? monoFont : valueFont));
    c4.setBackgroundColor(bg);
    c4.setBorderColor(BORDER_GRAY);
    c4.setPadding(7);
    table.addCell(c4);
  }

  private void addFooter(Document document, String refNo, String tin,
      String name, String creditedAmt, String fiscalYear,
      String officerName, AitRecord record) throws Exception {

    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, NBR_DARK);
    Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(80, 80, 80));
    Font noteFont = FontFactory.getFont(FontFactory.HELVETICA, 8.5f);

    // Notes cell
    PdfPCell noteCell = new PdfPCell();
    noteCell.setBorder(Rectangle.NO_BORDER);
    noteCell.setPaddingRight(10);
    noteCell.addElement(new Phrase("Important Notes:", boldFont));
    noteCell.addElement(new Phrase(
        "1. This certificate is valid for assessment year " + fiscalYear + ".", noteFont));
    noteCell.addElement(new Phrase(
        "2. Present this certificate when claiming AIT credit in ITR.", noteFont));
    noteCell.addElement(new Phrase(
        "3. This credit is non-transferable and non-refundable once applied.", noteFont));
    noteCell.addElement(new Phrase(
        "4. Any misrepresentation is punishable under Income Tax Ordinance, 1984.", noteFont));

    // Signature cell
    PdfPCell signCell = new PdfPCell();
    signCell.setBorder(Rectangle.NO_BORDER);
    signCell.setPaddingLeft(10);
    signCell.setPaddingRight(10);
    signCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    signCell.addElement(new Paragraph("\n\n", smallFont));
    signCell.addElement(new Phrase("_________________________", smallFont));
    signCell.addElement(new Paragraph(officerName, boldFont));
    signCell.addElement(new Phrase("National Board of Revenue", smallFont));
    signCell.addElement(new Phrase("Dhaka, Bangladesh", smallFont));

    // QR cell
    String qrPayload = "AIT CERTIFICATE\n" +
        "Ref: " + refNo + "\n" +
        "TIN: " + tin + "\n" +
        "Name: " + name + "\n" +
        "FY: " + fiscalYear + "\n" +
        "Credit: Tk." + creditedAmt + "\n" +
        "Verify: nirapod.gov.bd/verify-ait?ref=" + refNo;

    PdfPCell qrCell = new PdfPCell();
    qrCell.setBorder(Rectangle.NO_BORDER);
    qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    try {
        BufferedImage qrBuf = generateQR(qrPayload, 150);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrBuf, "png", baos);
        Image qrImage = Image.getInstance(baos.toByteArray());
        qrImage.scaleToFit(90, 90);
        qrCell.addElement(qrImage);
    } catch (Exception e) {
        qrCell.addElement(new Phrase("QR unavailable", smallFont));
    }
    qrCell.addElement(new Phrase("Scan to verify", smallFont));

    PdfPTable footer = new PdfPTable(new float[] { 3f, 2f, 1.5f });
    footer.setWidthPercentage(92);
    footer.addCell(noteCell);
    footer.addCell(signCell);
    footer.addCell(qrCell);
    document.add(footer);
  }

  private void addSystemNote(Document document) throws DocumentException {
    Font smallFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8,
        new Color(120, 120, 120));
    Paragraph note = new Paragraph(
        "N.B: This is a system-generated certificate issued by the Nirapod Tax Platform. " +
            "No manual signature is required. Verify authenticity at nirapod.gov.bd/verify-ait",
        smallFont);
    note.setAlignment(Element.ALIGN_CENTER);
    note.setSpacingBefore(8);
    document.add(note);
  }

  // =========================================================================
  // HELPERS
  // =========================================================================

  private BufferedImage generateQR(String text, int size) throws Exception {
	    java.util.Map<com.google.zxing.EncodeHintType, Object> hints =
	        new java.util.HashMap<>();
	    hints.put(com.google.zxing.EncodeHintType.MARGIN, 1);
	    hints.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION,
	        com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);

	    QRCodeWriter writer = new QRCodeWriter();
	    BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);

	    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	    for (int x = 0; x < size; x++)
	        for (int y = 0; y < size; y++)
	            image.setRGB(x, y, matrix.get(x, y) ? 0x004D2D : 0xFFFFFF);
	    return image;
	}

  private String resolveDisplayName(Taxpayer tp) {
    if (tp.getCompanyName() != null && !tp.getCompanyName().isBlank())
      return tp.getCompanyName();
    if (tp.getFullName() != null && !tp.getFullName().isBlank())
      return tp.getFullName();
    return "N/A";
  }

  private String formatSourceType(String src) {
    if (src == null)
      return "N/A";
    return switch (src) {
      case "IMPORT" -> "Import Duty (Section 53)";
      case "SUPPLIER" -> "Supplier Payment (Section 52)";
      case "SALARY" -> "Salary Deduction (Section 50)";
      case "CONTRACTOR" -> "Contractor Payment (Section 52A)";
      case "RENT" -> "Rent Payment (Section 53A)";
      default -> src;
    };
  }

  private String formatBDT(BigDecimal amount) {
    if (amount == null)
      return "0.00";
    return String.format("%,.2f", amount);
  }
}
