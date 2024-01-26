package me.lifelessnerd.purekitpvp.combathandlers.killhandler;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class Suicide implements CommandExecutor {
    Plugin plugin;

    public Suicide(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){

            return true;
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return true;
        }

        if (!(plugin.getConfig().getBoolean("suicide-command"))){
            player.sendMessage("This is disabled.");
            return true;
        }

        for(PotionEffect effect:player.getActivePotionEffects()){
        //Now use the method Player#removePotionEffect to remove the potion effect. This method accepts a PotionEffectType, so we need to get the type of the effect variable, and then remove it:
            player.removePotionEffect(effect.getType());
        }

        try {
            NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
            if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
                //Player does not have dataContainer, creating one with data from this hit
                //Make new object with that player, is mostly empty
                PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);

                player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
            }
            PlayerDamageDistribution damageData = player.getPersistentDataContainer().get(key, new PlayerDamageDistributionDataType());
            damageData.lastOtherDamager = "SUICIDE"; // NPE lies
            damageData.damageDistributionMap.put("SUICIDE", 100000);
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageData);

        }  catch (Exception e){
            System.out.println(e.getMessage());
        }

        player.setHealth(0);
        player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1));


        return true;
    }
}
