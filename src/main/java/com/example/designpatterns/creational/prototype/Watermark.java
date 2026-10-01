package com.example.designpatterns.creational.prototype;

/**
 * Deliberately <b>mutable</b> nested state.
 *
 * <p>This class exists to make the difference between a shallow and a deep copy
 * observable. If {@link ReportTemplate#copy()} shared this object between the
 * prototype and its copies, mutating one copy's watermark would silently corrupt
 * every future copy &mdash; the classic Prototype bug.
 *
 * <p>Full documentation: {@code docs/patterns/prototype.md}
 */
public class Watermark {

    private String text;
    private int opacity;

    public Watermark(String text, int opacity) {
        this.text = text;
        this.opacity = opacity;
    }

    public String text() { return text; }

    public int opacity() { return opacity; }

    public void setText(String text) { this.text = text; }

    public void setOpacity(int opacity) { this.opacity = opacity; }

    /** Returns an independent copy. */
    public Watermark copy() {
        return new Watermark(text, opacity);
    }

    @Override
    public String toString() {
        return text + " @" + opacity + '%';
    }
}
