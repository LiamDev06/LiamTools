package com.github.liamdev06.scheduler.timer;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.scheduler.interfaces.SchedulerAdapter;
import com.github.liamdev06.scheduler.interfaces.SchedulerTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Represents a timer that can create and handle time related intervals.
 */
public class SimpleTimer {

    /**
     * Default interval time in seconds.
     */
    public static final long DEFAULT_INTERVAL_TIME = 1;

    /**
     * Default {@link TimeUnit} for the interval time.
     */
    public static final @NonNull TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private final @NonNull String identifier;
    private final int startingValue;
    private final int stopValue;
    private int timer;
    private @Nullable SchedulerTask task;

    /**
     * Creates a new timer with a specified identifier and starting value.
     *
     * @param identifier Unique identifier for the timer.
     * @param startingValue Initial value of the timer.
     */
    public SimpleTimer(@NonNull String identifier, int startingValue) {
        this(identifier, startingValue, -1);
    }

    /**
     * Constructs a timer with a specified identifier, starting value, and stop value.
     *
     * @param identifier Unique identifier for the timer.
     * @param startingValue Initial value of the timer.
     * @param stopValue Value at which the timer should stop, or -1 if there's no stop value.
     *                  This is only applicable when using {@link #startTimer(TimeChange)}.
     */
    public SimpleTimer(@NonNull String identifier, int startingValue, int stopValue) {
        this.identifier = identifier;
        this.startingValue = startingValue;
        this.timer = startingValue;
        this.stopValue = stopValue;
    }

    /**
     * Starts the timer with the specified change type.
     *
     * @param change The type of change to perform on the timer (INCREMENT or DECREMENT).
     * @return The {@link SchedulerTask} associated with the timer.
     */
    public @NonNull SchedulerTask startTimer(TimeChange change) {
        return this.startTimer(change, false);
    }

    /**
     * Starts the timer with the specified change type and execution mode, sync or async.
     *
     * @param change The type of change to perform on the timer (INCREMENT or DECREMENT).
     * @param async Indicates whether the timer task should run asynchronously or not.
     * @return The {@link SchedulerTask} associated with the timer.
     */
    public @NonNull SchedulerTask startTimer(TimeChange change, boolean async) {
        // Only one task can run at the same time
        if (this.task != null) {
            throw new IllegalStateException("There is already an active task running in " + this.getClass().getSimpleName());
        }

        final Runnable task = () -> {
            int current;

            // Increment or decrement timer
            if (change == TimeChange.INCREMENT) {
                current = this.increment();
            } else {
                current = this.decrement();
            }

            // Stop the timer
            if (this.stopValue != -1 && current == this.stopValue) {
                this.stopTimer();
            }
        };

        final SchedulerAdapter adapter = LPlugin.getInstance().getSchedulerAdapter();
        if (async) {
            return this.task = adapter.asyncRepeating(task, DEFAULT_INTERVAL_TIME, DEFAULT_INTERVAL_TIME, DEFAULT_TIME_UNIT);
        } else {
            return this.task = adapter.syncRepeating(task, DEFAULT_INTERVAL_TIME, DEFAULT_INTERVAL_TIME, DEFAULT_TIME_UNIT);
        }
    }

    /**
     * Stops the timer task associated with this timer.
     */
    public void stopTimer() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    /**
     * Gets the unique identifier of this timer.
     *
     * @return The unique identifier of the timer.
     */
    public @NonNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * Force sets the timer value at a specific value.
     *
     * @param value The value to set the timer at.
     */
    public void set(int value) {
        this.timer = value;
    }

    /**
     * Increments the timer by 1.
     *
     * @return The updated value of the timer after incrementing.
     */
    public int increment() {
        return ++this.timer;
    }

    /**
     * Increments the timer by the provided value.
     *
     * @param value The value to increment the timer by.
     * @return The updated value of the timer after incrementing.
     */
    public int incrementBy(int value) {
        return this.timer += value;
    }

    /**
     * Decrements the timer by 1.
     *
     * @return The updated value of the timer after decrementing.
     */
    public int decrement() {
        return --this.timer;
    }

    /**
     * Decrements the timer by the provided value.
     *
     * @param value The value to decrement the timer by.
     * @return The updated value of the timer after decrementing.
     */
    public int decrementBy(int value) {
        return this.timer -= value;
    }

    /**
     * Resets the timer to its starting value.
     *
     * @return The starting value of the timer after resetting.
     */
    public int reset() {
        return this.timer = this.startingValue;
    }

    /**
     * Gets the current value of the timer.
     *
     * @return The current value of the timer.
     */
    public int get() {
        return this.timer;
    }

    /**
     * Gets the starting value of the timer.
     *
     * @return The starting timer value.
     */
    public int getStartingValue() {
        return this.startingValue;
    }

    /**
     * Gets the stop value of the timer.
     *
     * @return The stop timer value.
     *         This is only applicable if it does not return {@code -1}.
     */
    public int getStopValue() {
        return this.stopValue;
    }

    /**
     * Enum representing the types of changes that can be applied to the timer (INCREMENT or DECREMENT).
     */
    public enum TimeChange {
        /**
         * Increment the timer value.
         */
        INCREMENT,
        /**
         * Decrement the timer value.
         */
        DECREMENT
    }
}