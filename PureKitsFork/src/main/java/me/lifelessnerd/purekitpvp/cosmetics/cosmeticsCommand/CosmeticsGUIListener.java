package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand;

import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.KillEffectInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CosmeticsGUIListener implements Listener {

    Plugin plugin;

    public CosmeticsGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (!(e.getView().title().toString().contains("Cosmetics Menu"))) {
            return;
        }
        ItemStack clickedItem = e.getCurrentItem();
        e.setCancelled(true);

        Component itemDisplayName = clickedItem.displayName();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        String itemName = serializer.serialize(itemDisplayName);

        switch(itemName){
            case "Kill Effect":
                KillEffectInventory.openKillEffectInventory(player);
                return;
            case "Projectile Trail":

                return;
            case "Kill Message":
                return;

        }



    }


}
