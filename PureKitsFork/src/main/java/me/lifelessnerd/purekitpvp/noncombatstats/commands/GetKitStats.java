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

import java.util.*;

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

        HashMap<String, Integer> map = new HashMap<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for(String key : kitStatsKeys){

            map.put(key, kitStats.getInt(key));

        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        Collections.reverse(list);

        for (Integer integer : list) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue().equals(integer)) {
                    sortedMap.put(entry.getKey(), integer);
                }
            }
        }

        for(String key : sortedMap.keySet()){

            player.sendMessage(key + " - " + kitStats.getInt(key));

        }
        return true;
    }
}
