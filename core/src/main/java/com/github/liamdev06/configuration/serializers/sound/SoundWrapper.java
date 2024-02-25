package com.github.liamdev06.configuration.serializers.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public record SoundWrapper(Sound sound, double volume, double pitch) {
    public void playSoundIndividually(@NonNull Player player) {
        this.playSoundIndividually(player, player.getLocation());
    }

    public void playSoundIndividually(@NonNull Player player, @NonNull Location location) {
        player.playSound(location, this.sound, (float) this.volume, (float) this.pitch);
    }

    public void playSoundEveryone(@NonNull Location location) {
        location.getWorld().playSound(location, this.sound, (float) this.volume, (float) this.pitch);
    }
}