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

package net.lymarket.common.commands;


import net.lymarket.common.Api;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.ChatColor;
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
    
    public CommandInfo(Object object, Method commandMethod, Method subCommandMethod, String name, String description, String usageMessage, List < String > aliases){
        super(name, description, usageMessage, aliases);
        this.object = object;
        this.commandMethod = commandMethod;
        this.subCommandMethod = subCommandMethod;
    }
    
    public CommandInfo(Object object, Method commandMethod, Method subCommandMethod, Command command, Tab tab){
        super(command.name(), command.description(), command.usage(), Arrays.asList(command.aliases()));
        this.object = object;
        this.commandMethod = commandMethod;
        this.subCommandMethod = subCommandMethod;
        setPermission(command.permission());
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        try {
            CommandContext context = new CommandContext(sender, args, this);
            if (getPermission() != null || !getPermission().equalsIgnoreCase("")){
                if (!sender.hasPermission(this.getPermission())){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Api.NO_PERMISSION.replace("permission", this.getPermission())));
                    return false;
                }
            }
            
            
            CommandResponse ret = (CommandResponse) commandMethod.invoke(object, context);
            if (ret.getResponse().equals(CommandResponse.ResponseType.NO_PERMISSION)){
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
    public ArrayList < String > tabComplete(CommandSender sender, String alias, String[] args){
        try {
            TabContext context = new TabContext(sender, alias, args, this);
            final ArrayList < String > ret = (ArrayList < String >) subCommandMethod.invoke(object, context);
            if (ret == null)
                return new ArrayList <>();
            final ArrayList < String > finalList = new ArrayList <>();
            for ( String s : ret ){
                if (s.toLowerCase().startsWith(context.getArg(context.getArgs().length - 1).toLowerCase())){
                    finalList.add(s);
                }
            }
            return finalList;
            
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new ArrayList <>();
        }
    }
}
