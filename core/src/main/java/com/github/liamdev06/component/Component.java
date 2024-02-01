package com.github.liamdev06.component;

import com.github.liamdev06.LPlugin;

/**
 * Represents a component within the code-base, like a manager class.
 */
public interface Component {

    /**
     * Called when the component is being enabled.
     * <p>
     * This happens after an instance of the component has been created and as the last part
     * in the application startup process before {@link LPlugin#onStartup()} is triggered.
     */
    default void onEnable() { }

    /**
     * Called when the component is being disabled.
     * <p>
     * This happens after {@link LPlugin#onShutdown()} has been called.
     */
    default void onDisable() { }

    /**
     * If the parent application should automatically call the {@link #onEnable()} method for this component.
     * <p>
     * Otherwise, the parent application needs to manually enable this component through {@link Component#onEnable()}.
     *
     * @return If this component is automatically enabled or not. By default, this is {@code true}.
     */
    default boolean canAutoEnable() {
        return true;
    }

    /**
     * If the parent application should automatically call the {@link #onDisable()} method for this component.
     * <p>
     * Otherwise, the parent application needs to manually disable this component through {@link Component#onDisable()}.
     *
     * @return If this component is automatically disabled or not. By default, this is {@code true}.
     */
    default boolean canAutoDisable() {
        return true;
    }
}