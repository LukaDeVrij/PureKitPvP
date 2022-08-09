package me.lifelessnerd.purekitpvp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HelpCommand extends Subcommand {
    Plugin plugin;
    ArrayList<Subcommand> subcommands;
    public HelpCommand(ArrayList<Subcommand> subcommands, Plugin plugin) {
        this.plugin = plugin;
        this.subcommands = subcommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get a list of all commands";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp help";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (args.length == 1){
            String message = """
                    &bPureKitPvP - Help Menu
                    &a/kit &r- &eOpen up the kit menu
                    &a/getkit <kit> &r- &eGet a kit directly
                    &a/suicide &r- &eCommit suicide (if enabled)
                    &a/stats <player> &r- &eGet PVP stats of a player
                    &bFor admin commands, see &a/pkpvp help 2&b!
                    """;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));



        }
        if (args.length >= 2){

            if (args[1].equalsIgnoreCase("2")){

                if (!(player.hasPermission("purekitpvp.admin.*"))){
                    player.sendMessage("No permission!");
                    return true;
                }//Obsolete, any AdminCommandManager subcommands require admin perm

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPureKitPvP - Admin Commands"));
                for (Subcommand subcommand : subcommands){
                    String message =
                            "&a" + subcommand.getSyntax() + " &r- &e" + subcommand.getDescription();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }

            }

        }

        return true;
    }
}
