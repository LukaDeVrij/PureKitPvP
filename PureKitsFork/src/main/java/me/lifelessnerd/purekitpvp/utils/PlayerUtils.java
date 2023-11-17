package me.lifelessnerd.purekitpvp.utils;

import me.lifelessnerd.purekitpvp.PluginGetter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayerUtils {

    public static Set<Player> getPlayersInWorld(String worldName){
        Set<Player> players = new HashSet<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld().getName().equalsIgnoreCase(PluginGetter.Plugin().getConfig().getString("world"))) {
                players.add(onlinePlayer);
            }
        }
        return players;
    }
}
