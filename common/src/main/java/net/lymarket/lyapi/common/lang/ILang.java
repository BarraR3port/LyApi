/*
 * Copyright (c) 2022. BSD 3-Clause License, BarraR3port.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/BarraR3port/LyApi/blob/master/LICENSE
 *
 * Contact: barrar3port@gmail.com
 */

package net.lymarket.lyapi.common.lang;

import net.lymarket.lyapi.common.config.ConfigGenerator;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class ILang {
    
    protected final ConfigGenerator lang;
    protected String prefix;
    protected String errorPrefix;
    
    public ILang(ConfigGenerator configGenerator, String prefix, String errorPrefix){
        this.lang = configGenerator;
        this.prefix = prefix;
        this.errorPrefix = errorPrefix;
    }
    
    public void saveLang(){
        lang.saveData();
    }
    
    public void reloadLang(){
        try {
            lang.loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ConfigGenerator getConfig(){
        return lang;
    }
    
    
    public void sendMsg(CommandSender c, String key){
        if (key.contains("error.")){
            sendErrorMsg(c, key);
            return;
        }
        c.sendMessage(format(prefix + getConfig().getString(key)));
    }
    
    
    public void sendMsg(CommandSender c, String key, String wordToReplace, String replacement){
        if (key.contains("error.")){
            sendErrorMsg(c, key, wordToReplace, replacement);
            return;
        }
        c.sendMessage(format(prefix + getConfig().getString(key).replace("%" + wordToReplace + "%", replacement)));
    }
    
    public void sendMsg(Player c, String key, String wordToReplace, TextComponent replacement){
        if (key.contains("error.")){
            sendErrorMsg(c, key, wordToReplace, replacement);
            return;
        }
        String[] midMsg = (prefix + getConfig().getString(key)).split("%" + wordToReplace + "%");
        TextComponent text = new TextComponent(format(midMsg[0]));
        text.addExtra(replacement);
        text.addExtra(format(midMsg[1]));
        c.spigot().sendMessage(text);
        
    }
    
    public void sendMsg(CommandSender c, String key, HashMap<String, String> wordsToReplace){
        if (key.contains("error.")){
            sendErrorMsg(c, key, wordsToReplace);
            return;
        }
        String msg = getConfig().getString(key);
        for ( String wordToReplace : wordsToReplace.keySet() ){
            msg = msg.replace("%" + wordToReplace + "%", wordsToReplace.get(wordToReplace));
        }
        c.sendMessage(format(prefix + msg));
    }
    
    
    public void sendErrorMsg(CommandSender c, String key){
        c.sendMessage(format("&c[&7ERROR&c] " + getConfig().getString(key.contains("error.") ? "" : "error." + key)));
    }
    
    
    public void sendErrorMsg(CommandSender c, String key, String wordToReplace, String replacement){
        c.sendMessage(format("&c[&7ERROR&c] " + getConfig().getString(key.contains("error.") ? "" : "error." + key).replace("%" + wordToReplace + "%", replacement)));
    }
    
    public void sendErrorMsg(CommandSender c, String key, HashMap<String, String> wordsToReplace){
        String msg = getConfig().getString(key.contains("error.") ? "" : "error." + key);
        for ( String wordToReplace : wordsToReplace.keySet() ){
            msg = msg.replace("%" + wordToReplace + "%", wordsToReplace.get(wordToReplace));
        }
        c.sendMessage(format("&c[&7ERROR&c] " + msg));
    }
    
    public void sendErrorMsg(Player c, String key, String wordToReplace, TextComponent replacement){
        String msg = "&c[&7ERROR&c] " + getConfig().getString(key.contains("error.") ? "" : "error." + key);
        String[] midMsg = msg.split("%" + wordToReplace + "%");
        TextComponent text = new TextComponent(format(midMsg[0]));
        text.addExtra(replacement);
        text.addExtra(format(midMsg[1]));
        c.spigot().sendMessage(text);
    }
    
    
    public String getMSG(String key){
        return format(getConfig().getString(key));
    }
    
    public String getMSG(String key, String wordToReplace, String replacement){
        return format(getConfig().getString(key)).replace("%" + wordToReplace + "%", replacement);
    }
    
    public String getMSG(String key, HashMap<String, String> wordsToReplace){
        String msg = getConfig().getString(key);
        for ( String wordToReplace : wordsToReplace.keySet() ){
            msg = msg.replace("%" + wordToReplace + "%", wordsToReplace.get(wordToReplace));
        }
        return format(msg);
    }
    
    public String getErrorMSG(String key, HashMap<String, String> wordsToReplace){
        String msg = getConfig().getString(key.contains("error.") ? "" : "error." + key);
        for ( String wordToReplace : wordsToReplace.keySet() ){
            msg = msg.replace("%" + wordToReplace + "%", wordsToReplace.get(wordToReplace));
        }
        return format("&c[&7ERROR&c] " + msg);
    }
    
    public String format(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
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