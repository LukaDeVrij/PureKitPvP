package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class ItemDamagePreventer implements Listener {
    Plugin plugin;

    public ItemDamagePreventer(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        Player player = null;
        if (event.getDamager() instanceof Player){

            player = (Player) event.getEntity();



        }

    }
}
