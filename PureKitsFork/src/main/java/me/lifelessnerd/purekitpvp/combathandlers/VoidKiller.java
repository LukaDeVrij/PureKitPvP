package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.logging.Level;

public class VoidKiller implements Listener {
    Plugin plugin;
    public VoidKiller(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (!(p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        Location loc = p.getLocation();
        double yLevel = loc.getY();
        String yLevelStr = Double.toString(yLevel);
        if (plugin.getConfig().getBoolean("quick-voidkill")) {
            try {
                if (yLevel <= (double) plugin.getConfig().get("voidY")) {
                    for (PotionEffect effect : p.getActivePotionEffects()) {
                        p.removePotionEffect(effect.getType());
                    }
                    p.setHealth(1);
                    Location tpLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 150, loc.getZ());
                    p.teleport(tpLoc);
                }
            } catch (Exception ex) {
                plugin.getLogger().log(Level.SEVERE, "Please set voidY in the config!");
                plugin.getLogger().log(Level.INFO, ex.toString());
            }
        }

    }
}
