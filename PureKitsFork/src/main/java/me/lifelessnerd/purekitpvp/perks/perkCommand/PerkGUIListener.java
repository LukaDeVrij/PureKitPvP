package me.lifelessnerd.purekitpvp.perks.perkCommand;

import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkLib;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
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

public class PerkGUIListener implements Listener {
    Plugin plugin;
    public PerkGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (e.getCurrentItem() == null || e.getRawSlot() >= 53) {
            return;
        }

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

            if (itemName.contains("Perk Slot")) {
                int slot = itemName.charAt(itemName.length() - 1); //TODO: Wrong number it seems (93??)
                //Create inventory GUI
                TextComponent invTitle = Component.text("Perk Slot " + slot).color(TextColor.color(255, 150, 20));
                Inventory perkSlotInventory = Bukkit.createInventory(null, 54, invTitle);

                //Fill inventory with interactive perk items
                PerkLib perkLib = new PerkLib();
                for (String perk : perkLib.perkIcons.keySet()) {

                    ItemStack icon = new ItemStack(perkLib.perkIcons.get(perk));
                    ItemMeta itemMeta = icon.getItemMeta();
                    itemMeta.displayName(Component.text(perk).color(TextColor.color(200, 0, 0)));
                    itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    icon.setItemMeta(itemMeta);

                    ArrayList<Component> loreTBA = new ArrayList<>();

                    if (perkLib.perks.get(perk).contains("\n")) {
                        String[] decodedLore = MyStringUtils.perkLoreDecoder(perkLib.perks.get(perk));
                        for (String line : decodedLore) {
                            loreTBA.add(Component.text(line).color(TextColor.color(150, 150, 150)));
                        }
                    } else {
                        loreTBA.add(Component.text(perkLib.perks.get(perk)).color(TextColor.color(150, 150, 150)));
                    }

                    icon.lore(loreTBA);
                    perkSlotInventory.addItem(icon);
                }

                player.openInventory(perkSlotInventory);
            }
        }
        else if (inv.title().toString().contains("Perk Slot")){
            Component title = inv.title();
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            String itemName = serializer.serialize(title);
            int slot = itemName.charAt(itemName.length() - 1);
            System.out.println(slot); //TODO; 51 wot?
        }




    }

    public static void selectPerk(int perkSlot){

    }
}
