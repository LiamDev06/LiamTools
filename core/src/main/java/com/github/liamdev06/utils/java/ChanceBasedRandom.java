package com.github.liamdev06.utils.java;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Weighted random selection utility where values are selected based on their percentage chance.
 *
 * @param <T> The type of values to be selected.
 */
public class ChanceBasedRandom<T> {

    private final @NonNull List<Entry<T>> entries;

    public ChanceBasedRandom() {
        this.entries = new ArrayList<>();
    }

    /**
     * Inserts a new value and its associated percentage chance into the selection pool.
     * The percentage represents the chance of the value to be selected compared to other values in the pool.
     *
     * @param value The value to insert into the selection pool.
     * @param percentage The percentage chance of selecting this value.
     */
    public void insert(@NonNull T value, double percentage) {
        this.entries.add(new Entry<>(value, percentage));
    }

    /**
     * Retrieves a random value from the selection pool based on their percentage chances.
     *
     * @return A randomly selected value, or null if the pool is empty or percentages are not correctly set.
     */
    public @Nullable T get() {
        double totalPercentage = this.entries.stream().mapToDouble(entry -> entry.percentage).sum();
        double rand = ThreadLocalRandom.current().nextDouble() * totalPercentage;
        double currentPercentage = 0.0;

        for (Entry<T> entry : this.entries) {
            currentPercentage += entry.percentage;

            if (rand < currentPercentage) {
                return entry.value;
            }
        }

        // Fallback option if entries are not correctly set
        return null;
    }

    /**
     * Asynchronously retrieves a random value from the selection pool based on their percentage chances.
     *
     * @return A CompletableFuture representing the asynchronously selected value.
     *         If the pool is empty or percentages are not correctly set, the CompletableFuture will complete with null.
     */
    public @NonNull CompletableFuture<T> getAsync() {
        return CompletableFuture.supplyAsync(this::get);
    }

     /**
     * Internal record representing an entry in the selection pool.
     * Each entry contains a value and its associated percentage chance.
     *
     * @param <T> The type of the value.
     */
    private record Entry<T>(@NonNull T value, double percentage) { }
}