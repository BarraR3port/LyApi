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

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.events.OpenCustomMenuEvent;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Menu implements InventoryHolder {
    
    protected ItemStack CLOSE_ITEM;
    protected ItemStack PREV_ITEM;
    protected ItemStack NEXT_ITEM;
    protected ItemStack FILLER_GLASS;
    protected Inventory inventory;
    protected IPlayerMenuUtility playerMenuUtility;
    protected boolean isOnSchedule = false;
    protected boolean moveTopItems = true;
    protected boolean moveBottomItems = true;
    
    public Menu( IPlayerMenuUtility playerMenuUtility ){
        this( playerMenuUtility , XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial( ) );
    }
    
    public Menu( IPlayerMenuUtility playerMenuUtility , Material fillerGlass ){
        this.playerMenuUtility = playerMenuUtility;
        CLOSE_ITEM = new ItemBuilder( XMaterial.BARRIER.parseMaterial( ) ).setDisplayName( "&cClose" ).addTag( "ly-menu-close" , "ly-menu-close" ).build( );
        FILLER_GLASS = new ItemBuilder( fillerGlass ).setDisplayName( " " ).build( );
        NEXT_ITEM = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) ).setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=" ).setDisplayName( "&aNext" ).addTag( "ly-menu-next" , "ly-menu-next" ).build( );
        PREV_ITEM = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) ).setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=" ).setDisplayName( "&aPrevious" ).addTag( "ly-menu-previous" , "ly-menu-previous" ).build( );
    }
    
    public abstract String getMenuName( );
    
    public abstract int getSlots( );
    
    public abstract void handleMenu( InventoryClickEvent e );
    
    public abstract void handleDragEvent( InventoryDragEvent e );
    
    public abstract void handleClose( InventoryCloseEvent e );
    
    public abstract void handleMove( InventoryMoveItemEvent e );
    
    public abstract void handlePickUp( InventoryPickupItemEvent e );
    
    public abstract void setMenuItems( );
    
    public abstract boolean overridePlayerInv( );
    
    public abstract void setOverridePlayerInv( boolean overridePlayerInv );
    
    public boolean canMoveTopItems( ){
        return moveTopItems;
    }
    
    public void allowMoveTopItems( boolean moveTopItems ){
        this.moveTopItems = moveTopItems;
    }
    
    public boolean canMoveBottomItems( ){
        return moveBottomItems;
    }
    
    public void allowMoveBottomItems( boolean moveBottomItems ){
        this.moveBottomItems = moveBottomItems;
    }
    
    public void open( ){
        
        inventory = Bukkit.createInventory( this , getSlots( ) , LyApi.getInstance( ).getUtils( ).format( getMenuName( ) ) );
        
        this.setMenuItems( );
    
        OpenCustomMenuEvent e = new OpenCustomMenuEvent( this , playerMenuUtility.getOwner( ) );
        if ( e.isCancelled( ) ) {
            return;
        }
        Bukkit.getPluginManager( ).callEvent( e );
    
        playerMenuUtility.getOwner( ).openInventory( e.getMenu( ).inventory );
    
    }
    
    public void reOpen( ){
        inventory.clear( );
        this.setMenuItems( );
    }
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
    protected ItemStack createCustomSkull( String name , String head ){
        return new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( head )
                .setDisplayName( name )
                .build( );
    }
    
    protected ItemStack createCustomSkull( String name , List < String > lore , String head ){
        return new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( head )
                .setLore( lore )
                .setDisplayName( name )
                .build( );
    }
    
    protected void checkSomething( Player p , int slot , ItemStack item , String name , List < String > lore ){
        
        if ( isOnSchedule ) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory( ).getTopInventory( ).setItem( slot , new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=" )
                .setLore( lore )
                .setDisplayName( name )
                .build( ) );
        setOnSchedule( p , slot , item );
        
    }
    
    protected void checkSomething( Player p , int slot , ItemStack item , String name , String lore ){
        
        if ( isOnSchedule ) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory( ).getTopInventory( ).setItem( slot , new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=" )
                .addLoreLine( lore )
                .setDisplayName( name )
                .build( ) );
        setOnSchedule( p , slot , item );
        
    }
    
    protected void setOnSchedule( Player p , int slot , ItemStack item ){
        Bukkit.getServer( ).getScheduler( ).runTaskLaterAsynchronously( LyApi.getPlugin( ) , ( ) -> {
            
            if ( p.getOpenInventory( ).getTopInventory( ).getHolder( ) instanceof Menu ) {
                p.getOpenInventory( ).getTopInventory( ).setItem( slot , item );
                isOnSchedule = false;
            }
            
        } , 30L );
    }
    
    public Player getOwner( ){
        return playerMenuUtility.getOwner( );
    }
    
}
