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

package net.lymarket.lyapi.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.lymarket.common.Api;
import net.lymarket.lyapi.velocity.utils.ChatColor;

public class LyApiVelocity extends Api {
    
    
    private static LyApiVelocity instance;
    
    private static ProxyServer plugin;
    
    public LyApiVelocity( ProxyServer plugin , String noPermissionError ){
        super( noPermissionError );
        instance = this;
        LyApiVelocity.plugin = plugin;
    }
    
    public static ProxyServer getPlugin( ){
        return plugin;
    }
    
    public static LyApiVelocity getInstance( ){
        return instance;
    }
    
    @Override
    public void setErrorMSG( String permissionError ){
        NO_PERMISSION = ChatColor.translateAlternateColorCodes( '&' , permissionError );
    }
}
