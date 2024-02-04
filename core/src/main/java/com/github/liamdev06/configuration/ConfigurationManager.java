package com.github.liamdev06.configuration;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.utils.bukkit.BukkitFileUtil;
import com.github.liamdev06.utils.java.LoggerUtil;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages and stores all {@link ConfigurationProvider}.
 */
public class ConfigurationManager extends SinglePointInitiator {

    private final @NonNull Logger logger;
    private final @NonNull Map<String, ConfigurationProvider> configurations;

    public ConfigurationManager(@NonNull LPlugin plugin) throws IOException {
        this.configurations = new HashMap<>();

        Logger logger = LoggerUtil.createLoggerWithIdentifier(plugin, "ConfigManager");
        this.logger = logger;

        Class<? extends LPlugin> mainClass = plugin.getClass();
        if (!mainClass.isAnnotationPresent(LoadConfigurations.class)) {
            return;
        }

        boolean freshSetup = false;
        File dataFolder = plugin.getDataFolder();
        File config = new File(dataFolder, "config.yml");

        if (!config.exists()) {
            freshSetup = true;
            logger.info("Detected fresh plugin setup.");

            if (dataFolder.mkdir()) {
                logger.info("Plugin data folder was created.");
            }
        }

        for (String fileName : mainClass.getAnnotation(LoadConfigurations.class).value()) {
            String[] args = fileName.split("\\.");
            if (args.length == 0) {
                logger.error("Could not find an extension for config '" + fileName + "'. Skipping it in load in!");
                continue;
            }

            File file = new File(dataFolder, fileName);
            if (!file.exists() && file.createNewFile()) {
                logger.info("Created new plugin configuration file: " + dataFolder.getPath() + "/" + fileName);
            }

            AbstractConfigurationLoader<?> loader;
            String extension = args[args.length - 1];

            if (extension.equals("yml")) {
                loader = YamlConfigurationLoader.builder().file(file).build();
            } else if (extension.equals("json")) {
                loader = GsonConfigurationLoader.builder().file(file).build();
            } else {
                logger.error("Config with id '" + fileName + "' does not use the extension .yml or .json! Skipping it in load in!");
                continue;
            }

            ConfigurationProvider provider = new ConfigurationProvider(fileName, loader);
            this.registerConfig(provider);

            if (freshSetup) {
                BukkitFileUtil.setupPluginFile(plugin, fileName);
            }
        }
    }

    /**
     * Register a new configuration into storage.
     *
     * @param provider Instance of the configuration provider to register.
     */
    public void registerConfig(@NonNull ConfigurationProvider provider) {
        final String fileName = provider.getFileName();
        this.configurations.putIfAbsent(fileName, provider);
        this.logger.info("Registered the configuration with file name '" + fileName + "'.");
    }

    /**
     * Reloads all configurations currently loaded in.
     */
    public void reloadConfigurations() {
        this.configurations.values().forEach(ConfigurationProvider::reload);
    }

    /**
     * Find a cached configuration based on its file identifier.
     *
     * @param fileId The file name to find a configuration with.
     * @return Instance of the found {@link ConfigurationProvider} wrapped in an {@link Optional}.
     */
    public Optional<ConfigurationProvider> getConfigById(@NonNull ConfigIdentifier fileId) {
        return this.getConfigById(fileId.getKey());
    }

    /**
     * Find a cached configuration based on its file identifier.
     *
     * @param fileId The file name to find a configuration with.
     * @return Instance of the found {@link ConfigurationProvider} wrapped in an {@link Optional}.
     */
    public Optional<ConfigurationProvider> getConfigById(@NonNull String fileId) {
        return Optional.ofNullable(this.configurations.get(fileId));
    }
}