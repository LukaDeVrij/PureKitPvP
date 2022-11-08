package me.lifelessnerd.purekitpvp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){
            return false;
        }

        String message = """
                    &bPureKitPvP - Help Menu
                    &a/kit &r- &eOpen up the kit menu
                    &a/getkit <kit> &r- &eGet a kit directly
                    &a/suicide &r- &eCommit suicide (if enabled)
                    &a/stats <player> &r- &eGet PVP stats of a player
                    &a/perks &r- &eSelect perks
                    &bFor admin commands, see &a/pkpvp help 2&b!
                    """;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
}
