package com.github.liamdev06.utils.bukkit.legacy;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for working with legacy {@link Component}.
 */
@UtilityClass
public class LegacyComponentUtil {

    /**
     * Turns a {@link Component} into a {@link String}.
     *
     * @param component The Component to turn to a String.
     * @return String version of the input Component.
     */
    public static @NonNull String toString(@Nullable Component component) {
        if (component == null) {
            return "";
        }
        return LegacyPlayerMessenger.AMPERSAND.serialize(component);
    }

    /**
     * Turns a {@link String} into a {@link Component}.
     *
     * @param text The String text input to turn to a Component.
     * @return Component version of the input String.
     */
    public static @NonNull Component toComponent(@Nullable String text) {
        if (text == null) {
            return Component.empty();
        }
        return LegacyPlayerMessenger.AMPERSAND.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Turns a {@link List<Component>} into a {@link String<String>}.
     *
     * @param componentList The input list to make an identical copy but with {@link String} as type.
     * @return {@link List<String>} but with the same content as the input component list.
     */
    public static @NonNull List<String> toStringList(@Nullable List<Component> componentList) {
        if (componentList == null || componentList.isEmpty()) {
            return Collections.emptyList();
        }
        return componentList.stream()
                .map(LegacyComponentUtil::toString)
                .toList();
    }

    /**
     * Turns a {@link List<String>} into a {@link String<Component>}.
     *
     * @param stringList The input list to make an identical copy but with {@link Component} as type.
     * @return {@link List<Component>} but with the same content as the input string list.
     */
    public static @NonNull List<Component> toComponentList(@Nullable List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return Collections.emptyList();
        }
        return stringList.stream()
                .map(LegacyComponentUtil::toComponent)
                .toList();
    }
}