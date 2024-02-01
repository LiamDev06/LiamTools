package com.github.liamdev06.registry;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a class to automatically initialize its constructor which also automatically registers the class to {@link RegistryFactory}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {

    /**
     * @return The class to instantiate and register.
     */
    @NonNull Class<?>[] value();

    /**
     * If the {@link RegistryFactory} should log registration for the {@link #value()} being auto registered.
     *
     * @return {@code true} if {@link RegistryFactory} should log registration for the provided auto registering class, {@code false} otherwise.
     *         By default, this is {@code true}.
     */
    boolean shouldLog() default true;

}