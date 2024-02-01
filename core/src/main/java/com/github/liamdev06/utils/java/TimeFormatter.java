package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to help with time-related operations and displaying them nicely.
 */
@UtilityClass
public class TimeFormatter {

    /**
     * Converts seconds to a string representation of minutes and seconds.
     *
     * @param inputSeconds The input number of seconds.
     * @return A string representation of minutes and seconds.
     */
    public static @NonNull String formatSecondsToMinAndSec(int inputSeconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(inputSeconds);
        long seconds = inputSeconds - minutes * 60;

        StringBuilder text = new StringBuilder();

        if (minutes == 1) {
            text.append(minutes).append(" minute and ");
        } else if (minutes != 0) {
            text.append(minutes).append(" minutes and ");
        }

        if (seconds == 1) {
            text.append(seconds).append(" second");
        } else if (seconds != 0) {
            text.append(seconds).append(" seconds");
        }

        if (!text.toString().contains("second")) {
            text = new StringBuilder(text.toString().replace(" and ", ""));
        }
        return text.toString();
    }

    /**
     * Converts seconds to a string representation of minutes and seconds without text.
     *
     * @param inputSeconds The input number of seconds.
     * @return A string representation of minutes and seconds without text.
     */
    public static @NonNull String formatSecondsToMinSecWithoutText(int inputSeconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(inputSeconds);
        long seconds = inputSeconds - minutes * 60;

        String minutesStr = (minutes <= 9) ? "0" + minutes : String.valueOf(minutes);
        String secondsStr = (seconds <= 9) ? "0" + seconds : String.valueOf(seconds);

        return minutesStr + ":" + secondsStr;
    }

    /**
     * Converts a time period in milliseconds to a human-readable string representation of days, hours, minutes, and seconds.
     *
     * @param timePeriod The input time period in milliseconds.
     * @return A human-readable string representation of the time period.
     */
    public static @NonNull String formatMillsToReadable(long timePeriod) {
        if (timePeriod == 0) {
            return "now";
        }

        long days = TimeUnit.MILLISECONDS.toDays(timePeriod);
        timePeriod -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(timePeriod);
        timePeriod -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timePeriod);
        timePeriod -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timePeriod);

        final StringBuilder output = new StringBuilder();

        if (days > 0) {
            output.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }

        if (hours > 0) {
            output.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }

        if (minutes > 0) {
            output.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }

        if (seconds > 0) {
            output.append(seconds).append(" second").append(seconds > 1 ? "s" : "").append(" ");
        }
        return output.toString().trim();
    }

    /**
     * Gets a time icon for a given number of seconds.
     *
     * @param seconds The input number of seconds.
     * @return A time icon representing the number of seconds.
     */
    public static @NonNull String getTimeIcon(int seconds) {
        return switch (seconds) {
            case 10 -> "➉";
            case 9 -> "➈";
            case 8 -> "➇";
            case 7 -> "➆";
            case 6 -> "➅";
            case 5 -> "➄";
            case 4 -> "➃";
            case 3 -> "➂";
            case 2 -> "➁";
            case 1 -> "➀";
            default -> "◯";
        };
    }
}