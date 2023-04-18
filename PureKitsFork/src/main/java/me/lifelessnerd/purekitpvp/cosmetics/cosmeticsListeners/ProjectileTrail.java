package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class ProjectileTrail implements Listener {

    Plugin plugin;

    public ProjectileTrail(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileLaunch(PlayerLaunchProjectileEvent e) {
        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        // TODO wip
    }
}
