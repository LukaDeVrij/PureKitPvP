package me.lifelessnerd.purekitpvp.combathandlers.mobhandler;

import me.lifelessnerd.purekitpvp.PluginGetter;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

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

        EntityType entityTypeTBS;
        ItemStack item = player.getInventory().getItemInMainHand();
        boolean customMob = false;
        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "custom_mob_id"), PersistentDataType.STRING)){
            // Custom Mob has been attempted to spawn
            String customMobId = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "custom_mob_id"), PersistentDataType.STRING);
            String mobType = MobSpawnConfig.get().getString(customMobId + ".type");
            entityTypeTBS = EntityType.valueOf(mobType);
            plugin.getLogger().info("custom mob spawned");
            customMob = true;
        }
        else {
            String[] entityToBeSpawnedList = item.getType().toString().split("_SPAWN_EGG");
            String entityToBeSpawned = entityToBeSpawnedList[0];

            entityTypeTBS = EntityType.valueOf(entityToBeSpawned);

        }
        Location lookPlace = e.getInteractionPoint();
        Entity spawnedEntity = player.getWorld().spawnEntity(lookPlace, entityTypeTBS);

        if (spawnedEntity instanceof Horse) {
            ((Horse) spawnedEntity).setTamed(true);
            ((Horse) spawnedEntity).getInventory().addItem(new ItemStack(Material.SADDLE));

        }
        if (spawnedEntity instanceof Monster) {
            Player closestPlayer = getNearestPlayer(player);
            ((Monster) spawnedEntity).setTarget(closestPlayer);
            System.out.println(spawnedEntity + "'s target set to " + closestPlayer);
            spawnedEntity.customName(Component.text(player.getName() + "'s " + spawnedEntity.getType().name()));
            spawnedEntity.getPersistentDataContainer().set(new NamespacedKey(plugin, "custom_mob"), PersistentDataType.INTEGER, 1);
        }

        if (customMob) {
            String customModId = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "custom_mob_id"), PersistentDataType.STRING);
            System.out.println(customModId);
            if (spawnedEntity instanceof Mob){
                ItemStack mainHand = MobSpawnConfig.get().getItemStack(customModId + ".mainhand");
                ((Mob) spawnedEntity).getEquipment().setItemInMainHand(mainHand);
                ItemStack offHand = MobSpawnConfig.get().getItemStack(customModId + ".offhand");
                ((Mob) spawnedEntity).getEquipment().setItemInOffHand(offHand);
                ItemStack boots = MobSpawnConfig.get().getItemStack(customModId + ".boots");
                ((Mob) spawnedEntity).getEquipment().setBoots(boots);
                ItemStack chestplate = MobSpawnConfig.get().getItemStack(customModId + ".chestplate");
                ((Mob) spawnedEntity).getEquipment().setChestplate(chestplate);
                ItemStack leggings = MobSpawnConfig.get().getItemStack(customModId + ".leggings");
                ((Mob) spawnedEntity).getEquipment().setLeggings(leggings);
                ItemStack helmet = MobSpawnConfig.get().getItemStack(customModId + ".helmet");
                ((Mob) spawnedEntity).getEquipment().setHelmet(helmet);
            }

            if (spawnedEntity instanceof Zombie) {
                if (MobSpawnConfig.get().getBoolean(customModId + ".child")) {
                    ((Zombie) spawnedEntity).setBaby();
                }
                ((Zombie) spawnedEntity).setVisualFire(false);
                ((Zombie) spawnedEntity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 20, false, false));

            }
            if (spawnedEntity instanceof Skeleton) {
                ((Skeleton) spawnedEntity).setVisualFire(false);
                ((Skeleton) spawnedEntity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 20, false, false));
            }
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
        int amount = item.getAmount();
        item.setAmount(amount - 1);
        player.getInventory().remove(item);
        player.getInventory().setItemInMainHand(item);


    }
    // Stolen from https://www.spigotmc.org/threads/how-do-i-get-the-nearest-player.506654/
    public static @Nullable Player getNearestPlayer(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        ArrayList<Player> playersInWorld = new ArrayList<>(world.getEntitiesByClass(Player.class));
        if(playersInWorld.size()==1) return null;
        playersInWorld.remove(player); //Removes player itself
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

    @EventHandler
    public void mobTargetEvent(EntityTargetEvent e){

        Entity entity = e.getEntity();
        Entity target = e.getTarget();
        if (!(entity.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (!(entity instanceof Monster)) {
            return;
        }
        e.setCancelled(true);
        String entityName = entity.getName();
        String playerName = entityName.split("'s")[0]; // Gets name of player who spawned it
        Player player = Bukkit.getPlayerExact(playerName); // TODOX: This is quite janky, requires testing: seems to work!
        Player closestPlayer = getNearestPlayer(player); // NPE has no effect it seems; target becomes null and zombie is fine with that
        ((Monster) entity).setTarget(closestPlayer);
        System.out.println(entity + "'s target set to " + closestPlayer);


    }

}
