package com.example.designpatterns.creational.abstractfactory;

import java.util.List;

/** <b>Abstract Product B</b>: the body of a report. */
public interface ReportBody extends ReportPart {
    String render(List<String> rows);
}
