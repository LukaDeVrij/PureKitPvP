package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;

public class ChickenPrevention implements Listener {
    public Plugin plugin;
    public ChickenPrevention(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChickenSpawn(EntitySpawnEvent event) {

        Entity spawned = event.getEntity();

        if (!(spawned.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        if (spawned.getType().equals(EntityType.CHICKEN)){
            event.setCancelled(true);
            return;
        }
    }

}
// This class prevents chicken from spawning -> some kits may have eggs for combo material, they would spawn chicken, not anymore
