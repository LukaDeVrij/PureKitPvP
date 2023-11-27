package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkLib;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

                PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
                TextComponent desiredInvTitle = (TextComponent) LanguageConfig.lang.get("KITS_GUI_TITLE");
                if (inv.title().toString().contains((desiredInvTitle.content()))) { //I hate component

                    if (e.getRawSlot() < 45){
                        e.setCancelled(true);
                        if(clickedItem == null){
                            return;
                        }

                        String displayName = serializer.serialize(clickedItem.getItemMeta().displayName());

                        // Preview kit button
                        if (e.getClick() == ClickType.RIGHT){
                            KitPreview kitPreview = new KitPreview(plugin, displayName, player);
                            return;
                        }

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

                    } else if (e.getRawSlot() == 49){ // Perk help button
                        e.setCancelled(true);

                        player.chat("/perks");

                    } else if (e.getRawSlot() == 48){ // Prev page button
                        if (e.getCurrentItem() == null){
                            return;
                        }
                        e.setCancelled(true);
                        String title = serializer.serialize(e.getView().title());
                        int prevPage = Integer.parseInt(title.split(" - ")[1]) - 1;
                        player.chat("/kit " + prevPage);

                    } else if (e.getRawSlot() == 50){ // Next page button
                        if (e.getCurrentItem() == null){
                            return;
                        }
                        e.setCancelled(true);
                        String title = serializer.serialize(e.getView().title());
                        int nextPage = Integer.parseInt(title.split(" - ")[1]) + 1;
                        player.chat("/kit " + nextPage);


                    }

                }
            }
        }
    }
}
