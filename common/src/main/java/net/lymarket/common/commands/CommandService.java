package net.lymarket.common.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CommandService {
    
    private final HashMap < String, CommandInfo > commands;
    
    private CommandMap commandMap;
    
    public CommandService( ){
        commands = new HashMap <>( );
        try {
            final Field bukkitCommandMap = Bukkit.getServer( ).getClass( ).getDeclaredField( "commandMap" );
            bukkitCommandMap.setAccessible( true );
            commandMap = ( CommandMap ) bukkitCommandMap.get( Bukkit.getServer( ) );
        } catch ( Exception e ) {
        
        }
    }
    
    public void registerCommands( ILyCommand command ){
        Class < ? > klass = command.getClass( );
        Command cmd = null;
        SubCommand subCmd = null;
        Method cmdMethod = null;
        Method subCmdMethod = null;
        for ( Method method : klass.getMethods( ) ) {
            if ( method.isAnnotationPresent( Command.class ) ) {
                cmd = method.getAnnotation( Command.class );
                cmdMethod = method;
            }
            if ( method.isAnnotationPresent( SubCommand.class ) ) {
                subCmd = method.getAnnotation( SubCommand.class );
                subCmdMethod = method;
            }
            
        }
        if ( cmd == null ) return;
        
        CommandInfo commandInfo = new CommandInfo( command , cmdMethod , subCmdMethod , cmd , subCmd );
        commands.put( commandInfo.getName( ) , commandInfo );
        commandMap.register( commandInfo.getName( ) , commandInfo );
    }
}
