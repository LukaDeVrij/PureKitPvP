package me.lifelessnerd.purekitpvp.combathandlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class DeathHandler implements Listener {

    Plugin plugin;

    public DeathHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        String killer;
        Player player = e.getPlayer();
        if (e.getPlayer().getKiller() != null) {
            killer = e.getPlayer().getKiller().getName();
        }
        else {
            killer = "ENVIRONMENT";
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }
        Bukkit.broadcast(Component.text(player.getName() + " was killed by " + killer));
        //Check Streaks
        //streakChecker();
        e.setCancelled(true);
        player.teleport(new Location(player.getWorld(), 0, 145, 0));
        player.getInventory().clear();
        player.getActivePotionEffects().clear();

        Component mainTitle = Component.text("You died!").color(TextColor.color(230,60,60));
        Component subTitle = Component.text("The last blow was given by " + killer);
        Title title = Title.title(mainTitle, subTitle);
        player.showTitle(title);

        //Get all damage info and do stuff with it
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        PlayerDamageDistribution data = player.getPersistentDataContainer().get(key, new PlayerDamageDistributionDataType());
        String message = "&c&lYou died!";
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Death Recap:"));
        int totalDamageDone = 0;
        for (String hashmapKey : data.damageDistributionMap.keySet()){
            totalDamageDone += data.damageDistributionMap.get(hashmapKey);
        }
        for (String hashmapKey : data.damageDistributionMap.keySet()){
            int damageValue = data.damageDistributionMap.get(hashmapKey);
            double damagePercentage = (double) damageValue / (double) totalDamageDone * 100.0;
            String format = "&7- &a" + hashmapKey + "&7 did &a" + (int) damagePercentage + "%";
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
        }

        // Player was killed, all damage info reset
        player.getPersistentDataContainer().remove(key);

        //player.teleport(new Location(player.getWorld(), 0, 145, 0)); TODO: na testing dit uncommenten
        player.setFireTicks(0);
    }



    @EventHandler
    public void onPlayerCombatHit(EntityDamageByEntityEvent event){
        Player player = null;
        Player damager = null;
        // HAND TO HAND COMBAT
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            //HAND COMBAT
            damager = (Player) event.getDamager();
            player = (Player) event.getEntity();

        } else if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player){

            //Projectile combat
            player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();

            damager = (Player) arrow.getShooter();

        } else {
            //If we end up here idk what happened but nothing relevant (probably a zombie hitting a pig or something)
            return;
        }

        int damageAmount = (int) event.getDamage();

        //Make new object with that player, is mostly empty
        PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
        damageDistrib.lastDamager = damager.getName();


        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }

        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());

        //We get its damageData
        HashMap<String, Integer> damageMap = pulledPlayerData.damageDistributionMap;

        //If the damager has already done damage before, add damageValue to already existing value
        //If damager is new, add damageValue as value
        if (damageMap.containsKey(damager.getName())){
            int value = damageMap.get(damager.getName());
            damageMap.put(damager.getName(), value + damageAmount);
        } else {
            damageMap.put(damager.getName(), damageAmount);
        }

        //Put the edited object back in the player
        player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);


    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event){
        //EVERYTHING WHERE A PLAYER IS NOT INVOLVED
        if (!(event.getEntity() instanceof Player)){
            return;
        }

        Player player = (Player) event.getEntity();

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            //This is handled by onPlayerCombatHit
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
            //This is handled by onPlayerCombatHit or (with snowballs) onSnowballHit
            return;
        }

        int damageAmount = (int) event.getDamage();
        EntityDamageEvent.DamageCause cause = event.getCause();

        //Make new object with that player, is mostly empty
        PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
        damageDistrib.lastDamager = cause.name();


        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }

        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());

        //We get its damageData
        HashMap<String, Integer> damageMap = pulledPlayerData.damageDistributionMap;

        //If the damager has already done damage before, add damageValue to already existing value
        //If damager is new, add damageValue as value
        if (damageMap.containsKey(cause.name())){
            int value = damageMap.get(cause.name());
            damageMap.put(cause.name(), value + damageAmount);
        } else {
            damageMap.put(cause.name(), damageAmount);
        }

        //Put the edited object back in the player
        player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);


    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event){

        if (!(event.getHitEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getHitEntity();

        if (event.getEntity() instanceof Snowball | event.getEntity() instanceof Egg){
            player.sendMessage("SNOWBALL/EGG by" + event.getEntity().getShooter());
        }

    }

}
