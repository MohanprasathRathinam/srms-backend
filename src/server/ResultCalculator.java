package server;

public final class ResultCalculator {
    private ResultCalculator() {}

    public static int calculateTotal(int... marks) {
        int total = 0;
        for (int m : marks) total += m;
        return total;
    }

    public static double calculatePercentage(int total, int numberOfSubjects) {
        if (numberOfSubjects <= 0) return 0.0;
        int maxTotal = numberOfSubjects * 100;
        return (total * 100.0) / maxTotal;
    }

    public static String calculateGrade(double percentage) {
        if (percentage >= 90.0) return "A+";
        if (percentage >= 80.0) return "A";
        if (percentage >= 70.0) return "B";
        if (percentage >= 60.0) return "C";
        if (percentage >= 50.0) return "D";
        return "F";
    }
}


