package me.lifelessnerd.purekitpvp.combathandlers;

import me.lifelessnerd.purekitpvp.utils.DoubleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

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


        //Reset player
        e.setCancelled(true);
        player.teleport(new Location(player.getWorld(), 0, 145, 0));
        player.getInventory().clear();
        player.getActivePotionEffects().clear();


        DamageCauseLib damageCauseLib = new DamageCauseLib();

        //Get all damage info and print to killed player
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        PlayerDamageDistribution damageData = player.getPersistentDataContainer().get(key, new PlayerDamageDistributionDataType());
        String message = "&c&lYou died!";
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Death Recap:"));

        int totalDamageDone = 0;
        for (String hashmapKey : damageData.damageDistributionMap.keySet()){
            totalDamageDone += damageData.damageDistributionMap.get(hashmapKey);
        }
        for (String hashmapKey : damageData.damageDistributionMap.keySet()){
            int damageValue = damageData.damageDistributionMap.get(hashmapKey);
            double damagePercentage = (double) damageValue / (double) totalDamageDone * 100.0;

            if (damageCauseLib.damageCauseTranslations.containsKey(hashmapKey)){
                // hashMapKey is a damageCause object
                String damageCauseTranslation = damageCauseLib.damageCauseTranslations.get(hashmapKey);
                String format = "&7-&e" + damageCauseTranslation + "&7 did &a" + (int) damagePercentage + "%";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));

            }
            else {
                String format = "&7- &a" + hashmapKey + "&7 did &a" + (int) damagePercentage + "%";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));

            }
        }

        //Broadcasting and stuff
        // use 'lastPlayerBlow' to determine kill message and such

        String deathMessage = player.getName();

        String credit = " was killed by a mysterious force";
        String lastEnvironmentHit;
        boolean playerInvolved = true;


        //Check who should get credit
        if (damageData.lastPlayerDamager == null && damageData.lastOtherDamager != null){
            //This means the player was completely killed by environment, no player killer
            lastEnvironmentHit = damageData.lastOtherDamager;
            credit = damageCauseLib.deathMessages.get(lastEnvironmentHit);
            playerInvolved = false;

        }
        if (damageData.lastPlayerDamager != null && damageData.lastOtherDamager != null){
            //This means player got damage from both
            credit = damageData.lastPlayerDamager;
            deathMessage += " was killed by ";


        }
        if (damageData.lastPlayerDamager != null && damageData.lastOtherDamager == null){
            //Means player has only been hit by other players
            credit = damageData.lastPlayerDamager;
            deathMessage += " was killed by ";

        }

        deathMessage = deathMessage + credit;
        // Death message shows who got credit, broadcast to every player in pvp world
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld() == player.getWorld()) {
                onlinePlayer.sendMessage(deathMessage);
            }
        }

        
        Component mainTitle = Component.text("You died!").color(TextColor.color(230,60,60));
        Title title = Title.title(mainTitle, Component.text(""));
        player.showTitle(title);

        // get player who assisted from damageCausesMap (if there is a player in there)

        //Get all players for later (to see if DamageCause is a player or not)
        ArrayList<String> pvpPlayers = new ArrayList<>(); // List of all players in pvp world
        for (Player pvpPlayer : Bukkit.getOnlinePlayers()){
            if (pvpPlayer.getWorld() == player.getWorld()){
                pvpPlayers.add(pvpPlayer.getName());
            }
        }

        String highestAssistor = null;
        for (String key2 : damageData.damageDistributionMap.keySet()){
            if (!(key2.equalsIgnoreCase(credit)) && pvpPlayers.contains(key2)){ // Dont count the killer in the assist check
                //Only check players for assist
                if (highestAssistor == null){
                    highestAssistor = key2;
                    //First element is always highest
                }
                if (damageData.damageDistributionMap.get(key2) >= damageData.damageDistributionMap.get(highestAssistor)){
                    highestAssistor = key2;
                    //Check others to see if they are higher
                }
            }
        }

        Player assistor = null;
        if (highestAssistor != null){
            assistor = Bukkit.getPlayerExact(highestAssistor);
            assistor.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aYou assisted in the kill of &7" + player.getName()));
        }

        // Player was killed, all damage info reset
        player.getPersistentDataContainer().remove(key);

        //player.teleport(new Location(player.getWorld(), 0, 145, 0)); TODO: na testing dit uncommenten
        player.setFireTicks(0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5, 1));

        // Give kill credit and stats to killer and player ()
        //TODO: check if player killed himself (i.e. with arrow), if so, don't give kill (to prevent boosting)
        //Vars that can be used: assistor, credit (will need to check if human using pvpPlayers), player

        //Set stats of player
        //If this is the first time the player is killed
        NamespacedKey pvpStatsKey = new NamespacedKey(plugin, "pvpStats");
        if (!(player.getPersistentDataContainer().has(pvpStatsKey,new PlayerStatsDataType()))){
            //Player does not have pvpStatsContainer, creating one with empty data
            player.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), new PlayerStats(player));
        }
        PlayerStats playerStats  = player.getPersistentDataContainer().get(pvpStatsKey, new PlayerStatsDataType());

        playerStats.deaths += 1; // TODO: NPE but i cant figure out why or when triggered
        playerStats.consecutiveKills = 0;
        playerStats.updateRatio();

        player.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), playerStats);


        // If there is a human credit involved
        if (playerInvolved){

            Player creditPlayer = Bukkit.getPlayerExact(credit);

            //If killer first kill
            if (!(creditPlayer.getPersistentDataContainer().has(pvpStatsKey,new PlayerStatsDataType()))){
                //Player does not have pvpStatsContainer, creating one with empty data
                creditPlayer.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), new PlayerStats(creditPlayer));
            }

            PlayerStats killerStats  = creditPlayer.getPersistentDataContainer().get(pvpStatsKey, new PlayerStatsDataType());

            killerStats.kills += 1;
            killerStats.consecutiveKills += 1;
            killerStats.updateRatio();

            creditPlayer.sendActionBar(Component.text("Kills: " + killerStats.kills + "    K/D: " + DoubleUtils.round(killerStats.kdRatio,2)));

            creditPlayer.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), killerStats);

            //KillStreak check
            if (killerStats.consecutiveKills >= 3){
                for (Player pvpPlayer : Bukkit.getOnlinePlayers()){
                    if (pvpPlayer.getWorld() == player.getWorld()){

                        pvpPlayer.sendMessage(ChatColor.RED + credit + ChatColor.YELLOW + " is on a KILLING STREAK of " + ChatColor.RED + killerStats.consecutiveKills);

                    }
                }
            }
        }

        //Check if there is an assistor
        if (assistor != null){

            //If players first assist
            if (!(assistor.getPersistentDataContainer().has(pvpStatsKey,new PlayerStatsDataType()))){
                //Player does not have pvpStatsContainer, creating one with empty data
                assistor.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), new PlayerStats(assistor));
            }

            PlayerStats assistorStats  = assistor.getPersistentDataContainer().get(pvpStatsKey, new PlayerStatsDataType());
            /*/ TODO: Ik denk dat de container wel bestaat, maar dat de inhoud null is, kan gefixt worden met nullcheck en dan als null: gelijk zetten aan een empty object
             zelfde als ik de .has check hierboven /*/
            assistorStats.assists += 1;

            assistor.getPersistentDataContainer().set(pvpStatsKey, new PlayerStatsDataType(), assistorStats);

            assistor.sendActionBar(Component.text("Assists: " + assistorStats.assists + "    K/D: " + DoubleUtils.round(assistorStats.kdRatio, 2)));

        }

        player.sendActionBar(Component.text("Deaths: " + playerStats.deaths + "    K/D: " + DoubleUtils.round(playerStats.kdRatio, 2)));



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

        } else if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {

            //Projectile combat
            player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();

            damager = (Player) arrow.getShooter();
        } else if (event.getDamager() instanceof ThrownPotion && event.getEntity() instanceof Player){

            //Splash potion threw (only potions that do damage, only those are seen by this event)
            player = (Player) event.getEntity();
            ThrownPotion potion = (ThrownPotion) event.getDamager();

            damager = (Player) potion.getShooter(); //TODO: test if this works

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
