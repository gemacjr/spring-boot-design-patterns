package com.example.designpatterns.creational.abstractfactory;

/**
 * <b>Abstract Factory</b>: creates a matched set of report parts.
 *
 * <p>The difference from Factory Method is the <em>set</em>. A Factory Method
 * creates one product; an Abstract Factory creates a family whose members must be
 * used together. Because a client obtains all three parts from one factory, a PDF
 * header can never end up attached to an HTML body.
 *
 * <p>Full documentation: {@code docs/patterns/abstractfactory.md}
 */
public interface ReportFactory {

    ReportHeader createHeader();

    ReportBody createBody();

    ReportFooter createFooter();

    /** The family this factory produces. */
    String format();
}
