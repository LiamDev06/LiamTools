package com.github.liamdev06.registry;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.component.Component;
import com.github.liamdev06.registry.component.AutoRegisteringFeature;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory for instantiating classes and registering their instances.
 */
public class RegistryFactory extends SinglePointInitiator {

    private final @NonNull Reflections reflections;
    private final @NonNull Map<Class<?>, Object> registry;
    private final @NonNull Object mainClassInstance;
    private final @NonNull Class<?> mainClass;

    private final @NonNull Set<AutoRegisteringFeature> autoRegisteringComponents;

    public <T extends LPlugin> RegistryFactory(@NonNull Reflections reflections, @NonNull T mainClassInstance) {
        this.reflections = reflections;
        this.registry = new HashMap<>();
        this.mainClassInstance = mainClassInstance;
        this.mainClass = mainClassInstance.getClass();
        this.autoRegisteringComponents = new LinkedHashSet<>();
    }

    /**
     * Creates a new instance of the provided class or returns an existing instance of the class
     * if it has already been instantiated before. If an instance could not be created, null is returned.
     * <p>
     * The constructor of the provided class has to be either empty or only have the {@link #mainClassInstance} as a parameter.
     *
     * @param clazz The {@link Class} to create an instance of, or return an existing instance.
     * @return The newly created or already existing instance.
     */
    public @Nullable Object createEffectiveInstance(@NonNull Class<?> clazz) {
        Object classInstance = this.getClassInstance(clazz);
        if (classInstance != null) {
            return classInstance;
        }
        return this.createInstance(clazz);
    }

    /**
     * Creates a new instance by initializing the constructor of the provided class.
     * If the instance could not be created, null is returned.
     * <p>
     * The constructor of the provided class has to be either empty or only have the {@link #mainClassInstance} as a parameter.
     *
     * @param clazz The {@link Class} to create an instance of.
     * @return The created instance.
     */
    public @Nullable Object createInstance(@Nullable Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        final String displayName = clazz.getSimpleName() + " (" + clazz.getPackageName() + ")";

        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0) {
                throw new NoSuchMethodException("Class " + displayName + " has no public constructor.");
            }
            if (constructors.length > 1) {
                throw new RuntimeException("Class " + displayName + " has multiple public constructors. This cannot be used with RegistryFactory.");
            }

            Object instance;
            Constructor<?> constructor = constructors[0];

            if (constructor.getParameterCount() == 0) {
                instance = constructor.newInstance();
            } else {
                instance = clazz.getConstructor(this.mainClass).newInstance(this.mainClassInstance);
            }

            if (this.registry.putIfAbsent(clazz, instance) != null) {
                throw new UnsupportedOperationException("Duplicate class registration of class " + displayName + ".");
            }
            return instance;
        } catch (Exception exception) {
            throw new RuntimeException("Registry Factory could not create an instance for class " + clazz.getSimpleName(), exception);
        }
    }

    /**
     * Checks if the provided parameter class with {@link AutoRegister} annotation present has
     * {@code shouldLog} set to {@code true} or {@code false}.
     *
     * @param clazz The target {@link Class} to check for.
     * @return If the target class with {@link AutoRegister} present should log or not.
     */
    public boolean isLoggingEnabled(@NonNull Class<?> clazz) {
        if (clazz.isAnnotationPresent(AutoRegister.class)) {
            return clazz.getAnnotation(AutoRegister.class).shouldLog();
        }
        return false;
    }

    /**
     * Registers an {@link AutoRegisteringFeature}.
     *
     * @param component Instance of the component that supports auto registering its managed features.
     */
    public void registerAutoRegisteringComponent(@NonNull AutoRegisteringFeature component) {
        this.autoRegisteringComponents.add(component);
    }

    /**
     * Executes the implementation of all {@link AutoRegisteringFeature} which instantiates all auto registering classes.
     */
    public void executeAllAutoRegistering() {
        this.autoRegisteringComponents.forEach(component -> component.executeAutoRegistering(this));
    }

    /**
     * @param clazz The class whose instance to get.
     * @return The instance of the provided {@param clazz}. Returns {@code null} if non is present.
     */
    public @Nullable Object getClassInstance(@NonNull Class<?> clazz) {
        return this.registry.get(clazz);
    }

    /**
     * Gets all classes annotated with {@link AutoRegister} and then
     * filters out to only return the ones that matches the method parameter input class.
     *
     * @param registerClass The type of auto registered classes to return.
     * @return Set of classes that are marked with {@link AutoRegister} and has the input class type.
     */
    public @NonNull Set<Class<?>> getClassesWithRegistryType(@NonNull Class<?> registerClass) {
        return this.reflections.getTypesAnnotatedWith(AutoRegister.class)
                .stream()
                .filter(foundClass -> Arrays.stream(foundClass.getAnnotation(AutoRegister.class).value()).toList().contains(registerClass))
                .filter(foundClass -> registerClass.isAssignableFrom(foundClass) || foundClass.equals(registerClass))
                .collect(Collectors.toSet());
    }

    /**
     * Gets all classes annotated with {@link AutoRegister} and that extends the parameter {@code registerClass}.
     * The method also filters out to only return the ones that matches the input registering class.
     *
     * @param registerClass The type of auto registered classes to return.
     * @param extendClass The {@link Class<T>} that the found auto registered classes also must extend.
     * @return Set of classes that are marked with {@link AutoRegister}, extends {@link Class<T>} and has the {@param type} type value.
     */
    public <T> @NonNull Set<Class<? extends T>> getClassesWithRegistryType(@NonNull Class<?> registerClass, @NonNull Class<T> extendClass) {
        return this.getClassesWithRegistryType(registerClass)
                .stream()
                .filter(extendClass::isAssignableFrom)
                .map(foundClass -> (Class<? extends T>) foundClass)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all classes annotated with parameter {@code annotation}.
     *
     * @param annotation The {@link Annotation} to filter classes to return.
     * @return Set of classes that are annotated with the parameter {@code annotation}.
     */
    public @NonNull Set<Class<?>> getClassesWithAnnotation(@NonNull Class<? extends Annotation> annotation) {
        return this.reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * Gets all classes annotated with the parameter {@code annotation}.
     *
     * @param annotation The {@link Annotation} to filter classes to return.
     * @param extendClass The {@link Class} the classes marked with the annotation also must extend.
     * @return Set of classes that are marked with the parameter {@code annotation} and that extend the specific extend class.
     */
    public <T> @NonNull Set<Class<? extends T>> getClassesWithAnnotation(@NonNull Class<? extends Annotation> annotation, @NonNull Class<T> extendClass) {
        return this.getClassesWithAnnotation(annotation)
                .stream()
                .filter(extendClass::isAssignableFrom)
                .map(foundClass -> (Class<? extends T>) foundClass)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all classes that implements the provided interface.
     *
     * @param interfaceClass Interface class to implement.
     * @return Set of classes that implement the {@code interfaceClass}.
     */
    public <T> @NonNull Set<Class<? extends T>> getClassesImplementing(@NonNull Class<T> interfaceClass) {
        return this.reflections.getSubTypesOf(interfaceClass);
    }
}