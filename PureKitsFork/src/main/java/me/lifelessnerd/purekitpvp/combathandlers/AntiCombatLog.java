package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class AntiCombatLog implements Listener {
    Plugin plugin;
    public static HashMap<Player, Integer> combatData;
    public AntiCombatLog(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // TODO: implement if not too much work
    }
}
