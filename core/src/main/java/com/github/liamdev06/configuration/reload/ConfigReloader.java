package com.github.liamdev06.configuration.reload;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.registry.RegistryFactory;
import com.github.liamdev06.utils.java.LoggerUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public class ConfigReloader {

    private final Logger logger;

    private final @NonNull LPlugin plugin;
    private final @NonNull RegistryFactory registryFactory;

    public ConfigReloader(@NonNull LPlugin plugin) {
        this.logger = LoggerUtil.createLoggerWithIdentifier(plugin, this);
        this.plugin = plugin;
        this.registryFactory = plugin.getRegistryFactory();
    }

    public void reload() {
        this.plugin.getConfigurationManager().reloadConfigurations();

        this.registryFactory.getClassesImplementing(ConfigReloadable.class).forEach(clazz -> {
            ConfigReloadable reloadable = (ConfigReloadable) this.registryFactory.createEffectiveInstance(clazz);
            if (reloadable == null) {
                this.logger.warn("Could not reload class: {}", clazz.getName());
            } else {
                reloadable.loadConfig(this.plugin);
            }
        });
    }
}