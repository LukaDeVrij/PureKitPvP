package me.lifelessnerd.purekitpvp.combathandlers.leveling;

import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerLeveling {

    public static void createLevelXPPath(String playerName){

        FileConfiguration playerStats = PlayerStatsConfig.get();
        if (!(playerStats.isSet(playerName + ".level"))){

            playerStats.set(playerName + ".level", 1);

        }
        if (!(playerStats.isSet(playerName + ".xp"))){

            playerStats.set(playerName + ".xp", 1);

        }
        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();


    }

    public static void addExperience(Player player, int amount, String reason){

        String playerName = player.getName();
        FileConfiguration playerStats = PlayerStatsConfig.get();
        int currentValue = playerStats.getInt(playerName + ".xp");
        playerStats.set(playerName + ".xp", currentValue + amount);

        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();

        player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', "&a+" + amount + " XP" + " &7(" + reason + ")"));

    }

    public static void updateLevels(){

        FileConfiguration playerStats = PlayerStatsConfig.get();

        for (String key : playerStats.getKeys(false)){

            int xpValue = playerStats.getInt(key + ".xp");
            int level = xpValue / 100;
            if (!(level == playerStats.getInt(key + ".level"))){
                Player player = Bukkit.getPlayerExact(key);
                player.sendMessage(LanguageConfig.lang.get(LanguageKey.STATS_LEVEL_UP.toString()).
                        replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(level))));

            }
            playerStats.set(key + ".level", level);

        }

        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();


    }

}
