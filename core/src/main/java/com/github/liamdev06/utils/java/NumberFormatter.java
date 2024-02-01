package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

/**
 * Utility class for working with number formatting and number input.
 */
@UtilityClass
public class NumberFormatter {

    private static final char[] SUFFIXES = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};

    /**
     * Formats a number with a suffix (k, M, B, T, P, E) to represent thousands, millions, billions, etc.
     *
     * @param inputNumber The input number.
     * @return A formatted string with a suffix representing the magnitude of the number.
     */
    public static @NonNull String formatWithSuffix(long inputNumber) {
        if (inputNumber < 1000) {
            return String.valueOf(inputNumber);
        }

        int exp = (int) (Math.log(inputNumber) / Math.log(1000));
        double value = inputNumber / Math.pow(1000, exp);

        return String.format(Locale.US, "%.2f", value) + SUFFIXES[exp];
    }

    /**
     * Convert a special input string with suffixes (k, m, b) into a long number.
     * The method supports both integer and decimal numbers and ensures correct number formatting.
     * This can be useful to handle player number inputs so they can easily type large numbers.
     * For example:
     * - "100k" will be converted to 100,000.
     * - "56.3k" will be converted to 56,300.
     *
     * @param input The input string representing a number with suffixes (k, m, b).
     * @return The long number representation of the input string or -1 if the input is invalid.
     */
    public static String formatSuffixInputToNumber(@NonNull String input) {
        double value = Double.parseDouble(input.replaceAll("[^\\d.]", ""));

        char lastChar = Character.toLowerCase(input.charAt(input.length() - 1));
        int index = new String(SUFFIXES).toLowerCase().indexOf(lastChar);

        long multiplier = index >= 0 ? (long) Math.pow(10, 3 * index) : 1;

        return String.format(Locale.US, "%,d", (long) (value * multiplier));
    }

}