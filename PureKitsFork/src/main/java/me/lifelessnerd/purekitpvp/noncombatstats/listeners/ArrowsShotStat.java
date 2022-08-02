package me.lifelessnerd.purekitpvp.noncombatstats.listeners;

import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

public class ArrowsShotStat implements Listener {
    Plugin plugin;

    public ArrowsShotStat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArrowShot(EntityShootBowEvent event){

        if (!(event.getEntity() instanceof Player player)){
            return;
        }
        if (!(event.getProjectile() instanceof Arrow arrow)){
            return;
        }
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        FileConfiguration playerStats = PlayerStatsConfig.get();
        String pathPrefix = player.getName();

        if (playerStats.isSet(pathPrefix + ".arrows_shot")){
            int value = playerStats.getInt(pathPrefix + ".arrows_shot");
            playerStats.set(pathPrefix + ".arrows_shot", value + 1);
        } else {
            playerStats.set(pathPrefix + ".arrows_shot", 1);
        }

        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();


    }
    @EventHandler
    public void arrowsHit(ProjectileHitEvent event){

        if (event.getHitEntity() == null){
            return;
        }
        if (!(event.getHitEntity() instanceof Player hitPlayer)){
            return;
        }
        if (!(hitPlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }
        if (!(event.getEntity() instanceof Arrow arrow)){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player shootPlayer)){
            return;
        }

        FileConfiguration playerStats = PlayerStatsConfig.get();
        String pathPrefix = shootPlayer.getName();

        if (playerStats.isSet(pathPrefix + ".arrows_hit")){
            int value = playerStats.getInt(pathPrefix + ".arrows_hit");
            playerStats.set(pathPrefix + ".arrows_hit", value + 1);
        } else {
            playerStats.set(pathPrefix + ".arrows_hit", 1);
        }

        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();


    }
}
