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

package net.lymarket.lyapi.spigot.menu;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.UUID;

public abstract class PaginatedMenu<T> extends Menu {
    
    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;
    protected int size = 0;
    protected LinkedList<T> list = new LinkedList<>();
    
    private int[] borderSlots = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 35,
            36, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };
    
    public PaginatedMenu(IPlayerMenuUtility playerMenuUtility){
        this(playerMenuUtility, false);
    }
    
    public PaginatedMenu(IPlayerMenuUtility playerMenuUtility, boolean linked){
        super(playerMenuUtility, linked);
    }
    
    public PaginatedMenu(IPlayerMenuUtility playerMenuUtility, Material fillerItem, boolean linked){
        super(playerMenuUtility, fillerItem, linked);
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    public abstract void setSize();
    
    public void nextPage(){
        if (list.size() < maxItemsPerPage){
            return;
        }
        page++;
        inventory.clear();
        index = index + 1;
        setSize();
        addItemsAsync();
        setMenuItems();
        setCustomMenuItems();
    }
    
    public void reloadPage(){
        inventory.clear();
        setSize();
        addItemsAsync();
        setMenuItems();
        setCustomMenuItems();
    }
    
    public void prevPage(){
        page = Math.max(page - 1, 0);
        index = Math.max(page * maxItemsPerPage, 0);
        inventory.clear();
        setSize();
        addItemsAsync();
        setMenuItems();
        setCustomMenuItems();
    }
    
    
    public void addMenuBorder(){
        for ( int slot : borderSlots ){
            inventory.setItem(slot, FILLER_GLASS.clone());
        }
        addInteractiveItems();
    }
    
    public void addInteractiveItems(){
        inventory.setItem(getSlots() - 6, page == 0 ? FILLER_GLASS.clone() : PREV_ITEM.clone());
        inventory.setItem(getSlots() - 5, CLOSE_ITEM.clone());
        inventory.setItem(getSlots() - 4, list.size() > maxItemsPerPage ? NEXT_ITEM.clone() : FILLER_GLASS.clone());
    }
    
    
    private ItemStack createItem(String name, String head){
        assert XMaterial.PLAYER_HEAD.parseMaterial() != null;
        ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();
        
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", head));
        Field field;
        try {
            field = itemmeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemmeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }
        itemmeta.setDisplayName(name);
    
        item.setItemMeta(itemmeta);
        return item;
    }
    
    public void setBorderSlots(int[] borderSlots){
        this.borderSlots = borderSlots;
    }
    
    @Override
    public void setCustomMenuItems(){
        addMenuBorder();
    }
}
