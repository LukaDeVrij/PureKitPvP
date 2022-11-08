package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkLib;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;


public class GUIListener implements Listener {

    Plugin plugin;

    public GUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();

        if (player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))){

            if (!(e.getCurrentItem() == null && e.getRawSlot() >= 53)){
                ItemStack clickedItem = e.getCurrentItem();
                InventoryView inv = e.getView();
                if (inv.title().toString().contains("Kits")) { //I hate component

                    if (e.getRawSlot() < 52){
                        e.setCancelled(true);
                        if(clickedItem == null){
                            return;
                        }
                        String displayName = clickedItem.getItemMeta().getDisplayName();
                        displayName = ChatColor.stripColor(displayName);
                        player.chat("/getkit " + displayName);
                        player.closeInventory();
                    }

                    else if (e.getRawSlot() == 53){ // Reset button
                        e.setCancelled(true);
                        if (player.hasPermission("purekitpvp.admin.resetkit")){
                            player.chat("/purekitpvp resetkit");
                        }
                        else {
                            player.chat("/suicide");
                        }

                    } else if (e.getRawSlot() == 52){ // Perk help button
                        e.setCancelled(true);

                        player.chat("/perks");

                    }
                }
            }
        }
    }
}
