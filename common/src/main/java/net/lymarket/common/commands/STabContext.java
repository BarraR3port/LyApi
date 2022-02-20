package net.lymarket.common.commands;

import org.bukkit.command.CommandSender;


public class STabContext {
    
    private final CommandSender sender;
    private final CommandInfo command;
    private final String alias;
    private final String[] args;
    
    public STabContext( CommandSender sender , String alias , String[] args , CommandInfo command ){
        this.sender = sender;
        this.args = args;
        this.command = command;
        this.alias = alias;
    }
    
    public CommandSender getSender( ){
        return sender;
    }
    
    public String[] getArgs( ){
        return args;
    }
    
    public String getArg( int index ){
        if ( index > args.length - 1 ) return null;
        return args[index];
    }
    
    public double getDouble( int index ){
        return Double.parseDouble( getArg( index ) );
    }
    
    public int getInt( int index ){
        return Integer.parseInt( getArg( index ) );
    }
    
    public boolean getBoolean( int index ){
        return Boolean.parseBoolean( getArg( index ) );
    }
    
    public CommandInfo getCommand( ){
        return command;
    }
    
    public String getAlias( ){
        return alias;
    }
    
}
