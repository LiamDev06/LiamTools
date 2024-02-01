package com.github.liamdev06.utils.bukkit;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.registry.AutoRegister;
import com.github.liamdev06.registry.RegistryFactory;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Helper class for automatically registering and instantiating all Bukkit {@link Listener} with {@link RegistryFactory}.
 * Listeners are also registered into the {@link PluginManager}.
 */
public class ListenerRegistryFactory {

    private final @NonNull LPlugin plugin;

    public ListenerRegistryFactory(@NonNull LPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Instantiates and registers all listeners annotated with {@link AutoRegister}.
     */
    public void registerAllListeners() {
        final RegistryFactory registryFactory = this.plugin.getRegistryFactory();
        final PluginManager pluginManager = this.plugin.getServer().getPluginManager();

        for (Class<? extends Listener> listenerClass : registryFactory.getClassesWithRegistryType(Listener.class, Listener.class)) {
            Object listener = registryFactory.createEffectiveInstance(listenerClass);
            if (listener != null) {
                pluginManager.registerEvents((Listener) listener, this.plugin);
            }
        }
    }
}