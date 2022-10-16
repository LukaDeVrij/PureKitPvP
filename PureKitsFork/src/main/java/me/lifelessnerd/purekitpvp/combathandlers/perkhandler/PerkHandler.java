package me.lifelessnerd.purekitpvp.combathandlers.perkhandler;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;

public class PerkHandler {
    public static Plugin plugin;
    public PerkHandler(Plugin plugin){
        PerkHandler.plugin = plugin; //Idk what I did, IDEA carried me
    }

    public static void firePerks(Player player){ //Main class

        String currentKit = PlayerStatsConfig.get().getString(player.getName() + ".current_kit");
        ConfigurationSection perkSection = KitConfig.get().getConfigurationSection("kits." + currentKit + ".perks");

        if (perkSection == null){
            return;
        }
        Set<String> activePerks = perkSection.getKeys(false);
        for(String perk : activePerks){
            System.out.println(perk);
            switch(perk){

                default: //TODO: this always fires for no reason??? (along with the correct case)
                    //plugin.getLogger().log(Level.WARNING, "Perk " + perk + " does not exist!");

                case "JUGGERNAUT":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                    //
                case "BULLDOZER":

                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
                    //
                case "KNOWLEDGE":

                    player.giveExpLevels(1);
            }
        }
    }
}
