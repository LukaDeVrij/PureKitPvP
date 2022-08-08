package me.lifelessnerd.purekitpvp.noncombatstats.commands;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GetKitStats extends Subcommand {
    Plugin plugin;
    public GetKitStats(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public String getName() {
        return "kitstats";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get usage stats of kits";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp kitstats";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        FileConfiguration kitStats = KitStatsConfig.get();
        for(String key : kitStats.getKeys(false)){

            player.sendMessage(key + " - " + kitStats.getInt(key));

        }
        return true;
    }
}
