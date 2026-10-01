package com.example.designpatterns.creational.abstractfactory;

import java.util.List;

/**
 * <b>Client</b> of the Abstract Factory.
 *
 * <p>Written once, against the abstract types only. It never names
 * {@code PdfHeader} or {@code HtmlBody}, so supporting a third output format means
 * writing a new factory and touching nothing in this class.
 */
public class ReportAssembler {

    public String assemble(ReportFactory factory, String title, List<String> rows) {
        return factory.createHeader().render(title) + '\n'
                + factory.createBody().render(rows) + '\n'
                + factory.createFooter().render();
    }
}
