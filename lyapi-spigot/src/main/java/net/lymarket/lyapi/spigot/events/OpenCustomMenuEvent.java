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

package net.lymarket.lyapi.spigot.events;


import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OpenCustomMenuEvent extends Event implements Cancellable {
    
    private final Player player;
    private final HandlerList HANDLERS = new HandlerList();
    protected boolean canceled;
    private Menu menu;
    
    public OpenCustomMenuEvent(Menu menu, Player player){
        super();
        this.menu = menu;
        this.player = player;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public Menu getMenu(){
        return menu;
    }
    
    public void setMenu(Menu menu){
        this.menu = menu;
    }
    
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }
    
    @Override
    public boolean isCancelled(){
        return canceled;
    }
    
    @Override
    public void setCancelled(boolean b){
        this.canceled = b;
    }
}
