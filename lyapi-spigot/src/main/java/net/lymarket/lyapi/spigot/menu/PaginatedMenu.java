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
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public abstract class PaginatedMenu extends Menu {
    
    protected int page = 0;
    
    //28 empty slots per page
    protected int maxItemsPerPage = 28;
    
    protected int index = 0;
    
    protected ItemStack FILLER_GLASS = createFiller( );
    
    protected int size = 0;
    
    public PaginatedMenu( IPlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    public abstract void setSize( );
    
    public void nextPage( ){
        setSize( );
        if ( index + 1 <= size ) {
            page++;
            inventory.clear( );
            addMenuBorder( );
            setMenuItems( );
        }
    }
    
    public void prevPage( ){
        setSize( );
        page = Math.max( page - 1 , 0 );
        inventory.clear( );
        addMenuBorder( );
        setMenuItems( );
    }
    
    
    public void addMenuBorder( ){
        inventory.setItem( 48 , page == 0 ? FILLER_GLASS :
                new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                        .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=" )
                        .setDisplayName( "&aPrevious" )
                        .addTag( "ly-menu-previous" , "ly-menu-previous" )
                        .build( ) );
        
        inventory.setItem( 50 , new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=" )
                .setDisplayName( "&aNext" )
                .addTag( "ly-menu-next" , "ly-menu-next" )
                .build( ) );
    
        inventory.setItem( 49 , index + 1 >= size ? FILLER_GLASS : new ItemBuilder( XMaterial.BARRIER.parseMaterial( ) )
                .setDisplayName( "&cClose" )
                .addTag( "ly-menu-close" , "ly-menu-close" )
                .build( ) );
        
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , FILLER_GLASS );
            }
        }
        
        inventory.setItem( 17 , FILLER_GLASS );
        inventory.setItem( 18 , FILLER_GLASS );
        inventory.setItem( 26 , FILLER_GLASS );
        inventory.setItem( 27 , FILLER_GLASS );
        inventory.setItem( 35 , FILLER_GLASS );
        inventory.setItem( 36 , FILLER_GLASS );
        
        for ( int i = 44; i < 54; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , FILLER_GLASS );
            }
        }
        
    }
    
    private ItemStack createItem( String name , String head ){
        assert XMaterial.PLAYER_HEAD.parseMaterial( ) != null;
        ItemStack item = new ItemStack( XMaterial.PLAYER_HEAD.parseMaterial( ) , 1 , ( byte ) SkullType.PLAYER.ordinal( ) );
        SkullMeta itemmeta = ( SkullMeta ) item.getItemMeta( );
        
        GameProfile profile = new GameProfile( UUID.randomUUID( ) , null );
        profile.getProperties( ).put( "textures" , new Property( "textures" , head ) );
        Field field;
        try {
            field = itemmeta.getClass( ).getDeclaredField( "profile" );
            field.setAccessible( true );
            field.set( itemmeta , profile );
        } catch ( NoSuchFieldException | IllegalArgumentException | IllegalAccessException x ) {
            x.printStackTrace( );
        }
        itemmeta.setDisplayName( name );
        
        item.setItemMeta( itemmeta );
        return item;
    }
    
    private ItemStack createFiller( ){
        return new ItemBuilder( XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial( ) )
                .setDisplayName( " " ).build( );
    
    }
    
    
}
