package com.github.liamdev06.scheduler.annotation;

import com.github.liamdev06.registry.AutoRegister;
import com.github.liamdev06.scheduler.handler.AbstractSchedulerHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used on {@link AbstractSchedulerHandler} to mark that
 * the scheduler should be automatically started on plugin initialization/startup.
 * <p>
 * By using this, there is no need to manually start the scheduler.
 * the scheduler in the {@link com.github.liamdev06.LPlugin#onStartup()}.
 * <p>
 * To use this annotation, the class using must also annotate with {@link AutoRegister} for {@link AbstractSchedulerHandler}.
 * This is to ensure that the scheduler is properly initialized, so it can actually start.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoStartSchedulerHandler {

    /**
     * Set if the scheduler handler should run asynchronously.
     * By default, the scheduler will run synchronous on the main thread.
     *
     * @return True if the scheduler task should run asynchronously.
     */
    boolean async() default false;
}