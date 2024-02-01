package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for working with- and manipulating Strings.
 */
@UtilityClass
public class StringUtil {

    /**
     * Title case formats a {@link String}.
     *
     * @param string String to be formatted.
     * @return Formatted {@link String}.
     */
    public static @NonNull String capitalize(@NonNull String string) {
        return capitalize(string, true);
    }

    /**
     * Title case formats a {@link String}.
     *
     * @param string String to be formatted.
     * @param modifyUpperCaseLetters Whether to modify the already uppercase letters.
     * @return Formatted {@link String}.
     */
    public static @NonNull String capitalize(@NonNull String string, boolean modifyUpperCaseLetters) {
        string = modifyUpperCaseLetters ? string.toLowerCase() : string;

        return Arrays.stream(string.split("\\s+"))
                .map(word -> word.isEmpty() ? "" : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * Corrects all the additional spaces and punctuation misspellings
     *
     * @param string String to be corrected.
     * @return Corrected {@link String}.
     */
    public static @NonNull String punctuationCorrector(@NonNull String string) {
        // Corrects all the additional spaces
        string = string.replaceAll("\\s+", " ");

        // Corrects all the punctuation misspellings
        string = string.replaceAll("\\s*([,.;!?])(?!\\s*$)\\s*", "$1 ");

        return string;
    }

}