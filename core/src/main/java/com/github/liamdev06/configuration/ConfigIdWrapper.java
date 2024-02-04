package com.github.liamdev06.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a configuration identifier key to find a configuration in {@link ConfigurationManager}.
 */
public interface ConfigIdWrapper {

    /**
     * @return The key as a string.
     */
    @NonNull String getKey();

}