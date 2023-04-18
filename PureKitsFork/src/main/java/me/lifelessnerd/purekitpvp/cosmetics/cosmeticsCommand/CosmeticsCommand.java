package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand;

import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CosmeticsCommand implements CommandExecutor {
    Plugin plugin;

    public CosmeticsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(ChatColor.RED + "You can only use this menu in " + ChatColor.GRAY + plugin.getConfig().getString("world"));
            return true;
        }
        //Create inventory GUI
        TextComponent invTitle = Component.text("Cosmetics Menu").color(TextColor.color(255, 150, 20));
        Inventory perksInventory = Bukkit.createInventory(null, 54, invTitle);


        return false;
    }
}
