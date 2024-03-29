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

package net.lymarket.lyapi.support.version.v1_16_R3;

import com.mojang.datafixers.util.Pair;
import net.lymarket.lyapi.common.version.VersionSupport;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class v1_16_R3 extends VersionSupport {
    
    private static final UUID chatUUID = new UUID(0L, 0L);
    
    public v1_16_R3(Plugin plugin, String name){
        super(plugin, name);
    }
    
    @Override
    public String getTag(org.bukkit.inventory.ItemStack itemStack, String key){
        net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? null : tag.hasKey(key) ? tag.getString(key) : null;
    }
    
    @Override
    public boolean hasTag(org.bukkit.inventory.ItemStack itemStack, String key){
        net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = i.getTag();
        return tag != null && tag.hasKey(key);
    }
    
    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        p.sendTitle(title == null ? " " : title, subtitle == null ? " " : subtitle, fadeIn, stay, fadeOut);
    }
    
    @Override
    public void playAction(Player p, String text){
        CraftPlayer cPlayer = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO, chatUUID);
        cPlayer.getHandle().playerConnection.sendPacket(ppoc);
    }
    
    @Override
    public void hideEntity(Entity e, Player p){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getEntityId());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        
    }
    
    @Override
    public void hideArmor(Player victim, Player receiver){
        List < Pair < EnumItemSlot, ItemStack > > items = new ArrayList <>();
        List < Pair < EnumItemSlot, ItemStack > > hands = new ArrayList <>();
        hands.add(new Pair <>(EnumItemSlot.MAINHAND, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        hands.add(new Pair <>(EnumItemSlot.OFFHAND, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        
        items.add(new Pair <>(EnumItemSlot.HEAD, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        items.add(new Pair <>(EnumItemSlot.CHEST, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        items.add(new Pair <>(EnumItemSlot.LEGS, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        items.add(new Pair <>(EnumItemSlot.FEET, new ItemStack(net.minecraft.server.v1_16_R3.Item.getById(0))));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(victim.getEntityId(), hands);
        EntityPlayer pc = ((CraftPlayer) receiver).getHandle();
        if (victim != receiver){
            pc.playerConnection.sendPacket(packet2);
        }
        pc.playerConnection.sendPacket(packet1);
    }
    
    @Override
    public void showArmor(Player victim, Player receiver){
        List < Pair < EnumItemSlot, ItemStack > > items = new ArrayList <>();
        List < Pair < EnumItemSlot, ItemStack > > hands = new ArrayList <>();
        
        hands.add(new Pair <>(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(victim.getInventory().getItemInMainHand())));
        hands.add(new Pair <>(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(victim.getInventory().getItemInOffHand())));
        
        items.add(new Pair <>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(victim.getInventory().getHelmet())));
        items.add(new Pair <>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(victim.getInventory().getChestplate())));
        items.add(new Pair <>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(victim.getInventory().getLeggings())));
        items.add(new Pair <>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(victim.getInventory().getBoots())));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(victim.getEntityId(), hands);
        EntityPlayer pc = ((CraftPlayer) receiver).getHandle();
        if (victim != receiver){
            pc.playerConnection.sendPacket(packet2);
        }
        pc.playerConnection.sendPacket(packet1);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack addCustomData(org.bukkit.inventory.ItemStack i, String data){
        ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null){
            tag = new NBTTagCompound();
            itemStack.setTag(tag);
        }
        
        tag.setString("LyApi", data);
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack setTag(org.bukkit.inventory.ItemStack itemStack, String key, String value){
        net.minecraft.server.v1_16_R3.ItemStack is = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = is.getTag();
        if (tag == null){
            tag = new NBTTagCompound();
            is.setTag(tag);
        }
    
        tag.setString(key, value);
        return CraftItemStack.asBukkitCopy(is);
    }
    
    
    @Override
    public org.bukkit.inventory.ItemStack setCustomModelData(org.bukkit.inventory.ItemStack itemStack, int customModelData){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null){
            meta.setCustomModelData(customModelData);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
    
    @Override
    public int getCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null){
            return meta.getCustomModelData();
        }
        return 0;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack removeCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null){
            meta.setCustomModelData(null);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
    
    @Override
    public boolean hasCustomModelData(org.bukkit.inventory.ItemStack itemStack){
        return itemStack.getItemMeta() != null && itemStack.getItemMeta().hasCustomModelData();
    }
    
    @Override
    public String getCustomData(org.bukkit.inventory.ItemStack i){
        ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) return "";
        return tag.getString("LyApi");
    }
    
    @Override
    public org.bukkit.Material materialPlayerHead( ){
        return org.bukkit.Material.PLAYER_HEAD;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getPlayerHead(Player player, org.bukkit.inventory.ItemStack copyTagFrom){
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(materialPlayerHead());
        
        if (copyTagFrom != null){
            ItemStack i = CraftItemStack.asNMSCopy(head);
            i.setTag(CraftItemStack.asNMSCopy(copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy(i);
        }
        
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
            //noinspection ConstantConditions
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, ((CraftPlayer) player).getProfile());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        
        return head;
    }
    
    @Override
    public void spigotShowPlayer(Player victim, Player receiver){
        receiver.showPlayer(getPlugin(), victim);
    }
    
    @Override
    public void spigotHidePlayer(Player victim, Player receiver){
        receiver.hidePlayer(getPlugin(), victim);
    }
    
}
