package com.github.liamdev06.configuration;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.item.config.ItemStackConfigSerializer;
import com.github.liamdev06.utils.bukkit.BukkitFileUtil;
import com.github.liamdev06.utils.java.LoggerUtil;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import io.leangen.geantyref.TypeToken;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
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
    private final @NonNull ConfigurationOptions options;

    public ConfigurationManager(@NonNull LPlugin plugin) throws IOException {
        this.configurations = new HashMap<>();

        Logger logger = LoggerUtil.createLoggerWithIdentifier(plugin, "ConfigManager");
        this.logger = logger;

        TypeSerializerCollection serializers = TypeSerializerCollection.defaults()
                .childBuilder()
                .register(TypeToken.get(ItemStack.class), new ItemStackConfigSerializer())
                .build();
        this.options = ConfigurationOptions.defaults().serializers(serializers);

        Class<? extends LPlugin> mainClass = plugin.getClass();
        if (!mainClass.isAnnotationPresent(LoadConfigurations.class)) {
            return;
        }

        File dataFolder = plugin.getDataFolder();
        File config = new File(dataFolder, "config.yml");

        if (!config.exists()) {
            logger.info("Detected fresh plugin setup.");

            if (dataFolder.mkdir()) {
                logger.info("Plugin data folder was created.");
            }
        }

        for (String identifier : mainClass.getAnnotation(LoadConfigurations.class).value()) {
            File file = BukkitFileUtil.setupPluginFile(plugin, identifier + ".yml"); // TODO: Add support for JSON as well.
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .file(file)
                    .build();

            ConfigurationProvider provider = new ConfigurationProvider(identifier, loader, this.options);
            this.registerConfig(provider);
        }
    }

    /**
     * Register a new configuration into storage.
     *
     * @param provider Instance of the configuration provider to register.
     */
    public void registerConfig(@NonNull ConfigurationProvider provider) {
        final String identifier = provider.getFileId();
        this.configurations.putIfAbsent(identifier, provider);
        this.logger.info("Registered the configuration with file id '" + identifier + "'.");
    }

    /**
     * Reloads all configurations that are currently loaded in with {@link ConfigurationManager#getDefaultOptions()}.
     */
    public void reloadConfigurations() {
        this.configurations.values().forEach(provider -> provider.reload(this.getDefaultOptions()));
    }

    /**
     * Find a cached configuration based on its identifier wrapped in {@link ConfigIdWrapper}.
     *
     * @param identifier The identifier to find a configuration with.
     * @return Instance of the found {@link ConfigurationProvider} wrapped in an {@link Optional}.
     */
    public Optional<ConfigurationProvider> getConfigById(@NonNull ConfigIdWrapper identifier) {
        return this.getConfigById(identifier.getKey());
    }

    /**
     * Find a cached configuration based on its identifier.
     *
     * @param identifier The identifier to find a configuration with.
     * @return Instance of the found {@link ConfigurationProvider} wrapped in an {@link Optional}.
     */
    public Optional<ConfigurationProvider> getConfigById(@NonNull String identifier) {
        return Optional.ofNullable(this.configurations.get(identifier));
    }

    /**
     * @return Default configuration options for this library.
     */
    public @NonNull ConfigurationOptions getDefaultOptions() {
        return this.options;
    }
}