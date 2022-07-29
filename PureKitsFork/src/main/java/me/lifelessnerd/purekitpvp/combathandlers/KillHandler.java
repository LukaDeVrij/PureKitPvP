package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class KillHandler implements Listener {
    Plugin plugin;

    public KillHandler(Plugin plugin){
        this.plugin = plugin;
    }
}
