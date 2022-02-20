package net.lymarket.lyapi.support.version.v1_12_R1;

import net.lymarket.common.version.VersionSupport;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class v1_12_R1 extends VersionSupport {
    
    public v1_12_R1( Plugin plugin , String name ){
        super( plugin , name );
    }
    
    @Override
    public String getTag( org.bukkit.inventory.ItemStack itemStack , String key ){
        net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy( itemStack );
        NBTTagCompound tag = i.getTag( );
        return tag == null ? null : tag.hasKey( key ) ? tag.getString( key ) : null;
    }
    
    @Override
    public boolean hasTag( org.bukkit.inventory.ItemStack itemStack , String key ){
        net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy( itemStack );
        NBTTagCompound tag = i.getTag( );
        return tag != null && tag.hasKey( key );
    }
    
    @Override
    public void sendTitle( Player p , String title , String subtitle , int fadeIn , int stay , int fadeOut ){
        if ( title != null ) {
            if ( !title.isEmpty( ) ) {
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + title + "\"}" );
                PacketPlayOutTitle tit = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.TITLE , bc );
                PacketPlayOutTitle length = new PacketPlayOutTitle( fadeIn , stay , fadeOut );
                (( CraftPlayer ) p).getHandle( ).playerConnection.sendPacket( tit );
                (( CraftPlayer ) p).getHandle( ).playerConnection.sendPacket( length );
            }
        }
        if ( subtitle != null ) {
            IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + subtitle + "\"}" );
            PacketPlayOutTitle tit = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.SUBTITLE , bc );
            PacketPlayOutTitle length = new PacketPlayOutTitle( fadeIn , stay , fadeOut );
            (( CraftPlayer ) p).getHandle( ).playerConnection.sendPacket( tit );
            (( CraftPlayer ) p).getHandle( ).playerConnection.sendPacket( length );
        }
    }
    
    
    @Override
    public void playAction( Player p , String text ){
        org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer cPlayer = ( CraftPlayer ) p;
        net.minecraft.server.v1_12_R1.IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + text + "\"}" );
        PacketPlayOutChat ppoc = new PacketPlayOutChat( cbc , ChatMessageType.GAME_INFO );
        cPlayer.getHandle( ).playerConnection.sendPacket( ppoc );
    }
    
    @Override
    public void hideEntity( org.bukkit.entity.Entity e , Player p ){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy( e.getEntityId( ) );
        (( CraftPlayer ) p).getHandle( ).playerConnection.sendPacket( packet );
        
    }
    
    @Override
    public void hideArmor( Player victim , Player receiver ){
        PacketPlayOutEntityEquipment hand1 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.MAINHAND , new ItemStack( Item.getById( 0 ) ) );
        PacketPlayOutEntityEquipment hand2 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.OFFHAND , new ItemStack( Item.getById( 0 ) ) );
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.HEAD , new ItemStack( Item.getById( 0 ) ) );
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.CHEST , new ItemStack( Item.getById( 0 ) ) );
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.LEGS , new ItemStack( Item.getById( 0 ) ) );
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.FEET , new ItemStack( Item.getById( 0 ) ) );
        EntityPlayer pc = (( CraftPlayer ) receiver).getHandle( );
        if ( victim != receiver ) {
            pc.playerConnection.sendPacket( hand1 );
            pc.playerConnection.sendPacket( hand2 );
        }
        pc.playerConnection.sendPacket( helmet );
        pc.playerConnection.sendPacket( chest );
        pc.playerConnection.sendPacket( pants );
        pc.playerConnection.sendPacket( boots );
    }
    
    @Override
    public void showArmor( Player victim , Player receiver ){
        PacketPlayOutEntityEquipment hand1 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.MAINHAND , CraftItemStack.asNMSCopy( victim.getInventory( ).getItemInMainHand( ) ) );
        PacketPlayOutEntityEquipment hand2 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.OFFHAND , CraftItemStack.asNMSCopy( victim.getInventory( ).getItemInOffHand( ) ) );
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.HEAD , CraftItemStack.asNMSCopy( victim.getInventory( ).getHelmet( ) ) );
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.CHEST , CraftItemStack.asNMSCopy( victim.getInventory( ).getChestplate( ) ) );
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.LEGS , CraftItemStack.asNMSCopy( victim.getInventory( ).getLeggings( ) ) );
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , EnumItemSlot.FEET , CraftItemStack.asNMSCopy( victim.getInventory( ).getBoots( ) ) );
        EntityPlayer pc = (( CraftPlayer ) receiver).getHandle( );
        if ( victim != receiver ) {
            pc.playerConnection.sendPacket( hand1 );
            pc.playerConnection.sendPacket( hand2 );
        }
        pc.playerConnection.sendPacket( helmet );
        pc.playerConnection.sendPacket( chest );
        pc.playerConnection.sendPacket( pants );
        pc.playerConnection.sendPacket( boots );
    }
    
    @Override
    public org.bukkit.inventory.ItemStack addCustomData( org.bukkit.inventory.ItemStack i , String data ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.getTag( );
        if ( tag == null ) {
            tag = new NBTTagCompound( );
            itemStack.setTag( tag );
        }
        
        tag.setString( "LyApi" , data );
        return CraftItemStack.asBukkitCopy( itemStack );
    }
    
    @Override
    public org.bukkit.inventory.ItemStack setTag( org.bukkit.inventory.ItemStack i , String key , String value ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.getTag( );
        if ( tag == null ) {
            tag = new NBTTagCompound( );
            itemStack.setTag( tag );
        }
        
        tag.setString( key , value );
        return CraftItemStack.asBukkitCopy( itemStack );
    }
    
    @Override
    public boolean isCustomBedWarsItem( org.bukkit.inventory.ItemStack i ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.getTag( );
        if ( tag == null ) return false;
        return tag.hasKey( "LyApi" );
    }
    
    @Override
    public String getCustomData( org.bukkit.inventory.ItemStack i ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.getTag( );
        if ( tag == null ) return "";
        return tag.getString( "LyApi" );
    }
    
    @Override
    public boolean isPlayerHead( String material , int data ){
        return material.equals( "SKULL_ITEM" ) && data == 3;
    }
    
    @Override
    public org.bukkit.Material materialPlayerHead( ){
        return org.bukkit.Material.SKULL_ITEM;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getPlayerHead( Player player , org.bukkit.inventory.ItemStack copyTagFrom ){
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack( org.bukkit.Material.SKULL_ITEM , 1 , ( short ) 3 );
        
        if ( copyTagFrom != null ) {
            ItemStack i = CraftItemStack.asNMSCopy( head );
            i.setTag( CraftItemStack.asNMSCopy( copyTagFrom ).getTag( ) );
            head = CraftItemStack.asBukkitCopy( i );
        }
        
        SkullMeta headMeta = ( SkullMeta ) head.getItemMeta( );
        Field profileField;
        try {
            profileField = headMeta.getClass( ).getDeclaredField( "profile" );
            profileField.setAccessible( true );
            profileField.set( headMeta , (( CraftPlayer ) player).getProfile( ) );
        } catch ( NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1 ) {
            e1.printStackTrace( );
        }
        head.setItemMeta( headMeta );
        return head;
    }
}
