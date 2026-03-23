package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        String trail = checkCosmeticsDefined(player);

        // Could be extended with https://github.com/iInvisibilities/spiraltrails/blob/main/SpiralFactory.java
        Projectile projectile = e.getProjectile();
        produceTrailOnProjectile(trail, projectile);

    }

    @EventHandler
    public void onFishingThrow(PlayerFishEvent e){
        if (!(e.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (!(plugin.getConfig().getBoolean("fishing-rod-trail"))){
            return;
        }
        if (!(e.getState().equals(PlayerFishEvent.State.FISHING))){
            return;
        }
        Player player = e.getPlayer();
        String trail = checkCosmeticsDefined(player);

        Projectile projectile = e.getHook();
        produceTrailOnProjectile(trail, projectile);
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e){

        if (!(e.getEntity() instanceof Player player)){
            return;
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        String trail = checkCosmeticsDefined(player);

        Projectile projectile = (Projectile) e.getProjectile();
        produceTrailOnProjectile(trail, projectile);
    }

    public String checkCosmeticsDefined(Player player){
        String trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());

        if(trail == null){
            CosmeticsConfig.get().set("projectile_trail." + player, "none");
            CosmeticsConfig.save();
            CosmeticsConfig.reload();
            trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());
        }
        return trail;
    }

    public void produceTrailOnProjectile(String trail, Projectile projectile){

        new BukkitRunnable() {

            @Override
            public void run() {
                if(projectile.isDead() || projectile.isOnGround()) {
                    this.cancel();
                    return;
                }

                Location location = projectile.getLocation();

                switch(trail){
                    case "none":
                        return;
                    case "flame":
                        location.getWorld().spawnParticle(Particle.FLAME, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "sparkle":
                        location.getWorld().spawnParticle(Particle.END_ROD, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "water_drip":
                        location.getWorld().spawnParticle(Particle.DRIPPING_WATER, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "heart":
                        location.getWorld().spawnParticle(Particle.HEART, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "firework":
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "totem":
                        location.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    case "smoke":
                        location.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001, null, true);
                        break;
                    default:
                        break;
                }
            }
        }.runTaskTimer(plugin, 2, plugin.getConfig().getInt("projectile-trail-period"));
    }

}
