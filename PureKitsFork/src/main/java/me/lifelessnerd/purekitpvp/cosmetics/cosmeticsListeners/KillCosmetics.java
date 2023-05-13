package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class KillCosmetics implements Listener {

    public static Plugin plugin;

    public KillCosmetics(Plugin plugin) {
        KillCosmetics.plugin = plugin;
    }

    public static void fireKillCosmetic(Player player, Player creditPlayer) {
        String activeCosmetic = "firework";
        try {
            activeCosmetic = CosmeticsConfig.get().getString(creditPlayer.getName());
        } catch (Exception e) {
            activeCosmetic = "firework";
            plugin.getLogger().log(Level.WARNING, e.getMessage());
        } // If something in the config is wrong; switch statement will pick default and do nothing
        Location killLocation = player.getLocation();
        Location killerLocation = creditPlayer.getLocation();
        switch (activeCosmetic) {
            case "firework" -> {
                firework(killLocation);
            }
            case "blood_explosion" -> {
                bloodExplosion(killLocation);
            }
            case "ritual" -> {
                ritual(killLocation);
            }
            case "tornado" -> {
                tornado(killLocation);
            }
            default -> {
                //Idk how you would end up here: Do nothing
            }

        }

    }

    public static void firework(Location location) {
        location = addHeightToLocation(location, 1.4);
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.WHITE).flicker(true).build());
        fw.setMetadata("nodamage", new FixedMetadataValue(plugin, true));
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    @EventHandler
    public void preventFireworkDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework) {
            Firework fw = (Firework) e.getDamager();
            if (fw.hasMetadata("nodamage")) {
                e.setCancelled(true);
            }
        }
    }

    public static void bloodExplosion(Location location) {

        for (float i = 0.5f; i < 1.8f; i += 0.1) {
            location.getWorld().spawnParticle(
                    Particle.BLOCK_CRACK,
                    addHeightToLocation(location, i),
                    20,
                    Material.REDSTONE_BLOCK.createBlockData()
            );
        }

    }

    public static void ritual(Location location) {
        for (int i = 0; i < 360; i += 20) {
            Location flameloc = addHeightToLocation(location, 1.2);
            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 0.7);
            flameloc.setX(flameloc.getX() + Math.sin(i) * 0.7);
            location.getWorld().spawnParticle(Particle.FLAME, flameloc, 10, 0, 0, 0, 0.001);
        }
        for (int i = 0; i < 360; i += 10) {
            Location flameloc = addHeightToLocation(location, 0.6);
            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 1.5);
            flameloc.setX(flameloc.getX() + Math.sin(i) * 1.5);
            location.getWorld().spawnParticle(Particle.FLAME, flameloc, 10, 0, 0, 0, 0.001);
        }
        for (int i = 0; i < 360; i += 5) {
            Location flameloc = addHeightToLocation(location, 0);
            double extendedZ = flameloc.getZ() + Math.cos(i) / 2;
            double extendedX = flameloc.getZ() + Math.cos(i) / 2;
            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 2);
            flameloc.setX(flameloc.getX() + Math.sin(i) * 2);
            location.getWorld().spawnParticle(Particle.CLOUD, flameloc, 1, extendedX, 0, extendedZ, 0.001);
        }

    }

    public static void tornado(Location location) {
        for (double i = 1; i < 4; i = i + 0.4) {
            double newY = location.getY() + i;
            for (double j = 0; j < 360; j += 20) {
                double newZ = (location.getZ() + Math.cos(j) * (1 * Math.sqrt(i) - 0.5));
                double newX = (location.getX() + Math.sin(j) * (1 * Math.sqrt(i) - 0.5));
                location.getWorld().spawnParticle(Particle.FLAME, newX, newY, newZ, 1, 0.1, 0.5, 0.1, 0.001);
            }
        }
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_SHOOT, 1f, 1);
    }

    public static Location addHeightToLocation(Location location, double increaseY) {
        double x = location.getX();
        double z = location.getZ();
        double y = location.getY() + increaseY;
        // Create a new location with the updated y coordinate
        return new Location(location.getWorld(), x, y, z);
    }

}
