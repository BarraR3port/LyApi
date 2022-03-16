package net.lymarket.lyapi.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serializer {
    
    public String[] playerInventoryToBase64( PlayerInventory playerInventory ) throws IllegalStateException{
        //get the main content part, this doesn't return the armor
        String content = toBase64( playerInventory );
        String armor = itemStackArrayToBase64( playerInventory.getArmorContents( ) );
        
        return new String[]{content , armor};
    }
    
    public String inventoryToBase64( Inventory Inventory ) throws IllegalStateException{
        return toBase64( Inventory );
    }
    
    public Inventory fromBase64( String data ) throws IOException{
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream( Base64Coder.decodeLines( data ) );
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream( inputStream );
            Inventory inventory = Bukkit.getServer( ).createInventory( null , dataInput.readInt( ) );
            
            // Read the serialized inventory
            for ( int i = 0; i < inventory.getSize( ); i++ ) {
                inventory.setItem( i , ( ItemStack ) dataInput.readObject( ) );
            }
            
            dataInput.close( );
            return inventory;
        } catch ( ClassNotFoundException e ) {
            throw new IOException( "Unable to decode class type." , e );
        }
    }
    
    private String itemStackArrayToBase64( ItemStack[] items ) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream( outputStream );
            
            // Write the size of the inventory
            dataOutput.writeInt( items.length );
            
            // Save every element in the list
            for ( ItemStack item : items ) {
                dataOutput.writeObject( item );
            }
            
            // Serialize that array
            dataOutput.close( );
            return Base64Coder.encodeLines( outputStream.toByteArray( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( "Unable to save item stacks." , e );
        }
    }
    
    private String toBase64( Inventory inventory ) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream( outputStream );
            
            // Write the size of the inventory
            dataOutput.writeInt( inventory.getSize( ) );
            
            // Save every element in the list
            for ( int i = 0; i < inventory.getSize( ); i++ ) {
                dataOutput.writeObject( inventory.getItem( i ) );
            }
            
            // Serialize that array
            dataOutput.close( );
            return Base64Coder.encodeLines( outputStream.toByteArray( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( "Unable to save item stacks." , e );
        }
    }
    
}

