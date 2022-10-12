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

public final class CommandResponse {
    private ResponseType response;
    private String permission;
    
    private CommandResponse(){
        this.response = ResponseType.SUCCESS;
    }
    
    public static CommandResponse accept(){
        CommandResponse response = new CommandResponse();
        response.response = ResponseType.SUCCESS;
        return response;
    }
    
    public static CommandResponse deny(){
        CommandResponse response = new CommandResponse();
        response.response = ResponseType.NO_PERMISSION;
        response.permission = "default.permission";
        return response;
    }
    
    public static CommandResponse deny(String permission){
        CommandResponse response = new CommandResponse();
        response.response = ResponseType.NO_PERMISSION;
        response.permission = permission;
        return response;
    }
    
    public ResponseType getResponse(){
        return response;
    }
    
    private void setResponse(ResponseType response){
        this.response = response;
    }
    
    private void setPermission(String permission){
        this.permission = permission;
    }
    
    public String getPermission(){
        return this.response.equals(ResponseType.NO_PERMISSION) ? this.permission : "";
    }
}

