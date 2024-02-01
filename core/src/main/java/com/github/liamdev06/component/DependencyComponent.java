package com.github.liamdev06.component;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to give a component a dependency. A dependency must be another {@link Component}.
 * <p>
 * Classes annotated with {@link DependencyComponent} specify the required dependencies
 * for the annotated component to function properly. These are also the components that should be
 * instantiated before this annotated component.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DependencyComponent {

    /**
     * Array of classes representing the required {@link Component dependencies} for the annotated component.
     * <p>
     * These classes will be instantiated and registered to the {@link ComponentManager} before this annotated class,
     * so the annotated class can use them in its constructor without having to worry about order of instantiation.
     *
     * @return An array of classes representing the required dependencies.
     */
    @NonNull Class<? extends Component>[] value();
}