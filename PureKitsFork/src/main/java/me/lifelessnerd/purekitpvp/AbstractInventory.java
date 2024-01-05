package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public abstract class AbstractInventory implements Listener {
    protected Inventory inv;
    protected Plugin plugin;

    public AbstractInventory(int size, Component title, Plugin plugin) {
        this.plugin = plugin;
        // Create a new inventory, with no owner (as this isn't a real inventory)
        inv = Bukkit.createInventory(null, size, title);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    // You can call this whenever you want to put the items in
    public abstract void initializeItems();

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    public void unregisterEvents(){
        HandlerList.unregisterAll(this);

    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        Player player = (Player) e.getWhoClicked();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) return;

        // verify current item is not null
        if (e.getCurrentItem() == null) return;

        onInventoryClickLogic(e);
    }
    public abstract void onInventoryClickLogic(final InventoryClickEvent e);

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (!e.getInventory().equals(inv)) return;
        HandlerList.unregisterAll(this);
    }

}
