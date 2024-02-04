package com.github.liamdev06.item.config;

import com.github.liamdev06.utils.bukkit.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemStackConfigSerializer implements TypeSerializer<ItemStack> {

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {
        // TODO
        throw new UnsupportedOperationException("Serializing an ItemStack is not supported yet!");
    }

    @Override
    public ItemStack deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String materialString = node.node("material").getString("BEDROCK").toUpperCase();
        Material material = Material.getMaterial(materialString);
        if (material == null) {
            throw new SerializationException("Invalid material specified '" + materialString + "' at node: " + node);
        }

        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (node.hasChild("amount")) {
            item.setAmount(node.node("amount").getInt(1));
        }

        if (node.hasChild("model-data")) {
            meta.setCustomModelData(node.node("model-data").getInt());
        }

        if (node.hasChild("display-name")) {
            meta.displayName(ComponentUtil.toComponent(node.node("display-name").getString("")));
        }

        if (node.hasChild("lore")) {
            List<Component> lore = ComponentUtil.toComponentList(node.node("lore").getList(String.class));
            meta.lore(lore);
        }

        if (node.hasChild("enchantments")) {
            for (Map.Entry<Object, ? extends ConfigurationNode> enchantmentEntry : node.childrenMap().entrySet()) {
                String key = String.valueOf(enchantmentEntry.getKey()).toLowerCase();

                Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
                if (enchantment == null) {
                    continue;
                }

                int level = enchantmentEntry.getValue().getInt(1);
                meta.addEnchant(enchantment, level, true);
            }
        }

        if (node.hasChild("flags")) {
            node.node("flags").getList(String.class, Collections.emptyList()).forEach(flagName -> {
                ItemFlag flag;

                try {
                    flag = ItemFlag.valueOf(flagName.toUpperCase());
                } catch (IllegalArgumentException exception) {
                    throw new RuntimeException("Invalid item flag with name " + flagName + " at node: " + node);
                }

                meta.addItemFlags(flag);
            });
        }

        item.setItemMeta(meta);
        return item;
    }
}