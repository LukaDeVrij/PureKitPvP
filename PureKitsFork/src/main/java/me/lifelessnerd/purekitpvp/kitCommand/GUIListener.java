package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
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

            if (!(e.getCurrentItem() == null && e.getRawSlot() >= 53)){
                ItemStack clickedItem = e.getCurrentItem();
                InventoryView inv = e.getView();

                PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
                TextComponent desiredInvTitle = (TextComponent) LanguageConfig.lang.get("KITS_GUI_TITLE");
                if (inv.title().toString().contains((desiredInvTitle.content()))) { //I hate component

                    if(clickedItem == null){
                        return;
                    }
                    e.setCancelled(true);

                    // kit icons branch
                    if (e.getRawSlot() < 45){

                        String displayName = serializer.serialize(clickedItem.getItemMeta().displayName());
                        // Preview kit button
                        if (e.getClick() == ClickType.RIGHT){
                            KitPreviewInventory kitPreviewInventory = new KitPreviewInventory(54,
                                    LanguageConfig.lang.get("KITS_GUI_PREVIEW_TITLE").replaceText(ComponentUtils.replaceConfig("%KIT%", displayName)),
                                    plugin,
                                    displayName);
                            kitPreviewInventory.openInventory(player);
                            return;
                        }
                        player.chat("/getkit " + displayName);
                        player.closeInventory();
                    }

                    // options at the bottom branch
                    int rawSlot = e.getRawSlot();

                    switch(rawSlot){
                        case 53: // reset button
                            if (player.hasPermission("purekitpvp.admin.resetkit")){
                                player.chat("/purekitpvp resetkit");
                            }
                            else {
                                player.chat("/suicide");
                            }
                            break;

                        case 49: // Perk help button
                            player.chat("/perks");
                            break;

                        case 48: // Prev page button
                            String title = serializer.serialize(e.getView().title());
                            int prevPage = Integer.parseInt(title.split(" - ")[1]) - 1;
                            player.chat("/kit " + prevPage);
                            break;

                        case 50: // Next page button
                            String title2 = serializer.serialize(e.getView().title());
                            int nextPage = Integer.parseInt(title2.split(" - ")[1]) + 1;
                            player.chat("/kit " + nextPage);
                            break;

                    }
                }
            }
        }
    }
}
