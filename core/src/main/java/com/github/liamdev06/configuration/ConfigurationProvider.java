package com.github.liamdev06.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.ConfigurationLoader;

/**
 * Represents a configuration provider that can be used to access application configurations.
 * <p>
 * "SpongePowered's Configurate" library is used for configurations. Read more <a href="https://github.com/SpongePowered/Configurate">here</a>.
 */
public class ConfigurationProvider {

    private final @NonNull String fileId;
    private final @NonNull ConfigurationLoader<?> loader;
    private ConfigurationNode rootNode;

    public ConfigurationProvider(@NonNull String fileId, @NonNull ConfigurationLoader<?> loader) {
        this(fileId, loader, ConfigurationOptions.defaults());
    }

    public ConfigurationProvider(@NonNull String fileId, @NonNull ConfigurationLoader<?> loader, @NonNull ConfigurationOptions options) {
        this.fileId = fileId;
        this.loader = loader;
        this.reload(options);
    }

    /**
     * Reloads this configuration into cache.
     */
    public void reload() {
        this.reload(ConfigurationOptions.defaults());
    }

    /**
     * Reloads this configuration into cache.
     *
     * @param options Configuration options that should be used when reloading.
     */
    public void reload(@NonNull ConfigurationOptions options) {
        try {
            this.rootNode = this.loader.load(options);
        } catch (ConfigurateException exception) {
            throw new RuntimeException("Something went wrong when loading in the configuration with file id '" + this.fileId + "'", exception);
        }
    }

    /**
     * Gets the file id of this configuration.
     *
     * @return The file id as a non-null {@link String}.
     */
    public @NonNull String getFileId() {
        return this.fileId;
    }

    /**
     * Gets the loader for this configuration.
     *
     * @return The {@link ConfigurationLoader} for this configuration.
     */
    public @NonNull ConfigurationLoader<?> getLoader() {
        return this.loader;
    }

    /**
     * Gets the {@link ConfigurationNode root note} for this configuration.
     *
     * @return The configuration root node as a non-null {@link ConfigurationNode}.
     */
    public @NonNull ConfigurationNode getRootNode() {
        return this.rootNode;
    }
}