package com.example.designpatterns.creational.abstractfactory;

/**
 * Common supertype of every product in the report family.
 *
 * <p>{@link #format()} exists so the family membership of a part is observable at
 * runtime &mdash; that is what the test uses to prove no cross-family mixing occurs.
 *
 * <p>Full documentation: {@code docs/patterns/abstractfactory.md}
 */
public interface ReportPart {

    /** The family this part belongs to, e.g. {@code "PDF"} or {@code "HTML"}. */
    String format();
}
