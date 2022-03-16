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

package net.lymarket.lyapi.spigot.listeners;

import net.lymarket.lyapi.spigot.menu.InventoryMenu;
import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
    
    @EventHandler
    public void onMenuClick( InventoryClickEvent e ){
        
        if ( e.getClickedInventory( ) == null ) {
            return;
        }
        InventoryHolder holder = e.getInventory( ).getHolder( );
        
        if ( holder instanceof Menu ) {
            e.setCancelled( true );
            
            Menu menu = ( Menu ) holder;
            
            if ( e.getClickedInventory( ).getType( ) == InventoryType.PLAYER && !menu.interactPlayerInv( ) ) {
                return;
            }
            if ( e.getCurrentItem( ) == null ) {
                return;
            }
            if ( e.getCurrentItem( ).getType( ) == Material.AIR ) {
                return;
            }
            
            menu.handleMenu( e );
        }
        if ( holder instanceof InventoryMenu ) {
            if ( e.getSlotType( ) == InventoryType.SlotType.OUTSIDE ) return;
            InventoryMenu menu = ( InventoryMenu ) holder;
            menu.handleMenu( e );
        }
    }
}

