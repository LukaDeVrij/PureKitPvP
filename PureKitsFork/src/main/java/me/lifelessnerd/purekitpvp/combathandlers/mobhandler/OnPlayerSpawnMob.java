package me.lifelessnerd.purekitpvp.combathandlers.mobhandler;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class OnPlayerSpawnMob implements Listener {
    Plugin plugin;
    public static HashMap<Entity, Player> mobOwners = new HashMap<>();
    public static boolean toBeCancelled = false;

    public OnPlayerSpawnMob(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerUseSpawnEgg(PlayerInteractEvent e){

        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            return;

        }

        Component itemDisplayName = player.getInventory().getItemInMainHand().displayName();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        String itemName = serializer.serialize(itemDisplayName);

        if (!(itemName.contains("Spawn Egg"))) {
            return;
        }

        String[] entityToBeSpawnedList = player.getInventory().getItemInMainHand().getType().toString().split("_SPAWN_EGG");
        String entityToBeSpawned = entityToBeSpawnedList[0];

        EntityType entityTypeTBS = EntityType.valueOf(entityToBeSpawned);
        Location lookPlace = e.getInteractionPoint();
        Entity spawnedEntity = player.getWorld().spawnEntity(lookPlace, entityTypeTBS);

        if (spawnedEntity instanceof Horse){
            ((Horse) spawnedEntity).setTamed(true);
            ((Horse) spawnedEntity).getInventory().addItem(new ItemStack(Material.SADDLE));

        }

        toBeCancelled = true;

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                toBeCancelled = false;
            }
        }, 10L);

        mobOwners.put(spawnedEntity, player);
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        player.getInventory().remove(heldItem);


    }

    @EventHandler
    public void playerSpawnCreature(CreatureSpawnEvent e) {

        Entity entity = e.getEntity();

        if (!(entity.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if(toBeCancelled){
            e.setCancelled(true);
        }
    }

}
