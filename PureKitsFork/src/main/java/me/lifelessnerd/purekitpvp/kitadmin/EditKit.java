package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EditKit extends Subcommand {

    Plugin plugin;

    public EditKit(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "editkit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Edit a kit";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp editkit <kitName>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("purekitpvp.admin.editkit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 2)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }

        String kitName = args[1].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);

        if(KitConfig.get().get("kits." + kitName) == null){

            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        EditKitInventory editKitInventory = new EditKitInventory(54,
                Component.text("Editing " + kitName, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false),
                plugin, kitName);
        editKitInventory.openInventory(player);

        return true;
    }
}
