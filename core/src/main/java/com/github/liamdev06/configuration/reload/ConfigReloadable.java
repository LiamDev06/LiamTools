package com.github.liamdev06.configuration.reload;

import com.github.liamdev06.LPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ConfigReloadable {

    <T extends LPlugin> void loadConfig(@NonNull T plugin);
}