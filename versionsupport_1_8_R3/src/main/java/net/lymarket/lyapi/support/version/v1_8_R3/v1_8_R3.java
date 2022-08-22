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

package net.lymarket.lyapi.support.version.v1_8_R3;

import net.lymarket.lyapi.common.version.VersionSupport;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class v1_8_R3 extends VersionSupport {
    
    public v1_8_R3(Plugin pl, String name){
        super(pl, name);
    }
    
    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        if (title != null){
            if (!title.isEmpty()){
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
                PacketPlayOutTitle tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, bc);
                PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tit);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
            }
        }
        if (subtitle != null){
            IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, bc);
            PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tit);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
        }
    }
    
    @Override
    public void playAction(Player p, String text){
        CraftPlayer cPlayer = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cPlayer.getHandle().playerConnection.sendPacket(ppoc);
    }
    
    @Override
    public void hideEntity(org.bukkit.entity.Entity e, Player p){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getEntityId());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        
    }
    
    @Override
    public void hideArmor(Player victim, Player receiver){
        if (victim.equals(receiver)) return;
        PacketPlayOutEntityEquipment hand = new PacketPlayOutEntityEquipment(victim.getEntityId(), 0, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(victim.getEntityId(), 1, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(victim.getEntityId(), 2, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(victim.getEntityId(), 3, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(victim.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
        PlayerConnection boundTo = ((CraftPlayer) receiver).getHandle().playerConnection;
        boundTo.sendPacket(hand);
        boundTo.sendPacket(helmet);
        boundTo.sendPacket(chest);
        boundTo.sendPacket(pants);
        boundTo.sendPacket(boots);
    }
    
    @Override
    public void showArmor(Player victim, Player receiver){
        if (victim.equals(receiver)) return;
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();
        PacketPlayOutEntityEquipment hand1 = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, entityPlayer.inventory.getItemInHand());
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, entityPlayer.inventory.getArmorContents()[3]);
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, entityPlayer.inventory.getArmorContents()[2]);
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, entityPlayer.inventory.getArmorContents()[1]);
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, entityPlayer.inventory.getArmorContents()[0]);
        EntityPlayer boundTo = ((CraftPlayer) receiver).getHandle();
        if (victim != receiver){
            boundTo.playerConnection.sendPacket(hand1);
        }
        boundTo.playerConnection.sendPacket(helmet);
        boundTo.playerConnection.sendPacket(chest);
        boundTo.playerConnection.sendPacket(pants);
        boundTo.playerConnection.sendPacket(boots);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack addCustomData(org.bukkit.inventory.ItemStack i, String data){
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null){
            tag = new NBTTagCompound();
            itemStack.setTag(tag);
        }
        
        tag.setString("LyApi", data);
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    
    @Override
    public boolean hasTag(org.bukkit.inventory.ItemStack itemStack, String key){
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = i.getTag();
        return tag != null && tag.hasKey(key);
    }
    
    @Override
    public ItemStack setTag(ItemStack itemStack, String key, String value){
        net.minecraft.server.v1_8_R3.ItemStack is = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = is.getTag();
        if (tag == null){
            tag = new NBTTagCompound();
            is.setTag(tag);
        }
        
        tag.setString(key, value);
        return CraftItemStack.asBukkitCopy(is);
    }
    
    @Override
    public org.bukkit.Material materialPlayerHead( ){
        return org.bukkit.Material.SKULL_ITEM;
    }
    
    
    @Override
    public String getTag(ItemStack itemStack, String key){
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? null : tag.hasKey(key) ? tag.getString(key) : null;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack setCustomModelData(org.bukkit.inventory.ItemStack itemStack, int customModelData){
        System.out.println("[LyApi Version Support] In this version CustomModelData is not supported!");
        return itemStack;
    }
    
    @Override
    public int getCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        System.out.println("[LyApi Version Support] In this version CustomModelData is not supported!");
        return 0;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack removeCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        System.out.println("[LyApi Version Support] In this version CustomModelData is not supported!");
        return itemStack;
    }
    
    @Override
    public boolean hasCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        System.out.println("[LyApi Version Support] In this version CustomModelData is not supported!");
        return false;
    }
    
    @Override
    public String getCustomData(org.bukkit.inventory.ItemStack i){
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) return "";
        return tag.getString("LyApi");
    }
    
    
    @Override
    public boolean isPlayerHead(String material, int data){
        return material.equals("SKULL_ITEM") && data == 3;
    }
    
    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom){
        ItemStack head = new ItemStack(org.bukkit.Material.SKULL_ITEM, 1, (short) 3);
        
        if (copyTagFrom != null){
            net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(head);
            i.setTag(CraftItemStack.asNMSCopy(copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy(i);
        }
        
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, ((CraftPlayer) player).getProfile());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
    
    
}
