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