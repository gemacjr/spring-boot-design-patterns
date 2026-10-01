package com.example.designpatterns.behavioral.templatemethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** <b>Concrete Class</b>: fills in the CSV-specific steps only. */
public class CsvImportJob extends DataImportJob {

    private final List<Map<String, String>> skippedRows = new ArrayList<>();

    @Override
    protected String format() {
        return "CSV";
    }

    @Override
    protected List<Map<String, String>> parse(String raw) {
        List<Map<String, String>> rows = new ArrayList<>();
        String[] lines = raw.strip().split("\n");
        if (lines.length == 0) {
            return rows;
        }
        String[] headers = lines[0].split(",");
        for (int i = 1; i < lines.length; i++) {
            String[] cells = lines[i].split(",", -1);
            Map<String, String> row = new LinkedHashMap<>();
            for (int c = 0; c < headers.length && c < cells.length; c++) {
                row.put(headers[c].strip(), cells[c].strip());
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    protected boolean validate(Map<String, String> row) {
        String email = row.get("email");
        return email != null && email.contains("@");
    }

    @Override
    protected void onSkipped(Map<String, String> row) {
        skippedRows.add(row);
    }

    public List<Map<String, String>> skippedRows() {
        return List.copyOf(skippedRows);
    }
}
