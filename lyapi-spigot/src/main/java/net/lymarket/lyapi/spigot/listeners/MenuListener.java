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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
    
    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null)
            return;
    
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handleMenu(e);
        } else if (holder instanceof Menu menu){
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                return;
            if (!menu.canMoveTopItems() && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (e.getClickedInventory().getType() == InventoryType.PLAYER && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (e.getClickedInventory().getHolder() instanceof Menu && !menu.canMoveTopItems())
                e.setCancelled(true);
            menu.handleMenu(e);
        }
    }
    
    @EventHandler
    public void onMenuDrag(InventoryDragEvent e){
        if (e.getInventory() == null)
            return;
    
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handleDragEvent(e);
        } else if (holder instanceof Menu menu){
            if (!menu.canMoveTopItems() && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.PLAYER && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (menu.canMoveTopItems())
                e.setCancelled(true);
            if (e.getOldCursor() == null)
                return;
            menu.handleDragEvent(e);
        }
    }
    
    @EventHandler
    public void handleClose(InventoryCloseEvent e){
        if (e.getInventory() == null)
            return;
    
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handleClose(e);
        } else if (holder instanceof Menu menu){
            menu.handleClose(e);
        }
    }
    
    @EventHandler
    public void handleMove(InventoryMoveItemEvent e){
        if (e.getItem() == null || e.getItem().getType() == Material.AIR)
            return;
    
        InventoryHolder firstHolder = e.getInitiator().getHolder();
        InventoryHolder destinationHolder = e.getDestination().getHolder();
        if (firstHolder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handleMove(e);
        } else if (destinationHolder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handleMove(e);
        } else if (firstHolder instanceof Menu menu && destinationHolder instanceof Player){
            if (!menu.canMoveTopItems())
                e.setCancelled(true);
            menu.handleMove(e);
        } else if (firstHolder instanceof Player && destinationHolder instanceof Menu menu){
            if (!menu.canMoveBottomItems())
                e.setCancelled(true);
            menu.handleMove(e);
        }
    }
    
    @EventHandler
    public void handlePickUp(InventoryPickupItemEvent e){
        if (e.getItem() == null || e.getItem().getItemStack().getType() == Material.AIR)
            return;
    
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof InventoryMenu menu){
            menu.canMoveBottomItems();
            menu.canMoveTopItems();
            menu.handlePickUp(e);
        } else if (holder instanceof Menu menu){
            if (!menu.canMoveTopItems() && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.PLAYER && !menu.canMoveBottomItems())
                e.setCancelled(true);
            if (!menu.canMoveTopItems())
                e.setCancelled(true);
            menu.handlePickUp(e);
        }
    }
}

