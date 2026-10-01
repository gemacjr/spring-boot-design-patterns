package com.example.designpatterns.creational.abstractfactory;

/** <b>Concrete Factory</b> for the HTML family. */
public class HtmlReportFactory implements ReportFactory {
    @Override public ReportHeader createHeader() { return new HtmlReportParts.HtmlHeader(); }
    @Override public ReportBody createBody() { return new HtmlReportParts.HtmlBody(); }
    @Override public ReportFooter createFooter() { return new HtmlReportParts.HtmlFooter(); }
    @Override public String format() { return HtmlReportParts.FORMAT; }
}
