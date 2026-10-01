package com.example.designpatterns.creational.abstractfactory;

import java.util.List;

/**
 * <b>Concrete Products</b> of the PDF family, grouped in one file because they
 * are meaningless apart &mdash; they exist only as a matched set.
 */
public final class PdfReportParts {

    private PdfReportParts() {
    }

    public static final String FORMAT = "PDF";

    /** <b>Concrete Product A</b> (PDF family). */
    public static class PdfHeader implements ReportHeader {
        @Override public String format() { return FORMAT; }
        @Override public String render(String title) { return "==== " + title + " ===="; }
    }

    /** <b>Concrete Product B</b> (PDF family). */
    public static class PdfBody implements ReportBody {
        @Override public String format() { return FORMAT; }
        @Override public String render(List<String> rows) {
            StringBuilder out = new StringBuilder();
            for (String row : rows) {
                out.append("  • ").append(row).append('\n');
            }
            return out.toString();
        }
    }

    /** <b>Concrete Product C</b> (PDF family). */
    public static class PdfFooter implements ReportFooter {
        @Override public String format() { return FORMAT; }
        @Override public String render() { return "---- end of document ----"; }
    }
}
