package com.example.designpatterns.creational.abstractfactory;

/** <b>Concrete Factory</b> for the PDF family. */
public class PdfReportFactory implements ReportFactory {
    @Override public ReportHeader createHeader() { return new PdfReportParts.PdfHeader(); }
    @Override public ReportBody createBody() { return new PdfReportParts.PdfBody(); }
    @Override public ReportFooter createFooter() { return new PdfReportParts.PdfFooter(); }
    @Override public String format() { return PdfReportParts.FORMAT; }
}
