package com.github.liamdev06.item.util;

import io.leangen.geantyref.TypeToken;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility for working with and modifying item stacks.
 */
@UtilityClass
public class ItemUtil {

    /**
     * Configuration type token for {@link ItemStack} serialization.
     */
    public static final TypeToken<ItemStack> ITEM_TYPE_TOKEN = TypeToken.get(ItemStack.class);

    /**
     * Empty ItemStack with air.
     */
    public static final ItemStack AIR = new ItemStack(Material.AIR);

    /**
     * Strips the visual metadata from an ItemStack like its display name and lore.
     *
     * @param itemStack The {@link ItemStack} to trip metadata from.
     * @return The ItemStack entered as parameter.
     */
    public static @NonNull ItemStack stripVisualMetadata(@NonNull ItemStack itemStack) {
        itemStack.editMeta(meta -> {
            meta.displayName(null);
            meta.lore(null);
        });
        return itemStack;
    }
}