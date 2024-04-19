package com.github.liamdev06.configuration.reload;

import com.github.liamdev06.LPlugin;
import com.github.liamdev06.configuration.ConfigIdWrapper;
import com.github.liamdev06.configuration.ConfigurationProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Optional;

public interface ConfigReloadable {

    void loadConfig(@NonNull LPlugin plugin);

    static @NonNull ConfigurationNode reloadRootNode(@NonNull LPlugin plugin, @NonNull ConfigIdWrapper idWrapper) {
        Optional<ConfigurationProvider> config = plugin.getConfigurationManager().getConfigById(idWrapper);
        if (config.isEmpty()) {
            throw new NullPointerException("Could not find config root node with id: " + idWrapper.getKey());
        }

        ConfigurationProvider configurationProvider = config.get();
        configurationProvider.reload(plugin.getConfigurationManager().getDefaultOptions());
        return configurationProvider.getRootNode();
    }
}