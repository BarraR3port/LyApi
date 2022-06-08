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

public abstract class UpdatableMenu extends Menu implements IUpdatableMenu {
    
    public UpdatableMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
    }
    
    public UpdatableMenu(IPlayerMenuUtility playerMenuUtility, Material fillerGlass){
        super(playerMenuUtility, fillerGlass);
    }
    
    public void reOpen( ){
        this.inventory.clear();
        this.onReOpen();
        this.setMenuItems();
    }
    
    public void onReOpen( ){
    
    }
}