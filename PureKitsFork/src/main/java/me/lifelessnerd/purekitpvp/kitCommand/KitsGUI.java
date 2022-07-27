package me.lifelessnerd.purekitpvp.kitCommand;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KitsGUI implements TabExecutor {

    Plugin plugin;

    public KitsGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!plugin.getConfig().isSet("kits.")) {
            player.sendMessage(ChatColor.GRAY + "There aren't any kits yet!");
            return true;
        }

        //Create inventory GUI
        TextComponent inventoryTitle = Component.text("Kits").color(TextColor.color(255, 150, 20));
        Inventory kits = Bukkit.createInventory(null, 54, inventoryTitle);
        for (String key : plugin.getConfig().getConfigurationSection("kits").getKeys(false)) {

            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            //NPE GALORE, TODO: idiot proof if these keys do not exist, instead of crashing entire plugin with an NPE
            //Set block

            itemStack.setType(Material.getMaterial(plugin.getConfig().getString("kits." + key + ".guiitem")));

            //Set lore

            String loreText = plugin.getConfig().getString("kits." + key + ".guilore");
            loreText = ChatColor.translateAlternateColorCodes('&', loreText);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(loreText);
            itemMeta.setLore(lore); //Heck you Component

            //Set meta
            String kitIconName = plugin.getConfig().getString("kits." + key + ".displayname");
            kitIconName = ChatColor.translateAlternateColorCodes('&', kitIconName);
            itemMeta.setDisplayName(kitIconName); //Heck you Component
            itemStack.setItemMeta(itemMeta);

            //Add items to inventory
            kits.addItem(itemStack);
            player.openInventory(kits);

        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
