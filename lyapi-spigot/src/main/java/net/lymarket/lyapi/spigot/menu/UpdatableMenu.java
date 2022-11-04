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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class UpdatableMenu extends Menu implements IUpdatableMenu {
    
    private boolean updating = false;
    
    public UpdatableMenu(IPlayerMenuUtility playerMenuUtility){
        this(playerMenuUtility, false);
    }
    
    public UpdatableMenu(IPlayerMenuUtility playerMenuUtility, boolean linked){
        super(playerMenuUtility, linked);
    }
    
    public UpdatableMenu(IPlayerMenuUtility playerMenuUtility, Material fillerGlass, boolean linked){
        super(playerMenuUtility, fillerGlass, linked);
    }
    
    public void reOpen(){
        this.onReOpen();
        updating = true;
        this.setMenuItems();
        for ( Integer slots : occupiedSlots ){
            this.inventory.setItem(slots, null);
        }
        this.occupiedSlots.clear();
        this.occupiedSlots.addAll(newOccupiedSlots);
        this.newOccupiedSlots.clear();
        this.updating = false;
    }
    
    public void onReOpen(){
    
    }
    
    @Override
    public void setMenuItem(int slot, ItemStack item){
        this.inventory.setItem(slot, item);
        if (updating){
            newOccupiedSlots.add(slot);
        } else {
            occupiedSlots.add(slot);
        }
    }
}