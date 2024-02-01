package com.github.liamdev06.scheduler.interfaces;

/**
 * Represents a scheduled task.
 */
@FunctionalInterface
public interface SchedulerTask {

    /**
     * Cancels and stops this task.
     */
    void cancel();

}