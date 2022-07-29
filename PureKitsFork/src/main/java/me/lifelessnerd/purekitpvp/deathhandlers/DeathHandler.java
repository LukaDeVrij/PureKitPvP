package me.lifelessnerd.purekitpvp.deathhandlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class DeathHandler implements Listener {

    Plugin plugin;

    public DeathHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){

        Player player = e.getPlayer();
        Player killer = e.getPlayer().getKiller();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }
        Bukkit.broadcast(Component.text(killer + " killed " + player));
        //e.setCancelled(true);


    }

    public void onPlayerGetHit(EntityDamageByEntityEvent e){

        if (!(e.getDamager() instanceof Player && e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        Bukkit.broadcast(Component.text(damager + " damaged " + player));

    }

}
