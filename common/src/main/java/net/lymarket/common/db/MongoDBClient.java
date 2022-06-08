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

package net.lymarket.common.db;

import com.mongodb.ConnectionString;
import com.mongodb.Function;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.*;
import net.lymarket.common.Api;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class MongoDBClient {
    
    private final MongoClient client;
    private final MongoDatabase database;
    
    public MongoDBClient(String host, String database){
        CodecRegistry codec = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codec)
                .applyConnectionString(new ConnectionString(host))
                .build();
        client = MongoClients.create(settings);
        this.database = client.getDatabase(database);
    
    }
    
    public MongoClient getClient( ){
        return client;
    }
    
    public MongoDatabase getDatabase( ){
        return database;
    }
    
    public Document findOneFast(String name, Bson filter){
        MongoCollection < Document > collection = database.getCollection(name);
        return collection.find(filter).first();
    }
    
    public boolean updateOneFast(String name, Bson filter, Bson update){
        MongoCollection < Document > collection = database.getCollection(name);
        return collection.updateOne(filter, update).wasAcknowledged();
    }
    
    public boolean replaceOneFast(String name, Bson filter, Object replace){
        String json = Api.getGson().toJson(replace);
        Document document = Document.parse(json);
        
        return replaceOneFast(name, filter, document);
    }
    
    public boolean replaceOneFast(String name, Bson filter, Document replace){
        MongoCollection < Document > collection = database.getCollection(name);
        return collection.replaceOne(filter, replace).wasAcknowledged();
    }
    
    public ArrayList < Document > findManyFast(String name, Bson filter){
        MongoCollection < Document > collection = database.getCollection(name);
        ArrayList < Document > list = new ArrayList <>();
        collection.find(filter).forEach(list::add);
        return list;
    }
    
    public < T > ArrayList < T > findMany(String name, Function < T, Boolean > filter, Class < T > klass){
        ArrayList < T > list = new ArrayList <>();
        try {
            MongoCollection < Document > collection = database.getCollection(name);
            FindIterable < Document > documents = collection.find();
            MongoCursor < Document > cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = Api.getGson().fromJson(cursor.next().toJson(), klass);
                if (filter.apply(current)) list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public < T > ArrayList < T > findMany(String name, Class < T > klass){
        ArrayList < T > list = new ArrayList <>();
        try {
            MongoCollection < Document > collection = database.getCollection(name);
            FindIterable < Document > documents = collection.find();
            MongoCursor < Document > cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = Api.getGson().fromJson(cursor.next().toJson(), klass);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public < T > T findOne(String name, Function < T, Boolean > filter, Class < T > klass){
        MongoCollection < Document > collection = database.getCollection(name);
        FindIterable < Document > documents = collection.find();
        MongoCursor < Document > cursor = documents.cursor();
        while (cursor.hasNext()) {
            T current = Api.getGson().fromJson(cursor.next().toJson(), klass);
            if (filter.apply(current)) return current;
        }
        return null;
    }
    
    public boolean insertOne(String name, Object data){
        MongoCollection < Document > collection = database.getCollection(name);
        return collection.insertOne(Document.parse(Api.getGson().toJson(data))).wasAcknowledged();
    }
    
    public boolean deleteOne(String name, Bson filter){
        MongoCollection < Document > collection = database.getCollection(name);
        return collection.deleteOne(filter).wasAcknowledged();
    }
    
    public void Desconectar( ){
        client.close();
    }
}
