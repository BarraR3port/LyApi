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


import net.lymarket.lyapi.common.Api;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.common.commands.response.ResponseType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandInfo extends BukkitCommand {
    
    private final ILyCommand object;
    private final Method commandMethod;
    private final Method tabCompleteMethod;
    
    public CommandInfo(ILyCommand object, Method commandMethod, Method subCommandMethod, String name, String description, String usageMessage, List<String> aliases){
        super(name, description, usageMessage, aliases);
        this.object = object;
        this.commandMethod = commandMethod;
        this.tabCompleteMethod = subCommandMethod;
    }
    
    public CommandInfo(ILyCommand object, Method commandMethod, Method tabCompleteMethod, Command command, Tab tab){
        super(command.name(), command.description(), command.usage(), Arrays.asList(command.aliases()));
        this.object = object;
        this.commandMethod = commandMethod;
        this.tabCompleteMethod = tabCompleteMethod;
        setPermission(command.permission());
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        try {
            CommandContext context = new CommandContext(sender, args, this);
            if (getPermission() != null && !(getPermission().length() == 0)){
                if (!sender.hasPermission(this.getPermission())){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Api.NO_PERMISSION.replace("permission", this.getPermission())));
                    return false;
                }
            }
            
            
            CommandResponse ret = (CommandResponse) commandMethod.invoke(object, context);
            if (ret.getResponse().equals(ResponseType.NO_PERMISSION)){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Api.NO_PERMISSION.replace("permission", ret.getPermission())));
                return false;
            } else {
                return true;
            }
            
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args){
        try {
            TabContext context = new TabContext(sender, alias, args, this);
            final LinkedList<String> ret = (LinkedList<String>) tabCompleteMethod.invoke(object, context);
            if (ret == null)
                return new LinkedList<>();
            final LinkedList<String> finalList = new LinkedList<>();
            for ( String s : ret ){
                if (s.toLowerCase().startsWith(context.getArg(context.getArgs().length - 1).toLowerCase())){
                    finalList.add(s);
                }
            }
            return finalList;
            
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
}
