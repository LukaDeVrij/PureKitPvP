package me.lifelessnerd.purekitpvp.scoreboards;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

public abstract class AbstractObjective{
    Plugin plugin;
    public AbstractObjective(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract void show(Scoreboard scoreboard, Player player);
}
