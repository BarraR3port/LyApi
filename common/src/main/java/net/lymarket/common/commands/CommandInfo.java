package net.lymarket.common.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInfo extends BukkitCommand {
    
    private final Object object;
    private final Method commandMethod;
    private final Method subCommandMethod;
    
    public CommandInfo( Object object , Method commandMethod , Method subCommandMethod , String name , String description , String usageMessage , List < String > aliases ){
        super( name , description , usageMessage , aliases );
        this.object = object;
        this.commandMethod = commandMethod;
        this.subCommandMethod = subCommandMethod;
    }
    
    public CommandInfo( Object object , Method commandMethod , Method subCommandMethod , Command command , SubCommand subCommands ){
        super( command.name( ) , command.description( ) , command.usage( ) , Arrays.asList( command.aliases( ) ) );
        this.object = object;
        this.commandMethod = commandMethod;
        this.subCommandMethod = subCommandMethod;
        setPermission( command.permission( ) );
    }
    
    @Override
    public boolean execute( CommandSender sender , String commandLabel , String[] args ){
        try {
            SCommandContext context = new SCommandContext( sender , args , this );
            if ( !sender.hasPermission( this.getPermission( ) ) ) {
                sender.sendMessage( "" );
                return false;
            }
            
            boolean ret = ( boolean ) commandMethod.invoke( object , context );
            if ( !ret ) sender.sendMessage( "" );
            return ret;
            
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            e.printStackTrace( );
            return false;
        }
    }
    
    @Override
    public ArrayList < String > tabComplete( CommandSender sender , String alias , String[] args ){
        try {
            STabContext context = new STabContext( sender , alias , args , this );
            
            return ( ArrayList < String > ) subCommandMethod.invoke( object , context );
            
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            e.printStackTrace( );
            return null;
        }
    }
}
