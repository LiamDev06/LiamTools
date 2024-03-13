package com.github.liamdev06.configuration.reload;

import com.github.liamdev06.LPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ConfigReloadable {

    void loadConfig(@NonNull LPlugin plugin);
}