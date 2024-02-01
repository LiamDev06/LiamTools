package com.github.liamdev06.utils.java;

import com.github.liamdev06.LPlugin;
import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating loggers.
 */
@UtilityClass
public class LoggerUtil {

    /**
     * Creates a new logger using the given name.
     *
     * @param name The logger name.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLogger(@NonNull String name) {
        return LoggerFactory.getLogger(name);
    }

    /**
     * Creates a new logger for the given class.
     *
     * @param clazz The class to create the logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLogger(@NonNull Class<?> clazz) {
        return createLogger(clazz.getSimpleName());
    }

    /**
     * Creates a new logger for the given object.
     *
     * @param target The target object to have logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLogger(@NonNull Object target) {
        return createLogger(target.getClass().getSimpleName());
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-LoggerName" using the given name.
     *
     * @param applicationIdentifier Internal identifier of the application to prefix the logger name with.
     * @param name The logger name.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull String applicationIdentifier, @NonNull String name) {
        return createLogger(applicationIdentifier + "-" + name);
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-LoggerName" using the given name.
     *
     * @param entryPoint Instance of the {@link LPlugin} to retrieve the {@link LPlugin#getPluginIdentifier()} from.
     * @param name The logger name.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull LPlugin entryPoint, @NonNull String name) {
        return createLoggerWithIdentifier(entryPoint.getPluginIdentifier(), name);
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-ClassName" using the given class name.
     *
     * @param applicationIdentifier Internal identifier of the application to prefix the logger name with.
     * @param clazz The class to create the logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull String applicationIdentifier, @NonNull Class<?> clazz) {
        return LoggerFactory.getLogger(applicationIdentifier + "-" + clazz.getSimpleName());
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-ClassName" using the given class name.
     *
     * @param entryPoint Instance of the {@link LPlugin} to retrieve the {@link LPlugin#getPluginIdentifier()} from.
     * @param clazz The class to create the logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull LPlugin entryPoint, @NonNull Class<?> clazz) {
        return createLoggerWithIdentifier(entryPoint.getPluginIdentifier(), clazz);
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-ObjectName" using the given object name.
     *
     * @param applicationIdentifier Internal identifier of the application to prefix the logger name with.
     * @param target The target object to create the logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull String applicationIdentifier, @NonNull Object target) {
        return LoggerFactory.getLogger(applicationIdentifier + "-" + target.getClass().getSimpleName());
    }

    /**
     * Creates a new logger using the format of "ApplicationIdentifier-ObjectName" using the given object name.
     *
     * @param entryPoint Instance of the {@link LPlugin} to retrieve the {@link LPlugin#getPluginIdentifier()} from.
     * @param target The target object to create the logger for.
     * @return A {@link Logger} instance.
     */
    public static @NonNull Logger createLoggerWithIdentifier(@NonNull LPlugin entryPoint, @NonNull Object target) {
        return createLoggerWithIdentifier(entryPoint.getPluginIdentifier(), target);
    }
}