package me.lifelessnerd.purekitpvp.kitAdmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class DeleteKit extends Subcommand {
    Plugin plugin;

    public DeleteKit(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public String getName() {
        return "deletekit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Remove a kit";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp deletekit <kitName>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return true;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {

        if (!sender.hasPermission("purekitpvp.admin.deletekit")){
            sender.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 2)){
            sender.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }

        String kitName = args[1].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);

        if(KitConfig.get().get("kits." + kitName) == null){

            sender.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        KitConfig.get().getConfigurationSection("kits").set(kitName, null);
        KitConfig.save();
        KitConfig.reload();
        sender.sendMessage(Component.text("You removed kit ", NamedTextColor.GREEN).append(Component.text(kitName, NamedTextColor.WHITE)));


        return true;
    }
}
