package me.lifelessnerd.purekitpvp.combatHandler;

import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkFireHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;

public class HealthEvents implements Listener {
    public static Plugin plugin;
    public HealthEvents(Plugin plugin){
        HealthEvents.plugin = plugin;
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event){

        if (!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        PerkFireHandler.fireHealthPerks(player);


    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        PerkFireHandler.fireHealthPerks(player);
    }

}
