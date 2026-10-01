package com.example.designpatterns.behavioral.templatemethod;

import java.util.List;

/** Outcome of one import run. {@code steps} makes the fixed algorithm observable. */
public record ImportReport(String format, int parsed, int imported, int skipped, List<String> steps) {

    public ImportReport {
        steps = List.copyOf(steps);
    }
}
