package com.aryan.inventory.util;

import java.util.Scanner;

public class CaptializeString {

    // Method to capitalize first character and lowercase the rest
    public static String capitalizeString(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return as is for null or empty
        }

        // Trim leading/trailing spaces but preserve internal spaces
        String trimmed = input.trim();

        // First character uppercase, rest lowercase
        return trimmed.substring(0, 1).toUpperCase() +
               trimmed.substring(1).toLowerCase();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String userInput = scanner.nextLine();

        String result = capitalizeString(userInput);
        System.out.println("Formatted string: " + result);

        scanner.close();
    }
}
