package me.lifelessnerd.purekitpvp.combathandlers.mobhandler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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

        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))  {
            return;

        }

        if (!(player.getInventory().getItemInMainHand().getType().toString().contains("_SPAWN_EGG"))){
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
        if (spawnedEntity instanceof Monster){
            Player closestPlayer = getNearestPlayer(player);
            ((Monster) spawnedEntity).setTarget(closestPlayer);
            System.out.println(spawnedEntity + "'s target set to " + closestPlayer);
            spawnedEntity.customName(Component.text(player.getName() + "'s " + spawnedEntity.getType().name()));
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
    // Stolen from https://www.spigotmc.org/threads/how-do-i-get-the-nearest-player.506654/
    public static @Nullable Player getNearestPlayer(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        ArrayList<Player> playersInWorld = new ArrayList<>(world.getEntitiesByClass(Player.class));
        if(playersInWorld.size()==1) return null;
        playersInWorld.remove(player);
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.get(0);
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
