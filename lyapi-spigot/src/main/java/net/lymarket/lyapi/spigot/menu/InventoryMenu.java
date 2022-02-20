package net.lymarket.lyapi.spigot.menu;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public abstract class InventoryMenu extends Menu {
    
    public InventoryMenu( IPlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    public abstract void handleMenu( InventoryDragEvent e );
    
    public abstract void handleMenu( InventoryCloseEvent e );
    
    
}
