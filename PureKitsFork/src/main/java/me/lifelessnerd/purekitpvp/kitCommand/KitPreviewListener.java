package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class KitPreviewListener implements Listener {

    Plugin plugin;

    public KitPreviewListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (e.getCurrentItem() == null){
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        InventoryView inv = e.getView();

        TextComponent desiredInvTitle = (TextComponent) LanguageConfig.lang.get("KITS_GUI_PREVIEW_TITLE");

        if (inv.title().toString().contains((desiredInvTitle.content()))) { //I sometimes hate component - gotta do this with UUID in the future

            if (e.getRawSlot() >= 53){
                return;
            }
            e.setCancelled(true);
            if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"))){
                String pdcValue = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING);
                switch(pdcValue){
                    case "Select":
                        String kitName = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING);
                        player.chat("/getkit " + kitName);
                        player.closeInventory();
                        break;
                    case "Stats":
                        //Can be used later?
                        break;
                    case "Back":
                        player.chat("/kit");
                        break;
                    default:
                        break;
                }
            }

        }

    }
}
