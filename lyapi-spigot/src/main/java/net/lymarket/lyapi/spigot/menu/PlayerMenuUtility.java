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

package net.lymarket.lyapi.spigot.menu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility implements IPlayerMenuUtility {
    
    private Player owner;
    private Player target;
    
    public PlayerMenuUtility(Player owner){
        this.owner = owner;
    }
    
    public Player getOwner(){
        return this.owner;
    }
    
    public void setOwner(Player owner){
        this.owner = owner;
    }
    
    
    public Player getTarget(){
        return target;
    }
    
    public void setTarget(Player target){
        this.target = target;
    }
}
