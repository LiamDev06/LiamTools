package com.github.liamdev06.utils.java;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a variable that can hold a key replacement and the replacement value.
 * This can for example be used to add content replacements in a player message or description replacements in an item.
 * <p>
 * @param replace The {@link String string} to replace with, without the delimiters. For example 'player' (becoming <\player>) or 'title' (becoming <\title>).
 * @param replaceWith The {@link Object object} to replace the replacement string with. For example replacing the <player> with "Player#getName"
 */
public record ContentVariable(@NonNull String replace, @NonNull Object replaceWith) {

    public ContentVariable(@NonNull String replace, @NonNull Object replaceWith) {
        this.replace = "<" + replace + ">";
        this.replaceWith = replaceWith;
    }

    /**
     * @return The {@link #replaceWith replace with content} but as a {@link String string} instead of an {@link Object object}.
     */
    public @NonNull String stringReplaceWith() {
        return String.valueOf(this.replaceWith);
    }

    /**
     * Turns an array of {@link ContentVariable} into a key-value map.
     * The key being the {@link #replace} and the value being the {@link #replaceWith}.
     *
     * @param variables An array of {@link ContentVariable} to turn into a map.
     * @return A {@link Map} with {@link ContentVariable} entries.
     */
    public static @NonNull Map<String, Object> toMap(@NonNull ContentVariable[] variables) {
        final Map<String, Object> map = new HashMap<>();

        for (ContentVariable variable : variables) {
            map.putIfAbsent(variable.replace.substring(0, variable.replace.length() - 1), variable.replaceWith);
        }
        return map;
    }
}