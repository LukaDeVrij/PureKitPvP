package me.lifelessnerd.purekitpvp.perks.perkhandler;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PerkHandler {
    public static Plugin plugin;
    public PerkHandler(Plugin plugin){
        PerkHandler.plugin = plugin;
    }

    public static void fireKillPerks(Player player){ //Main class

        String currentKit = PlayerStatsConfig.get().getString(player.getName() + ".current_kit");
        ConfigurationSection perkSection = KitConfig.get().getConfigurationSection("kits." + currentKit + ".perks");

        if (perkSection == null){
            return;
        }
        Set<String> activePerks = perkSection.getKeys(false);
        for(String perk : activePerks){
            //System.out.println(perk);
            switch(perk){
                case "JUGGERNAUT":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                    break;
                case "BULLDOZER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
                    break;
                case "KNOWLEDGE":

                    player.giveExpLevels(1);
                    break;
                case "NOTORIETY":

                    if(Math.random() <= 0.1) {
                        ItemStack weapon = player.getInventory().getItemInMainHand();
                        ItemMeta weaponMeta = weapon.getItemMeta();
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
                case "BLACK MAGIC":

                    if(Math.random() <= 0.3) {
                        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 1);
                        player.getInventory().addItem(pearl);
                        player.sendMessage(ChatColor.YELLOW + "You gained an ender pearl!");
                    }
                    break;
                case "SPEEDSTER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
                    break;

            }
        }
    }

    public static void fireCombatPerks(Player damaged, Player damager){

        String currentKit = PlayerStatsConfig.get().getString(damager.getName() + ".current_kit");
        ConfigurationSection perkSection = KitConfig.get().getConfigurationSection("kits." + currentKit + ".perks");

        if (perkSection == null){
            return;
        }
        Set<String> activePerks = perkSection.getKeys(false);
        for(String perk : activePerks){

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
                    }
                    break;
            }

        }

    }

    public static void fireSnowballPerks(Player damaged, Player shooter) {
        String currentKit = PlayerStatsConfig.get().getString(shooter.getName() + ".current_kit");
        ConfigurationSection perkSection = KitConfig.get().getConfigurationSection("kits." + currentKit + ".perks");

        if (perkSection == null){
            return;
        }
        Set<String> activePerks = perkSection.getKeys(false);
        for(String perk : activePerks){

            switch(perk){
                case "SNOWMAN":
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));
                    break;
                case "DISRUPTOR":
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
                    break;
            }

        }

    }
}
