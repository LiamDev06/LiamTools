package com.github.liamdev06.configuration.serializers.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class LocationConfigSerializer implements TypeSerializer<Location> {

    @Override
    public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String worldName = node.node("world").getString("");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new SerializationException("Could not find a world with name: " + worldName);
        }

        double x = node.node("x").getDouble();
        double y = node.node("y").getDouble();
        double z = node.node("z").getDouble();
        double yaw = node.node("yaw").getDouble();
        double pitch = node.node("pitch").getDouble();
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }
}