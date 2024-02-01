package com.github.liamdev06.scheduler.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A scheduler for running synchronous and asynchronous tasks.
 */
public interface SchedulerAdapter {

    /**
     * Performs the input {@link Runnable task} synchronously.
     *
     * @param task The task to perform.
     */
    default void executeSync(@NonNull Runnable task) {
        this.sync().execute(task);
    }

    /**
     * Performs the input {@link Runnable task} asynchronously.
     *
     * @param task The task to perform.
     */
    default void executeAsync(@NonNull Runnable task) {
        this.async().execute(task);
    }

    /**
     * Executes the given {@link Runnable task} with a delay synchronously.
     *
     * @param task The task to perform.
     * @param delay The delay before the task is executed.
     * @param unit The {@link TimeUnit} to use for the {@param delay}.
     * @return Instance of the task perform in a {@link SchedulerTask}.
     */
    @NonNull SchedulerTask syncLater(@NonNull Runnable task, long delay, @NonNull TimeUnit unit);

    /**
     * Executes the given {@link Runnable task} repeatedly synchronously with a given interval.
     *
     * @param task The task to perform.
     * @param initialDelay The initial delay before the repeating sequence starts.
     * @param sequenceInterval The interval between each repeating sequence.
     * @param unit The {@link TimeUnit} for the {@param interval}.
     * @return Instance of the task perform in a {@link SchedulerTask}.
     */
    @NonNull SchedulerTask syncRepeating(@NonNull Runnable task, long initialDelay, long sequenceInterval, @NonNull TimeUnit unit);

    /**
     * Executes the given {@link Runnable task} with a delay asynchronously.
     *
     * @param task The task to perform.
     * @param delay The delay before the task is executed.
     * @param unit The {@link TimeUnit} to use for the {@param delay}.
     * @return Instance of the task perform in a {@link SchedulerTask}.
     */
    @NonNull SchedulerTask asyncLater(@NonNull Runnable task, long delay, @NonNull TimeUnit unit);

    /**
     * Executes the given {@link Runnable task} repeatedly asynchronously with a given interval.
     *
     * @param task The task to perform.
     * @param initialDelay The initial delay before the repeating sequence starts.
     * @param sequenceInterval The interval between each repeating sequence.
     * @param unit The {@link TimeUnit} for the {@param interval}.
     * @return Instance of the task perform in a {@link SchedulerTask}.
     */
    @NonNull SchedulerTask asyncRepeating(@NonNull Runnable task, long initialDelay, long sequenceInterval, @NonNull TimeUnit unit);

    /**
     * Shuts down the scheduler used for this adapter.
     */
    void shutdownScheduler();

    /**
     * Shuts down the executor used for this adapter.
     */
    void shutdownExecutor();

    /**
     * Shutdown down both the scheduler and executor.
     */
    default void shutdown() {
        this.shutdownScheduler();
        this.shutdownExecutor();
    }

    /**
     * @return A synchronous {@link Executor} instance.
     */
    @NonNull Executor sync();

    /**
     * @return An asynchronous {@link Executor} instance.
     */
    @NonNull Executor async();
}