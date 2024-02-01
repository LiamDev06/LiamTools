package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility class for working with- and manipulating enums.
 */
@UtilityClass
public class EnumUtil {

    /**
     * Nicely formats an {@code Enum} constant.
     *
     * @param value {@link Enum} constant to format.
     * @return Formatted {@link Enum} constant as a {@link String}.
     */
    public static @NonNull String formatEnum(@NonNull Enum<?> value) {
        return StringUtil.capitalize(value.name().replaceAll("_", " "));
    }
}