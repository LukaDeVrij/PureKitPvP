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

            if (!(e.getCurrentItem() == null && e.getRawSlot() >= 53)){ //NPE, idk why
                ItemStack clickedItem = e.getCurrentItem();
                InventoryView inv = e.getView();
                if (inv.title().toString().contains("Kits")) { //I hate component

                    if (!(e.getRawSlot() == 53)){ // 53 is reset button
                        e.setCancelled(true);
                        if(clickedItem == null){ //
                            return;
                        }
                        String displayName = clickedItem.getItemMeta().getDisplayName();
                        displayName = ChatColor.stripColor(displayName);
                        player.chat("/getkit " + displayName);
                        player.closeInventory();
                    }
                    else {
                        e.setCancelled(true);
                        if (player.hasPermission("purekitpvp.admin.resetkit")){
                            player.chat("/resetkit");
                        }
                        else {
                            player.setHealth(0);
                        }
                    }

                }
            }
        }
    }
}
