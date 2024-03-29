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

package net.lymarket.lyapi.common.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CommandService {
    
    private final HashMap<String, CommandInfo> commands;
    
    private CommandMap commandMap;
    
    public CommandService(){
        commands = new HashMap<>();
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception ignored) {
        
        }
    }
    
    public void registerCommands(ILyCommand command){
        Class<?> klass = command.getClass();
        Command cmd = null;
        Tab subCmd = null;
        Method cmdMethod = null;
        Method tabCompleteMethod = null;
        for ( Method method : klass.getMethods() ){
            if (method.isAnnotationPresent(Command.class)){
                cmd = method.getAnnotation(Command.class);
                cmdMethod = method;
            }
            if (method.isAnnotationPresent(Tab.class)){
                subCmd = method.getAnnotation(Tab.class);
                tabCompleteMethod = method;
            }
        }
        if (cmd == null) return;
        
        CommandInfo commandInfo = new CommandInfo(command, cmdMethod, tabCompleteMethod, cmd, subCmd);
        commands.put(commandInfo.getName(), commandInfo);
        commandMap.register(commandInfo.getName(), commandInfo);
    }
    
    public void unRegisterCustomCommands(){
        for ( CommandInfo commandInfo : commands.values() ){
            commandInfo.unregister(commandMap);
        }
        commands.clear();
    }
    
    public void unRegisterCustomCommand(String commandName){
        if (commands.containsKey(commandName)){
            commands.get(commandName).unregister(commandMap);
            commands.remove(commandName);
        }
    }
    
}
