package com.github.liamdev06.utils.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility class for working with {@link Player}.
 */
@UtilityClass
public class PlayerUtil {

    public static final double DEFAULT_RESET_AMOUNT = 20;
    public static final float DEFAULT_WALK_SPEED = 0.2f;

    /**
     * Performs a simple reset on the target {@link Player}.
     * <p>
     * The difference of a simple reset and a {@link #fullResetPlayer(Player)} is the values the method resets.
     * This makes it possible for the parent plugin to be more selective in which values they want to reset for the player.
     *
     * @param player Instance of the {@link Player} to reset.
     */
    public static void simpleResetPlayer(@NonNull Player player) {
        player.setHealth(DEFAULT_RESET_AMOUNT);
        player.setHealthScale(DEFAULT_RESET_AMOUNT);

        player.setFoodLevel((int) DEFAULT_RESET_AMOUNT);

        player.setAllowFlight(false);
        player.setFlying(false);

        player.setSneaking(false);
        player.setSprinting(false);
        player.setWalkSpeed(DEFAULT_WALK_SPEED);

        player.setLevel(0);
        player.setExp(0);
    }

    /**
     * Performs a full reset on the target {@link Player}.
     * <p>
     * The difference of a full reset and a {@link #simpleResetPlayer(Player)} is the values the method resets.
     * This makes it possible for the parent plugin to be more selective in which values they want to reset for the player.
     *
     * @param player Instance of the {@link Player} to reset.
     * @see #simpleResetPlayer(Player)
     */
    public static void fullResetPlayer(@NonNull Player player) {
        simpleResetPlayer(player);

        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.clearActivePotionEffects();
    }
}