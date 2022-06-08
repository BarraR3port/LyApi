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

package net.lymarket.common.error;

public class LyApiInitializationError extends Exception {
    
    
    public LyApiInitializationError(String version){
        super("There has being an error when loading the NMS for the version:" + version);
    }
}
