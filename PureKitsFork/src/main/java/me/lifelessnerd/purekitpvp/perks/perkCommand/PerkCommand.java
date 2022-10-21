package me.lifelessnerd.purekitpvp.perks.perkCommand;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PerkCommand implements CommandExecutor {
    Plugin plugin;

    public PerkCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(ChatColor.RED + "You can only use this menu in " + ChatColor.GRAY + plugin.getConfig().getString("world"));
            return true;
        }
        //Create inventory GUI
        TextComponent invTitle = Component.text("Perks Menu").color(TextColor.color(255, 150, 20));
        Inventory perksInventory = Bukkit.createInventory(null, 54, invTitle);

        //Info item
        ItemStack infoItem = new ItemStack(Material.BOOK);
        TextComponent infoItemName = Component.text("Perks Info").color(TextColor.color(233, 67, 47));
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.displayName(infoItemName);
        infoMeta = ComponentUtils.setLore(infoMeta, "&aYou can equip a total of &e5 &aperks total.", 0);
        infoMeta = ComponentUtils.setLore(infoMeta, "&7Click on a perk slot to choose a perk for that slot.", 1);
        infoMeta = ComponentUtils.setLore(infoMeta, "&7It will replace any perk currently in that slot.", 2);
        infoMeta = ComponentUtils.setLore(infoMeta, "&7Perks are abilities that are always active.", 3);
        infoItem.setItemMeta(infoMeta);
        perksInventory.setItem(13, infoItem);


        //Perk items
        ItemStack perk1Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        TextComponent noPerkSelectedName = Component.text("Perk 1").color(TextColor.color(233, 67, 47));
        ItemMeta perk1Meta = perk1Slot.getItemMeta();
        perk1Meta.displayName(noPerkSelectedName); //TODO: does not work? huh
        perk1Meta.setDisplayName("Perk 1"); //Because the above does not work, mf'er THIS DOESNT WORKJ EITHER WHAT
        perk1Meta = ComponentUtils.setLore(perk1Slot.getItemMeta(), "&cNo Perk Selected", 0);
        perk1Slot.setItemMeta(perk1Meta);
        perksInventory.setItem(20, perk1Slot);


        ItemStack perk2Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        perk1Slot.getItemMeta().displayName(noPerkSelectedName);
        perk1Slot.setItemMeta(perk2Slot.getItemMeta());
        ItemStack perk3Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        perk1Slot.getItemMeta().displayName(noPerkSelectedName);
        perk1Slot.setItemMeta(perk3Slot.getItemMeta());
        ItemStack perk4Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        perk1Slot.getItemMeta().displayName(noPerkSelectedName);
        perk1Slot.setItemMeta(perk4Slot.getItemMeta());
        ItemStack perk5Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        perk1Slot.getItemMeta().displayName(noPerkSelectedName);
        perk1Slot.setItemMeta(perk5Slot.getItemMeta());


        player.openInventory(perksInventory);
        return true;
    }
}
