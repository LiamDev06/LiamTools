package com.github.liamdev06.scheduler.handler;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.scheduler.interfaces.SchedulerAdapter;
import com.github.liamdev06.scheduler.interfaces.SchedulerTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Represents a handler to run scheduler repeating tasks.
 * This makes it possible to abstract task scheduling logic to separate classes.
 */
public abstract class AbstractSchedulerHandler {

    private final @NonNull String identifier;
    private final long initialTime, intervalTime;
    private final @NonNull TimeUnit unit;
    private @Nullable SchedulerTask task;

    public AbstractSchedulerHandler(@NonNull String identifier, long initialTime, long intervalTime, @NonNull TimeUnit unit) {
        this.identifier = identifier;
        this.initialTime = initialTime;
        this.intervalTime = intervalTime;
        this.unit = unit;
    }

    /**
     * {@link SchedulerTask} run code that will be executed on {@link AbstractSchedulerHandler#start(boolean)}.
     */
    public abstract void run();

    /**
     * Start a new repeating {@link SchedulerTask} in this handler.
     *
     * @param async If the task should run asynchronous or not.
     */
    protected void start(boolean async) {
        if (this.task != null) {
            throw new IllegalStateException("This runnable already has an active task");
        }

        final SchedulerAdapter schedulerAdapter = LPlugin.getInstance().getSchedulerAdapter();
        if (async) {
            this.task = schedulerAdapter.asyncRepeating(this::run, this.initialTime, this.intervalTime, this.unit);
        } else {
            this.task = schedulerAdapter.syncRepeating(this::run, this.initialTime, this.intervalTime, this.unit);
        }
    }

    /**
     * Stop the current {@link SchedulerTask} in this handler.
     */
    protected void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    /**
     * @return Internal identifier of this handler.
     */
    public @NonNull String getIdentifier() {
        return this.identifier;
    }
}