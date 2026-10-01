package com.example.designpatterns.creational.prototype;

/**
 * <b>Prototype</b>: an object that produces new instances by copying itself.
 *
 * <p>Useful when configuring an object is expensive or fiddly but copying it is
 * cheap. Here, a fully configured template is registered once and cloned per
 * report, rather than being rebuilt from parameters every time.
 *
 * <p>This class implements {@link #copy()} by hand rather than using
 * {@code Cloneable}/{@code Object.clone()}. {@code clone()} is widely considered
 * a design mistake: it bypasses constructors, produces shallow copies by default,
 * and its contract is not enforced by the compiler. A plain copy method (or a copy
 * constructor) is clearer and safer.
 *
 * <p>Full documentation: {@code docs/patterns/prototype.md}
 */
public class ReportTemplate {

    private final String name;
    private final String pageSize;
    private final Watermark watermark;

    public ReportTemplate(String name, String pageSize, Watermark watermark) {
        this.name = name;
        this.pageSize = pageSize;
        this.watermark = watermark;
    }

    public String name() { return name; }

    public String pageSize() { return pageSize; }

    public Watermark watermark() { return watermark; }

    /**
     * Deep copy. The nested {@link Watermark} is copied too — a shallow copy here
     * would let callers corrupt the prototype through the copy.
     */
    public ReportTemplate copy() {
        return new ReportTemplate(name, pageSize, watermark.copy());
    }

    @Override
    public String toString() {
        return "ReportTemplate{name='" + name + "', pageSize='" + pageSize
                + "', watermark=" + watermark + '}';
    }
}
