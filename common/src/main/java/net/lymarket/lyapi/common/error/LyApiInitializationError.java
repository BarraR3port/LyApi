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

package net.lymarket.lyapi.common.error;

public class LyApiInitializationError extends Exception {
    
    
    public LyApiInitializationError(String version){
        super("There has being an error when loading the NMS for the version:" + version);
    }
}
