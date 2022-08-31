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
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.*;
import java.util.regex.PatternSyntaxException;

public class Utils {
    
    private static final HashMap<String, String> inventory = new HashMap<>();
    
    public static void sendMessage(CommandSender player, String message){
        player.sendMessage(format(message));
    }
    
    public static void sendMessage(CommandSender sender, TextComponent... message){
        if (sender instanceof Player){
            ((Player) sender).spigot().sendMessage(message);
        } else {
            sender.sendMessage(formatTC(message));
        }
    }
    
    public static void sendMessage(Player sender, TextComponent... message){
        sender.spigot().sendMessage(message);
    }
    
    public static void playSound(Player p, String sound){
        XSound.play(p, sound);
    }
    
    public static void playSound(Player p, Sound sound){
        XSound.play(p, sound.name());
    }
    
    
    public static String getServer(){
        return LyApi.getPlugin().getConfig().getString("server.name");
    }
    
    public static String format(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static String[] format(String... msg){
        for ( int i = 0; i < msg.length; i++ ){
            msg[i] = ChatColor.translateAlternateColorCodes('&', msg[i]);
        }
        return msg;
    }
    
    public static String[] formatTC(TextComponent... msg){
        String[] finalMsg = new String[msg.length];
        for ( int i = 0; i < msg.length; i++ ){
            if (msg[i].getHoverEvent() != null){
                finalMsg[i] = ChatColor.translateAlternateColorCodes('&', msg[i].toLegacyText() + " &8[HOVER >] " + msg[i].getHoverEvent().getValue().toString());
            } else {
                finalMsg[i] = ChatColor.translateAlternateColorCodes('&', msg[i].toLegacyText());
            }
        }
        return finalMsg;
    }
    
    public static TextComponent formatTC(String msg){
        return new TextComponent(format(msg));
    }
    
    public static TextComponent hoverOverMessage(String msg, List<String> hover){
        final TextComponent text = new TextComponent(format(msg));
        final StringBuilder hovermsg = new StringBuilder();
        final Iterator<String> iterator = hover.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            hovermsg.append(s).append((iterator.hasNext() ? "\n" : ""));
        }
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(format(hovermsg.toString())).create()));
        return text;
    }
    
    public static TextComponent hoverOverMessageURL(String msg, List<String> hover, String url){
        final TextComponent text = new TextComponent(format(msg));
        final StringBuilder hovermsg = new StringBuilder();
        final Iterator<String> iterator = hover.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            hovermsg.append(s).append((iterator.hasNext() ? "\n" : ""));
        }
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(format(hovermsg.toString())).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return text;
    }
    
    public static TextComponent hoverOverMessageRunCommand(String msg, List<String> hover, String command){
        final TextComponent text = new TextComponent(format(msg));
        final StringBuilder hovermsg = new StringBuilder();
        final Iterator<String> iterator = hover.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            hovermsg.append(s).append((iterator.hasNext() ? "\n" : ""));
        }
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(format(hovermsg.toString())).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return text;
    }
    
    public static TextComponent hoverOverMessageSuggestCommand(String msg, List<String> hover, String command){
        final TextComponent text = new TextComponent(format(msg));
        final StringBuilder hovermsg = new StringBuilder();
        final Iterator<String> iterator = hover.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            hovermsg.append(s).append((iterator.hasNext() ? "\n" : ""));
        }
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(format(hovermsg.toString())).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return text;
    }
    
    public static void savePlayerInventory(String name, PlayerInventory inv){
        final String[] serialized = Serializer.playerInventoryToBase64(inv);
        final List<String> serialized2 = Arrays.asList(serialized);
        final String serialized3 = serialized2.get(0);
        inventory.put(name, serialized3);
    }
    
    public static void playActionBar(Player p, String text){
        LyApi.getInstance().getNMS().playAction(p, format(text));
    }
    
    public static void removePlayerInventory(String name){
        inventory.remove(name);
    }
    
    public static String stripColors(String text){
        ArrayList<String> toRemove = new ArrayList<>();
        for ( int i = 0; i < text.length(); i++ ){
            if (text.charAt(i) == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(text.charAt(i + 1)) > -1){
                toRemove.add(String.valueOf(text.subSequence(i, i + 2)));
            }
        }
        for ( String s : toRemove ){
            text = text.replace(s, "");
        }
        return text;
    }
    
    public static TextComponent stripColorsToTextComponent(String text){
        return new TextComponent(stripColors(text));
    }
    
    public static Inventory getPlayerInventory(String name){
        final String noSerialized = inventory.get(name);
        try {
            return Serializer.fromBase64(noSerialized);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Color colorConverter(String color){
        if ("RED".equalsIgnoreCase(color)){
            return Color.RED;
        } else if ("BLUE".equalsIgnoreCase(color)){
            return Color.BLUE;
        } else if ("GREEN".equalsIgnoreCase(color)){
            return Color.GREEN;
        } else if ("YELLOW".equalsIgnoreCase(color)){
            return Color.YELLOW;
        } else if ("WHITE".equalsIgnoreCase(color)){
            return Color.WHITE;
        } else if ("GRAY".equalsIgnoreCase(color)){
            return Color.GRAY;
        } else if ("PURPLE".equalsIgnoreCase(color)){
            return Color.PURPLE;
        } else if ("AQUA".equalsIgnoreCase(color)){
            return Color.AQUA;
        } else if ("LIME".equalsIgnoreCase(color)){
            return Color.LIME;
        } else if ("SILVER".equalsIgnoreCase(color)){
            return Color.SILVER;
        } else if ("MAROON".equalsIgnoreCase(color)){
            return Color.MAROON;
        } else if ("OLIVE".equalsIgnoreCase(color)){
            return Color.OLIVE;
        } else if ("TEAL".equalsIgnoreCase(color)){
            return Color.TEAL;
        } else if ("FUCHSIA".equalsIgnoreCase(color)){
            return Color.FUCHSIA;
        } else if ("NAVY".equalsIgnoreCase(color)){
            return Color.NAVY;
        } else if ("ORANGE".equalsIgnoreCase(color)){
            return Color.ORANGE;
        } else if ("BLACK".equalsIgnoreCase(color)){
            return Color.BLACK;
        }
        try {
            return Color.fromRGB(Integer.parseInt(color));
        } catch (NumberFormatException ignored) {
        }
        try {
            String[] rgb = color.split(",");
            int RED = Integer.parseInt(rgb[0]);
            int GREEN = Integer.parseInt(rgb[1]);
            int BLUE = Integer.parseInt(rgb[2]);
            return Color.fromRGB(RED, GREEN, BLUE);
        } catch (IndexOutOfBoundsException | PatternSyntaxException | NumberFormatException ignored) {
        }
        return Color.WHITE;
    }
    
}
