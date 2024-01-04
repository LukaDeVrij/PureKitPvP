package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;


public class DoubleHealthListeners implements Listener {
    Plugin plugin;
    public DoubleHealthListeners(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        if (EventDataClass.doubleHealth){
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(40.0D);
            player.setHealth(40.0D);
            // TODO and set HP to be max instantly DO THIS EVERYWHERE
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }
        if (EventDataClass.doubleHealth){
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(20.0D);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        World fromWorld = e.getFrom();
        if (fromWorld.getName().equals(plugin.getConfig().getString("world"))){
            return;
        }
        if (EventDataClass.doubleHealth){
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(20.0D);
        }
    }
}
