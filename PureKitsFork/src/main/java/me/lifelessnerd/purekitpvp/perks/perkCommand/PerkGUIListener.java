package me.lifelessnerd.purekitpvp.perks.perkCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PerkGUIListener implements Listener {
    Plugin plugin;
    public PerkGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {

            if (!(e.getCurrentItem() == null && e.getRawSlot() >= 53)) {
                ItemStack clickedItem = e.getCurrentItem();
                InventoryView inv = e.getView();
                if (inv.title().toString().contains("Perks Menu")) { //I hate component
                    e.setCancelled(true);
                    if (clickedItem.getType() != Material.RED_STAINED_GLASS_PANE){
                        return;
                    }
                    Component itemDisplayName = clickedItem.displayName();
                    PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
                    String itemName = serializer.serialize(itemDisplayName);
                    System.out.println(itemName);


                }
            }
        }
    }

    public static void selectPerk(int perkSlot){

    }
}
