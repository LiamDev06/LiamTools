package com.github.liamdev06.utils.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Utility class for working with {@link Sound}.
 */
@UtilityClass
public class SoundUtil {

    private static final float DEFAULT_VOLUME = 1;
    private static final float DEFAULT_PITCH = 1;

    /**
     * Play a sound to a {@link Player} at their {@link org.bukkit.Location} with a default volume a pitch.
     *
     * @param player An online {@link Player} to play the sound for.
     * @param sound The Bukkit {@link Sound} to play.
     */
    public static void playSound(@NonNull Player player, @NonNull Sound sound) {
        playSound(player, sound, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Play a sound to a {@link Player} at their {@link org.bukkit.Location} with a specified volume and default pitch.
     *
     * @param player An online {@link Player} to play the sound for.
     * @param sound The Bukkit {@link Sound} to play.
     * @param volume The volume for the sound.
     */
    public static void playSound(@NonNull Player player, @NonNull Sound sound, float volume) {
        playSound(player, sound, volume, DEFAULT_PITCH);
    }

    /**
     * Play a sound to a {@link Player} at their {@link org.bukkit.Location} with a specified volume and pitch.
     *
     * @param player An online {@link Player} to play the sound for.
     * @param sound The Bukkit {@link Sound} to play.
     * @param volume The volume for the sound.
     * @param pitch The pitch for the sound.
     */
    public static void playSound(@NonNull Player player, @NonNull Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a sound to a {@link Player} at their {@link org.bukkit.Location} with a default volume a pitch,
     * but by providing their {@link UUID}.
     *
     * @param uuid The {@link UUID} of an online {@link Player} to play the sound for.
     * @param sound The Bukkit {@link Sound} to play.
     */
    public static void playSound(@NonNull UUID uuid, @NonNull Sound sound) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            playSound(player, sound);
        }
    }
}