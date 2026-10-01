package com.example.designpatterns.creational.abstractfactory;

/** <b>Abstract Product A</b>: the header of a report. */
public interface ReportHeader extends ReportPart {
    String render(String title);
}
