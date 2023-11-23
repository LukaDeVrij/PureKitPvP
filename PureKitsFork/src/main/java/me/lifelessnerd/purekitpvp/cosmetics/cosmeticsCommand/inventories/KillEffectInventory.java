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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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
    static Plugin plugin; // static?

    public KillEffectInventory(Plugin plugin) {
        KillEffectInventory.plugin = plugin;
    } // could make this this.plugin

    public static void openKillEffectInventory(Player player){
        // This method is responsible for drawing items in inventory on clicking 'Kill Effect'
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

            //MEH: This 4x is not needed; just once at the start and then add green to that item?
            String configValue = CosmeticsConfig.get().getString("kill_effect." + player.getName());
            // if player is not present in file; add it to config and try again
            if (configValue == null){
                CosmeticsConfig.get().set("kill_effect." + player.getName(), plugin.getConfig().getString("default-kill-effect"));
                CosmeticsConfig.save();
                CosmeticsConfig.reload();
                configValue = CosmeticsConfig.get().getString("kill_effect." + player.getName());
            }

            if (configValue.equalsIgnoreCase(killEffect)){
                displayName = displayName.color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false);
                displayName = displayName.decoration(TextDecoration.BOLD, true);
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            } else {
                displayName = displayName.color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);;
            }

            itemMeta.displayName(displayName);
            effectItem.setItemMeta(itemMeta);
            cosmeticsInventory.addItem(effectItem);
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowIM = backArrow.getItemMeta();
        backArrowIM.displayName(Component.text("Go Back").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN));
        backArrow.setItemMeta(backArrowIM);
        cosmeticsInventory.setItem(49, backArrow);
        player.openInventory(cosmeticsInventory);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // This method is responsible for logic after a cosmetics item is clicked
        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        if (!(e.getView().title().toString().contains("Change Kill Effect"))) {
            return;
        }
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null){
            return;
        }
        e.setCancelled(true);

        String itemDisplayName = MyStringUtils.componentToString(clickedItem.displayName());
        String attachedCosmeticName = MyStringUtils.itemNameToCosmeticId(itemDisplayName);
        // Do stuff to the config with this string
//        System.out.println(attachedCosmeticName);

        if (itemDisplayName.equalsIgnoreCase("Go Back")){
            player.closeInventory();
            player.chat("/cosmetics");
            return;
        }

        CosmeticsConfig.get().set("kill_effect." + player.getName(), attachedCosmeticName);
        CosmeticsConfig.save();
        CosmeticsConfig.reload();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,0);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Selected " + "&c" + itemDisplayName));
        e.getInventory().close(); // This is a choice I made, may not be optimal

    }
}
