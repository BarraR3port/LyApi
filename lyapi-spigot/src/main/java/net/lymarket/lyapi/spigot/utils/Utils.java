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

import com.cryptomorin.xseries.XSound;
import net.lymarket.lyapi.spigot.LyApi;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Utils {
    
    private final HashMap < String, String > inventory;
    private final Serializer serializer = new Serializer( );
    
    public Utils( ){
        this.inventory = new HashMap <>( );
    }
    
    public void sendMessage( Player player , String message ){
        player.sendMessage( format( message ) );
    }
    
    public void playSound( Player p , String sound ){
        XSound.play( p , sound );
    }
    
    public void playSound( Player p , Sound sound ){
        XSound.play( p , sound.name( ) );
    }
    
    
    public String getServer( ){
        return LyApi.getPlugin( ).getConfig( ).getString( "server.name" );
    }
    
    public String format( String msg ){
        return ChatColor.translateAlternateColorCodes( '&' , msg );
    }
    
    public TextComponent formatTC( String msg ){
        return new TextComponent( format( msg ) );
    }
    
    
    public TextComponent hoverOverMessage( String msg , List < String > hover ){
        TextComponent text = new TextComponent( LyApi.getInstance( ).getUtils( ).format( msg ) );
        StringBuilder hovermsg = new StringBuilder( );
        Iterator < String > iterator = hover.iterator( );
        while (iterator.hasNext( )) {
            String s = iterator.next( );
            hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
        }
        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( LyApi.getInstance( ).getUtils( ).format( hovermsg.toString( ) ) ).create( ) ) );
        return text;
    }
    
    public TextComponent hoverOverMessageURL( String msg , List < String > hover , String url ){
        TextComponent text = new TextComponent( LyApi.getInstance( ).getUtils( ).format( msg ) );
        StringBuilder hovermsg = new StringBuilder( );
        Iterator < String > iterator = hover.iterator( );
        while (iterator.hasNext( )) {
            String s = iterator.next( );
            hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
        }
        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( LyApi.getInstance( ).getUtils( ).format( hovermsg.toString( ) ) ).create( ) ) );
        text.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , url ) );
        return text;
    }
    
    public TextComponent hoverOverMessageRunCommand( String msg , List < String > hover , String command ){
        TextComponent text = new TextComponent( LyApi.getInstance( ).getUtils( ).format( msg ) );
        StringBuilder hovermsg = new StringBuilder( );
        Iterator < String > iterator = hover.iterator( );
        while (iterator.hasNext( )) {
            String s = iterator.next( );
            hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
        }
        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( LyApi.getInstance( ).getUtils( ).format( hovermsg.toString( ) ) ).create( ) ) );
        text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , command ) );
        return text;
    }
    
    public TextComponent hoverOverMessageSuggestCommand( String msg , List < String > hover , String command ){
        TextComponent text = new TextComponent( LyApi.getInstance( ).getUtils( ).format( msg ) );
        StringBuilder hovermsg = new StringBuilder( );
        Iterator < String > iterator = hover.iterator( );
        while (iterator.hasNext( )) {
            String s = iterator.next( );
            hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
        }
        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( LyApi.getInstance( ).getUtils( ).format( hovermsg.toString( ) ) ).create( ) ) );
        text.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND , command ) );
        return text;
    }
    
    public void savePlayerInventory( String name , PlayerInventory inv ){
        String[] serialized = serializer.playerInventoryToBase64( inv );
        List < String > serialized2 = Arrays.asList( serialized );
        String serialized3 = serialized2.get( 0 );
        this.inventory.put( name , serialized3 );
    }
    
    public void removePlayerInventory( String name ){
        this.inventory.remove( name );
    }
    
    public Inventory getPlayerInventory( String name ){
        String noSerialized = this.inventory.get( name );
        try {
            return serializer.fromBase64( noSerialized );
        } catch ( IOException e ) {
            e.printStackTrace( );
        }
        return null;
    }
    
}
