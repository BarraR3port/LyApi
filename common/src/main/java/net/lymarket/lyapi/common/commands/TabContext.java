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

package net.lymarket.lyapi.common.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TabContext {
    
    private final CommandSender sender;
    private final CommandInfo command;
    private final String alias;
    private final String[] args;
    
    public TabContext(CommandSender sender, String alias, String[] args, CommandInfo command){
        this.sender = sender;
        this.args = args;
        this.command = command;
        this.alias = alias;
    }
    
    public CommandSender getSender(){
        return sender;
    }
    
    public String[] getArgs(){
        return args;
    }
    
    public String getArg(int index){
        if (index > args.length - 1) return null;
        return args[index];
    }
    
    public int getArgLength(){
        return args.length;
    }
    
    public boolean isPlayer(){
        return sender instanceof Player;
    }
    
    public double getDouble(int index){
        return Double.parseDouble(getArg(index));
    }
    
    public int getInt(int index){
        return Integer.parseInt(getArg(index));
    }
    
    public boolean getBoolean(int index){
        return Boolean.parseBoolean(getArg(index));
    }
    
    public CommandInfo getCommand(){
        return command;
    }
    
    public String getAlias(){
        return alias;
    }
    
}
