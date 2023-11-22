package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileTrail implements Listener {
    /* TODO: THIS CLASS HAS DUPOLICATE CODE because of the difference in events -> can be circumvented by passing projectile and player
    TODO: can it? since one is projectile and the other is abstractarrow?? CROSSBOWS are added but is a projectile*/
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
        String trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());

        if(trail == null){
            CosmeticsConfig.get().set("projectile_trail." + player, "none");
            CosmeticsConfig.save();
            CosmeticsConfig.reload();
            trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());
        }

        //  Inspiration from https://github.com/iInvisibilities/spiraltrails/blob/main/SpiralFactory.java
        // Not used right now, maybe in the future: this would allow for spirals and such
        Projectile projectile = e.getProjectile();
//        System.out.println(projectile.toString());
        int rotationSpeed = 30;
        final double INCREMENT = (2 * Math.PI) / rotationSpeed;
        new BukkitRunnable() {

            double circlePointOffset = 0;
            Location lastLocation = player.getEyeLocation();

            @Override
            public void run() {
                if(projectile.isDead() || projectile.isOnGround()) {
                    this.cancel();
                    return;
                }

                String trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());
                // Could be removed: pass trail into the runnable once at event (How?)
                // How? with a class that implements Runnable - a lot of code -> this is fine

                Location location = projectile.getLocation();
//                System.out.println(trail);
                switch(trail){
                    case "none":
                        return;
                    case "flame":
                        location.getWorld().spawnParticle(Particle.FLAME, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "sparkle":
                        location.getWorld().spawnParticle(Particle.END_ROD, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "water_drip":
                        location.getWorld().spawnParticle(Particle.DRIP_WATER, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "heart":
                        location.getWorld().spawnParticle(Particle.HEART, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "firework":
                        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "totem":
                        location.getWorld().spawnParticle(Particle.TOTEM, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "smoke":
                        location.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    default:
                        break;
                }
            }
        }.runTaskTimer(plugin, 2, plugin.getConfig().getInt("projectile-trail-period"));

    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e){

        if (!(e.getEntity() instanceof Player player)){
            return;
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        String trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());

        if(trail == null){
            CosmeticsConfig.get().set("projectile_trail." + player.getName(), "none");
            CosmeticsConfig.save();
            CosmeticsConfig.reload();
            trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());
        }

        //  Inspiration from https://github.com/iInvisibilities/spiraltrails/blob/main/SpiralFactory.java
        // Not used right now, maybe in the future: this would allow for spirals and such
        Projectile arrow = (Projectile) e.getProjectile();
        // Not necessarily an arrow, could be a firework

        int rotationSpeed = 30;
        final double INCREMENT = (2 * Math.PI) / rotationSpeed;
        new BukkitRunnable() {

            double circlePointOffset = 0;
            Location lastLocation = player.getEyeLocation();

            @Override
            public void run() {
                if(arrow.isDead() || arrow.isOnGround()) { // hopefully this is most of em
                    this.cancel();
                    return;
                }

                String trail = CosmeticsConfig.get().getString("projectile_trail." + player.getName());

                Location location = arrow.getLocation();
//                System.out.println(trail);
                switch(trail){
                    case "none":
                        return;
                    case "flame":
                        location.getWorld().spawnParticle(Particle.FLAME, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "sparkle":
                        location.getWorld().spawnParticle(Particle.END_ROD, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "water_drip":
                        location.getWorld().spawnParticle(Particle.DRIP_WATER, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "heart":
                        location.getWorld().spawnParticle(Particle.HEART, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "firework":
                        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "totem":
                        location.getWorld().spawnParticle(Particle.TOTEM, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    case "smoke":
                        location.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, plugin.getConfig().getInt("projectile-trail-count"), 0.1, 0.1, 0.1, 0.001);
                        break;
                    default:
                        break;
                }
            }
        }.runTaskTimer(plugin, 2, plugin.getConfig().getInt("projectile-trail-period"));
    }


}
