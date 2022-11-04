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
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MenuSelector extends Menu {
    
    protected ItemStack ACCEPT;
    
    protected ItemStack DENY;
    
    public MenuSelector(IPlayerMenuUtility playerMenuUtility){
        this(playerMenuUtility, false);
    }
    
    public MenuSelector(IPlayerMenuUtility playerMenuUtility, boolean linked){
        super(playerMenuUtility, linked);
        try {
            ACCEPT = new ItemBuilder(XMaterial.LIME_STAINED_GLASS.parseMaterial(), 5).setDurability((short) 5)
                    .setDisplayName(LyApi.getLanguage().getMSG("menu.accept"))
                    .addTag("ly-menu-accept", "ly-menu-accept")
                    .build();
            DENY = new ItemBuilder(XMaterial.RED_STAINED_GLASS.parseMaterial(), 14).setDurability((short) 14)
                    .setDisplayName(LyApi.getLanguage().getMSG("menu.deny"))
                    .addTag("ly-menu-deny", "ly-menu-deny")
                    .build();
            
        } catch (NullPointerException ignored) {
            ACCEPT = new ItemBuilder(XMaterial.LIME_STAINED_GLASS.parseMaterial(), 5).setDurability((short) 5)
                    .setDisplayName("&aAccept")
                    .addTag("ly-menu-accept", "ly-menu-accept")
                    .build();
            DENY = new ItemBuilder(XMaterial.RED_STAINED_GLASS.parseMaterial(), 14).setDurability((short) 14)
                    .setDisplayName("&cDeny")
                    .addTag("ly-menu-deny", "ly-menu-deny")
                    .build();
        }
    }
    
    
    @Override
    public String getMenuName(){
        return "Select";
    }
    
    @Override
    public int getSlots(){
        return 27;
    }
    
    @Override
    public void setMenuItems(){
        inventory.setItem(12, ACCEPT);
        
        inventory.setItem(14, DENY);
        
        inventory.setItem(18, super.CLOSE_ITEM);
        
        setSubMenuItems();
    }
    
    public abstract void setSubMenuItems();
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        
        if (NBTItem.hasTag(item, "ly-menu-close")){
            getPrevMenu().open();
            return;
        } else if (NBTItem.hasTag(item, "ly-menu-accept")){
            if (handleAccept()){
                getAcceptManu().open();
            }
            return;
        } else if (NBTItem.hasTag(item, "ly-menu-deny")){
            if (handleDeny()){
                getDenyManu().open();
            }
            return;
        }
        
        handleSubMenu(e);
        
    }
    
    public abstract void handleSubMenu(InventoryClickEvent e);
    
    public abstract Menu getAcceptManu();
    
    public abstract Menu getDenyManu();
    
    public abstract Menu getPrevMenu();
    
    public abstract boolean handleAccept();
    
    public abstract boolean handleDeny();
    
    public void setAcceptItem(ItemStack item){
        ACCEPT = item;
    }
    
    public void setDenyItem(ItemStack item){
        DENY = item;
    }
}
