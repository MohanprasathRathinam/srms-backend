package com.smsys.server;

import java.util.Map;

public class ResultCalculator {

    public String calculateResult(Map<String, String> params) {
        try {
            // Extract parameters
            String name = params.getOrDefault("name", "");
            String roll = params.getOrDefault("roll", "");
            int math = Integer.parseInt(params.getOrDefault("math", "0"));
            int physics = Integer.parseInt(params.getOrDefault("physics", "0"));
            int chemistry = Integer.parseInt(params.getOrDefault("chemistry", "0"));
            int english = Integer.parseInt(params.getOrDefault("english", "0"));
            int cs = Integer.parseInt(params.getOrDefault("cs", "0"));

            // Validate marks
            if (math < 0 || math > 100 || physics < 0 || physics > 100 ||
                chemistry < 0 || chemistry > 100 || english < 0 || english > 100 ||
                cs < 0 || cs > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100");
            }

            // Calculate total and percentage
            int total = math + physics + chemistry + english + cs;
            double percentage = (double) total / 5.0;

            // Determine grade
            String grade = calculateGrade(percentage);

            // Build JSON string manually to avoid external dependency
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            sb.append("\"name\":\"").append(escapeJson(name)).append("\",");
            sb.append("\"rollNumber\":\"").append(escapeJson(roll)).append("\",");
            sb.append("\"total\":").append(total).append(',');
            sb.append("\"percentage\":").append(String.format(java.util.Locale.US, "%.2f", percentage)).append(',');
            sb.append("\"grade\":\"").append(escapeJson(grade)).append("\",");
            sb.append("\"subjectsCount\":5,");
            sb.append("\"maxMarksPerSubject\":100,");
            sb.append("\"subjects\":{");
            sb.append("\"Mathematics\":").append(math).append(',');
            sb.append("\"Physics\":").append(physics).append(',');
            sb.append("\"Chemistry\":").append(chemistry).append(',');
            sb.append("\"English\":").append(english).append(',');
            sb.append("\"Computer Science\":").append(cs);
            sb.append('}');
            sb.append('}');

            return sb.toString();

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid marks format");
        }
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int)c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}


