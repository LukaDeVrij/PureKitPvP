package me.lifelessnerd.purekitpvp.combathandlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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


        Player player = e.getPlayer();


        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        //Check Streaks if player was lastPlayerBlow
        //streakChecker();

        //Reset player
        e.setCancelled(true);
        player.teleport(new Location(player.getWorld(), 0, 145, 0));
        player.getInventory().clear();
        player.getActivePotionEffects().clear();


        //Get all damage info and print to killed player
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

        //Broadcasting and stuff
        // use 'lastPlayerBlow' to determine kill message and such
        DamageCauseLib damageCauseLib = new DamageCauseLib();
        String deathMessage = player.getName();

        String credit = " was killed by a mysterious force";
        String lastEnvironmentHit;

        //Check who should get credit
        if (data.lastPlayerDamager == null && data.lastOtherDamager != null){
            //This means the player was completely killed by environment, no player killer
            lastEnvironmentHit = data.lastOtherDamager;
            credit = damageCauseLib.deathMessages.get(lastEnvironmentHit); //TODO: not grabbing idk why?

        }
        if (data.lastPlayerDamager != null && data.lastOtherDamager != null){
            //This means player got damage from both
            credit = " was killed by " + data.lastPlayerDamager;

        }
        if (data.lastPlayerDamager != null && data.lastOtherDamager == null){
            //Means player has only been hit by other players
            credit = " was killed by " + data.lastPlayerDamager;

        }

        deathMessage = deathMessage + credit;
        // Death message shows who got credit
        player.sendMessage(deathMessage);
        
        Component mainTitle = Component.text("You died!").color(TextColor.color(230,60,60));
        Title title = Title.title(mainTitle, Component.text(""));
        player.showTitle(title);


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
        damageDistrib.lastPlayerDamager = damager.getName();


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

        pulledPlayerData.lastPlayerDamager = damager.getName();

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




        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            //Make new object with that player, is mostly empty
            PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
            damageDistrib.lastPlayerDamager = cause.name();
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

        pulledPlayerData.lastOtherDamager = cause.name();

        //Put the edited object back in the player
        player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);


    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event){

        if (!(event.getHitEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getHitEntity();
        Player shooter = null;
        if (event.getEntity() instanceof Snowball | event.getEntity() instanceof Egg){
            player.sendMessage("SNOWBALL/EGG by" + (Player) event.getEntity().getShooter());
            shooter = (Player) event.getEntity().getShooter();
        }
        else {
            return;
        }

        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            //Make new object with that player, is mostly empty
            PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
            damageDistrib.lastPlayerDamager = shooter.getName();
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }

        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());




    }

}
