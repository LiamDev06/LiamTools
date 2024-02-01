package com.github.liamdev06.utils.bukkit;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Utility class when working with Minecraft tick related data and numbers.
 */
@UtilityClass
public class TickUtil {

    /**
     * How many milliseconds a tick is.
     */
    public static final long TICK_IN_MILLIS = 50;

    /**
     * Convert a {@link TimeUnit} input to ticks.
     *
     * @param time The time to convert.
     * @param unit The original {@link TimeUnit} of the {@param time}.
     * @return Inputted time converted into ticks.
     */
    public static long convertToTicks(long time, @NonNull TimeUnit unit) {
        return unit.toMillis(time) / 50;
    }
}