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
import net.lymarket.common.version.VersionSupport;
import net.lymarket.lyapi.spigot.listeners.MenuListener;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.PlayerMenuUtility;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;

public final class LyApi extends Api {
    
    private static final HashMap < Player, IPlayerMenuUtility > playerMenuUtilityMap = new HashMap <>( );
    
    private static LyApi instance;
    
    private static Plugin plugin;
    
    private final Utils utils;
    
    private final String version = Bukkit.getServer( ).getClass( ).getName( ).split( "\\." )[3];
    
    private final CommandService commandService;
    
    private final String pluginName;
    
    private VersionSupport nms;
    
    public LyApi( Plugin plugin , String pluginName ){
        this( plugin , pluginName , "§cYou don't have permission to do that!" );
    }
    
    public LyApi( Plugin plugin , String pluginName , String noPermissionError ){
        super( noPermissionError );
        instance = this;
        utils = new Utils( );
        LyApi.plugin = plugin;
        this.pluginName = "[" + pluginName + "] ";
        
        Class supp;
        
        this.commandService = new CommandService( );
        try {
            supp = Class.forName( "net.lymarket.lyapi.support.version." + version + "." + version );
        } catch ( ClassNotFoundException e ) {
            return;
        }
        
        try {
            //noinspection unchecked
            this.nms = ( VersionSupport ) supp.getConstructor( Class.forName( "org.bukkit.plugin.Plugin" ) , String.class ).newInstance( plugin , version );
        } catch ( InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e ) {
            e.printStackTrace( );
        }
        
        
        Bukkit.getServer( ).getPluginManager( ).registerEvents( new MenuListener( ) , plugin );
    }
    
    public static Plugin getPlugin( ){
        return plugin;
    }
    
    public static LyApi getInstance( ){
        return instance;
    }
    
    public static IPlayerMenuUtility getPlayerMenuUtility( Player p ){
        IPlayerMenuUtility playerMenuUtility;
        
        if ( playerMenuUtilityMap.containsKey( p ) ) {
            return playerMenuUtilityMap.get( p );
        }
        
        playerMenuUtility = new PlayerMenuUtility( p );
        playerMenuUtilityMap.put( p , playerMenuUtility );
        return playerMenuUtility;
    }
    
    public VersionSupport getNMS( ){
        return nms;
    }
    
    public Utils getUtils( ){
        return utils;
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
    
    public void log( Level logLevel , String message ){
        plugin.getLogger( ).log( logLevel , pluginName + " " + message );
    }
    
    public void log( Level logLevel , String message , Error error ){
        plugin.getLogger( ).log( logLevel , pluginName + " " + message , error );
    }
    
}
