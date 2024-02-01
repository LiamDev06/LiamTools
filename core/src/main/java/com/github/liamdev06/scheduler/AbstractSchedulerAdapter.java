package com.github.liamdev06.scheduler;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.scheduler.interfaces.SchedulerAdapter;
import com.github.liamdev06.scheduler.interfaces.SchedulerTask;
import com.github.liamdev06.utils.java.LoggerUtil;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of {@link SchedulerAdapter} using {@link ScheduledExecutorService}. Handles the underlying scheduler and worker instances.
 * @see SchedulerAdapter for Javadocs on the implemented scheduler methods.
 */
public abstract class AbstractSchedulerAdapter extends SinglePointInitiator implements SchedulerAdapter {

    private static final @NonNull String WORKER_THREAD_PREFIX =  "liam-tools-worker-";
    private static final @NonNull String SCHEDULER_THREAD_NAME = "liam-tools-scheduler";

    private final @NonNull Logger logger;
    private final @NonNull ScheduledThreadPoolExecutor scheduler;
    private final @NonNull ForkJoinPool worker;

    public AbstractSchedulerAdapter(@NonNull LPlugin plugin) {
        this.logger = LoggerUtil.createLoggerWithIdentifier(plugin, this);
        this.scheduler = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = Executors.defaultThreadFactory()
                    .newThread(runnable);
            thread.setName(SCHEDULER_THREAD_NAME);
            return thread;
        });
        this.scheduler.setRemoveOnCancelPolicy(true);
        this.scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.worker = new ForkJoinPool(
                16,
                new WorkerThreadFactory(),
                new ExceptionHandler(this.logger),
                false
        );
    }

    @Override
    public @NonNull SchedulerTask asyncLater(@NonNull Runnable task, long delay, @NonNull TimeUnit unit) {
        ScheduledFuture<?> future = this.scheduler.schedule(() -> this.worker.execute(task), delay, unit);
        return () -> future.cancel(false);
    }

    @Override
    public @NonNull SchedulerTask asyncRepeating(@NonNull Runnable task, long initialDelay, long interval, @NonNull TimeUnit unit) {
        ScheduledFuture<?> future = this.scheduler.scheduleAtFixedRate(() -> this.worker.execute(task), initialDelay, interval, unit);
        return () -> future.cancel(false);
    }

    @Override
    public void shutdownScheduler() {
        this.scheduler.shutdown();

        try {
            if (!this.scheduler.awaitTermination(1, TimeUnit.MINUTES)) {
                this.logger.error("Timed out! Was waiting for thread '" + SCHEDULER_THREAD_NAME + "' to terminate.");
            }
        } catch (InterruptedException exception) {
            this.logger.error("Interrupted! Was waiting for thread '" + SCHEDULER_THREAD_NAME + "' to terminate.", exception);
        }
    }

    @Override
    public void shutdownExecutor() {
        this.worker.shutdown();

        final String formattedWorkerName = WORKER_THREAD_PREFIX.substring(0, WORKER_THREAD_PREFIX.length() - 1);
        try {
            if (!this.worker.awaitTermination(1, TimeUnit.MINUTES)) {
                this.logger.error("Timed out! Was waiting for worker '" + formattedWorkerName + "' to terminate.");
            }
        } catch (InterruptedException exception) {
            this.logger.error("Interrupted! Was waiting for worker '" + formattedWorkerName + "' to terminate.", exception);
        }
    }

    @Override
    public @NonNull Executor async() {
        return this.worker;
    }

    /**
     * @return Instance of the {@link ScheduledThreadPoolExecutor} used in this implementation.
     */
    public @NonNull ScheduledThreadPoolExecutor getScheduler() {
        return this.scheduler;
    }

    /**
     * @return Instance of the {@link ForkJoinPool} used in this implementation.
     */
    public @NonNull ForkJoinPool getWorker() {
        return this.worker;
    }

    /**
     * Factory for creating new worker executor threads.
     */
    private static final class WorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        private static final @NonNull AtomicInteger COUNT = new AtomicInteger(0);

        @Override
        public ForkJoinWorkerThread newThread(@NonNull ForkJoinPool pool) {
            ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            thread.setDaemon(true);
            thread.setName(WORKER_THREAD_PREFIX + COUNT.getAndIncrement());
            return thread;
        }
    }

    /**
     * Used to log exceptions that occur within threads.
     *
     * @param logger Instance of the {@link Logger} to log exception with.
     */
    private record ExceptionHandler(@NonNull Logger logger) implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(@NonNull Thread thread, @NonNull Throwable exception) {
            this.logger.error("An exception was caught in thread {}.", thread.getName(), exception);
        }
    }
}