package com.example.designpatterns.behavioral.templatemethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Abstract Class</b> in the Template Method pattern.
 *
 * <p>{@link #run} is the template method: it fixes the <em>order</em> of the
 * algorithm — parse, validate each row, persist the survivors, summarise — and is
 * declared {@code final} so no subclass can quietly reorder or skip a step. That
 * {@code final} is not pedantry: it is the invariant the pattern exists to
 * protect. Without it you have ordinary inheritance and no guarantee at all.
 *
 * <p>Subclasses supply the varying steps ({@link #parse}, {@link #validate}) and
 * may optionally observe via the <em>hook</em> {@link #onSkipped}, which has an
 * empty default so subclasses pay nothing to ignore it.
 *
 * <p>Compare {@code JdbcTemplate}: Spring fixes acquire-connection → execute →
 * map-results → release-connection, and you supply only the varying middle.
 *
 * <p>Full documentation: {@code docs/patterns/templatemethod.md}
 */
public abstract class DataImportJob {

    /** The template method. Fixed algorithm; final by design. */
    public final ImportReport run(String raw) {
        List<String> steps = new ArrayList<>();

        steps.add("begin");
        List<Map<String, String>> rows = parse(raw);
        steps.add("parse");

        List<Map<String, String>> valid = new ArrayList<>();
        int skipped = 0;
        for (Map<String, String> row : rows) {
            if (validate(row)) {
                valid.add(row);
            } else {
                skipped++;
                onSkipped(row);
            }
        }
        steps.add("validate");

        persist(valid);
        steps.add("persist");

        steps.add("summarise");
        return new ImportReport(format(), rows.size(), valid.size(), skipped, steps);
    }

    /** Varying step: how raw text becomes rows. */
    protected abstract List<Map<String, String>> parse(String raw);

    /** Varying step: what makes a row acceptable. */
    protected abstract boolean validate(Map<String, String> row);

    /** Varying step: the format's name, for the report. */
    protected abstract String format();

    /** <b>Hook</b>: optional. Default does nothing, so subclasses may ignore it. */
    protected void onSkipped(Map<String, String> row) {
    }

    /** Shared step, identical for every subclass — the reason the base class exists. */
    protected void persist(List<Map<String, String>> rows) {
        // In a real job this would write to a repository.
    }
}
