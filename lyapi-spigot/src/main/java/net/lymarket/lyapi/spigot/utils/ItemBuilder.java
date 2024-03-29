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

package net.lymarket.lyapi.spigot.utils;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder implements Cloneable {
    
    private ItemStack is;
    
    public ItemBuilder(String material){
        String[] args = material.split(":");
        if (args.length == 2){
            is = new ItemStack(Material.valueOf(args[0]), 1, (byte) Integer.parseInt(args[1]));
        } else is = new ItemStack(Material.valueOf(material));
    }
    
    public ItemBuilder(Material m){
        this(m, 0);
    }
    
    public ItemBuilder(ItemStack is){
        this.is = is;
    }
    
    public ItemBuilder(Material m, int data){
        is = new ItemStack(m, 1, (byte) data);
    }
    
    public ItemBuilder setDurability(short dur){
        is.setDurability(dur);
        return this;
    }
    
    public ItemBuilder setAmount(int amount){
        is.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setDisplayName(String name){
        if (name == null) return this;
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Utils.format(name));
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        if (ench == null) return this;
        if (level < 0) return this;
        is.addUnsafeEnchantment(ench, level);
        return this;
    }
    
    public ItemBuilder removeEnchantment(Enchantment ench){
        if (ench == null) return this;
        is.removeEnchantment(ench);
        return this;
    }
    
    public ItemBuilder setSkullOwner(String owner){
        if (owner == null) return this;
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }
    
    public ItemBuilder addEnchant(Enchantment ench, int level){
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
        is.addEnchantments(enchantments);
        return this;
    }
    
    
    public ItemBuilder addUnsafeEnchantment(Map<Enchantment, Integer> enchantments){
        is.addUnsafeEnchantments(enchantments);
        return this;
    }
    
    public ItemBuilder hideEnchants(boolean b){
        if (!b) return this;
        ItemMeta meta = is.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setEnchanted(boolean b){
        if (!b) return this;
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setInfinityDurability(){
        is.setDurability(Short.MAX_VALUE);
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore){
        if (lore == null) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(lore.stream().map(Utils::format).collect(Collectors.toList()));
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder removeLoreLine(String line){
        if (line == null) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder removeLoreLine(int index){
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder clearLore(){
        ItemMeta im = is.getItemMeta();
        im.setLore(null);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addLoreLine(String line){
        if (line == null) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(Utils.format(line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addLoreLine(String line, int pos){
        if (line == null) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, Utils.format(line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder setDyeColor(DyeColor color){
        if (color == null) return this;
        this.is.setDurability(color.getData());
        return this;
    }
    
    /** @deprecated  */
    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color){
        if (color == null) return this;
        if (!is.getType().equals(Material.WOOL)) return this;
        this.is.setDurability(color.getData());
        return this;
    }
    
    public ItemBuilder setLeatherArmorColor(Color color){
        if (color == null) return this;
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }
    
    /**
     * Para ocupar esto solo es necesario agregar esto y ya estaría lista la cabeza.
     */
    public ItemBuilder setHeadSkin(String skin){
        if (skin == null || skin.equals("")) return this;
        is = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) SkullType.PLAYER.ordinal());
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", skin));
        SkullMeta itemmeta = (SkullMeta) is.getItemMeta();
        Field field;
        try {
            field = itemmeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemmeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }
        is.setItemMeta(itemmeta);
        return this;
    }
    
    public ItemBuilder addTag(String key, String value){
        if (key == null || value == null) return this;
        NBTItem item = new NBTItem(is);
        item.setString(key, value);
        this.is = item.getItem();
        return this;
    }
    
    public ItemBuilder addTag(String key, Integer value){
        if (key == null || value == null) return this;
        NBTItem item = new NBTItem(is);
        item.setInteger(key, value);
        this.is = item.getItem();
        return this;
    }
    
    public ItemBuilder createBook(String title, String... pages){
        if (title == null || pages == null) return this;
        if (pages.length == 0) return this;
        BookMeta bm = (BookMeta) is.getItemMeta();
        bm.setTitle(title);
        bm.setPages(pages);
        is.setItemMeta(bm);
        return this;
    }
    
    public ItemBuilder setBookTitle(String title){
        if (title == null) return this;
        BookMeta bm = (BookMeta) is.getItemMeta();
        bm.setTitle(title);
        is.setItemMeta(bm);
        return this;
    }
    
    public ItemBuilder setDyeColor(int color){
        this.is.setDurability((byte) color);
        return this;
    }
    
    public ItemBuilder setFireWorkColor(Color color){
        if (color == null) return this;
        if (!is.getType().equals((XMaterial.FIREWORK_ROCKET.parseMaterial()))) return this;
        FireworkEffectMeta fm = (FireworkEffectMeta) is.getItemMeta();
        FireworkEffect.Builder fe = FireworkEffect.builder();
        fe.withColor(color);
        fm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        //Add more things...
        fm.setEffect(fe.build());
        is.setItemMeta(fm);
        return this;
    }
    
    public ItemBuilder setFireWorkColors(Color... color){
        if (color == null) return this;
        if (!is.getType().equals(XMaterial.FIREWORK_ROCKET.parseMaterial())) return this;
        FireworkEffectMeta fm = (FireworkEffectMeta) is.getItemMeta();
        FireworkEffect.Builder fe = FireworkEffect.builder();
        fe.withColor(color);
        fm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        fm.setEffect(fe.build());
        is.setItemMeta(fm);
        return this;
    }
    
    public ItemBuilder setFireWorkBuild(FireworkEffect.Builder builder){
        if (builder == null) return this;
        if (!is.getType().equals(XMaterial.FIREWORK_ROCKET.parseMaterial())) return this;
        FireworkEffectMeta fm = (FireworkEffectMeta) is.getItemMeta();
        fm.setEffect(builder.build());
        fm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        is.setItemMeta(fm);
        return this;
    }
    
    public ItemBuilder setFireWorkBuild(FireworkEffect effects){
        if (effects == null) return this;
        if (!is.getType().equals(XMaterial.FIREWORK_ROCKET.parseMaterial())) return this;
        FireworkEffectMeta fm = (FireworkEffectMeta) is.getItemMeta();
        fm.setEffect(effects);
        fm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        is.setItemMeta(fm);
        return this;
    }
    
    public ItemBuilder setBookAuthor(String author){
        if (author == null) return this;
        BookMeta bm = (BookMeta) is.getItemMeta();
        bm.setAuthor(author);
        is.setItemMeta(bm);
        return this;
    }
    
    public ItemBuilder setBookPages(String... pages){
        if (pages == null) return this;
        BookMeta bm = (BookMeta) is.getItemMeta();
        bm.setPages(pages);
        is.setItemMeta(bm);
        return this;
    }
    
    public ItemBuilder setBookPage(int page, String text){
        if (text == null) return this;
        BookMeta bm = (BookMeta) is.getItemMeta();
        bm.setPage(page, text);
        is.setItemMeta(bm);
        return this;
    }
    
    public ItemBuilder setCustomModelData(int customModelData){
        NBTItem item = new NBTItem(is);
        item.setCustomModelData(customModelData);
        this.is = item.getItem();
        return this;
    }
    
    
    public ItemStack build(){
        return is;
    }
    
    @Override
    public ItemBuilder clone(){
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
