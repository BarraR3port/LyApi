package net.lymarket.lyapi.spigot.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.lyapi.spigot.SMain;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Menu implements InventoryHolder {
    
    protected Inventory inventory;
    protected IPlayerMenuUtility playerMenuUtility;
    protected boolean isOnSchedule = false;
    
    public Menu( IPlayerMenuUtility playerMenuUtility ){
        this.playerMenuUtility = playerMenuUtility;
    }
    
    public abstract String getMenuName( );
    
    public abstract int getSlots( );
    
    public abstract void handleMenu( InventoryClickEvent e );
    
    public abstract void setMenuItems( );
    
    public abstract boolean overridePlayerInv( );
    
    public boolean interactPlayerInv( ){
        return false;
    }
    
    
    public void open( ){
        inventory = Bukkit.createInventory( this , getSlots( ) , SMain.getInstance( ).getUtils( ).format( getMenuName( ) ) );
        
        this.setMenuItems( );
        
        playerMenuUtility.getOwner( ).openInventory( inventory );
    }
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
    protected ItemStack createCustomSkull( String name , String head ){
        return new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( head )
                .setDisplayName( name )
                .build( );
    }
    
    protected ItemStack createCustomSkull( String name , List < String > lore , String head ){
        return new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( head )
                .setLore( lore )
                .setDisplayName( name )
                .build( );
    }
    
    protected void checkSomething( Player p , int slot , ItemStack item , String name , List < String > lore ){
        
        if ( isOnSchedule ) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory( ).getTopInventory( ).setItem( slot , new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=" )
                .setLore( lore )
                .setDisplayName( name )
                .build( ) );
        setOnSchedule( p , slot , item );
        
    }
    
    protected void checkSomething( Player p , int slot , ItemStack item , String name , String lore ){
        
        if ( isOnSchedule ) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory( ).getTopInventory( ).setItem( slot , new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=" )
                .addLoreLine( lore )
                .setDisplayName( name )
                .build( ) );
        setOnSchedule( p , slot , item );
        
    }
    
    protected void setOnSchedule( Player p , int slot , ItemStack item ){
        Bukkit.getServer( ).getScheduler( ).runTaskLaterAsynchronously( SMain.getPlugin( ) , ( ) -> {
            
            if ( p.getOpenInventory( ).getTopInventory( ).getHolder( ) instanceof Menu ) {
                p.getOpenInventory( ).getTopInventory( ).setItem( slot , item );
                isOnSchedule = false;
            }
            
        } , 30L );
    }
}
