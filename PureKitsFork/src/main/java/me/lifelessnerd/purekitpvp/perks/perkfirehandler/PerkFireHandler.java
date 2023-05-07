package me.lifelessnerd.purekitpvp.perks.perkfirehandler;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.PerkData;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PerkFireHandler {
    public static Plugin plugin;
    public PerkFireHandler(Plugin plugin){
        PerkFireHandler.plugin = plugin;
    }

    public static void fireKillPerks(Player player){

        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(player.getName());

        if (playerPerks == null){
            return;
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();
        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }
        for(String perk : perks){
            //System.out.println(perk);
            switch(perk){
                case "JUGGERNAUT":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                    break;
                case "BULLDOZER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1));
                    break;
                case "KNOWLEDGE":

                    player.giveExpLevels(1);
                    break;
                case "NOTORIETY":

                    if(Math.random() <= 0.15) {
                        ItemStack weapon = player.getInventory().getItemInMainHand();
                        ItemMeta weaponMeta = weapon.getItemMeta();
                        if (weaponMeta == null){continue;} // If weapon is hand
                        int sharpnessLevel = weaponMeta.getEnchantLevel(Enchantment.DAMAGE_ALL);

                        if (plugin.getConfig().getInt("notoriety-maximum") != -1){
                            // So if the boundary is set to SOME value
                            if (sharpnessLevel >= plugin.getConfig().getInt("notoriety-maximum")){
                                player.sendMessage(ChatColor.YELLOW + "You have reached the maximum Notoriety!");
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1,0);
                                continue;
                            }
                        }
                        weaponMeta.removeEnchant(Enchantment.DAMAGE_ALL);
                        weaponMeta.addEnchant(Enchantment.DAMAGE_ALL, sharpnessLevel + 1, true);
                        weapon.setItemMeta(weaponMeta);
                        player.sendMessage(ChatColor.YELLOW + "Your sharpness level has increased!");
                    }
                    break;
                case "ENDERMAGIC":

                    if(Math.random() <= 0.3) {
                        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 1);
                        player.getInventory().addItem(pearl);
                        player.sendMessage(ChatColor.YELLOW + "You gained an ender pearl!");
                    }
                    break;
                case "SPEEDSTER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                    break;

            }
        }
    }

    public static void fireCombatPerks(Player damaged, Player damager){

        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(damager.getName());

        if (playerPerks == null){
            return;
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();
        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }
        for(String perk : perks){

            switch(perk){
                case "ROBBERY":
                    if(damager.getInventory().getItemInMainHand().getType() != Material.AIR) { // if hit with hand
                        return;
                    }
                    if (Math.random() <= 0.3){
                        ItemStack item = damaged.getInventory().getItemInMainHand();
                        damaged.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                        //Check what slots in inventory (!= hotbar) are empty
                        ArrayList<Integer> airSlots = new ArrayList<Integer>();
                        for (int slot = 9; slot <= 35; slot++){
                            if(damaged.getInventory().getItem(slot) == null){
                                airSlots.add(slot);
                            }
                        }
                        Collections.shuffle(airSlots);
                        if (airSlots.size() != 0){ // this is exploitable, if a player has a full inv, stuff does not change. So else
                            damaged.getInventory().setItem(airSlots.get(0), item);
                        } else { //fixes above exploit
                            int randomNum = ThreadLocalRandom.current().nextInt(9, 35 + 1);
                            ItemStack toBeSwapped = damaged.getInventory().getItem(randomNum);
                            damaged.getInventory().setItem(randomNum, item);
                            damaged.getInventory().setItem(damaged.getInventory().getHeldItemSlot(), toBeSwapped);
                        }
                        // I could have just always swapped, sometimes swap with air
                        // oh well shite
                        damaged.sendMessage(ChatColor.YELLOW + "You were disarmed!");
                    }
                    break;
            }

        }

    }
    public static void fireVampirePerk(EntityDamageByEntityEvent event) {

        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(event.getDamager().getName());
        Player player = (Player) event.getDamager();
        if (playerPerks == null){
            return;
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();
        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }
        for(String perk : perks){

            if (perk.equalsIgnoreCase("VAMPIRE")){

                if (event.isCritical()){
                    //Main logic
                    double damageDealt = event.getDamage();
                    double regen = damageDealt / 2;

                    if (player.getHealth() + regen <= 20){
                        player.setHealth(player.getHealth() + regen);
                    } else {
                        player.setHealth(20);
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1,0);

                }

            }
        }

    }

    public static void fireSnowballPerks(ProjectileHitEvent event, Player damaged, Player shooter) {
        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(shooter.getName());

        if (playerPerks == null){
            return;
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();
        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }

        for(String perk : perks){

            switch(perk){
                case "SNOWMAN":
                    if (event.getEntity() instanceof Snowball) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
                    }
                    break;
                case "DISRUPTOR":
                    if (event.getEntity() instanceof Egg) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
                    }
                    break;
            }

        }

    }
    public static void fireEnderpearlPerks(Player player, PlayerLaunchProjectileEvent event) {
        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(player.getName());

        if (playerPerks == null){
            return;
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();
        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }

        for(String perk : perks){

            switch(perk){
                case "ENDERMAN":

                    Block lookingAt = event.getPlayer().getTargetBlock(50);
                    player.teleport(lookingAt.getLocation());
                    event.setCancelled(true);
                    int epAmount = player.getInventory().getItemInMainHand().getAmount();
                    ItemStack toBeGiven = player.getInventory().getItemInMainHand();
                    toBeGiven.setAmount(epAmount - 1);
                    player.getInventory().setItemInMainHand(toBeGiven);

                    break;

            }

        }


    }

}
