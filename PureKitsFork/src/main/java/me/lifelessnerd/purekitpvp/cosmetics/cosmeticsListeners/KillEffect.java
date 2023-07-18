package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.text.html.HTMLDocument;
import java.util.logging.Level;

public class KillEffect implements Listener {

    public static Plugin plugin;

    public KillEffect(Plugin plugin) {
        KillEffect.plugin = plugin;
    }

    public static void fireKillCosmetic(Player player, Player creditPlayer) {
        String activeCosmetic = "firework";
        try {
            // This construct is weird; it works tho
            activeCosmetic = CosmeticsConfig.get().getString("kill_effect." + creditPlayer.getName());
            if (activeCosmetic == null){
                throw new NullPointerException("Player is not defined in Cosmetics config! They have been added with default.");
            }
        } catch (Exception e) {
            String configValue = CosmeticsConfig.get().getString("kill_effect." + creditPlayer.getName());
            // if player is not present in file; add it to config and try again
            if (configValue == null){
                CosmeticsConfig.get().set("kill_effect." + creditPlayer.getName(), plugin.getConfig().getString("default-kill-effect"));
                CosmeticsConfig.save();
                CosmeticsConfig.reload();
                activeCosmetic = CosmeticsConfig.get().getString("kill_effect." + creditPlayer.getName());
            }
            plugin.getLogger().log(Level.INFO, e.getMessage());
        } // If something in the config is wrong; switch statement will pick default and do nothing
        Location killLocation = player.getLocation();
        Location killerLocation = creditPlayer.getLocation();
        switch (activeCosmetic) { // NPE lies, try catch above fixes this
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
            case "cow_rocket" -> {
                cow_rocket(killLocation);
            }
            case "dragon_breath" -> {
                dragon_breath(killLocation);
            }
            case "diamond_rain" -> {
                diamond_rain(killLocation);
            }
            default -> {
                //Idk how you would end up here: Do nothing
            }

        }

    }

    public static void firework(Location location) {
        location = location.add(0,1.4,0);
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
                    location.add(0,i,0),
                    20,
                    Material.REDSTONE_BLOCK.createBlockData()
            );
        }

    }

    public static void ritual(Location location) {
        for (int i = 0; i < 360; i += 20) {
            Location flameloc = location.add(0,1.2,0);
            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 0.7);
            flameloc.setX(flameloc.getX() + Math.sin(i) * 0.7);
            location.getWorld().spawnParticle(Particle.FLAME, flameloc, 10, 0, 0, 0, 0.001);
        }
        for (int i = 0; i < 360; i += 10) {
            Location flameloc = location.add(0,0.6,0);
            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 1.5);
            flameloc.setX(flameloc.getX() + Math.sin(i) * 1.5);
            location.getWorld().spawnParticle(Particle.FLAME, flameloc, 10, 0, 0, 0, 0.001);
        }
        for (int i = 0; i < 360; i += 5) {
            Location flameloc = location;
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

    public static void cow_rocket(Location location) {
        Entity cow = location.getWorld().spawnEntity(location, EntityType.COW);
        cow.setGravity(false);
        cow.teleport(location.add(0,2,0));

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run () {
                cow.teleport(location.add(0,0.2,0));
                cow.getLocation().getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1, i*0.1f);
                cow.getLocation().getWorld().spawnParticle(Particle.GLOW_SQUID_INK, location, 2,0.1, 0.1, 0.1, 0.001);
                if (i >= 16){
                    this.cancel();
                    cow.remove();
                    cow.getLocation().createExplosion(0, false, false);
                }
                i++;

            }
        }.runTaskTimer(plugin, 0, 1);

    }

    private static void dragon_breath(Location location) {

        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 500, 0.2, 1, 0.2, 0.05);
        location.getWorld().playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
    }

    private static void diamond_rain(Location location){

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                ItemStack is = new ItemStack(Material.DIAMOND, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(i)); // Change display name so they do not stack
                is.setItemMeta(im);

                Item dropped = location.getWorld().dropItemNaturally(location, is);
                dropped.getPersistentDataContainer().set(new NamespacedKey(plugin, "cosmetic"), PersistentDataType.BOOLEAN, true);
                // PDC attached to entity instead if itemstack, these (apparently) do not transfer between states
                location.getWorld().playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                Bukkit.getScheduler().runTaskLater(plugin, dropped::remove, 100L); // IDEA lambda magic

                if (i >= 10){
                    this.cancel();
                }
                i++;

            }
        }.runTaskTimer(plugin, 0, 2);

    }

}
