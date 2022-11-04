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

package net.lymarket.lyapi.common.db;

import java.util.LinkedHashMap;

public abstract class MongoDB<K, V> {
    
    protected final String TABLE_NAME;
    protected final LinkedHashMap<K, V> list = new LinkedHashMap<>();
    protected final MongoDBClient database;
    
    public MongoDB(MongoDBClient database, String TABLE_NAME){
        this.database = database;
        this.TABLE_NAME = TABLE_NAME;
        this.trashFinder();
    }
    
    public void trashFinder(){
    
    }
    
}