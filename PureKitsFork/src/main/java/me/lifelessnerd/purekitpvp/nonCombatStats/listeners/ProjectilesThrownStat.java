package me.lifelessnerd.purekitpvp.nonCombatStats.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ProjectilesThrownStat implements Listener {
    Plugin plugin;

    public ProjectilesThrownStat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileThrow(PlayerLaunchProjectileEvent event){

        Player player = event.getPlayer();
        Projectile projectile = event.getProjectile();

        FileConfiguration playerStats = PlayerStatsConfig.get();
        String pathPrefix = player.getName();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        if (projectile instanceof EnderPearl){
            if (playerStats.isSet(pathPrefix + ".ender_pearls_thrown")){
                int value = playerStats.getInt(pathPrefix + ".ender_pearls_thrown");
                playerStats.set(pathPrefix + ".ender_pearls_thrown", value + 1);
            } else {
                playerStats.set(pathPrefix + ".ender_pearls_thrown", 1);
            }
        } else if (projectile instanceof Egg){
            if (playerStats.isSet(pathPrefix + ".eggs_thrown")){
                int value = playerStats.getInt(pathPrefix + ".eggs_thrown");
                playerStats.set(pathPrefix + ".eggs_thrown", value + 1);
            } else {
                playerStats.set(pathPrefix + ".eggs_thrown", 1);
            }
        } else if (projectile instanceof Snowball){
            if (playerStats.isSet(pathPrefix + ".snowballs_thrown")){
                int value = playerStats.getInt(pathPrefix + ".snowballs_thrown");
                playerStats.set(pathPrefix + ".snowballs_thrown", value + 1);
            } else {
                playerStats.set(pathPrefix + ".snowballs_thrown", 1);
            }
        } else if (projectile instanceof ThrownPotion){
            if (playerStats.isSet(pathPrefix + ".potions_thrown")){
                int value = playerStats.getInt(pathPrefix + ".potions_thrown");
                playerStats.set(pathPrefix + ".potions_thrown", value + 1);
            } else {
                playerStats.set(pathPrefix + ".potions_thrown", 1);
            }
        } else if (projectile instanceof ThrownExpBottle){
            if (playerStats.isSet(pathPrefix + ".xp_potions_thrown")){
                int value = playerStats.getInt(pathPrefix + ".xp_potions_thrown");
                playerStats.set(pathPrefix + ".xp_potions_thrown", value + 1);
            } else {
                playerStats.set(pathPrefix + ".xp_potions_thrown", 1);
            }
        }



        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();

    }
}
