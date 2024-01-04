package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PickupListener implements Listener {
    Plugin plugin;
    public PickupListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent e){

        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        Item itemOnGround = e.getItem();
        PersistentDataContainer itemContainer = itemOnGround.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cosmetic");
        if (itemContainer.get(key, PersistentDataType.BOOLEAN) == null){
            return;
        }
        if (!(itemContainer.get(key, PersistentDataType.BOOLEAN))){ //lies
            return;
        }
        e.setCancelled(true);


    }
}
