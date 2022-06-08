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

public abstract class UpPaginatedMenu extends PaginatedMenu implements IUpdatableMenu {
    
    public UpPaginatedMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
    }
    
    public UpPaginatedMenu(IPlayerMenuUtility playerMenuUtility, Material fillerItem){
        super(playerMenuUtility, fillerItem);
    }
    
    
    public void reOpen( ){
        this.inventory.clear();
        this.onReOpen();
        this.setMenuItems();
    }
    
    public void onReOpen( ){
    
    }
    
    
}
