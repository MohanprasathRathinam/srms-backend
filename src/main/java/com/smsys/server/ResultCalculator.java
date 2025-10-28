package com.smsys.server;

import org.json.JSONObject;
import java.util.Map;

public class ResultCalculator {
    
    public JSONObject calculateResult(Map<String, String> params) {
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
            
            // Create result JSON
            JSONObject result = new JSONObject();
            result.put("name", name);
            result.put("rollNumber", roll);
            result.put("total", total);
            result.put("percentage", Math.round(percentage * 100.0) / 100.0);
            result.put("grade", grade);
            result.put("subjectsCount", 5);
            result.put("maxMarksPerSubject", 100);
            
            // Add individual subject marks
            JSONObject subjects = new JSONObject();
            subjects.put("Mathematics", math);
            subjects.put("Physics", physics);
            subjects.put("Chemistry", chemistry);
            subjects.put("English", english);
            subjects.put("Computer Science", cs);
            result.put("subjects", subjects);
            
            return result;
            
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
}


