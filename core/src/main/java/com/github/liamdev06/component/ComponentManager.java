package com.github.liamdev06.component;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.registry.RegistryFactory;
import com.github.liamdev06.registry.component.AutoRegisteringFeature;
import com.github.liamdev06.utils.java.LoggerUtil;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.util.*;

/**
 * Manages and stores all components within the target application.
 */
public class ComponentManager extends SinglePointInitiator implements AutoRegisteringFeature {

    private final @NonNull Logger logger;
    private final @NonNull Map<Class<? extends Component>, Component> components;
    private final @NonNull RegistryFactory registryFactory;

    public ComponentManager(@NonNull LPlugin entryPoint, @NonNull RegistryFactory registryFactory) {
        this.components = new HashMap<>();
        this.logger = LoggerUtil.createLoggerWithIdentifier(entryPoint, this);
        this.registryFactory = registryFactory;
        this.registryFactory.registerAutoRegisteringComponent(this);
    }

    /**
     * Registers a component by providing the class of the component to register.
     * This component will get auto-instantiated.
     *
     * @param componentClass The class of the component to register.
     */
    public void registerComponent(@NonNull Class<? extends Component> componentClass) {
        this.registerComponent(componentClass, null);
    }

    /**
     * Register a component by providing an instance of the component.
     *
     * @param componentInstance Instance of the component.
     */
    public void registerComponent(@NonNull Component componentInstance) {
        this.registerComponent(componentInstance.getClass(), componentInstance);
    }

    /**
     * Handles the logic for registering a component.
     * <p>
     * Depending on the provided parameters, the code will either only
     * map the provided instance to the provided class, or also create a new instance of the provided class.
     *
     * @param componentClass The class of the component.
     * @param componentInstance The instance of the component. If no instance exists and needs to be created, provide {@code null}.
     */
    private void registerComponent(@NonNull Class<? extends Component> componentClass, @Nullable Component componentInstance) {
        String name = componentClass.getSimpleName();

        if (componentInstance == null) {
            componentInstance = (Component) this.registryFactory.createEffectiveInstance(componentClass);

            // Instance returned null, exception log message is sent by registry factory
            if (componentInstance == null) {
                return;
            }
        }

        if (this.components.putIfAbsent(componentClass, componentInstance) != null) {
            throw new UnsupportedOperationException("Duplicate component registration of class " + name);
        }

        if (this.registryFactory.isLoggingEnabled(componentClass)) {
            this.logger.info("Registered component: " + name);
        }
    }

    /**
     * Gets an instance of a component by providing its class.
     *
     * @param componentClass The class of the component to get.
     * @return Instance of the target {@link Component}.
     */
    public <T extends Component> @Nullable T getComponent(@NonNull Class<T> componentClass) {
        if (!this.components.containsKey(componentClass)) {
            return null;
        }
        return componentClass.cast(this.components.get(componentClass));
    }

    /**
     * @return All registered components.
     */
    public @NonNull Map<Class<? extends Component>, Component> getComponents() {
        return this.components;
    }
    
    @Override
    public void executeAutoRegistering(@NonNull RegistryFactory registryFactory) {
        ComponentSorter componentSorter = new ComponentSorter(registryFactory.getClassesWithRegistryType(Component.class, Component.class));
        for (Class<? extends Component> component : componentSorter.sort()) {
            this.registerComponent(component);
        }
    }

    /**
     * Enables all registered and instantiated {@link Component components} that
     * are marked to be automatically enabled with {@link Component#canAutoEnable()}.
     */
    public void enableAllComponents() {
        for (Component component : this.components.values()) {
            if (component.canAutoEnable()) {
                component.onEnable();
            }
        }
    }

    /**
     * Disables all registered and instantiated {@link Component components} that
     * are marked to be automatically disabled with {@link Component#canAutoDisable()}.
     */
    public void disableAllComponents() {
        for (Component component : this.components.values()) {
            if (component.canAutoDisable()) {
                component.onDisable();
            }
        }
    }

    /**
     * Helper class for sorting components based on their dependencies.
     * This class provides methods to sort a set of components in a way that ensures
     * components with dependencies are registered after their dependencies.
     */
    protected static class ComponentSorter {
        private final @NonNull Map<Class<? extends Component>, List<Class<? extends Component>>> componentDependencies;

        /**
         * Constructs a new component sorter with the provided set of components.
         * A component may be annotated with {@link DependencyComponent} which means it has a dependencies to it.
         *
         * @param components A set of classes representing components.
         */
        public ComponentSorter(@NonNull Set<Class<? extends Component>> components) {
            final Map<Class<? extends Component>, List<Class<? extends Component>>> componentDependencies = new HashMap<>();

            for (Class<? extends Component> component : components) {
                List<Class<? extends Component>> dependencies;

                if (component.isAnnotationPresent(DependencyComponent.class)) {
                    dependencies = Arrays.asList(component.getAnnotation(DependencyComponent.class).value());
                } else {
                    dependencies = Collections.emptyList();
                }

                componentDependencies.put(component, dependencies);
            }

            this.componentDependencies = componentDependencies;
        }

        /**
         * Sorts the components based on their dependencies and returns the sorted list.
         * <p>
         * The sorted list can be used when instantiating and registering components to make
         * sure they are instantiated in the correct order.
         *
         * @return A list of classes representing components, sorted based on their dependencies.
         */
        public @NonNull List<Class<? extends Component>> sort() {
            final Set<Class<? extends Component>> visited = new HashSet<>();
            final Deque<Class<? extends Component>> stack = new LinkedList<>();

            // Visit each component if not already visited
            for (Class<? extends Component> componentClass : this.componentDependencies.keySet()) {
                if (!visited.contains(componentClass)) {
                    this.visit(componentClass, visited, stack);
                }
            }

            // Return the list as classes but reverse to get correct registration order
            List<Class<? extends Component>> list = new ArrayList<>();
            while (!stack.isEmpty()) {
                list.add(stack.pop());
            }
            Collections.reverse(list);
            return list;
        }

        /**
         * Recursively visits the components and their dependencies, adding them to the stack in correct order.
         *
         * @param componentClass The component class to be visited.
         * @param visited A set to keep track of visited components.
         * @param stack A stack to store components in the correct order.
         */
        private void visit(@NonNull Class<? extends Component> componentClass,
                           @NonNull Set<Class<? extends Component>> visited,
                           @NonNull Deque<Class<? extends Component>> stack) {
            visited.add(componentClass);

            List<Class<? extends Component>> dependencies = this.componentDependencies.get(componentClass);
            if (dependencies != null) {
                for (Class<? extends Component> dependency : dependencies) {
                    if (visited.contains(dependency)) {
                        continue;
                    }
                    this.visit(dependency, visited, stack);
                }
            }

            stack.push(componentClass);
        }
    }
}