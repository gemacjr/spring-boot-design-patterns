package com.example.designpatterns.behavioral.templatemethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>Concrete Class</b>: fills in the JSON-specific steps only.
 *
 * <p>The parser is a deliberately tiny hand-rolled one — a real job would use
 * Jackson. Keeping it dependency-free keeps the pattern, not the parsing, in view.
 */
public class JsonImportJob extends DataImportJob {

    @Override
    protected String format() {
        return "JSON";
    }

    @Override
    protected List<Map<String, String>> parse(String raw) {
        List<Map<String, String>> rows = new ArrayList<>();
        for (String object : raw.split("\\},")) {
            String body = object.replace("[", "").replace("]", "")
                    .replace("{", "").replace("}", "").strip();
            if (body.isBlank()) {
                continue;
            }
            Map<String, String> row = new LinkedHashMap<>();
            for (String pair : body.split(",")) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    row.put(unquote(kv[0]), unquote(kv[1]));
                }
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

    private static String unquote(String value) {
        return value.strip().replace("\"", "");
    }
}
