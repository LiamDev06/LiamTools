package com.github.liamdev06.configuration.serializers.sound;

import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SoundConfigSerializer implements TypeSerializer<SoundWrapper> {

    public static final double DEFAULT_VOLUME = 1;
    public static final double DEFAULT_PITCH = 0;

    @Override
    public SoundWrapper deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String string = node.getString("");
        String[] args = string.split("\\.");

        if (args.length == 0) {
            return new SoundWrapper(Sound.AMBIENT_CAVE, DEFAULT_VOLUME, DEFAULT_PITCH);
        }

        String soundName = args[0];
        Sound sound;
        double volume = DEFAULT_VOLUME;
        double pitch = DEFAULT_PITCH;

        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException exception) {
            throw new SerializationException("Could not parse sound with input '" + soundName + "'");
        }

        if (args.length > 1) {
            String volumeString = args[1];

            try {
                volume = Double.parseDouble(volumeString);
            } catch (NumberFormatException exception) {
                throw new SerializationException("Could not parse double with input '" + volumeString + "'");
            }
        }

        if (args.length > 2) {
            String pitchString = args[2];

            try {
                pitch = Double.parseDouble(pitchString);
            } catch (NumberFormatException exception) {
                throw new SerializationException("Could not parse double with input '" + pitchString + "'");
            }
        }
        return new SoundWrapper(sound, volume, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable SoundWrapper obj, ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("This is not done yet!");
    }
}