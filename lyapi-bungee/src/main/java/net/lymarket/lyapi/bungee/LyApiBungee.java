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

package net.lymarket.lyapi.bungee;

import net.lymarket.lyapi.common.Api;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class LyApiBungee extends Api {
    
    private static LyApiBungee instance;
    
    private static Plugin plugin;
    
    public LyApiBungee(Plugin plugin, String noPermissionError){
        super(noPermissionError);
        instance = this;
        LyApiBungee.plugin = plugin;
    }
    
    public static Plugin getPlugin( ){
        return plugin;
    }
    
    public static LyApiBungee getInstance( ){
        return instance;
    }
    
    @Override
    public void setErrorMSG(String permissionError){
        NO_PERMISSION = ChatColor.translateAlternateColorCodes('&', permissionError);
    }
}
