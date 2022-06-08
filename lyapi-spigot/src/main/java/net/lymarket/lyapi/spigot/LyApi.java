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

package net.lymarket.lyapi.spigot;


import net.lymarket.common.Api;
import net.lymarket.common.commands.CommandService;
import net.lymarket.common.error.LyApiInitializationError;
import net.lymarket.common.lang.ILang;
import net.lymarket.common.version.VersionSupport;
import net.lymarket.lyapi.spigot.listeners.MenuListener;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;

public final class LyApi extends Api {
    
    private static final HashMap < Player, IPlayerMenuUtility > playerMenuUtilityMap = new HashMap <>();
    
    private static LyApi instance;
    
    private static Plugin plugin;
    
    private static ILang lang;
    
    private final String pluginName;
    
    private final String version;
    
    private final CommandService commandService;
    
    private VersionSupport nms;
    
    public LyApi(JavaPlugin plugin, String pluginName) throws LyApiInitializationError{
        this(plugin, pluginName, "§cYou don't have permission to do that!");
    }
    
    public LyApi(JavaPlugin plugin, String pluginName, ILang language) throws LyApiInitializationError{
        this(plugin, pluginName, "§cYou don't have permission to do that!", language);
    }
    
    public LyApi(JavaPlugin plugin, String pluginName, String noPermissionError) throws LyApiInitializationError{
        this(plugin, pluginName, noPermissionError, null);
    }
    
    public LyApi(JavaPlugin plugin, String pluginName, String noPermissionError, ILang language) throws LyApiInitializationError{
        super(noPermissionError);
        instance = this;
        this.pluginName = "[" + pluginName + "] ";
        LyApi.plugin = plugin;
        lang = language;
        this.version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        Class supp;
        try {
            supp = Class.forName("net.lymarket.lyapi.support.version." + version + "." + version);
        } catch (ClassNotFoundException e) {
            throw new LyApiInitializationError(version);
        }
        
        try {
            //noinspection unchecked
            this.nms = (VersionSupport) supp.getConstructor(Class.forName("org.bukkit.plugin.Plugin"), String.class).newInstance(plugin, version);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.commandService = new CommandService();
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
    }
    
    public static Plugin getPlugin( ){
        return plugin;
    }
    
    public static LyApi getInstance( ){
        return instance;
    }
    
    public static IPlayerMenuUtility getPlayerMenuUtility(Player p){
        IPlayerMenuUtility playerMenuUtility;
        
        if (playerMenuUtilityMap.containsKey(p)){
            return playerMenuUtilityMap.get(p);
        }
        
        playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }
    
    public static ILang getLanguage( ){
        return lang;
    }
    
    /**
     * Set the main Language {@link ILang}.
     *
     * <p>The state of immutable instances will never change.</p>
     * <p>
     * This method allows you to set the language of the plugin.
     */
    public LyApi setLanguage(ILang language){
        lang = language;
        return this;
    }
    
    public VersionSupport getNMS( ){
        return nms;
    }
    
    public String getVersion( ){
        return version;
    }
    
    public CommandService getCommandService( ){
        return this.commandService;
    }
    
    public String getPluginName( ){
        return this.pluginName;
    }
    
    public void log(Level logLevel, String message){
        plugin.getLogger().log(logLevel, pluginName + " " + message);
    }
    
    public void log(Level logLevel, String message, Error error){
        plugin.getLogger().log(logLevel, pluginName + " " + message, error);
    }
    
    @Override
    public void setErrorMSG(String permissionError){
        NO_PERMISSION = ChatColor.translateAlternateColorCodes('&', permissionError);
    }
}
