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

import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
    
    @EventHandler
    public void onMenuClick( InventoryClickEvent e ){
        if ( e.getClickedInventory( ) == null )
            return;
    
        InventoryHolder holder = e.getInventory( ).getHolder( );
        
        if ( holder instanceof Menu ) {
    
            Menu menu = ( Menu ) holder;
    
            if ( menu.canMoveTopItems( ) && menu.canMoveBottomItems( ) ) {
                e.setCancelled( true );
                return;
            }
            if ( e.getClickedInventory( ).getType( ) == InventoryType.PLAYER && menu.canMoveBottomItems( ) )
                return;
            if ( e.getClickedInventory( ).getHolder( ) instanceof Menu && menu.canMoveTopItems( ) )
                return;
    
            if ( e.getCurrentItem( ) == null || e.getCurrentItem( ).getType( ) == Material.AIR )
                return;
    
            menu.handleMenu( e );
        }
    }
    
    @EventHandler
    public void onMenuDrag( InventoryDragEvent e ){
        
        if ( e.getInventory( ) == null )
            return;
        
        InventoryHolder holder = e.getInventory( ).getHolder( );
        
        if ( holder instanceof Menu ) {
            
            Menu menu = ( Menu ) holder;
            
            if ( menu.canMoveTopItems( ) && menu.canMoveBottomItems( ) ) {
                e.setCancelled( true );
                return;
            }
            if ( e.getInventory( ).getType( ) == InventoryType.PLAYER && menu.canMoveBottomItems( ) )
                return;
            if ( menu.canMoveTopItems( ) )
                return;
            
            if ( e.getOldCursor( ) == null )
                return;
            menu.handleDragEvent( e );
        }
    }
    
    @EventHandler
    public void handleClose( InventoryCloseEvent e ){
        
        if ( e.getInventory( ) == null )
            return;
        
        InventoryHolder holder = e.getInventory( ).getHolder( );
        
        if ( holder instanceof Menu ) {
            Menu menu = ( Menu ) holder;
            menu.handleClose( e );
        }
    }
    
    @EventHandler
    public void handleMove( InventoryMoveItemEvent e ){
        
        if ( e.getItem( ) == null )
            return;
        
        InventoryHolder firstHolder = e.getInitiator( ).getHolder( );
        
        InventoryHolder destinationHolder = e.getDestination( ).getHolder( );
        
        if ( firstHolder instanceof Menu && destinationHolder instanceof Player ) {
            
            Menu menu = ( Menu ) firstHolder;
            
            if ( menu.canMoveTopItems( ) ) {
                e.setCancelled( true );
                return;
            }
            
            if ( e.getItem( ) == null || e.getItem( ).getType( ) == Material.AIR )
                return;
            
            menu.handleMove( e );
            return;
        }
        if ( firstHolder instanceof Player && destinationHolder instanceof Menu ) {
            
            Menu menu = ( Menu ) destinationHolder;
            
            if ( menu.canMoveBottomItems( ) ) {
                e.setCancelled( true );
                return;
            }
            
            if ( e.getItem( ) == null || e.getItem( ).getType( ) == Material.AIR )
                return;
            
            menu.handleMove( e );
        }
    }
    
    @EventHandler
    public void handlePickUp( InventoryPickupItemEvent e ){
        
        if ( e.getItem( ) == null )
            return;
        InventoryHolder firstHolder = e.getInventory( ).getHolder( );
        
        
        if ( firstHolder instanceof Menu ) {
            
            Menu menu = ( Menu ) firstHolder;
            
            if ( menu.canMoveTopItems( ) && menu.canMoveBottomItems( ) ) {
                e.setCancelled( true );
                return;
            }
            if ( e.getInventory( ).getType( ) == InventoryType.PLAYER && menu.canMoveBottomItems( ) )
                return;
            if ( menu.canMoveTopItems( ) )
                return;
            
            if ( e.getItem( ) == null || e.getItem( ).getItemStack( ).getType( ) == Material.AIR )
                return;
            
            menu.handlePickUp( e );
        }
        
    }
}

