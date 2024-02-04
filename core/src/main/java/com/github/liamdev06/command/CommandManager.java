package com.github.liamdev06.command;

import com.github.liamdev06.registry.RegistryFactory;
import com.github.liamdev06.registry.component.AutoRegisteringFeature;
import com.github.liamdev06.utils.java.SinglePointInitiator;
import dev.jorel.commandapi.CommandAPICommand;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Command manager wrapper to provide extensions with custom integrations for the {@link dev.jorel.commandapi.CommandAPI}.
 */
public class CommandManager extends SinglePointInitiator implements AutoRegisteringFeature {

    public CommandManager(@NonNull RegistryFactory registryFactory) {
        registryFactory.registerAutoRegisteringComponent(this);
    }

    /**
     * Performs the auto registering of all {@link CommandAPICommand} in {@link CommandFactory}.
     * <p>
     * Uses the {@link RegistryFactory} to automatically register them.
     */
    @Override
    public void executeAutoRegistering(@NonNull RegistryFactory registryFactory) {
        for (Class<? extends CommandFactory> factory : registryFactory.getClassesWithRegistryType(CommandFactory.class, CommandFactory.class)) {
            Object instance = registryFactory.createEffectiveInstance(factory);
            if (instance == null) {
                continue;
            }

            CommandFactory commandFactory = (CommandFactory) instance;
            for (CommandAPICommand command : commandFactory.getCommands()) {
                this.registerCommand(command);
            }
        }
    }

    /**
     * Register a {@link CommandAPICommand} with our own command manager.
     *
     * @param command The {@link CommandAPICommand} to register.
     */
    public void registerCommand(@NonNull CommandAPICommand command) {
        command.register();
    }
}