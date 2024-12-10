import java.io.*;
import java.util.*;

// Main class to process Roman numeral operations
public class RomanNumeralProcessor {

    // Method to convert a single Roman character to its decimal value
    public static int romanCharToDecimal(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0; // Return 0 for any invalid character
        }
    }

    // Method to validate if a string contains only valid Roman numerals
    public static boolean isValidRomanNumeral(String roman) {
        for (char c : roman.toCharArray()) {
            if ("IVXLCDM".indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    // Method to convert a Roman numeral string to a decimal number
    public static int romanToDecimal(String roman) {
        int total = 0;
        int length = roman.length();

        for (int i = 0; i < length; i++) {
            int current = romanCharToDecimal(roman.charAt(i));
            int next = (i + 1 < length) ? romanCharToDecimal(roman.charAt(i + 1)) : 0;

            // If the current value is less than the next value, subtract it
            if (current < next) {
                total -= current;
            } else {
                total += current;
            }
        }
        return total;
    }

    // Method to perform arithmetic operations on two decimal numbers
    public static int performOperation(int num1, int num2, char operation) {
        switch (operation) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            default:
                return 0; // Return 0 for any invalid operation
        }
    }

    // Method to convert a decimal number to its word representation
    public static String decimalToWords(int num) {
        if (num == 0) {
            return "Zero";
        }

        String[] ones = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
        String[] teens = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen" };
        String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };
        String[] thousands = { "", "Thousand", "Million", "Billion" };

        // Handle negative numbers
        if (num < 0) {
            return "Negative " + decimalToWords(-num);
        }

        StringBuilder result = new StringBuilder();

        int partIndex = 0;
        while (num > 0) {
            int part = num % 1000;
            if (part != 0) {
                result.insert(0, convertPartToWords(part, ones, teens, tens) + thousands[partIndex] + " ");
            }
            num /= 1000;
            partIndex++;
        }

        return result.toString().trim();
    }

    // Helper method to convert a part of the number (less than 1000) to words
    private static String convertPartToWords(int num, String[] ones, String[] teens, String[] tens) {
        StringBuilder part = new StringBuilder();

        if (num >= 100) {
            part.append(ones[num / 100]).append(" Hundred ");
            num %= 100;
        }

        if (num >= 10 && num < 20) {
            part.append(teens[num - 10]).append(" ");
        } else {
            if (num >= 20) {
                part.append(tens[num / 10]).append(" ");
            }
            if (num % 10 > 0) {
                part.append(ones[num % 10]).append(" ");
            }
        }

        return part.toString();
    }

    // Method to process the input file and write results to the output file
    public static void processFile(String inputFile, String outputFile) {
        try (Scanner scanner = new Scanner(new File(inputFile));
                PrintWriter writer = new PrintWriter(new File(outputFile))) {

            while (scanner.hasNext()) {
                String roman1 = scanner.next();
                char operation = scanner.next().charAt(0);
                String roman2 = scanner.next();

                // Validate the Roman numeral inputs
                if (!isValidRomanNumeral(roman1) || !isValidRomanNumeral(roman2)) {
                    writer.printf("Invalid Roman numeral(s) detected: %s %c %s%n", roman1, operation, roman2);
                    continue;
                }

                int num1 = romanToDecimal(roman1);
                int num2 = romanToDecimal(roman2);
                int result = performOperation(num1, num2, operation);

                String wordResult = decimalToWords(result);

                writer.printf("%s %c %s = %d (%s)%n", roman1, operation, roman2, result, wordResult);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error opening file(s): " + e.getMessage());
        }
    }

    // Main method to execute the program
    public static void main(String[] args) {
        String inputFile = "Input.txt";
        String outputFile = "Output.txt";

        processFile(inputFile, outputFile);
        System.out.println("Processing complete. Check " + outputFile + " for results.");
    }
}
