package net.lymarket.lyapi.spigot;


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

public final class SMain {
    
    private static final HashMap < Player, IPlayerMenuUtility > playerMenuUtilityMap = new HashMap <>( );
    
    private static SMain instance;
    
    private static Plugin plugin;
    
    private final Utils utils;
    
    private final String version = Bukkit.getServer( ).getClass( ).getName( ).split( "\\." )[3];
    
    private final CommandService commandService;
    
    private VersionSupport nms;
    
    public SMain( Plugin plugin ){
    
        instance = this;
        utils = new Utils( );
        SMain.plugin = plugin;
    
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
    
    public static SMain getInstance( ){
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
}
