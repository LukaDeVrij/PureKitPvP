package me.lifelessnerd.purekitpvp.noncombatstats.commands;

import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GetKitStats implements CommandExecutor {
    Plugin plugin;
    public GetKitStats(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        FileConfiguration kitStats = KitStatsConfig.get();
        for(String key : kitStats.getKeys(false)){

            sender.sendMessage(key + " - " + kitStats.getInt(key));

        }
        return true;
    }
}
