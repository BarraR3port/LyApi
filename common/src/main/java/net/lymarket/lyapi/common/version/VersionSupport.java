/*
 * Copyright (c) 2022. BSD 3-Clause License, BarraR3port.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/BarraR3port/LyApi/blob/master/LICENSE
 *
 * Contact: barrar3port@gmail.com
 */

package net.lymarket.lyapi.common.version;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class VersionSupport {
    
    private static String name;
    private final Plugin plugin;
    
    public VersionSupport(Plugin plugin, String versionName){
        name = versionName;
        this.plugin = plugin;
    }
    
    public static String getName(){
        return name;
    }
    
    public Plugin getPlugin(){
        return plugin;
    }
    
    /**
     * Send title, subtitle. null for empty
     */
    public abstract void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    
    /**
     * Send action-bar message
     */
    public abstract void playAction(Player p, String text);
    
    /**
     * Add custom data to an ItemStack
     */
    public abstract ItemStack addCustomData(ItemStack i, String data);
    
    
    public abstract ItemStack setTag(ItemStack itemStack, String key, String value);
    
    /**
     * Get a custom item tag.
     *
     * @return null if not present.
     */
    public abstract String getTag(ItemStack itemStack, String key);
    
    /**
     * Has item tag.
     *
     * @return null if not present.
     */
    public abstract boolean hasTag(ItemStack itemStack, String key);
    
    public abstract ItemStack setCustomModelData(ItemStack itemStack, int customModelData);
    
    public abstract int getCustomModelData(ItemStack itemStack);
    
    public abstract ItemStack removeCustomModelData(ItemStack itemStack);
    
    public abstract boolean hasCustomModelData(ItemStack i);
    
    /**
     * Get the NBTTag from a LyBedWars item
     */
    public abstract String getCustomData(ItemStack i);
    
    
    /**
     * Check if is a player head
     */
    public boolean isPlayerHead(String material, int data){
        return material.equalsIgnoreCase("PLAYER_HEAD");
    }
    
    /**
     * Player head material
     */
    public abstract Material materialPlayerHead();
    
    
    /**
     * Get player head with skin.
     *
     * @param copyTagFrom will copy nbt tag from this item.
     */
    public abstract ItemStack getPlayerHead(Player player, @Nullable ItemStack copyTagFrom);
    
    
    public void spigotShowPlayer(Player victim, Player receiver){
        receiver.showPlayer(victim);
    }
    
    public void spigotHidePlayer(Player victim, Player receiver){
        receiver.hidePlayer(victim);
    }
    
    /**
     * Hide an entity
     */
    public abstract void hideEntity(Entity e, Player p);
    
    /**
     * Hide player armor to a player
     */
    public abstract void hideArmor(Player victim, Player receiver);
    
    /**
     * Show a player armor
     */
    public abstract void showArmor(Player victim, Player receiver);
}
