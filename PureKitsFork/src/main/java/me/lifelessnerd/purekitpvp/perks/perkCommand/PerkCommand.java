package me.lifelessnerd.purekitpvp.perks.perkCommand;

import me.lifelessnerd.purekitpvp.files.KitConfig;
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
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        TextComponent infoItemLore =
                Component.text("You can equip a total of 5 perks total.").color(TextColor.color(255, 255, 50)).append(
                Component.text("Click on a perk slot to choose a perk for that slot.").color(TextColor.color(180, 180, 180)).append(
                Component.text("It will replace any perk currently in that slot.").color(TextColor.color(180, 180, 180)).append(
                Component.text("Perks are abilities that are always active.").color(TextColor.color(180, 180, 180)))));
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(infoItemLore);
        infoItem.getItemMeta().displayName(infoItemName);
        infoItem.getItemMeta().lore(lore); //Werkt dit zo? of moet ik het eerst nog opslaan en dan setten?


        ItemStack perk1Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack perk2Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack perk3Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack perk4Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack perk5Slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);



        return false;
    }
}
