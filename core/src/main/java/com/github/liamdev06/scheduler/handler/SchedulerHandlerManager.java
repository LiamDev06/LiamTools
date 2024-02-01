package com.github.liamdev06.scheduler.handler;

import com.github.liamdev06.registry.RegistryFactory;
import com.github.liamdev06.registry.component.AutoRegisteringFeature;
import com.github.liamdev06.scheduler.annotation.AutoStartSchedulerHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Scheduler handler manager that manages and stores all {@link AbstractSchedulerHandler}s.
 */
public class SchedulerHandlerManager implements AutoRegisteringFeature {

    private final @NonNull Map<String, AbstractSchedulerHandler> handlers = new HashMap<>();

    public SchedulerHandlerManager(@NonNull RegistryFactory registryFactory) {
        registryFactory.registerAutoRegisteringComponent(this);
    }

    @Override
    public void executeAutoRegistering(@NonNull RegistryFactory registryFactory) {
        for (Class<?> clazz : registryFactory.getClassesWithRegistryType(AbstractSchedulerHandler.class)) {
            AbstractSchedulerHandler handler = (AbstractSchedulerHandler) registryFactory.createEffectiveInstance(clazz);
            if (handler == null) {
                continue;
            }
            this.registerHandler(handler);
        }
    }

    /**
     * Automatically starts all {@link AbstractSchedulerHandler} that are annotated with {@link AutoStartSchedulerHandler}.
     */
    public void startAllAutoSchedulers() {
        for (AbstractSchedulerHandler handler : this.handlers.values()) {
            Class<? extends AbstractSchedulerHandler> handlerClass = handler.getClass();

            if (handlerClass.isAnnotationPresent(AutoStartSchedulerHandler.class)) {
                boolean async = handlerClass.getAnnotation(AutoStartSchedulerHandler.class).async();
                this.start(handler.getIdentifier(), async);
            }
        }
    }

    /**
     * Manually register a new scheduler handler.
     *
     * @param handler The {@link AbstractSchedulerHandler} to register.
     */
    public void registerHandler(@NonNull AbstractSchedulerHandler handler) {
        this.handlers.putIfAbsent(handler.getIdentifier(), handler);
    }

    /**
     * Start a new task within a {@link AbstractSchedulerHandler} that runs synchronously on the main thread.
     *
     * @param identifier Internal identifier of the {@link AbstractSchedulerHandler} to start a task in.
     * @see SchedulerHandlerManager#start(String, boolean)
     */
    public void start(@NonNull String identifier) {
        this.start(identifier, false);
    }

    /**
     * Start a new task within a {@link AbstractSchedulerHandler}.
     *
     * @param identifier Internal identifier of the {@link AbstractSchedulerHandler} to start a task in.
     * @param async If the task should run asynchronously or not.
     */
    public void start(@NonNull String identifier, boolean async) {
        if (this.handlers.containsKey(identifier)) {
            AbstractSchedulerHandler handler = this.handlers.get(identifier);
            handler.start(async);
        }
    }

    /**
     * Stop a running task running within a {@link AbstractSchedulerHandler}.
     *
     * @param identifier Internal identifier of the {@link AbstractSchedulerHandler}.
     */
    public void stop(@NonNull String identifier) {
        if (this.handlers.containsKey(identifier)) {
            AbstractSchedulerHandler handler = this.handlers.get(identifier);
            handler.stop();
        }
    }
}