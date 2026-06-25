package com.nirapod.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;


import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.*;
import com.nirapod.dto.request.ReportExportRequest;
import com.nirapod.dto.response.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;


import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportExportService {

  private static final int EXPORT_LIMIT = 1000;


  private static final Color NBR_BLUE = new Color(26, 63, 143); // #1A3F8F
  private static final Color NBR_GREEN = new Color(14, 120, 71); // #0E7847
  private static final Color NBR_DARK = new Color(0, 77, 45); // #004D2D
  private static final Color GOLD = new Color(180, 140, 0); // #B48C00
  private static final Color LIGHT_GRAY = new Color(248, 250, 253);
  private static final Color BORDER = new Color(232, 236, 240);
  private static final Color LIGHT_GREEN = new Color(232, 247, 239);

  private static final Font TITLE_FONT = new Font(Font.HELVETICA, 16, Font.BOLD, NBR_BLUE);
  private static final Font HEAD_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
  private static final Font META_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, NBR_BLUE);
  private static final Font META_VAL = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.DARK_GRAY);
  private static final Font TH_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
  private static final Font TD_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
  private static final Font AMT_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, NBR_BLUE);
  private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY);

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
  private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

  private final VatReturnDAO vatReturnDAO;
  private final IncomeTaxReturnDAO itrDAO;
  private final PenaltyDAO penaltyDAO;
  private final RefundDAO refundDAO;
  private final TaxZoneDAO taxZoneDAO;

  public ReportExportService(VatReturnDAO vatReturnDAO,
      IncomeTaxReturnDAO itrDAO,
      PenaltyDAO penaltyDAO,
      RefundDAO refundDAO,
      TaxZoneDAO taxZoneDAO) {
    this.vatReturnDAO = vatReturnDAO;
    this.itrDAO = itrDAO;
    this.penaltyDAO = penaltyDAO;
    this.refundDAO = refundDAO;
    this.taxZoneDAO = taxZoneDAO;
  }

  // PDF EXPORT

  public byte[] exportAsPdf(ReportExportRequest req, AuthPrincipal principal) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // Landscape
      PdfWriter writer = PdfWriter.getInstance(doc, out);

      writer.setPageEvent(new PdfPageEventHelper() {
        @Override
        public void onEndPage(PdfWriter w, Document d) {
          ColumnText.showTextAligned(
              w.getDirectContent(), Element.ALIGN_CENTER,
              new Phrase("Page " + w.getPageNumber() + "  |  Nirapod Tax Platform  |  Confidential",
                  new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY)),
              d.getPageSize().getWidth() / 2, 20, 0);
        }
      });

      doc.open();
      PdfContentByte canvas = writer.getDirectContent();

      addPdfHeader(doc, canvas, writer, req);
      addPdfMeta(doc, req, principal);
      addPdfGoldDivider(doc);

      switch (req.getReportType()) {
        case "VAT_COLLECTION" -> addVatPdfTable(doc, req, principal);
        case "INCOME_TAX" -> addItrPdfTable(doc, req, principal);
        case "PENALTY_COLLECTION" -> addPenaltyPdfTable(doc, req, principal);
        case "REFUND_STATUS" -> addRefundPdfTable(doc, req, principal);
        default -> addUnsupportedNote(doc, req.getReportType());
      }

      addPdfFooter(doc, req, principal);
      doc.close();
      return out.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("PDF export failed: " + e.getMessage(), e);
    }
  }


  private void addPdfHeader(Document doc, PdfContentByte canvas,
      PdfWriter writer, ReportExportRequest req)
      throws Exception {

    canvas.saveState();
    canvas.setColorFill(NBR_BLUE);
    canvas.rectangle(36, doc.getPageSize().getHeight() - 60,
        doc.getPageSize().getWidth() - 72, 30);
    canvas.fill();
    canvas.restoreState();

    Font bandFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, Color.WHITE);
    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
        new Phrase("NATIONAL BOARD OF REVENUE - GOVERNMENT OF THE PEOPLE'S REPUBLIC OF BANGLADESH",
            bandFont),
        doc.getPageSize().getWidth() / 2,
        doc.getPageSize().getHeight() - 48, 0);

    doc.add(new Paragraph(" ")); 

    // Title
    String reportTitle = resolveReportTitle(req.getReportType());
    Paragraph title = new Paragraph(reportTitle, TITLE_FONT);
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(2);
    doc.add(title);

    Font subFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.DARK_GRAY);
    Paragraph sub = new Paragraph("Nirapod VAT & Tax Management System - Official Report", subFont);
    sub.setAlignment(Element.ALIGN_CENTER);
    sub.setSpacingAfter(8);
    doc.add(sub);
  }

  private void addPdfMeta(Document doc, ReportExportRequest req, AuthPrincipal principal)
      throws DocumentException {

    PdfPTable meta = new PdfPTable(new float[] { 1f, 1f, 1f, 1f });
    meta.setWidthPercentage(100);
    meta.setSpacingAfter(6);

    addMetaCell(meta, "Fiscal Year", req.getFiscalYear());
    addMetaCell(meta, "Zone", req.getZone() != null && !req.getZone().isBlank()
        ? req.getZone()
        : "All Zones");
    addMetaCell(meta, "Generated By", principal != null ? principal.getEmail() : "System");
    addMetaCell(meta, "Generated At", LocalDateTime.now().format(DT_FMT));

    doc.add(meta);
  }

  private void addMetaCell(PdfPTable table, String label, String value) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(LIGHT_GRAY);
    cell.setBorderColor(BORDER);
    cell.setPadding(6);
    cell.addElement(new Phrase(label, META_FONT));
    cell.addElement(new Phrase(value != null ? value : "-", META_VAL));
    table.addCell(cell);
  }

  private void addPdfGoldDivider(Document doc) throws DocumentException {
    PdfPTable div = new PdfPTable(1);
    div.setWidthPercentage(100);
    div.setSpacingAfter(8);
    PdfPCell c = new PdfPCell();
    c.setBackgroundColor(GOLD);
    c.setFixedHeight(1.5f);
    c.setBorder(Rectangle.NO_BORDER);
    div.addCell(c);
    doc.add(div);
  }

  private void addVatPdfTable(Document doc, ReportExportRequest req,
      AuthPrincipal principal) throws DocumentException {
    Page<VatCollectionRowDTO> page = vatReturnDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(),
            parseZoneId(req.getZone()), // ← Long — সঠিক
            emptyIfNull(req.getCircle()),
            emptyIfNull(req.getStatus()),
            null,
            PageRequest.of(0, EXPORT_LIMIT))
        .map(VatCollectionRowDTO::from);

    String[] headers = { "#", "BIN No", "Business Name", "Zone", "Circle",
        "Period", "Gross Supplies", "Output Tax", "Input Tax",
        "Net VAT", "Status", "Submitted At" };
    float[] widths = { 3, 8, 18, 8, 8, 8, 10, 10, 10, 10, 8, 9 };

    PdfPTable table = createPdfTable(headers, widths);

    int i = 1;
    double totalNetVat = 0;
    for (VatCollectionRowDTO r : page.getContent()) {
      boolean shade = (i % 2 == 0);
      addTd(table, String.valueOf(i++), shade, Element.ALIGN_CENTER);
      addTd(table, r.getBinNo(), shade, Element.ALIGN_LEFT);
      addTd(table, r.getBusinessName(), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getZone()), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getCircle()), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getPeriod()), shade, Element.ALIGN_LEFT);
      addAmtTd(table, r.getGrossSales(), shade);
      addAmtTd(table, r.getOutputVat(), shade);
      addAmtTd(table, r.getInputVatCredit(), shade);
      addAmtTd(table, r.getNetVat(), shade);
      addTd(table, r.getStatus(), shade, Element.ALIGN_CENTER);
      addTd(table, nvl(r.getSubmittedAt()), shade, Element.ALIGN_CENTER);
      if (r.getNetVat() != null)
        totalNetVat += r.getNetVat().doubleValue();
    }

    // Totals row
    addTotalRow(table, 12, 10, totalNetVat, "Total Net VAT");

    doc.add(table);
    addRecordCount(doc, page.getTotalElements());
  }


  private void addItrPdfTable(Document doc, ReportExportRequest req,
      AuthPrincipal principal) throws DocumentException {
    Page<IncomeTaxRowDTO> page = itrDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(),
            emptyIfNull(req.getStatus()),
            "", null,
            PageRequest.of(0, EXPORT_LIMIT))
        .map(IncomeTaxRowDTO::from);

    String[] headers = { "#", "TIN Number", "Taxpayer Name", "Category",
        "Assessment Year", "Gross Income", "Tax Payable",
        "Tax Paid", "Tax Due", "Status", "Filed At" };
    float[] widths = { 3, 9, 18, 9, 9, 11, 10, 10, 10, 8, 9 };

    PdfPTable table = createPdfTable(headers, widths);

    int i = 1;
    double totalPayable = 0, totalPaid = 0, totalDue = 0;
    for (IncomeTaxRowDTO r : page.getContent()) {
      boolean shade = (i % 2 == 0);
      addTd(table, String.valueOf(i++), shade, Element.ALIGN_CENTER);
      addTd(table, r.getTinNumber(), shade, Element.ALIGN_LEFT);
      addTd(table, r.getTaxpayerName(), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getTaxpayerType()), shade, Element.ALIGN_CENTER);
      addTd(table, nvl(r.getAssessmentYear()), shade, Element.ALIGN_CENTER);
      addAmtTd(table, r.getGrossIncome(), shade);
      addAmtTd(table, r.getTaxPayable(), shade);
      addAmtTd(table, r.getTaxPaid(), shade);
      addAmtTd(table, r.getTaxDue(), shade);
      addTd(table, r.getStatus(), shade, Element.ALIGN_CENTER);
      addTd(table, nvl(r.getFiledAt()), shade, Element.ALIGN_CENTER);
      if (r.getTaxPayable() != null)
        totalPayable += r.getTaxPayable().doubleValue();
      if (r.getTaxPaid() != null)
        totalPaid += r.getTaxPaid().doubleValue();
      if (r.getTaxDue() != null)
        totalDue += r.getTaxDue().doubleValue();
    }

    addTotalRow(table, 11, 8, totalPayable, "Total Tax Payable");

    doc.add(table);
    addRecordCount(doc, page.getTotalElements());
  }


  private void addPenaltyPdfTable(Document doc, ReportExportRequest req,
      AuthPrincipal principal) throws DocumentException {
    Page<PenaltyRowDTO> page = penaltyDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(), "", "",
            null, PageRequest.of(0, EXPORT_LIMIT))
        .map(PenaltyRowDTO::from);

    String[] headers = { "#", "Penalty No", "Taxpayer", "TIN",
        "Type", "Severity", "Base Amount",
        "Interest", "Total", "Status", "Issued At" };
    float[] widths = { 3, 10, 16, 10, 10, 8, 10, 9, 10, 8, 9 };

    PdfPTable table = createPdfTable(headers, widths);

    int i = 1;
    double totalAmt = 0;
    for (PenaltyRowDTO r : page.getContent()) {
      boolean shade = (i % 2 == 0);
      addTd(table, String.valueOf(i++), shade, Element.ALIGN_CENTER);
      addTd(table, r.getPenaltyRefNo(), shade, Element.ALIGN_LEFT);
      addTd(table, r.getTaxpayerName(), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getTinNumber()), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getPenaltyType()), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getSeverity()), shade, Element.ALIGN_CENTER);
      addAmtTd(table, r.getBaseAmount(), shade);
      addAmtTd(table, r.getInterestAmount(), shade);
      addAmtTd(table, r.getTotalAmount(), shade);
      addTd(table, r.getStatus(), shade, Element.ALIGN_CENTER);
      addTd(table, nvl(r.getIssuedAt()), shade, Element.ALIGN_CENTER);
      if (r.getTotalAmount() != null)
        totalAmt += r.getTotalAmount().doubleValue();
    }

    addTotalRow(table, 11, 9, totalAmt, "Total Penalty Collected");

    doc.add(table);
    addRecordCount(doc, page.getTotalElements());
  }


  private void addRefundPdfTable(Document doc, ReportExportRequest req,
      AuthPrincipal principal) throws DocumentException {
    Page<RefundRowDTO> page = refundDAO
        .findByFiscalYearNameAndFilters(
            req.getFiscalYear(), "",
            null, PageRequest.of(0, EXPORT_LIMIT))
        .map(RefundRowDTO::from);

    String[] headers = { "#", "Reference No", "Taxpayer", "TIN",
        "Type", "Claimed", "Approved", "Paid", "Status", "Submitted" };
    float[] widths = { 3, 12, 16, 10, 10, 11, 11, 11, 8, 10 };

    PdfPTable table = createPdfTable(headers, widths);

    int i = 1;
    double totalApproved = 0;
    for (RefundRowDTO r : page.getContent()) {
      boolean shade = (i % 2 == 0);
      addTd(table, String.valueOf(i++), shade, Element.ALIGN_CENTER);
      addTd(table, r.getRefundRefNo(), shade, Element.ALIGN_LEFT);
      addTd(table, r.getTaxpayerName(), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getTinNumber()), shade, Element.ALIGN_LEFT);
      addTd(table, nvl(r.getRefundType()), shade, Element.ALIGN_LEFT);
      addAmtTd(table, r.getClaimedAmount(), shade);
      addAmtTd(table, r.getApprovedAmount(), shade);
      addAmtTd(table, r.getPaidAmount(), shade);
      addTd(table, r.getStatus(), shade, Element.ALIGN_CENTER);
      addTd(table, nvl(r.getSubmittedAt()), shade, Element.ALIGN_CENTER);
      if (r.getApprovedAmount() != null)
        totalApproved += r.getApprovedAmount().doubleValue();
    }

    addTotalRow(table, 10, 7, totalApproved, "Total Approved Refund");

    doc.add(table);
    addRecordCount(doc, page.getTotalElements());
  }


  private PdfPTable createPdfTable(String[] headers, float[] widths)
      throws DocumentException {
    PdfPTable table = new PdfPTable(headers.length);
    table.setWidthPercentage(100);
    table.setWidths(widths);
    table.setSpacingBefore(4);
    table.setHeaderRows(1);

    for (String h : headers) {
      PdfPCell cell = new PdfPCell(new Phrase(h, TH_FONT));
      cell.setBackgroundColor(NBR_BLUE);
      cell.setBorderColor(BORDER);
      cell.setPadding(5);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cell);
    }
    return table;
  }

  private void addTd(PdfPTable t, String val, boolean shade, int align) {
    PdfPCell cell = new PdfPCell(new Phrase(val != null ? val : "-", TD_FONT));
    cell.setBackgroundColor(shade ? LIGHT_GRAY : Color.WHITE);
    cell.setBorderColor(BORDER);
    cell.setPadding(4);
    cell.setHorizontalAlignment(align);
    t.addCell(cell);
  }

  private void addAmtTd(PdfPTable t, Object val, boolean shade) {
    String txt = "-";
    if (val instanceof java.math.BigDecimal bd) {
      txt = String.format("%,.2f", bd);
    } else if (val instanceof Double d) {
      txt = String.format("%,.2f", d);
    }
    PdfPCell cell = new PdfPCell(new Phrase(txt, AMT_FONT));
    cell.setBackgroundColor(shade ? LIGHT_GRAY : Color.WHITE);
    cell.setBorderColor(BORDER);
    cell.setPadding(4);
    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    t.addCell(cell);
  }

  private void addTotalRow(PdfPTable table, int cols, int amtColIndex,
      double totalValue, String label) {
    for (int c = 0; c < cols; c++) {
      String txt;
      Font fnt;
      int align;
      Color bg = LIGHT_GREEN;

      if (c == 0) {
        txt = "TOTAL";
        fnt = new Font(Font.HELVETICA, 8, Font.BOLD, NBR_DARK);
        align = Element.ALIGN_LEFT;
      } else if (c == amtColIndex) {
        txt = String.format("BDT %,.2f", totalValue);
        fnt = new Font(Font.HELVETICA, 8, Font.BOLD, NBR_DARK);
        align = Element.ALIGN_RIGHT;
      } else {
        txt = "";
        fnt = TD_FONT;
        align = Element.ALIGN_LEFT;
      }

      PdfPCell cell = new PdfPCell(new Phrase(txt, fnt));
      cell.setBackgroundColor(bg);
      cell.setBorderColor(BORDER);
      cell.setPadding(5);
      cell.setHorizontalAlignment(align);
      if (c == 0)
        cell.setFixedHeight(20f);
      table.addCell(cell);
    }
  }

  private void addRecordCount(Document doc, long total) throws DocumentException {
    Font f = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);
    Paragraph p = new Paragraph("Total records in database: " + total
        + (total > EXPORT_LIMIT ? "  (export limited to first " + EXPORT_LIMIT + ")" : ""), f);
    p.setSpacingBefore(4);
    doc.add(p);
  }

  private void addUnsupportedNote(Document doc, String type) throws DocumentException {
    Paragraph p = new Paragraph("PDF export not yet implemented for: " + type,
        new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY));
    p.setAlignment(Element.ALIGN_CENTER);
    doc.add(p);
  }

  private void addPdfFooter(Document doc, ReportExportRequest req,
      AuthPrincipal principal) throws DocumentException {
    doc.add(Chunk.NEWLINE);
    PdfPTable footer = new PdfPTable(new float[] { 1f, 1f });
    footer.setWidthPercentage(100);
    footer.setSpacingBefore(10);

    PdfPCell noteCell = new PdfPCell();
    noteCell.setBorder(Rectangle.NO_BORDER);
    noteCell.addElement(new Phrase(
        "N.B: This is a system-generated report. All figures in BDT (Bangladeshi Taka).",
        FOOTER_FONT));
    noteCell.addElement(new Phrase(
        "Nirapod VAT & Tax Management System - National Board of Revenue",
        FOOTER_FONT));
    footer.addCell(noteCell);

    PdfPCell signCell = new PdfPCell();
    signCell.setBorder(Rectangle.NO_BORDER);
    signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    signCell.addElement(new Phrase("Generated: " + LocalDateTime.now().format(DT_FMT), FOOTER_FONT));
    signCell.addElement(new Phrase("Fiscal Year: " + req.getFiscalYear(), FOOTER_FONT));
    footer.addCell(signCell);

    doc.add(footer);
  }

  public byte[] exportAsExcel(ReportExportRequest req, AuthPrincipal principal) {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      switch (req.getReportType()) {
        case "VAT_COLLECTION" -> buildVatSheet(workbook, req, principal);
        case "INCOME_TAX" -> buildItrSheet(workbook, req, principal);
        case "PENALTY_COLLECTION" -> buildPenaltySheet(workbook, req, principal);
        case "REFUND_STATUS" -> buildRefundSheet(workbook, req, principal);
        default -> buildUnsupportedSheet(workbook, req.getReportType());
      }

      workbook.write(out);
      return out.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Excel export failed: " + e.getMessage(), e);
    }
  }

  private void buildVatSheet(XSSFWorkbook wb, ReportExportRequest req,
      AuthPrincipal principal) {
    XSSFSheet sheet = wb.createSheet("VAT Collection");
    ExcelStyles s = new ExcelStyles(wb);

    int row = 0;
    row = writeExcelHeader(sheet, s, wb, "VAT Collection Report", req, principal, row, 12);

    String[] headers = { "#", "BIN No", "Business Name", "Zone", "Circle",
        "Period", "Gross Supplies (BDT)", "Output Tax (BDT)",
        "Input Tax (BDT)", "Net VAT (BDT)", "Status", "Submitted At" };
    writeExcelColumnHeaders(sheet, s, row++, headers);

    Page<VatCollectionRowDTO> page = vatReturnDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(),
            parseZoneId(req.getZone()),
            emptyIfNull(req.getCircle()), emptyIfNull(req.getStatus()),
            null, PageRequest.of(0, EXPORT_LIMIT))
        .map(VatCollectionRowDTO::from);

    int i = 1;
    double totalNetVat = 0;
    for (VatCollectionRowDTO r : page.getContent()) {
      XSSFRow dataRow = sheet.createRow(row++);
      boolean shade = (i % 2 == 0);
      setCell(dataRow, 0, String.valueOf(i++), s.textStyle(shade));
      setCell(dataRow, 1, r.getBinNo(), s.monoStyle(shade));
      setCell(dataRow, 2, r.getBusinessName(), s.textStyle(shade));
      setCell(dataRow, 3, nvl(r.getZone()), s.textStyle(shade));
      setCell(dataRow, 4, nvl(r.getCircle()), s.textStyle(shade));
      setCell(dataRow, 5, nvl(r.getPeriod()), s.textStyle(shade));
      setNumCell(dataRow, 6, r.getGrossSales(), s.amtStyle(shade));
      setNumCell(dataRow, 7, r.getOutputVat(), s.amtStyle(shade));
      setNumCell(dataRow, 8, r.getInputVatCredit(), s.amtStyle(shade));
      setNumCell(dataRow, 9, r.getNetVat(), s.amtStyle(shade));
      setCell(dataRow, 10, r.getStatus(), s.statusStyle(r.getStatus()));
      setCell(dataRow, 11, nvl(r.getSubmittedAt()), s.textStyle(shade));
      if (r.getNetVat() != null)
        totalNetVat += r.getNetVat().doubleValue();
    }

    writeTotalsRow(sheet, s, row, 12, new int[] { 9 }, new double[] { totalNetVat },
        new String[] { "Net VAT" });
    autoSizeColumns(sheet, 12);
  }

  private void buildItrSheet(XSSFWorkbook wb, ReportExportRequest req,
      AuthPrincipal principal) {
    XSSFSheet sheet = wb.createSheet("Income Tax Returns");
    ExcelStyles s = new ExcelStyles(wb);

    int row = 0;
    row = writeExcelHeader(sheet, s, wb, "Income Tax Returns Report", req, principal, row, 11);

    String[] headers = { "#", "TIN Number", "Taxpayer Name", "Category",
        "Assessment Year", "Gross Income (BDT)", "Tax Payable (BDT)",
        "Tax Paid (BDT)", "Tax Due (BDT)", "Status", "Filed At" };
    writeExcelColumnHeaders(sheet, s, row++, headers);

    Page<IncomeTaxRowDTO> page = itrDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(), emptyIfNull(req.getStatus()), "", null,
            PageRequest.of(0, EXPORT_LIMIT))
        .map(IncomeTaxRowDTO::from);

    int i = 1;
    double totalDue = 0;
    for (IncomeTaxRowDTO r : page.getContent()) {
      XSSFRow dataRow = sheet.createRow(row++);
      boolean shade = (i % 2 == 0);
      setCell(dataRow, 0, String.valueOf(i++), s.textStyle(shade));
      setCell(dataRow, 1, r.getTinNumber(), s.monoStyle(shade));
      setCell(dataRow, 2, r.getTaxpayerName(), s.textStyle(shade));
      setCell(dataRow, 3, nvl(r.getTaxpayerType()), s.textStyle(shade));
      setCell(dataRow, 4, nvl(r.getAssessmentYear()), s.textStyle(shade));
      setNumCell(dataRow, 5, r.getGrossIncome(), s.amtStyle(shade));
      setNumCell(dataRow, 6, r.getTaxPayable(), s.amtStyle(shade));
      setNumCell(dataRow, 7, r.getTaxPaid(), s.amtStyle(shade));
      setNumCell(dataRow, 8, r.getTaxDue(), s.amtStyle(shade));
      setCell(dataRow, 9, r.getStatus(), s.statusStyle(r.getStatus()));
      setCell(dataRow, 10, nvl(r.getFiledAt()), s.textStyle(shade));
      if (r.getTaxDue() != null)
        totalDue += r.getTaxDue().doubleValue();
    }

    writeTotalsRow(sheet, s, row, 11, new int[] { 8 }, new double[] { totalDue },
        new String[] { "Tax Due" });
    autoSizeColumns(sheet, 11);
  }

  private void buildPenaltySheet(XSSFWorkbook wb, ReportExportRequest req,
      AuthPrincipal principal) {
    XSSFSheet sheet = wb.createSheet("Penalty Collection");
    ExcelStyles s = new ExcelStyles(wb);

    int row = 0;
    row = writeExcelHeader(sheet, s, wb, "Penalty Collection Report", req, principal, row, 11);

    String[] headers = { "#", "Penalty No", "Taxpayer", "TIN",
        "Type", "Severity", "Base Amount (BDT)",
        "Interest (BDT)", "Total (BDT)", "Status", "Issued At" };
    writeExcelColumnHeaders(sheet, s, row++, headers);

    Page<PenaltyRowDTO> page = penaltyDAO
        .findByAssessmentYearAndFilters(
            req.getFiscalYear(), "", "", null,
            PageRequest.of(0, EXPORT_LIMIT))
        .map(PenaltyRowDTO::from);

    int i = 1;
    double totalAmt = 0;
    for (PenaltyRowDTO r : page.getContent()) {
      XSSFRow dataRow = sheet.createRow(row++);
      boolean shade = (i % 2 == 0);
      setCell(dataRow, 0, String.valueOf(i++), s.textStyle(shade));
      setCell(dataRow, 1, r.getPenaltyRefNo(), s.monoStyle(shade));
      setCell(dataRow, 2, r.getTaxpayerName(), s.textStyle(shade));
      setCell(dataRow, 3, nvl(r.getTinNumber()), s.textStyle(shade));
      setCell(dataRow, 4, nvl(r.getPenaltyType()), s.textStyle(shade));
      setCell(dataRow, 5, nvl(r.getSeverity()), s.textStyle(shade));
      setNumCell(dataRow, 6, r.getBaseAmount(), s.amtStyle(shade));
      setNumCell(dataRow, 7, r.getInterestAmount(), s.amtStyle(shade));
      setNumCell(dataRow, 8, r.getTotalAmount(), s.amtStyle(shade));
      setCell(dataRow, 9, r.getStatus(), s.statusStyle(r.getStatus()));
      setCell(dataRow, 10, nvl(r.getIssuedAt()), s.textStyle(shade));
      if (r.getTotalAmount() != null)
        totalAmt += r.getTotalAmount().doubleValue();
    }

    writeTotalsRow(sheet, s, row, 11, new int[] { 8 }, new double[] { totalAmt },
        new String[] { "Total Penalty" });
    autoSizeColumns(sheet, 11);
  }

  private void buildRefundSheet(XSSFWorkbook wb, ReportExportRequest req,
      AuthPrincipal principal) {
    XSSFSheet sheet = wb.createSheet("Refund Status");
    ExcelStyles s = new ExcelStyles(wb);

    int row = 0;
    row = writeExcelHeader(sheet, s, wb, "Refund Status Report", req, principal, row, 10);

    String[] headers = { "#", "Reference No", "Taxpayer", "TIN",
        "Type", "Claimed (BDT)", "Approved (BDT)", "Paid (BDT)",
        "Status", "Submitted At" };
    writeExcelColumnHeaders(sheet, s, row++, headers);

    Page<RefundRowDTO> page = refundDAO
        .findByFiscalYearNameAndFilters(
            req.getFiscalYear(), "", null,
            PageRequest.of(0, EXPORT_LIMIT))
        .map(RefundRowDTO::from);

    int i = 1;
    double totalApproved = 0;
    for (RefundRowDTO r : page.getContent()) {
      XSSFRow dataRow = sheet.createRow(row++);
      boolean shade = (i % 2 == 0);
      setCell(dataRow, 0, String.valueOf(i++), s.textStyle(shade));
      setCell(dataRow, 1, r.getRefundRefNo(), s.monoStyle(shade));
      setCell(dataRow, 2, r.getTaxpayerName(), s.textStyle(shade));
      setCell(dataRow, 3, nvl(r.getTinNumber()), s.textStyle(shade));
      setCell(dataRow, 4, nvl(r.getRefundType()), s.textStyle(shade));
      setNumCell(dataRow, 5, r.getClaimedAmount(), s.amtStyle(shade));
      setNumCell(dataRow, 6, r.getApprovedAmount(), s.amtStyle(shade));
      setNumCell(dataRow, 7, r.getPaidAmount(), s.amtStyle(shade));
      setCell(dataRow, 8, r.getStatus(), s.statusStyle(r.getStatus()));
      setCell(dataRow, 9, nvl(r.getSubmittedAt()), s.textStyle(shade));
      if (r.getApprovedAmount() != null)
        totalApproved += r.getApprovedAmount().doubleValue();
    }

    writeTotalsRow(sheet, s, row, 10, new int[] { 6 }, new double[] { totalApproved },
        new String[] { "Approved" });
    autoSizeColumns(sheet, 10);
  }

  private void buildUnsupportedSheet(XSSFWorkbook wb, String type) {
    XSSFSheet sheet = wb.createSheet("Report");
    XSSFRow row = sheet.createRow(0);
    row.createCell(0).setCellValue("Excel export not yet implemented for: " + type);
  }


  private int writeExcelHeader(XSSFSheet sheet, ExcelStyles s, XSSFWorkbook wb,
      String title, ReportExportRequest req,
      AuthPrincipal principal, int startRow, int colCount) {
    int row = startRow;

    // Row 0: merged title
    XSSFRow titleRow = sheet.createRow(row++);
    titleRow.setHeight((short) 800);
    XSSFCell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(title);
    titleCell.setCellStyle(s.titleStyle());
    sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, colCount - 1));

    // Row 1: sub-header
    XSSFRow subRow = sheet.createRow(row++);
    subRow.setHeight((short) 450);
    XSSFCell subCell = subRow.createCell(0);
    subCell.setCellValue(
        "Nirapod VAT & Tax Management System  |  " +
            "Fiscal Year: " + req.getFiscalYear() + "  |  " +
            "Zone: " + (req.getZone() != null && !req.getZone().isBlank()
                ? req.getZone()
                : "All")
            + "  |  " +
            "Generated: " + LocalDateTime.now().format(DT_FMT) + "  |  " +
            "By: " + (principal != null ? principal.getEmail() : "System"));
    subCell.setCellStyle(s.subHeaderStyle());
    sheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, 0, colCount - 1));

    // Blank row
    sheet.createRow(row++);

    return row;
  }

  private void writeExcelColumnHeaders(XSSFSheet sheet, ExcelStyles s,
      int rowIndex, String[] headers) {
    XSSFRow row = sheet.createRow(rowIndex);
    row.setHeight((short) 500);
    for (int i = 0; i < headers.length; i++) {
      XSSFCell cell = row.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(s.columnHeaderStyle());
    }
  }

  private void writeTotalsRow(XSSFSheet sheet, ExcelStyles s, int rowIndex,
      int colCount, int[] amtCols, double[] amounts,
      String[] labels) {
    XSSFRow row = sheet.createRow(rowIndex);
    XSSFCell labelCell = row.createCell(0);
    labelCell.setCellValue("TOTALS (" + String.join(", ", labels) + ")");
    labelCell.setCellStyle(s.totalLabelStyle());
    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, amtCols[0] - 1));

    for (int i = 0; i < amtCols.length; i++) {
      XSSFCell cell = row.createCell(amtCols[i]);
      cell.setCellValue(amounts[i]);
      cell.setCellStyle(s.totalAmtStyle());
    }
  }

  private void setCell(XSSFRow row, int col, String val, XSSFCellStyle style) {
    XSSFCell cell = row.createCell(col);
    cell.setCellValue(val != null ? val : "-");
    cell.setCellStyle(style);
  }

  private void setNumCell(XSSFRow row, int col, Object val, XSSFCellStyle style) {
    XSSFCell cell = row.createCell(col);
    if (val instanceof java.math.BigDecimal bd) {
      cell.setCellValue(bd.doubleValue());
    } else if (val instanceof Double d) {
      cell.setCellValue(d);
    } else {
      cell.setCellValue(0.0);
    }
    cell.setCellStyle(style);
  }

  private void autoSizeColumns(XSSFSheet sheet, int count) {
    for (int i = 0; i < count; i++) {
      sheet.autoSizeColumn(i);
      int w = sheet.getColumnWidth(i);
      sheet.setColumnWidth(i, Math.min(w + 512, 15000));
    }
  }


  private static class ExcelStyles {
    private final XSSFWorkbook wb;
    private final XSSFFont headerFont;
    private final XSSFFont bodyFont;
    private final XSSFFont monoFont;
    private final XSSFFont amtFont;
    private final XSSFFont totalFont;

    // NBR Blue: #1A3F8F â†’ POI indexed: use XSSFColor
    private final XSSFColor NBR_BLUE_XL = new XSSFColor(new byte[] { (byte) 26, (byte) 63, (byte) 143 });
    private final XSSFColor NBR_DARK_XL = new XSSFColor(new byte[] { (byte) 0, (byte) 77, (byte) 45 });
    private final XSSFColor LIGHT_BG_XL = new XSSFColor(new byte[] { (byte) 248, (byte) 250, (byte) 253 });
    private final XSSFColor SHADE_BG_XL = new XSSFColor(new byte[] { (byte) 240, (byte) 244, (byte) 248 });
    private final XSSFColor GREEN_BG_XL = new XSSFColor(new byte[] { (byte) 232, (byte) 247, (byte) 239 });
    private final XSSFColor GOLD_XL = new XSSFColor(new byte[] { (byte) 180, (byte) 140, (byte) 0 });

    ExcelStyles(XSSFWorkbook wb) {
      this.wb = wb;

      headerFont = wb.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 11);
      headerFont.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));

      bodyFont = wb.createFont();
      bodyFont.setFontHeightInPoints((short) 9);

      monoFont = wb.createFont();
      monoFont.setFontHeightInPoints((short) 9);
      monoFont.setFontName("Courier New");
      monoFont.setColor(NBR_BLUE_XL);
      monoFont.setBold(true);

      amtFont = wb.createFont();
      amtFont.setFontHeightInPoints((short) 9);
      amtFont.setBold(true);
      amtFont.setColor(NBR_DARK_XL);

      totalFont = wb.createFont();
      totalFont.setFontHeightInPoints((short) 10);
      totalFont.setBold(true);
      totalFont.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));
    }

    XSSFCellStyle titleStyle() {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(NBR_BLUE_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      XSSFFont f = wb.createFont();
      f.setFontHeightInPoints((short) 14);
      f.setBold(true);
      f.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));
      style.setFont(f);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
    }

    XSSFCellStyle subHeaderStyle() {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(SHADE_BG_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      XSSFFont f = wb.createFont();
      f.setFontHeightInPoints((short) 8);
      f.setItalic(true);
      f.setColor(NBR_BLUE_XL);
      style.setFont(f);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
    }

    XSSFCellStyle columnHeaderStyle() {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(NBR_BLUE_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(headerFont);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      style.setBorderBottom(BorderStyle.MEDIUM);
      style.setBottomBorderColor(GOLD_XL);
      style.setWrapText(true);
      return style;
    }

    XSSFCellStyle textStyle(boolean shade) {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(shade ? SHADE_BG_XL : LIGHT_BG_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(bodyFont);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      style.setBorderBottom(BorderStyle.THIN);
      style.setBottomBorderColor(new XSSFColor(new byte[] { (byte) 220, (byte) 228, (byte) 236 }));
      return style;
    }

    XSSFCellStyle monoStyle(boolean shade) {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(shade ? SHADE_BG_XL : LIGHT_BG_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(monoFont);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
    }

    XSSFCellStyle amtStyle(boolean shade) {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(shade ? SHADE_BG_XL : LIGHT_BG_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(amtFont);
      style.setAlignment(HorizontalAlignment.RIGHT);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
      return style;
    }

    XSSFCellStyle statusStyle(String status) {
      XSSFCellStyle style = textStyle(false);
      style.setAlignment(HorizontalAlignment.CENTER);
      return style;
    }

    XSSFCellStyle totalLabelStyle() {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(NBR_DARK_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(totalFont);
      style.setAlignment(HorizontalAlignment.LEFT);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
    }

    XSSFCellStyle totalAmtStyle() {
      XSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(GREEN_BG_XL);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      XSSFFont f = wb.createFont();
      f.setBold(true);
      f.setFontHeightInPoints((short) 10);
      f.setColor(NBR_DARK_XL);
      style.setFont(f);
      style.setAlignment(HorizontalAlignment.RIGHT);
      style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
      return style;
    }
  }


  private String nvl(String s) {
    return s != null && !s.isBlank() ? s : "-";
  }

  private String emptyIfNull(String s) {
    return s != null ? s.trim() : "";
  }

  private Long parseZoneId(String zone) {
    if (zone == null || zone.isBlank())
      return null;
    try {
      return Long.parseLong(zone);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private String resolveReportTitle(String type) {
    return switch (type) {
      case "VAT_COLLECTION" -> "VAT COLLECTION REPORT";
      case "INCOME_TAX" -> "INCOME TAX RETURNS REPORT";
      case "PENALTY_COLLECTION" -> "PENALTY COLLECTION REPORT";
      case "REFUND_STATUS" -> "REFUND STATUS REPORT";
      default -> type + " REPORT";
    };
  }
}