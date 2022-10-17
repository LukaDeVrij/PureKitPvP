package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.combathandlers.perkhandler.PerkLib;
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
import java.util.Arrays;
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

                        //Create inventory GUI
                        TextComponent inventoryTitle = Component.text("Perk Help").color(TextColor.color(255, 150, 20));
                        Inventory perkHelp = Bukkit.createInventory(null, 27, inventoryTitle);

                        PerkLib perkLib = new PerkLib();
                        for (String perk : perkLib.perkIcons.keySet()) {

                            ItemStack icon = new ItemStack(perkLib.perkIcons.get(perk));
                            ItemMeta itemMeta = icon.getItemMeta();
                            itemMeta.displayName(Component.text(perk).color(TextColor.color(200, 0, 0)));
                            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            icon.setItemMeta(itemMeta);

                            ArrayList<Component> loreTBA = new ArrayList<>();

                            if (perkLib.perks.get(perk).contains("\n")){
                                String[] decodedLore = MyStringUtils.perkLoreDecoder(perkLib.perks.get(perk));
                                for (String line : decodedLore){
                                    loreTBA.add(Component.text(line).color(TextColor.color(150, 150, 150)));
                                }
                            } else {
                                loreTBA.add(Component.text(perkLib.perks.get(perk)).color(TextColor.color(150, 150, 150)));
                            }

                            icon.lore(loreTBA);
                            perkHelp.addItem(icon);
                        }

                        //Go back to /kit
                        ItemStack backButton = new ItemStack(Material.BARRIER);
                        ItemMeta backButtonMeta = backButton.getItemMeta();
                        backButtonMeta.displayName(Component.text("Back"));
                        List<Component> loreList2 = new ArrayList<>();
                        TextComponent txt21 = Component.text("Go back to kit selection!").color(TextColor.color(100,100,100));
                        loreList2.add(txt21);
                        backButton.lore(loreList2);
                        backButton.setItemMeta(backButtonMeta);
                        perkHelp.setItem(26, backButton);

                        player.openInventory(perkHelp);

                    }

                }
                if (inv.title().toString().contains("Perk Help")){
                    e.setCancelled(true);
                    if (e.getRawSlot() == 26){

                        if(clickedItem == null){
                            return;
                        }
                        player.chat("/kit");

                    }
                }
            }
        }
    }
}
