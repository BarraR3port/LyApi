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

package net.lymarket.lyapi.common.commands.response;

public class CommandResponse {
    protected ResponseType response;
    private String permission;
    
    public CommandResponse accept(){
        this.response = ResponseType.SUCCESS;
        return this;
    }
    
    public CommandResponse deny(){
        this.permission = "default.permission";
        this.response = ResponseType.NO_PERMISSION;
        return this;
    }
    
    public CommandResponse deny(String permission){
        this.permission = permission;
        this.response = ResponseType.NO_PERMISSION;
        return this;
    }
    
    public ResponseType getResponse(){
        return response;
    }
    
    public String getPermission(){
        return this.response.equals(ResponseType.NO_PERMISSION) ? this.permission : "";
    }
    
    public enum ResponseType {
        SUCCESS,
        NO_PERMISSION
    }
}

