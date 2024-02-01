package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility class for working with- and manipulating booleans.
 */
@UtilityClass
public class BooleanUtil {

    /**
     * Nicely formats a boolean into a short version of a "console-standard" readable way, returning either 'Y' for
     * "yes", or 'N' for "no" based on the boolean's value.
     *
     * @param value The {@code boolean} to format.
     * @return Formatted boolean either returning 'Y' or 'N' as {@link String}.
     * @see #formatBooleanFriendly(boolean)
     */
    public static @NonNull String formatBooleanShortFriendly(boolean value) {
        return value ? "Y" : "N";
    }

    /**
     * Nicely formats a boolean in a friendly "human" readable way, returning either 'Yes' or 'No' based on the boolean's value.
     *
     * @param value The {@code boolean} to format.
     * @return Formatted boolean either returning 'Yes' if the boolean is {@code true}, or 'No' if the boolean is {@code false}.
     */
    public static @NonNull String formatBooleanFriendly(boolean value) {
        return value ? "Yes" : "No";
    }

    /**
     * Nicely formats a boolean by capitalizing the first character of the boolean's {@link String string value}.
     *
     * @param value The {@code boolean} to capitalize.
     * @return Formatted boolean with the first character of the boolean's {@link String string value} capitalize.
     */
    public static @NonNull String formatBooleanCode(boolean value) {
        return value ? "True" : "False";
    }
}