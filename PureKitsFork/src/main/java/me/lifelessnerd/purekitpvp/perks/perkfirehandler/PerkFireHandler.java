package me.lifelessnerd.purekitpvp.perks.perkfirehandler;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.PerkData;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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

    public static Set<String> getPerks(Player player) {
        ConfigurationSection playerPerks = PerkData.get().getConfigurationSection(player.getName());

        if (playerPerks == null){
            return new HashSet<>(); // No perks? Return empty set; this way nothing happens
        }

        Set<String> perkSlots = playerPerks.getKeys(false);
        Set<String> perks = new HashSet<>();

        for (String perkSlot : perkSlots){
            perks.add(playerPerks.getString(perkSlot));
        }
        return perks;
    }

    public static void fireKillPerks(Player player){

        Set<String> perks = getPerks(player);

        for(String perk : perks){
            //System.out.println(perk);
            switch(perk){
                case "JUGGERNAUT":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                    break;
                case "BULLDOZER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 1));
                    break;
                case "KNOWLEDGE":

                    player.giveExpLevels(1);
                    break;
                case "NOTORIETY":

                    ItemStack weapon = player.getInventory().getItemInMainHand();
                    ItemMeta weaponMeta = weapon.getItemMeta();
                    if (weaponMeta == null){continue;} // If weapon is hand TODO maybe - check if it is a weapon?
                    if (weapon.getType() == Material.BOW){continue;} // meh
                    int sharpnessLevel = weaponMeta.getEnchantLevel(Enchantment.SHARPNESS);

                    double rnd = Math.random();
                    if(rnd * 100 <= (100 - plugin.getConfig().getInt("notoriety-chance-decrease") * sharpnessLevel)) {
                        if (plugin.getConfig().getInt("notoriety-maximum") != -1){
                            // So if the boundary is set to SOME value
                            if (sharpnessLevel >= plugin.getConfig().getInt("notoriety-maximum")){
                                player.sendMessage(LanguageConfig.lang.get("PERKS_PERK_NOTORIETY_MAX"));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1,0);
                                continue;
                            }
                        }
                        weaponMeta.removeEnchant(Enchantment.SHARPNESS);
                        weaponMeta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel + 1, true);
                        weapon.setItemMeta(weaponMeta);
                        player.sendMessage(LanguageConfig.lang.get("PERKS_PERK_NOTORIETY_UPGRADE").
                                replaceText(ComponentUtils.replaceConfig("%CHANCE%",
                                String.valueOf(100 - plugin.getConfig().getInt("notoriety-chance-decrease") * sharpnessLevel))));
                    }
                    break;
                case "ENDERMAGIC":

                    if(Math.random() <= 0.3) {
                        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 1);
                        player.getInventory().addItem(pearl);
                        player.sendMessage(LanguageConfig.lang.get("PERKS_PERK_ENDERMAGIC_OCCUR"));
                    }
                    break;
                case "SPEEDSTER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                    break;
                case "MARKSMAN":

                    ItemStack weaponMarksman = player.getInventory().getItemInMainHand();
                    ItemMeta weaponMarksmanMeta = weaponMarksman.getItemMeta();
                    if (weaponMarksman.getType() != Material.BOW){continue;}
                    if (weaponMarksmanMeta == null){continue;} // If weapon is hand
                    int powerLevel = weaponMarksmanMeta.getEnchantLevel(Enchantment.POWER);

                    double rnd2 = Math.random();
                    if(rnd2 * 100 <= (100 - plugin.getConfig().getInt("notoriety-chance-decrease") * powerLevel)) {

                        if (plugin.getConfig().getInt("marksman-maximum") != -1){
                            // So if the boundary is set to SOME value
                            if (powerLevel >= plugin.getConfig().getInt("marksman-maximum")){
                                player.sendMessage(LanguageConfig.lang.get("PERKS_PERK_MARKSMAN_MAX"));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1,0);
                                continue;
                            }
                        }
                        weaponMarksmanMeta.removeEnchant(Enchantment.POWER);
                        weaponMarksmanMeta.addEnchant(Enchantment.POWER, powerLevel + 1, true);
                        weaponMarksman.setItemMeta(weaponMarksmanMeta);
                        player.sendMessage(LanguageConfig.lang.get("PERKS_PERK_MARKSMAN_UPGRADE").
                                replaceText(ComponentUtils.replaceConfig("%CHANCE%",
                                String.valueOf(100 - plugin.getConfig().getInt("notoriety-chance-decrease") * powerLevel))));
                    }
                    break;

            }
        }
    }

    public static void fireRangedPerks(EntityDamageByEntityEvent event, Player damager){

        Set<String> perks = getPerks(damager);

        for(String perk : perks){
            //System.out.println(perk);
            switch(perk){
                case "APOLLO":
                    // Event is already Arrow on Player; damager has already been extracted out of the event
                    if (event.getDamager() instanceof SpectralArrow arrow){
                        ItemStack arrowItem = arrow.getItemStack();
                        arrowItem.setAmount(1);
                        damager.getInventory().addItem(arrowItem);
                    } else {
                        Arrow arrow = (Arrow) event.getDamager();
                        ItemStack arrowItem = arrow.getItemStack();
                        arrowItem.setAmount(1);
                        damager.getInventory().addItem(arrowItem);
                    }

                    damager.playSound(damager.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,0);
                    break;
            }
        }

    }
    public static void fireCombatPerks(Player damaged, Player damager){

        Set<String> perks = getPerks(damager);

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
                        damaged.sendMessage(LanguageConfig.lang.get("PERKS_PERK_ROBBERY_DISARMED").
                                replaceText(ComponentUtils.replaceConfig("%PLAYER%", damager.getName())));
                        damager.sendMessage(LanguageConfig.lang.get("PERKS_PERK_ROBBERY_DISARMER").
                                replaceText(ComponentUtils.replaceConfig("%PLAYER%", damaged.getName())));
                    }
                    break;
            }

        }

    }
    public static void fireVampirePerk(EntityDamageByEntityEvent event) {

        Player player = (Player) event.getDamager();
        Set<String> perks = getPerks(player);

        for(String perk : perks){

            if (perk.equalsIgnoreCase("VAMPIRE")){

                if (event.isCritical()){
                    //Main logic
                    double damageDealt = event.getDamage();
                    double regen = damageDealt / 2;
                    double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
//                    System.out.println(maxHealth);
                    if (player.getHealth() + regen <= maxHealth){
                        player.setHealth(player.getHealth() + regen);
                    } else {
                        player.setHealth(maxHealth);
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1,0);

                }

            }
        }

    }

    public static void fireSnowballPerks(ProjectileHitEvent event, Player damaged, Player shooter) {

        Set<String> perks = getPerks(shooter);

        for(String perk : perks){

            switch(perk){
                case "SNOWMAN":
                    if (event.getEntity() instanceof Snowball) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 0));
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

        Set<String> perks = getPerks(player);

        for(String perk : perks){

            switch(perk){
                case "ENDERMAN":

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (Entity en : event.getPlayer().getNearbyEntities(2, 2, 2)) {
                                //This has to be here; event.getProjectile does not work; idk why https://www.spigotmc.org/threads/ride-enderpearl.263710/

                                if (en.getType() == EntityType.ENDER_PEARL) {

                                    en.addPassenger(event.getPlayer());

                                }
                            }
                        }
                    }.runTaskLater(plugin, 1);

                    break;

            }

        }


    }

    public static void fireHealthPerks(Player player){

        Set<String> perks = getPerks(player);

        for(String perk : perks) {

            switch (perk) {
                case "ADRENALINE":

                    if (player.getHealth() >= 6){

                        player.setWalkSpeed(0.2f);
                        break;
                    }

                    player.setWalkSpeed(0.3f);

            }
        }
    }

    public static void fireEnderpearlDamagePerks(Player player, EntityDamageByEntityEvent event){
        Set<String> perks = getPerks(player);

        for(String perk : perks) {

            switch (perk) {
                case "ENDERMAN":

                    event.setCancelled(true); // Negates not only the damage but also the tick
                    break;

            }
        }
    }

}
