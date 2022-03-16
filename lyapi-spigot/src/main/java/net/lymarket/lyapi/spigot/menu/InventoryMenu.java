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

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public abstract class InventoryMenu extends Menu {
    
    public InventoryMenu( IPlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    public abstract void handleMenu( InventoryDragEvent e );
    
    public abstract void handleMenu( InventoryCloseEvent e );
    
    
}
