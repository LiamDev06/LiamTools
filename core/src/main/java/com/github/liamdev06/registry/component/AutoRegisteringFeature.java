package com.github.liamdev06.registry.component;

import com.github.liamdev06.registry.RegistryFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a component that has support for auto registering its child-classes and features.
 */
@FunctionalInterface
public interface AutoRegisteringFeature {

    /**
     * Auto registers all child-classes that the parent component implementing this interface handles/supports.
     *
     * @param registryFactory Instance of the {@link RegistryFactory} used for instantiation.
     */
    void executeAutoRegistering(@NonNull RegistryFactory registryFactory);

}