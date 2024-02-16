package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
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
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.GENERIC_NO_PERMISSION.toString()));
            return true;
        }

        if (!(args.length >= 2)){
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.GENERIC_LACK_OF_ARGS.toString()));
            return false;
        }

        String kitName = args[1].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);

        if(KitConfig.get().get("kits." + kitName) == null){

            player.sendMessage(LanguageConfig.lang.get(LanguageKey.KITS_DOES_NOT_EXIST.toString()));
            return true;
        }

        EditKitInventory editKitInventory = new EditKitInventory(54,
                Component.text("Editing " + kitName, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false),
                plugin, kitName);
        editKitInventory.openInventory(player);

        return true;
    }
}
