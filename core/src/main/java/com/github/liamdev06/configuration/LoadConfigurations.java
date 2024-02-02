package com.github.liamdev06.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate to specify which configuration files to load in on application start up into {@link ConfigurationManager}.
 * <p>
 * This annotation can only be annotated on the class implementing {@link com.github.liamdev06.LPlugin}, and
 * the {@link ConfigurationManager} must be instantiated in the same class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadConfigurations {

    /**
     * @return Array of {@link String} file names corresponding to the configuration files to load in.
     *         Only loads in embedded configurations located in the JAR's {@code /resources} directory.
     *         <p>
     *         If the file is not located in the root resources directory, also include the added path.
     */
    @NonNull String[] value();
}