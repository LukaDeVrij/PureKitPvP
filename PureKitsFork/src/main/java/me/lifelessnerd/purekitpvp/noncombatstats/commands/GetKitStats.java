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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        ArrayList<String> kitStatsKeys = new ArrayList<>(kitStats.getKeys(false));
        Collections.sort(kitStatsKeys);
        for(String key : kitStatsKeys){

            player.sendMessage(key + " - " + kitStats.getInt(key));

        } //TODO: sort this based on value, so make a hashmap, put all stuff into it, sort on value, iterate through and print
        return true;
    }
}
