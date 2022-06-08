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

package net.lymarket.common.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ConfigGenerator extends CommentConfig {
    
    private final String fileName;
    private final String resourcePath;
    private final JavaPlugin plugin;
    private final File filePath;
    private File file;
    
    /**
     * @param plugin Instancia del plugin
     * @param name   Nombre del archivo
     */
    public ConfigGenerator(JavaPlugin plugin, String name){
        this.plugin = plugin;
    
        this.fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.resourcePath = fileName;
    
        this.filePath = plugin.getDataFolder();
    
        loadFile();
        createData();
    
        try {
            loadConfig();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param plugin   Instancia del plugin
     * @param name     Nombre del archivo
     * @param filePath Path del directorio
     */
    public ConfigGenerator(JavaPlugin plugin, String name, String resourcePath, String filePath){
        this.plugin = plugin;
        
        this.fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.resourcePath = resourcePath;
        
        this.filePath = new File(filePath);
        
        loadFile();
        createData();
        
        try {
            loadConfig();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadConfig( ) throws Exception{
        try {
            this.load(file);
        } catch (FileNotFoundException e) {
            loadFile();
            createData();
    
            try {
                loadConfig();
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void reloadConfig( ){
        try {
            this.loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadFile( ){
        this.file = new File(filePath, this.fileName);
    }
    
    public void saveData( ){
        this.file = new File(filePath, this.fileName);
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
            createData();
            saveData();
        }
    }
    
    @Override
    public void save(File file) throws IOException{
        super.save(file);
    }
    
    public void createData( ){
        if (!file.exists()){
            if (!this.filePath.exists()){
                final boolean result = this.filePath.mkdirs();
                if (!result){
                    throw new IllegalStateException("Could not create directory for " + this.filePath.getAbsolutePath());
                }
            }
    
            //If file isn't a resource, create from scratch
            try {
                final boolean result = this.file.createNewFile();
                if (!result){
                    throw new IllegalStateException("Could not create directory for " + this.filePath.getAbsolutePath());
                }
                writeToFile(this.plugin.getResource(this.resourcePath), this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void delete( ){
        if (this.file.exists()){
            final boolean result = this.file.delete();
            if (!result){
                throw new IllegalStateException("Could not delete file " + this.file.getAbsolutePath());
            }
        }
    }
    
    @Override
    public void load(File file) throws IOException, InvalidConfigurationException{
        Validate.notNull(file, "File cannot be null");
        FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }
    
    //WRITE TO FILE FROM INPUT STREAM
    private void writeToFile(final InputStream input, final File target) throws IOException{
        final OutputStream output = Files.newOutputStream(target.toPath());
        final byte[] buffer = new byte[8 * 1024];
        int length = input.read(buffer);
        while (length > 0) {
            output.write(buffer, 0, length);
            length = input.read(buffer);
        }
        input.close();
        output.close();
    }
    
    public String[] getStringArrayList(String path){
        return this.getStringList(path).toArray(new String[0]);
    }
    
}
