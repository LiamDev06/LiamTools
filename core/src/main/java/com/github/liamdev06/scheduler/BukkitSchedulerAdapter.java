package com.github.liamdev06.scheduler;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.scheduler.interfaces.SchedulerAdapter;
import com.github.liamdev06.scheduler.interfaces.SchedulerTask;
import com.github.liamdev06.utils.bukkit.TickUtil;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Bukkit Implementation of {@link SchedulerAdapter} using {@link BukkitScheduler}.
 * <p>
 * @see SchedulerAdapter for Javadocs on the implemented scheduler methods.
 */
public class BukkitSchedulerAdapter extends AbstractSchedulerAdapter implements SchedulerAdapter {

    private final @NonNull Executor sync;
    private final @NonNull BukkitScheduler bukkitScheduler;
    private final @NonNull LPlugin plugin;

    public BukkitSchedulerAdapter(@NonNull LPlugin plugin) {
        super(plugin);
        final Server server = plugin.getServer();
        this.sync = runnable -> server.getScheduler().runTask(plugin, runnable);
        this.bukkitScheduler = server.getScheduler();
        this.plugin = plugin;
    }

    @Override
    public @NonNull SchedulerTask syncLater(@NonNull Runnable task, long delay, @NonNull TimeUnit unit) {
        int taskId = this.bukkitScheduler.runTaskLater(
                this.plugin,
                task,
                TickUtil.convertToTicks(delay, unit)
        ).getTaskId();
        return () -> this.bukkitScheduler.cancelTask(taskId);
    }

    @Override
    public @NonNull SchedulerTask syncRepeating(@NonNull Runnable task, long initialDelay, long interval, @NonNull TimeUnit timeUnit) {
        int taskId = this.bukkitScheduler.runTaskTimer(
                this.plugin,
                task,
                TickUtil.convertToTicks(initialDelay, timeUnit),
                TickUtil.convertToTicks(interval, timeUnit)
        ).getTaskId();
        return () -> this.bukkitScheduler.cancelTask(taskId);
    }

    @Override
    public @NonNull Executor sync() {
        return this.sync;
    }
}