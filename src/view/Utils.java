package view;

import java.util.Scanner;

// ... existing code ...
// (Class này đã được tách các phương thức tiện ích sang UtilityMethods.java)
public final class Utils {
    private Utils() {}

    public static String checkID(String id) {
        id = id.toUpperCase();
        if (id.isEmpty()) return null;
        if (id.matches("^IT\\d{3}$")) return "IT";
        if (id.matches("^BIZ\\d{3}$")) return "Biz";
        return null;
    }

    public static double checkValidDouble(String prompt, String errorMessage, Double min, Double max) {
        Scanner scanner = new Scanner(System.in);
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if ((min != null && value < min) || (max != null && value > max)) {
                    System.out.println(errorMessage);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String checkString(String prompt, String regex) {
        Scanner sc = new Scanner(System.in);
        String result;
        while (true) {
            System.out.print(prompt);
            result = sc.nextLine().trim();
            if (result.isBlank() || !result.matches(regex))
                System.out.println("Invalid! Re-enter!");
            else {
                return result;
            }
        }
    }

    public static boolean checkAnswer(String prompt, String warning, String first, String second) {
        Scanner sc = new Scanner(System.in);
        String answer;
        while (true) {
            System.out.print(prompt);
            answer = sc.nextLine().trim().toUpperCase();
            if (answer.equalsIgnoreCase(first))
                return true;
            else if (answer.equalsIgnoreCase(second))
                return false;
            else System.out.println(warning);
        }
    }

    // Đã tách các phương thức tiện ích sang UtilityMethods.java
}
