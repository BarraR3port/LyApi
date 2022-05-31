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

package net.lymarket.lyapi.spigot.utils;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {
    
    private ItemStack is;
    
    public ItemBuilder( String material ){
        String[] args = material.split( ":" );
        if ( args.length == 2 ) {
            is = new ItemStack( Material.valueOf( args[0] ) , 1 , ( byte ) Integer.parseInt( args[1] ) );
        } else is = new ItemStack( Material.valueOf( material ) );
    }
    
    public ItemBuilder( Material m ){
        this( m , 1 );
    }
    
    public ItemBuilder( ItemStack is ){
        this.is = is;
    }
    
    public ItemBuilder( Material m , int data ){
        is = new ItemStack( m , 1 , ( byte ) data );
    }
    
    public ItemBuilder setDurability( short dur ){
        is.setDurability( dur );
        return this;
    }
    
    public ItemBuilder setDisplayName( String name ){
        if ( name == null ) return this;
        ItemMeta im = is.getItemMeta( );
        im.setDisplayName( Utils.format( name ) );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder addUnsafeEnchantment( Enchantment ench , int level ){
        if ( ench == null ) return this;
        if ( level < 0 ) return this;
        is.addUnsafeEnchantment( ench , level );
        return this;
    }
    
    public ItemBuilder removeEnchantment( Enchantment ench ){
        if ( ench == null ) return this;
        is.removeEnchantment( ench );
        return this;
    }
    
    public ItemBuilder setSkullOwner( String owner ){
        if ( owner == null ) return this;
        try {
            SkullMeta im = ( SkullMeta ) is.getItemMeta( );
            im.setOwner( owner );
            is.setItemMeta( im );
        } catch ( ClassCastException ignored ) {
        }
        return this;
    }
    
    public ItemBuilder addEnchant( Enchantment ench , int level ){
        ItemMeta im = is.getItemMeta( );
        im.addEnchant( ench , level , true );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder addEnchantments( Map < Enchantment, Integer > enchantments ){
        is.addEnchantments( enchantments );
        return this;
    }
    
    
    public ItemBuilder addUnsafeEnchantment( Map < Enchantment, Integer > enchantments ){
        is.addUnsafeEnchantments( enchantments );
        return this;
    }
    
    public ItemBuilder hideEnchants( boolean b ){
        if ( !b ) return this;
        ItemMeta meta = is.getItemMeta( );
        meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        is.setItemMeta( meta );
        return this;
    }
    
    public ItemBuilder setEnchanted( boolean b ){
        if ( !b ) return this;
        ItemMeta meta = is.getItemMeta( );
        meta.addEnchant( Enchantment.LUCK , 1 , false );
        meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        is.setItemMeta( meta );
        return this;
    }
    
    public ItemBuilder setInfinityDurability( ){
        is.setDurability( Short.MAX_VALUE );
        return this;
    }
    
    public ItemBuilder setLore( List < String > lore ){
        if ( lore == null ) return this;
        ItemMeta im = is.getItemMeta( );
        im.setLore( lore.stream( ).map( Utils::format ).collect( Collectors.toList( ) ) );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder removeLoreLine( String line ){
        if ( line == null ) return this;
        ItemMeta im = is.getItemMeta( );
        List < String > lore = new ArrayList <>( im.getLore( ) );
        if ( !lore.contains( line ) ) return this;
        lore.remove( line );
        im.setLore( lore );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder removeLoreLine( int index ){
        ItemMeta im = is.getItemMeta( );
        List < String > lore = new ArrayList <>( im.getLore( ) );
        if ( index < 0 || index > lore.size( ) ) return this;
        lore.remove( index );
        im.setLore( lore );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder addLoreLine( String line ){
        if ( line == null ) return this;
        ItemMeta im = is.getItemMeta( );
        List < String > lore = new ArrayList <>( );
        if ( im.hasLore( ) ) lore = new ArrayList <>( im.getLore( ) );
        lore.add( Utils.format( line ) );
        im.setLore( lore );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder addLoreLine( String line , int pos ){
        if ( line == null ) return this;
        ItemMeta im = is.getItemMeta( );
        List < String > lore = new ArrayList <>( im.getLore( ) );
        lore.set( pos , Utils.format( line ) );
        im.setLore( lore );
        is.setItemMeta( im );
        return this;
    }
    
    public ItemBuilder setDyeColor( DyeColor color ){
        this.is.setDurability( color.getData( ) );
        return this;
    }
    
    /** @deprecated  */
    @Deprecated
    public ItemBuilder setWoolColor( DyeColor color ){
        if ( !is.getType( ).equals( Material.WOOL ) ) return this;
        this.is.setDurability( color.getData( ) );
        return this;
    }
    
    public ItemBuilder setLeatherArmorColor( Color color ){
        try {
            LeatherArmorMeta im = ( LeatherArmorMeta ) is.getItemMeta( );
            im.setColor( color );
            is.setItemMeta( im );
        } catch ( ClassCastException ignored ) {
        }
        return this;
    }
    
    /**
     * Para ocupar esto solo es necesario agregar esto y ya estaría lista la cabeza.
     */
    public ItemBuilder setHeadSkin( String skin ){
        is = new ItemStack( XMaterial.PLAYER_HEAD.parseMaterial( ) , 1 , ( byte ) SkullType.PLAYER.ordinal( ) );
        GameProfile profile = new GameProfile( UUID.randomUUID( ) , null );
        profile.getProperties( ).put( "textures" , new Property( "textures" , skin ) );
        SkullMeta itemmeta = ( SkullMeta ) is.getItemMeta( );
        Field field;
        try {
            field = itemmeta.getClass( ).getDeclaredField( "profile" );
            field.setAccessible( true );
            field.set( itemmeta , profile );
        } catch ( NoSuchFieldException | IllegalArgumentException | IllegalAccessException x ) {
            x.printStackTrace( );
        }
        is.setItemMeta( itemmeta );
        return this;
    }
    
    public ItemBuilder addTag( String key , String value ){
        
        NBTItem item = new NBTItem( is );
        item.setString( key , value );
        this.is = item.getItem( );
        return this;
    }
    
    public ItemBuilder addTag( String key , Integer value ){
        NBTItem item = new NBTItem( is );
        item.setInteger( key , value );
        this.is = item.getItem( );
        return this;
    }
    
    public ItemBuilder createBook( String title , String... pages ){
        if ( title == null || pages == null ) return this;
        if ( pages.length == 0 ) return this;
        BookMeta bm = ( BookMeta ) is.getItemMeta( );
        bm.setTitle( title );
        bm.setPages( pages );
        is.setItemMeta( bm );
        return this;
    }
    
    public ItemBuilder setBookTitle( String title ){
        BookMeta bm = ( BookMeta ) is.getItemMeta( );
        bm.setTitle( title );
        is.setItemMeta( bm );
        return this;
    }
    
    public ItemBuilder setBookAuthor( String author ){
        BookMeta bm = ( BookMeta ) is.getItemMeta( );
        bm.setAuthor( author );
        is.setItemMeta( bm );
        return this;
    }
    
    public ItemBuilder setBookPages( String... pages ){
        BookMeta bm = ( BookMeta ) is.getItemMeta( );
        bm.setPages( pages );
        is.setItemMeta( bm );
        return this;
    }
    
    public ItemBuilder setBookPage( int page , String text ){
        BookMeta bm = ( BookMeta ) is.getItemMeta( );
        bm.setPage( page , text );
        is.setItemMeta( bm );
        return this;
    }
    
    
    public ItemStack build( ){
        return is;
    }
    
}
