package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories;

import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsCommand;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsLib;
import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KillEffectInventory implements Listener {
    Plugin plugin;

    public KillEffectInventory(Plugin plugin) {
        this.plugin = plugin;
    }

    public static void openKillEffectInventory(Player player){

        CosmeticsLib lib = new CosmeticsLib();

        TextComponent invTitle = Component.text("Change Kill Effect").color(TextColor.color(255, 150, 20));
        Inventory cosmeticsInventory = Bukkit.createInventory(null, 54, invTitle);
        for (String killEffect : lib.killEffects.keySet()) {
            ItemStack effectItem = new ItemStack(lib.killEffects.get(killEffect), 1);
            ItemMeta itemMeta = effectItem.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            Component displayName = Component.text(MyStringUtils.cosmeticIdToItemName(killEffect));
            if (CosmeticsConfig.get().getString(player.getName()).equalsIgnoreCase(killEffect)){
                // TODO: NPE, if player is not present in file; I have to find some place to add playername to file if not present
                displayName = displayName.color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false);
            } else {
                displayName = displayName.color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);;
            }

            itemMeta.displayName(displayName);
            effectItem.setItemMeta(itemMeta);
            cosmeticsInventory.addItem(effectItem);
        }
        player.openInventory(cosmeticsInventory);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (!(e.getView().title().toString().contains("Change Kill Effect"))) {
            return;
        }
        ItemStack clickedItem = e.getCurrentItem();
        e.setCancelled(true);

        Component itemDisplayName = clickedItem.displayName();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        String itemName = serializer.serialize(itemDisplayName);
        String attachedCosmeticName = MyStringUtils.itemNameToCosmeticId(itemName);
        // Do stuff to the config with this string
    }
}
