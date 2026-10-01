package com.example.designpatterns.creational.abstractfactory;

import java.util.List;

/** <b>Concrete Products</b> of the HTML family. */
public final class HtmlReportParts {

    private HtmlReportParts() {
    }

    public static final String FORMAT = "HTML";

    /** <b>Concrete Product A</b> (HTML family). */
    public static class HtmlHeader implements ReportHeader {
        @Override public String format() { return FORMAT; }
        @Override public String render(String title) { return "<h1>" + title + "</h1>"; }
    }

    /** <b>Concrete Product B</b> (HTML family). */
    public static class HtmlBody implements ReportBody {
        @Override public String format() { return FORMAT; }
        @Override public String render(List<String> rows) {
            StringBuilder out = new StringBuilder("<ul>");
            for (String row : rows) {
                out.append("<li>").append(row).append("</li>");
            }
            return out.append("</ul>").toString();
        }
    }

    /** <b>Concrete Product C</b> (HTML family). */
    public static class HtmlFooter implements ReportFooter {
        @Override public String format() { return FORMAT; }
        @Override public String render() { return "<footer>end of document</footer>"; }
    }
}
