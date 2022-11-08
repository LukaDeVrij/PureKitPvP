package me.lifelessnerd.purekitpvp.combathandlers;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkFireHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PearlListener implements Listener {

    Plugin plugin;

    public PearlListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileThrow(PlayerLaunchProjectileEvent event) {

        if (!(event.getProjectile() instanceof EnderPearl)){
            return;
        }

        Player player = event.getPlayer();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        PerkFireHandler.fireEnderpearlPerks(player, event);



    }
}
