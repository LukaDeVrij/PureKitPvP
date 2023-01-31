package me.lifelessnerd.purekitpvp;

import org.bukkit.plugin.Plugin;

public class PluginGetter {

    static Plugin plugin;

    public PluginGetter(Plugin plugin) {
        PluginGetter.plugin = plugin;
    }

    public static Plugin Plugin(){
        return plugin;
    }

}
