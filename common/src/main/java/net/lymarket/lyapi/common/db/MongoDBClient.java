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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.Function;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.LinkedList;

public class MongoDBClient {
    
    protected final Gson gson;
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
        gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("MMM dd, yyyy HH:mm:ss a").serializeNulls().create();
        
    }
    
    public Gson getGson(){
        return gson;
    }
    
    public MongoClient getClient(){
        return client;
    }
    
    public MongoDatabase getDatabase(){
        return database;
    }
    
    public Document findOneFast(String name, Bson filter){
        MongoCollection<Document> collection = database.getCollection(name);
        return collection.find(filter).first();
    }
    
    public boolean updateOneFast(String name, Bson filter, Bson update){
        MongoCollection<Document> collection = database.getCollection(name);
        return collection.updateOne(filter, update).wasAcknowledged();
    }
    
    public boolean replaceOneFast(String name, Bson filter, Object replace){
        String json = gson.toJson(replace);
        Document document = Document.parse(json);
    
        return replaceOneFast(name, filter, document);
    }
    
    public boolean replaceOneFast(String name, Bson filter, Document replace){
        MongoCollection<Document> collection = database.getCollection(name);
        return collection.replaceOne(filter, replace).wasAcknowledged();
    }
    
    public LinkedList<Document> findManyFast(String name, Bson filter){
        MongoCollection<Document> collection = database.getCollection(name);
        LinkedList<Document> list = new LinkedList<>();
        collection.find(filter).forEach(list::add);
        return list;
    }
    
    public <T> LinkedList<T> findMany(String name, Function<T, Boolean> filter, Class<T> klass){
        LinkedList<T> list = new LinkedList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(name);
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = gson.fromJson(cursor.next().toJson(), klass);
                if (filter.apply(current)) list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public <T> LinkedList<T> findMany(String name, Class<T> klass){
        LinkedList<T> list = new LinkedList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(name);
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = gson.fromJson(cursor.next().toJson(), klass);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public <T> LinkedList<T> findManyFiltered(String name, Bson filter, Class<T> klass){
        LinkedList<T> list = new LinkedList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(name);
            FindIterable<Document> documents = collection.find(filter);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = gson.fromJson(cursor.next().toJson(), klass);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public <T> LinkedList<T> findManyPaginated(String name, int currentPage, int maxPerPage, Class<T> klass){
        LinkedList<T> list = new LinkedList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(name);
            FindIterable<Document> documents = collection.find().skip(currentPage).limit(maxPerPage);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = gson.fromJson(cursor.next().toJson(), klass);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public <T> LinkedList<T> findManyPaginatedAndFilter(String name, Bson filter, int currentPage, int maxPerPage, Class<T> klass){
        LinkedList<T> list = new LinkedList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(name);
            FindIterable<Document> documents = collection.find(filter).skip(currentPage).limit(maxPerPage);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                T current = gson.fromJson(cursor.next().toJson(), klass);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public <T> T findOne(String name, Function<T, Boolean> filter, Class<T> klass){
        MongoCollection<Document> collection = database.getCollection(name);
        FindIterable<Document> documents = collection.find();
        MongoCursor<Document> cursor = documents.cursor();
        while (cursor.hasNext()) {
            T current = gson.fromJson(cursor.next().toJson(), klass);
            if (filter.apply(current)) return current;
        }
        return null;
    }
    
    public <T> T findOneFiltered(String name, Bson filter, Class<T> klass){
        MongoCollection<Document> collection = database.getCollection(name);
        Document document = collection.find(filter).first();
        if (document == null) return null;
        return gson.fromJson(document.toJson(), klass);
    }
    
    public boolean insertOne(String name, Object data){
        MongoCollection<Document> collection = database.getCollection(name);
        return collection.insertOne(Document.parse(gson.toJson(data))).wasAcknowledged();
    }
    
    public boolean deleteOne(String name, Bson filter){
        MongoCollection<Document> collection = database.getCollection(name);
        return collection.deleteOne(filter).wasAcknowledged();
    }
    
    public void Desconectar(){
        client.close();
    }
}
