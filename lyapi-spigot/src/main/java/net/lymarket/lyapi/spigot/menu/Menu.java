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

package net.lymarket.lyapi.spigot.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.events.OpenCustomMenuEvent;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Menu implements InventoryHolder {
    
    private final ArrayList<BukkitTask> scheduleTask = new ArrayList<>();
    private final boolean linked;
    protected ItemStack CLOSE_ITEM;
    protected ItemStack PREV_ITEM;
    protected ItemStack NEXT_ITEM;
    protected ItemStack FILLER_GLASS;
    protected Inventory inventory;
    protected IPlayerMenuUtility playerMenuUtility;
    protected boolean isOnSchedule = false;
    protected boolean moveTopItems = false;
    protected boolean moveBottomItems = false;
    protected LinkedList<Integer> occupiedSlots = new LinkedList<>();
    private UUID menu_uuid;
    private boolean debug = false;
    
    public Menu(IPlayerMenuUtility playerMenuUtility){
        this(playerMenuUtility, XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), false);
        menu_uuid = UUID.randomUUID();
    }
    
    public Menu(IPlayerMenuUtility playerMenuUtility, boolean linked){
        this(playerMenuUtility, XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), linked);
        menu_uuid = UUID.randomUUID();
    }
    
    public Menu(IPlayerMenuUtility playerMenuUtility, Material fillerGlass, boolean linked){
        this.playerMenuUtility = playerMenuUtility;
        this.linked = linked;
        try {
            FILLER_GLASS = new ItemBuilder(fillerGlass).setDurability((short) Integer.parseInt(LyApi.getLanguage().getMSG("menu.filler-glass-color-id"))).setDisplayName(" ").build();
            NEXT_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0=").setDisplayName(LyApi.getLanguage().getMSG("menu.next")).addTag("ly-menu-next", "ly-menu-next").build();
            PREV_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0=").setDisplayName(LyApi.getLanguage().getMSG("menu.prev")).addTag("ly-menu-previous", "ly-menu-previous").build();
            CLOSE_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin(linked ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==").setDisplayName(LyApi.getLanguage().getMSG(linked ? "menu.go-back" : "menu.close")).addTag("ly-menu-close", "ly-menu-close").build();
        } catch (NullPointerException | NumberFormatException ignored) {
            FILLER_GLASS = new ItemBuilder(fillerGlass).setDurability((short) 15).setDisplayName(" ").build();
            NEXT_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0=").setDisplayName("&aNext").addTag("ly-menu-next", "ly-menu-next").build();
            PREV_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0=").setDisplayName("&aPrevious").addTag("ly-menu-previous", "ly-menu-previous").build();
            CLOSE_ITEM = new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin(linked ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==").setDisplayName(linked ? "&cGo Back" : "&cClose").addTag("ly-menu-close", "ly-menu-close").build();
        }
        menu_uuid = UUID.randomUUID();
    }
    
    public abstract String getMenuName();
    
    public abstract int getSlots();
    
    public abstract void setMenuItems();
    
    @Contract(pure = true)
    public void setMenuItemsAsync(){
    }
    
    public void addItemsAsync(){
        updateList();
        Bukkit.getScheduler().runTaskAsynchronously(LyApi.getPlugin(), this::setMenuItemsAsync);
    }
    
    public abstract void handleMenu(InventoryClickEvent e);
    
    public void handleDragEvent(InventoryDragEvent e){
    
    }
    
    public void updateList(){
    
    }
    
    public void handleClose(InventoryCloseEvent e){
        for ( BukkitTask task : scheduleTask ){
            task.cancel();
        }
        subHandleClose(e);
    }
    
    public void subHandleClose(InventoryCloseEvent e){
    
    }
    
    public void handleMove(InventoryMoveItemEvent e){
    
    }
    
    public void handlePickUp(InventoryPickupItemEvent e){
    
    }
    
    public boolean canMoveTopItems(){
        return moveTopItems;
    }
    
    public void allowMoveTopItems(){
        this.moveTopItems = true;
    }
    
    public void disallowMoveTopItems(){
        this.moveTopItems = false;
    }
    
    public boolean canMoveBottomItems(){
        return moveBottomItems;
    }
    
    public void allowMoveBottomItems(){
        this.moveBottomItems = true;
    }
    
    public void disallowMoveBottomItems(){
        this.moveBottomItems = false;
    }
    
    public void setMenuItem(int slot, ItemStack item){
        this.inventory.setItem(slot, item);
        occupiedSlots.add(slot);
    }
    
    public void open(){
        if (!onOpen()){
            destroy();
            return;
        }
        inventory = Bukkit.createInventory(this, getSlots(), Utils.format(getMenuName()));
        if (isDebug()){
            for ( int i = 0; i < getSlots(); i++ ){
                inventory.setItem(i, new ItemBuilder(FILLER_GLASS.clone()).setDisplayName("Slot: &e" + i).addLoreLine("&cMENU IN DEBUG MODE.").build());
            }
        }
        this.addItemsAsync();
        this.setMenuItems();
        this.setCustomMenuItems();
        OpenCustomMenuEvent e = new OpenCustomMenuEvent(this, playerMenuUtility.getOwner());
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()){
            destroy();
            return;
        }
        playerMenuUtility.getOwner().openInventory(e.getMenu().inventory);
    }
    
    public boolean onOpen(){
        return true;
    }
    
    @Override
    public Inventory getInventory(){
        return inventory;
    }
    
    public ItemStack createCustomSkull(String name, String head){
        return new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin(head).setDisplayName(name).build();
    }
    
    public ItemStack createCustomSkull(String name, List<String> lore, String head){
        return new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin(head).setLore(lore).setDisplayName(name).build();
    }
    
    public void checkSomething(Player p, int slot, ItemStack item, String name, List<String> lore, UUID menu_uuid){
        
        if (isOnSchedule) return;
        
        isOnSchedule = true;
    
        p.getOpenInventory().getTopInventory().setItem(slot, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=").setLore(lore).setDisplayName(name).build());
        setOnSchedule(p, slot, item, menu_uuid);
        
    }
    
    public void checkSomething(Player p, int slot, ItemStack item, String name, String lore, UUID menu_uuid){
        
        if (isOnSchedule) return;
        
        isOnSchedule = true;
    
        p.getOpenInventory().getTopInventory().setItem(slot, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=").addLoreLine(lore).setDisplayName(name).build());
        setOnSchedule(p, slot, item, menu_uuid);
        
    }
    
    public void setOnSchedule(Player p, int slot, ItemStack item, UUID menu_uuid){
        scheduleTask.add(Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(LyApi.getPlugin(), () -> {
            if (getMenuUUID().equals(menu_uuid)){
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof Menu){
                    Menu menu = (Menu) p.getOpenInventory().getTopInventory().getHolder();
                    if (menu.getMenuUUID().equals(menu_uuid)){
                        p.getOpenInventory().getTopInventory().setItem(slot, item);
                    }
                }
                isOnSchedule = false;
            }
    
        }, 15L));
    }
    
    public Player getOwner(){
        return playerMenuUtility.getOwner();
    }
    
    public UUID getMenuUUID(){
        return menu_uuid;
    }
    
    public boolean isLinked(){
        return linked;
    }
    
    public boolean isDebug(){
        return debug;
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    
    public void setCustomMenuItems(){
    
    }
    
    private void destroy(){
        getOwner().closeInventory();
        inventory.clear();
        inventory = null;
        playerMenuUtility = null;
        scheduleTask.forEach(BukkitTask::cancel);
        menu_uuid = null;
        isOnSchedule = false;
        debug = false;
        CLOSE_ITEM = null;
        FILLER_GLASS = null;
        PREV_ITEM = null;
        NEXT_ITEM = null;
        occupiedSlots.clear();
        occupiedSlots = null;
        
    }
}
