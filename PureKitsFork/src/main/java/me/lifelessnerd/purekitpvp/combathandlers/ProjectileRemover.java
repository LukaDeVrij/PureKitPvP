package me.lifelessnerd.purekitpvp.combathandlers;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectileRemover implements Listener {
    Plugin plugin;
    static HashMap<Projectile, Player> projectiles;

    public ProjectileRemover(Plugin plugin) {
        this.plugin = plugin;
        projectiles = new HashMap<>();
    }

    @EventHandler
    public void onProjectileLaunch(PlayerLaunchProjectileEvent e){
        Player player = e.getPlayer();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        addToHashMap(e.getProjectile(), player);


    }
    @EventHandler
    public void playerShootArrow(EntityShootBowEvent e){
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        addToHashMap((Projectile) e.getProjectile(), player);
        // Cast? What can be shot with a bow that isn't a projectile?!
    }

    private void addToHashMap(Projectile proj, Player player) {


        projectiles.put(proj, player);

        new BukkitRunnable() {
            @Override
            public void run() {
                projectiles.remove(proj, player);
//                System.out.println("removed something: now " + projectiles.size());
            }
        }.runTaskLater(plugin, 300L);
    }

    public static void removeExistingProjectiles(Player player){
        try {
            for (Projectile projectile : projectiles.keySet()){
                if (projectiles.get(projectile).equals(player) && !(projectile.isDead())){
//                    System.out.println("removing things");
//                    System.out.println(projectiles.size());
                    projectile.remove();
                }

            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
