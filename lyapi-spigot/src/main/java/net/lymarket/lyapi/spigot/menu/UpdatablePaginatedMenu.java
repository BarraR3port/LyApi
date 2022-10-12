/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, LyMarket
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/Lydark-Studio/LyApi/blob/master/LICENSE
 *
 * Contact: contact@lymarket.net
 */

package net.lymarket.lyapi.spigot.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class UpdatablePaginatedMenu<T> extends PaginatedMenu<T> implements IUpdatableMenu {
    private boolean updating = false;
    
    public UpdatablePaginatedMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility, false);
    }
    
    public UpdatablePaginatedMenu(IPlayerMenuUtility playerMenuUtility, boolean linked){
        super(playerMenuUtility, linked);
    }
    
    public UpdatablePaginatedMenu(IPlayerMenuUtility playerMenuUtility, Material fillerItem, boolean linked){
        super(playerMenuUtility, fillerItem, linked);
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
        newOccupiedSlots.clear();
        updating = false;
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
