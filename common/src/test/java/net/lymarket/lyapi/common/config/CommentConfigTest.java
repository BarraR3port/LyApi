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

package net.lymarket.lyapi.common.config;

import junit.framework.TestCase;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CommentConfigTest extends TestCase {
    
    
    private final Map<String, List<String>> comments = new HashMap<>();
    
    
    public void testSaveToString(){
        String contents = "#    ██╗   ██╗██╗██████╗ ████████╗██╗   ██╗ █████╗ ██╗         ██████╗ ██╗███╗   ██╗ █████╗ ████████╗ █████╗\n" +
                "#    ██║   ██║██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██║         ██╔══██╗██║████╗  ██║██╔══██╗╚══██╔══╝██╔══██╗\n" +
                "#    ██║   ██║██║██████╔╝   ██║   ██║   ██║███████║██║         ██████╔╝██║██╔██╗ ██║███████║   ██║   ███████║\n" +
                "#    ╚██╗ ██╔╝██║██╔══██╗   ██║   ██║   ██║██╔══██║██║         ██╔═══╝ ██║██║╚██╗██║██╔══██║   ██║   ██╔══██║\n" +
                "#     ╚████╔╝ ██║██║  ██║   ██║   ╚██████╔╝██║  ██║███████╗    ██║     ██║██║ ╚████║██║  ██║   ██║   ██║  ██║\n" +
                "#      ╚═══╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝    V${project.version}\n" +
                "#                                                                                                @BarraR3port\n" +
                "\n" +
                "\n" +
                "config-version: 1.5\n" +
                "\n" +
                "global:\n" +
                "  prefix: '&aDivine '\n" +
                "  max-homes: 10\n" +
                "  msg:\n" +
                "    garbage-collector-time: 7 # Cuanto tiempo puede durar 1 mensaje guardado en la caché -- en horas\n" +
                "  server-type: SPIGOT # Tipo de servidor | BUNGEECORD | SPIGOT | VELOCITY\n" +
                "  server-name: builder\n" +
                "\n" +
                "friend:\n" +
                "  request:\n" +
                "    time-to-expire: 7 # En cuanto tiempo expira la petición de amistad | -1 para que sea ilimitada | en días\n" +
                "\n" +
                "chat:\n" +
                "  color: true # Permitir que cualquiera utilice colores en el chat. De lo contrario necesitará el permiso vpc.chat.color\n" +
                "  format: '%playerDisplayName% > %message%'\n" +
                "  party-format: '&e[Party]&r'\n" +
                "\n" +
                "reply:\n" +
                "  format: '&b%playerDisplayName% &7[&4&lRE&7]: &e%repliedDisplayName% &8> &7%message%\n" +
                "    &8--> &a%reply%' # El mensaje original tendrá un límite de 100 letras para evitar que se desborde el mensaje\n" +
                "claim:\n" +
                "  join-claim:\n" +
                "    time-to-expire: 30 # in seconds\n" +
                "    money-reward: 40\n" +
                "    words-to-claim:\n" +
                "    - hello\n" +
                "    - welcome\n" +
                "    - hola\n" +
                "    - bienvenido\n" +
                "  rank-up-claim:\n" +
                "    time-to-expire: 30 # in seconds\n" +
                "    money-reward: 40\n" +
                "    words-to-claim:\n" +
                "    - gg\n" +
                "    - felicitaciones\n" +
                "\n" +
                "private-worlds:\n" +
                "- world1\n" +
                "- world2\n" +
                "\n" +
                "db:\n" +
                "  type: MYSQL # MYSQL | MONGODB | H2\n" +
                "  host: localhost\n" +
                "  port: 3306\n" +
                "  database: divine\n" +
                "  username: podcrash\n" +
                "  password: podcrash\n" +
                "  urli: mongodb://localhost:27018/?readPreference=primary&directConnection=true&ssl=false\n" +
                "\n" +
                "resource-pack:\n" +
                "  url: https://download.mc-packs.net/pack/8e7f3db3bb3e32a24b3432d0dc1402ea2f6bf9ad.zip\n" +
                "  hash: 8e7f3db3bb3e32a24b3432d0dc1402ea2f6bf9ad\n" +
                "\n" +
                "effects:\n" +
                "  chat-tagging:\n" +
                "    enabled: true\n" +
                "    type: BUBBLE_POP\n" +
                "    effect-type: circle\n" +
                "    circle-radius: 5\n" +
                "    circle-rate: 20\n" +
                "    sphere-radius: 5\n" +
                "    sphere-rate: 20\n" +
                "    color:\n" +
                "      red: 255\n" +
                "      green: 255\n" +
                "      blue: 255\n" +
                "      size: 10\n" +
                "    offset:\n" +
                "      x: 0\n" +
                "      y: 0\n" +
                "      z: 0\n" +
                "\n" +
                "words-to-replace:\n" +
                "  word:\n" +
                "    the-word: ':smile:'\n" +
                "    to-replace: ➧\n" +
                "  word1:\n" +
                "    the-word: :D\n" +
                "    to-replace: ➧\n" +
                "  word2:\n" +
                "    the-word: ':cry:'\n" +
                "    to-replace: ➨\n" +
                "  word3:\n" +
                "    the-word: T-T\n" +
                "    to-replace: ➨\n" +
                "  word4:\n" +
                "    the-word: ':llora:'\n" +
                "    to-replace: ➨\n" +
                "  word5:\n" +
                "    the-word: ':mad:'\n" +
                "    to-replace: ➩\n" +
                "  word6:\n" +
                "    the-word: ':enojado:'\n" +
                "    to-replace: ➩\n" +
                "  word7:\n" +
                "    the-word: ':impressed:'\n" +
                "    to-replace: ➱\n" +
                "  word8:\n" +
                "    the-word: ':sorprendido:'\n" +
                "    to-replace: ➱\n" +
                "  word9:\n" +
                "    the-word: :o\n" +
                "    to-replace: ➱\n" +
                "  word10:\n" +
                "    the-word: ':heart:'\n" +
                "    to-replace: ➳\n" +
                "  word11:\n" +
                "    the-word: <3\n" +
                "    to-replace: ➳\n" +
                "  word12:\n" +
                "    the-word: ':corazon:'\n" +
                "    to-replace: ➳\n" +
                "  word13:\n" +
                "    the-word: ':corazón:'\n" +
                "    to-replace: ➳\n" +
                "  word14:\n" +
                "    the-word: '>:('\n" +
                "    to-replace: ➩\n" +
                "  word15:\n" +
                "    the-word: 8)\n" +
                "    to-replace: ➲\n" +
                "  word16:\n" +
                "    the-word: ':diablo:'\n" +
                "    to-replace: ➵\n" +
                "  word17:\n" +
                "    the-word: '>:)'\n" +
                "    to-replace: ➵\n" +
                "  word18:\n" +
                "    the-word: ':evil:'\n" +
                "    to-replace: ➵\n" +
                "  word19:\n" +
                "    the-word: ':beso:'\n" +
                "    to-replace: ➶\n" +
                "  word20:\n" +
                "    the-word: ':kiss:'\n" +
                "    to-replace: ➶\n" +
                "  word21:\n" +
                "    the-word: :*\n" +
                "    to-replace: ➶\n" +
                "  word22:\n" +
                "    the-word: ':lentes:'\n" +
                "    to-replace: ➲\n" +
                "  word23:\n" +
                "    the-word: ':poop:'\n" +
                "    to-replace: ➷\n" +
                "  word24:\n" +
                "    the-word: ':popo:'\n" +
                "    to-replace: ➷\n" +
                "  word25:\n" +
                "    the-word: ':caca:'\n" +
                "    to-replace: ➷\n" +
                "  word26:\n" +
                "    the-word: ':mierda:'\n" +
                "    to-replace: ➷\n" +
                "  word27:\n" +
                "    the-word: ':shit:'\n" +
                "    to-replace: ➷\n" +
                "  word28:\n" +
                "    the-word: ':hot:'\n" +
                "    to-replace: ➸\n" +
                "  word29:\n" +
                "    the-word: ':horny:'\n" +
                "    to-replace: ➸\n" +
                "  word30:\n" +
                "    the-word: ':shy:'\n" +
                "    to-replace: ➹\n" +
                "  word31:\n" +
                "    the-word: ':sonrojado:'\n" +
                "    to-replace: ➹\n" +
                "  word32:\n" +
                "    the-word: ':tilin:'\n" +
                "    to-replace: soy re putisimo\n" +
                "  word33:\n" +
                "    the-word: ':risa:'\n" +
                "    to-replace: ⟙\n" +
                "  word34:\n" +
                "    the-word: ':triste:'\n" +
                "    to-replace: ⟕\n" +
                "  word35:\n" +
                "    the-word: ':sad:'\n" +
                "    to-replace: ⟕\n" +
                "  word36:\n" +
                "    the-word: :(\n" +
                "    to-replace: ⟕\n" +
                "  word37:\n" +
                "    the-word: ':lagrimas:'\n" +
                "    to-replace: ⟖\n" +
                "  word38:\n" +
                "    the-word: ':fuego:'\n" +
                "    to-replace: ⟗\n" +
                "  word39:\n" +
                "    the-word: ':vomit:'\n" +
                "    to-replace: ⟘\n" +
                "  word40:\n" +
                "    the-word: ':vomito:'\n" +
                "    to-replace: ⟘\n";
        
        List<String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));
        int currentLayer = 0;
        AtomicReference<StringBuilder> currentPath = new AtomicReference<>(new StringBuilder());
        StringBuilder sb = new StringBuilder();
        AtomicInteger lineNumber = new AtomicInteger();
        System.out.println("Saving To String....");
        list.forEach((line) -> {
            lineNumber.getAndIncrement();
            sb.append(line);
            if (line.contains(":")){
                int layerFromLine = this.getLayerFromLine(line, lineNumber.get());
                if (layerFromLine < currentLayer){
                    new StringBuilder(this.regressPathBy(currentLayer - layerFromLine, currentPath.toString()));
                }
                
                String key = this.getKeyFromLine(line);
                
                assert key != null;
                
                currentPath.set(new StringBuilder(key));
                String path = currentPath.toString();
                if (this.comments.containsKey(path)){
                    (this.comments.get(path)).forEach(sb::append);
                }
            }
            
        });
        System.out.println(sb);
    }
    
    
    private void addCommentLine(String currentPath, String line){
    
        List<String> list = comments.get(currentPath);
        if (list == null){
            list = new ArrayList<>();
        }
        list.add(line);
        
        comments.put(currentPath, list);
    }
    
    private String getKeyFromLine(String line){
        
        String key = null;
        
        for ( int i = 0; i < line.length(); i++ ){
            if (line.charAt(i) == ':'){
                key = line.substring(0, i);
                break;
            }
        }
        
        return key == null ? null : key.trim();
    }
    
    private String regressPathBy(int i, String currentPath){
        
        if (i <= 0){
            return currentPath;
        }
        String[] split = currentPath.split("\\.");
        
        StringBuilder rebuild = new StringBuilder();
        for ( int j = 0; j < split.length - i; j++ ){
            rebuild.append(split[j]);
            if (j <= (split.length - j)){
                rebuild.append(".");
            }
        }
        
        return rebuild.toString();
    }
    
    private int getLayerFromLine(String line, int lineNumber){
        
        double d = 0;
        for ( int i = 0; i < line.length(); i++ ){
            if (line.charAt(i) == ' '){
                d += 0.5;
            } else {
                break;
            }
        }
        
        return (int) d;
    }
}