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

package net.lymarket.common.lang;

import net.lymarket.common.config.ConfigGenerator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public abstract class ILang {
    
    protected final ConfigGenerator lang;
    
    protected String prefix;
    
    protected String errorPrefix;
    
    public ILang( ConfigGenerator configGenerator , String prefix , String errorPrefix ){
        this.lang = configGenerator;
        this.prefix = prefix;
        this.errorPrefix = errorPrefix;
    }
    
    public void saveLang( ){
        lang.saveData( );
    }
    
    public void reloadLang( ){
        try {
            lang.loadConfig( );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }
    
    public ConfigGenerator getConfig( ){
        return lang;
    }
    
    
    public void sendMsg( CommandSender c , String key ){
        if ( key.contains( "error." ) ) {
            sendErrorMsg( c , key );
            return;
        }
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , prefix + getConfig( ).getString( key ) ) );
    }
    
    
    public void sendMsg( CommandSender c , String key , String wordToReplace , String replacement ){
        if ( key.contains( "error." ) ) {
            sendErrorMsg( c , key , wordToReplace , replacement );
            return;
        }
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , prefix + getConfig( ).getString( key ).replace( "%" + wordToReplace + "%" , replacement ) ) );
    }
    
    public void sendMsg( CommandSender c , String key , HashMap < String, String > wordsToReplace ){
        if ( key.contains( "error." ) ) {
            sendErrorMsg( c , key , wordsToReplace );
            return;
        }
        String msg = getConfig( ).getString( key );
        for ( String wordToReplace : wordsToReplace.keySet( ) ) {
            msg = msg.replace( "%" + wordToReplace + "%" , wordsToReplace.get( wordToReplace ) );
        }
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , prefix + msg ) );
    }
    
    
    public void sendErrorMsg( CommandSender c , String key ){
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , "&c[&7ERROR&c] " + getConfig( ).getString( key.contains( "error." ) ? "" : "error." + key ) ) );
    }
    
    
    public void sendErrorMsg( CommandSender c , String key , String wordToReplace , String replacement ){
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , "&c[&7ERROR&c] " + getConfig( ).getString( key.contains( "error." ) ? "" : "error." + key ).replace( "%" + wordToReplace + "%" , replacement ) ) );
    }
    
    public void sendErrorMsg( CommandSender c , String key , HashMap < String, String > wordsToReplace ){
        String msg = getConfig( ).getString( key.contains( "error." ) ? "" : "error." + key );
        for ( String wordToReplace : wordsToReplace.keySet( ) ) {
            msg = msg.replace( "%" + wordToReplace + "%" , wordsToReplace.get( wordToReplace ) );
        }
        c.sendMessage( ChatColor.translateAlternateColorCodes( '&' , "&c[&7ERROR&c] " + msg ) );
    }
    
    
    public String getMSG( String key ){
        return ChatColor.translateAlternateColorCodes( '&' , getConfig( ).getString( key ) );
    }
    
    public String getMSG( String key , String wordToReplace , String replacement ){
        return ChatColor.translateAlternateColorCodes( '&' , getConfig( ).getString( key ) ).replace( "%" + wordToReplace + "%" , replacement );
    }
    
    
    //TODO finish this:
    // public TextComponent hoverOverMessage( String msg , List < String > hover ){
    //     TextComponent text = new TextComponent( ChatColor.translateAlternateColorCodes( '&' , msg ) );
    //     StringBuilder hovermsg = new StringBuilder( );
    //     Iterator < String > iterator = hover.iterator( );
    //     while (iterator.hasNext( )) {
    //         String s = iterator.next( );
    //         hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
    //     }
    //     text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( ChatColor.translateAlternateColorCodes( '&' , hovermsg.toString( ) ) ).create( ) ) );
    //     return text;
    // }
    //TODO finish this:
    // public TextComponent hoverOverMessageURL( String msg , List < String > hover , String url ){
    //     TextComponent text = new TextComponent( ChatColor.translateAlternateColorCodes( '&' , msg ) );
    //     StringBuilder hovermsg = new StringBuilder( );
    //     Iterator < String > iterator = hover.iterator( );
    //     while (iterator.hasNext( )) {
    //         String s = iterator.next( );
    //         hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
    //     }
    //     text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( ChatColor.translateAlternateColorCodes( '&' , hovermsg.toString( ) ) ).create( ) ) );
    //     text.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , url ) );
    //     return text;
    // }
    //TODO finish this:
    // public TextComponent hoverOverMessageRunCommand( String msg , List < String > hover , String command ){
    //     TextComponent text = new TextComponent( ChatColor.translateAlternateColorCodes( '&' , msg ) );
    //     StringBuilder hovermsg = new StringBuilder( );
    //     Iterator < String > iterator = hover.iterator( );
    //     while (iterator.hasNext( )) {
    //         String s = iterator.next( );
    //         hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
    //     }
    //     text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( ChatColor.translateAlternateColorCodes( '&' , hovermsg.toString( ) ) ).create( ) ) );
    //     text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , command ) );
    //     return text;
    // }
    //TODO finish this:
    // public TextComponent hoverOverMessageSuggestCommand( String msg , List < String > hover , String command ){
    //     TextComponent text = new TextComponent( ChatColor.translateAlternateColorCodes( '&' , msg ) );
    //     StringBuilder hovermsg = new StringBuilder( );
    //     Iterator < String > iterator = hover.iterator( );
    //     while (iterator.hasNext( )) {
    //         String s = iterator.next( );
    //         hovermsg.append( s ).append( (iterator.hasNext( ) ? "\n" : "") );
    //     }
    //     text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , new ComponentBuilder( ChatColor.translateAlternateColorCodes( '&' , hovermsg.toString( ) ) ).create( ) ) );
    //     text.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND , command ) );
    //     return text;
    // }
}