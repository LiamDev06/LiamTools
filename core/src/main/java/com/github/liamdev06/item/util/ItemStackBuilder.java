package com.github.liamdev06.item.util;

import com.github.liamdev06.utils.bukkit.legacy.LegacyPlayerMessenger;
import com.github.liamdev06.utils.java.ContentVariable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Helper class for creating and modifying {@link ItemStack}.
 * <p>
 * The class wraps an {@link ItemStack} object and provides convenient chainable, 'builder-pattern' methods for
 * manipulating the item's metadata.
 */
public class ItemStackBuilder {

    private final @NonNull ItemStack item;

    /**
     * Creates a new builder with a {@link Material} for the {@link ItemStack}.
     *
     * @param material The {@link Material} for the item.
     */
    public ItemStackBuilder(@NonNull Material material) {
        this.item = new ItemStack(material);
    }

    /**
     * Creates a new builder based on an existing {@link ItemStack}.
     *
     * @param item The {@link ItemStack} to create the builder for.
     */
    public ItemStackBuilder(@NonNull ItemStack item) {
        this.item = item;
    }

    /**
     * Sets the amount of items in this {@link ItemStack}.
     *
     * @param amount New amount of items in the stack.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    /**
     * Sets the display name of this {@link ItemStack}.
     *
     * @param name The name to set as a {@link String}.
     * @return This builder instance.
     * @see #name(String, ContentVariable...)
     */
    public @NonNull ItemStackBuilder name(@NonNull String name) {
        return this.name(name, (ContentVariable) null);
    }

    /**
     * Sets the display name of this {@link ItemStack}.
     *
     * @param name The name to set as a {@link String}.
     * @param variables Optional {@link ContentVariable content variables} to dynamically replace content within the name.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder name(@NonNull String name, @Nullable ContentVariable... variables) {
        this.item.editMeta(meta -> meta.displayName(LegacyPlayerMessenger.handleToComponent(name, variables)));
        return this;
    }

    /**
     * Sets the lore for this {@link ItemStack}.
     *
     * @param lines The lines to set as lore as raw {@link Component}.
     *              These components are not handled by {@link MiniMessage}.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder lore(@NonNull Component... lines) {
        this.item.editMeta(meta -> meta.lore(List.of(lines)));
        return this;
    }

    /**
     * Sets the lore for this {@link ItemStack}.
     *
     * @param lines The lines to set as lore as {@link Component}.
     *              These components are handled by {@link MiniMessage} automatically.
     * @return This builder instance.
     * @see #lore(List, ContentVariable...)
     */
    public @NonNull ItemStackBuilder lore(@NonNull String... lines) {
        return this.lore(List.of(lines), (ContentVariable) null);
    }

    /**
     * Sets the lore for this {@link ItemStack}.
     *
     * @param lines The lines to set as lore as {@link Component}.
     * @param variables Optional {@link ContentVariable content variables} to dynamically replace content within the lore lines.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder lore(@NonNull List<String> lines, @Nullable ContentVariable... variables) {
        this.item.editMeta(meta -> {
            final List<Component> array = lines.stream()
                    .map(s -> LegacyPlayerMessenger.handleToComponent(s, variables))
                    .toList();
            meta.lore(array);
        });
        return this;
    }

    /**
     * Adds {@link ItemFlag} to this {@link ItemStack}.
     *
     * @param flags The {@link ItemFlag} to add.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder addItemFlag(@NonNull ItemFlag... flags) {
        this.item.editMeta(meta -> meta.addItemFlags(flags));
        return this;
    }

    /**
     * Removes {@link ItemFlag} from this {@link ItemStack}.
     *
     * @param flags The {@link ItemFlag} to remove.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder removeItemFlag(@NonNull ItemFlag... flags) {
        this.item.editMeta(meta -> meta.removeItemFlags(flags));
        return this;
    }

    /**
     * Adds an {@link Enchantment} to this {@link ItemStack}.
     * <p>
     * This supports adding unsafe enchantments.
     *
     * @param enchantment The {@link Enchantment} to add.
     * @param level The level to set for the enchantment.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder addEnchantment(@NonNull Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Removes an already applied {@link Enchantment} from this {@link ItemStack}.
     *
     * @param enchantment The {@link Enchantment} to remove.
     * @return This builder instance.
     */
    public @NonNull ItemStackBuilder removeEnchantment(@NonNull Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Returns the {@link #item} used in this builder to give an output {@link ItemStack}.
     *
     * @return The built {@link ItemStack}.
     */
    public @NonNull ItemStack build() {
        return this.item;
    }
}