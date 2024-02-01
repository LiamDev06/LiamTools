package com.github.liamdev06.utils.bukkit;

import com.github.liamdev06.utils.java.ContentVariable;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utility class for sending messages to players that applies default message handling from {@link MiniMessage}.
 */
@UtilityClass
public class PlayerMessenger {

    public static final @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * Sends a message to a player with {@link MiniMessage} handling.
     *
     * @param player The player to send the message to.
     * @param message The formatted message to send.
     * @param variables Optional variables to replace in the message.
     */
    public static void sendMessage(@NonNull Player player, @NonNull String message, ContentVariable @NonNull ... variables) {
        player.sendMessage(handleToComponent(message, variables));
    }

    /**
     * Sends a message to all online players with {@link MiniMessage} handling.
     *
     * @param message The formatted message to send.
     * @param variables Optional variables to replace in the message.
     */
    public static void sendMessageEveryone(@NonNull String message, ContentVariable @NonNull ... variables) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(handleToComponent(message, variables)));
    }

    /**
     * Sends a message as a {@link Component} to all online players with {@link MiniMessage} handling.
     *
     * @param component The {@link Component} to send to the player.
     * @param variables Optional variables to replace in the message.
     */
    public static void sendComponentEveryone(@NonNull Component component, ContentVariable @NonNull ... variables) {
        sendMessageEveryone(handleToString(component, variables));
    }

    /**
     * Sends an action bar message to a player with {@link MiniMessage} handling.
     *
     * @param player The player to send the action bar message to.
     * @param message The action bar message to send.
     * @param variables Optional variables to replace in the action bar.
     */
    public static void sendActionBar(@NonNull Player player, @NonNull String message, ContentVariable @NonNull ... variables) {
        player.sendActionBar(handleToComponent(message, variables));
    }

    /**
     * Sends a title to a player with {@link MiniMessage} handling.
     *
     * @param player The player to send the title to.
     * @param title The title text to send.
     * @param subtitle The subtitle text to send.
     * @param variables Optional variables to replace in the title and subtitle.
     */
    public static void sendTitle(@NonNull Player player, @NonNull String title, @NonNull String subtitle, ContentVariable @NonNull ... variables) {
        sendTitle(player, title, subtitle, Title.DEFAULT_TIMES, variables);
    }

    /**
     * Sends a title to a player with {@link MiniMessage} handling and specified time.
     *
     * @param player The player to send the title to.
     * @param title The title text to send.
     * @param subtitle The subtitle text to send.
     * @param times Decides the fade in, stay and fade out duration for the title.
     * @param variables Optional variables to replace in the title and subtitle.
     */
    public static void sendTitle(@NonNull Player player, @NonNull String title, @NonNull String subtitle, Title.Times times, ContentVariable @NonNull ... variables) {
        player.showTitle(Title.title(
                handleToComponent(title, variables),
                handleToComponent(subtitle, variables),
                times
        ));
    }

    /**
     * Converts a {@link String} with optional variables into an Adventure {@link Component}
     * by using {@link MiniMessage} to deserialize it.
     *
     * @param input The input string with optional variables.
     * @param variables The variables to replace in the input string.
     * @return An Adventure {@link Component} containing the formatted text.
     */
    public static @NonNull Component handleToComponent(@NonNull String input, @Nullable ContentVariable... variables) {
        return MINI_MESSAGE.deserialize(handleVariables(input, variables)).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Converts a {@link Component} with optional variables into a {@link String}
     * by using {@link MiniMessage} to serialize it.
     *
     * @param input The input component with optional variables.
     * @param variables The variables to replace in the input component.
     * @return A {@link String} containing the component's text.
     */
    public static @NonNull String handleToString(@NonNull Component input, @Nullable ContentVariable... variables) {
        return handleVariables(MINI_MESSAGE.serialize(input), variables);
    }

    /**
     * Handles the replacement of variables in a string.
     *
     * @param input The input string with variables to be replaced.
     * @param variables The variables to replace in the input string.
     * @return The input string with variables replaced.
     */
    public static @NonNull String handleVariables(@NonNull String input, @Nullable ContentVariable... variables) {
        if (variables == null) {
            return input;
        }

        for (ContentVariable variable : variables) {
            if (variable != null) {
                input = input.replace(variable.replace(), String.valueOf(variable.replaceWith()));
            }
        }
        return input;
    }
}