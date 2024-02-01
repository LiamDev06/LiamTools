package com.github.liamdev06.utils.java;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Extend a subclass with this class to create a "single point of initiation" for the inheriting (sub) class.
 * <p>
 * This makes it possible for the inheriting class to only be instantiated one single time, and never again in the same lifespan.
 * This can be useful for APIs and libraries where certain classes should only be instantiated once within the parent library but
 * never instantiated within the application using the library, although be accessed.
 */
public class SinglePointInitiator {

    private static final @NonNull Set<String> INSTANTIATED = new HashSet<>();

    public SinglePointInitiator() {
        final String key = this.getKey();
        if (INSTANTIATED.contains(key)) {
            throw new IllegalStateException("This class '" + this.getClass().getSimpleName() + "' has a single point initiator and has already been instantiated once!");
        } else {
            INSTANTIATED.add(key);
        }
    }

    /**
     * @return If the {@link #getClass() inheriting class} has been instantiated or not.
     */
    public boolean isInstantiated() {
        return INSTANTIATED.contains(this.getKey());
    }

    private @NonNull String getKey() {
        return this.getClass().getName();
    }
}