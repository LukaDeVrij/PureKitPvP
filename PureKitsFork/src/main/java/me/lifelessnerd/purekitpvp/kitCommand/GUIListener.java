package me.lifelessnerd.purekitpvp.kitCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class GUIListener implements Listener {

    Plugin plugin;

    public GUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();

        if (player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))){

            ItemStack clickedItem = e.getCurrentItem();
            InventoryView inv = e.getView();
            if (inv.title().toString().contains("Kits")) { //I hate component
                e.setCancelled(true);
                String displayName = clickedItem.getItemMeta().getDisplayName();
                displayName = ChatColor.stripColor(displayName);
                player.chat("/getkit " + displayName);
                player.closeInventory();
            }

        }
    }
}
