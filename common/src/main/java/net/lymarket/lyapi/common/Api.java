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

package net.lymarket.lyapi.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Api {
    
    public static String NO_PERMISSION;
    
    private static Gson gson;
    
    public Api(String permissionError){
        NO_PERMISSION = permissionError;
        gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("MMM dd, yyyy HH:mm:ss a").serializeNulls().create();
    }
    
    public Api(){
        this("&cYou don't have permission to do that.");
    }
    
    public static Gson getGson(){
        return gson;
    }
    
    public Api getApi(){
        return this;
    }
    
    public abstract void setErrorMSG(String permissionError);
}
