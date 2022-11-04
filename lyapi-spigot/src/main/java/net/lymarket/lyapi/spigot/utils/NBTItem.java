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

package net.lymarket.lyapi.spigot.utils;

import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class NBTItem {
    
    private ItemStack is;
    
    public NBTItem(ItemStack is){
        this.is = is;
    }
    
    public static String getTag(ItemStack itemStack, String key){
        return LyApi.getInstance().getNMS().getTag(itemStack, key);
    }
    
    public static boolean hasTag(ItemStack itemStack, String key){
        return LyApi.getInstance().getNMS().hasTag(itemStack, key);
    }
    
    public static boolean hasCustomModelData(ItemStack itemStack){
        return LyApi.getInstance().getNMS().hasCustomModelData(itemStack);
    }
    
    public static int getCustomModelData(ItemStack itemStack){
        return LyApi.getInstance().getNMS().getCustomModelData(itemStack);
    }
    
    public static Integer getInteger(ItemStack item, String key){
        return Integer.valueOf(Objects.requireNonNull(getTag(item, key)));
    }
    
    public void setString(String key, String value){
        this.is = setTag(is, key, value);
    }
    
    public String getString(String key){
        String value = getTag(is, key);
        return value == null ? "" : value;
    }
    
    public void setInteger(String key, Integer value){
        this.is = setTag(is, key, String.valueOf(value));
    }
    
    public Integer getInteger(String key){
        return Integer.valueOf(Objects.requireNonNull(getTag(is, key)));
    }
    
    public ItemStack getItem(){
        return is;
    }
    
    public ItemStack setTag(ItemStack itemStack, String key, String value){
        return LyApi.getInstance().getNMS().setTag(itemStack, key, value);
    }
    
    public String getTag(String key){
        return getTag(is, key);
    }
    
    public boolean hasTag(String key){
        return hasTag(is, key);
    }
    
    public boolean hasCustomModelData(){
        return hasCustomModelData(is);
    }
    
    public int getCustomModelData(){
        return getCustomModelData(is);
    }
    
    public ItemStack setCustomModelData(int customModelData){
        return LyApi.getInstance().getNMS().setCustomModelData(is, customModelData);
    }
    
    public boolean isCustomItem(){
        return hasTag(is, "LyApi");
    }
    
    
}