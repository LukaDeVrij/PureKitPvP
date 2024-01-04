package me.lifelessnerd.purekitpvp.noncombatstats.commands;

import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GetStats implements TabExecutor {
    Plugin plugin;

    public GetStats(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        FileConfiguration playerStatsConfig = PlayerStatsConfig.get();
        String argumentPlayer = player.getName();
        if (args.length == 1){
            argumentPlayer = args[0];
        }

        Set<String> allKeys = playerStatsConfig.getKeys(true);
        if (!(allKeys.contains(argumentPlayer))){
            player.sendMessage("That player does not have any stats attached to them.");
            return true;
        }


        player.sendMessage(argumentPlayer + "'s " + ChatColor.BLUE + "Statistics");
        for (String key : allKeys){
            if (key.split("\\.").length == 1){
                continue;
            }
            if (key.split("\\.")[0].equalsIgnoreCase(argumentPlayer)) {

                //Only children of player
                if (key.split("\\.")[1].equalsIgnoreCase("kdratio")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a    " +
                            MyStringUtils.itemCamelCase(key.split("\\.")[1]) + "&c- &r" + playerStatsConfig.getDouble(key)));
                } else if (key.split("\\.")[1].equalsIgnoreCase("current_kit")){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a    " +
                            MyStringUtils.itemCamelCase(key.split("\\.")[1]) + "&c- &r" + playerStatsConfig.getString(key)));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a    " +
                            MyStringUtils.itemCamelCase(key.split("\\.")[1]) + "&c- &r" + playerStatsConfig.getInt(key)));
                }
            }
        }



        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return null;
    }
}
