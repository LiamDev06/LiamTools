package com.github.liamdev06.command;

import dev.jorel.commandapi.CommandAPICommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory for creating a new {@link CommandAPICommand} which automatically registers them.
 */
public interface CommandFactory {

    /**
     * Builds a full {@link CommandAPICommand}.
     * <p>
     * The command is automatically registered ({@link CommandAPICommand#register()}) meaning there is no
     * need to manually perform it.
     *
     * @return Instance of the built {@link CommandAPICommand} to register.
     */
    default @Nullable CommandAPICommand buildSingleCommand() {
        return null;
    }

    /**
     * Builds multiple full {@link CommandAPICommand}.
     * <p>
     * The commands are automatically registered ({@link CommandAPICommand#register()}) meaning there is no
     * need to manually perform it on the commands.
     *
     * @return Instance of the list of built {@link CommandAPICommand}s to register.
     */
    default @NonNull List<CommandAPICommand> buildMultipleCommands() {
        return Collections.emptyList();
    }

    /**
     * Retrieves all commands found in this Command factory
     * by putting together {@link #buildSingleCommand()} and {@link #buildMultipleCommands()}.
     *
     * @return List of all commands found in this factory.
     */
    default @NonNull List<CommandAPICommand> getCommands() {
        final List<CommandAPICommand> list = new ArrayList<>(this.buildMultipleCommands());
        CommandAPICommand command = this.buildSingleCommand();
        if (command != null) {
            list.add(command);
        }
        return list;
    }
}