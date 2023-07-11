package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import me.lifelessnerd.purekitpvp.PluginGetter;
import me.lifelessnerd.purekitpvp.combathandlers.libs.DamageCauseLib;
import me.lifelessnerd.purekitpvp.combathandlers.libs.DeathCauseLib;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsCommand;
import me.lifelessnerd.purekitpvp.files.CosmeticsConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.logging.Level;

public class KillMessage {

    public static class create {
        public static String byEnvironment(String player, String lastEnvironmentHit){

            return player + getCosmeticMessage(player, lastEnvironmentHit, false);
        }

        public static String byEnvironmentAndPlayer(String player, String killer, String environmentHit){
            DeathCauseLib deathCauseLib = new DeathCauseLib();
            Set<String> interestingCauses = deathCauseLib.defaultDeathMessages.keySet();
            if(interestingCauses.contains(environmentHit)){
                String preprocessed = player + getCosmeticMessage(player, environmentHit, false);
                return preprocessed.replace("{killer}", killer);
            } else {
                return (player + getCosmeticMessage(player, environmentHit, true)).replace("{killer}", killer);
            }

        }

        public static String byPlayer(String player, String killer){

            String preprocessed = getCosmeticMessage(killer, null, true);
//            System.out.println(preprocessed);
            return player + preprocessed.replace("{killer}", killer);
        }
    }

    public static String getCosmeticMessage(String player, String environmentHit, boolean killMessage) {
        DeathCauseLib deathCauseLib = new DeathCauseLib();

        String activeCosmetic = "default";
        try {
            // This construct is weird; it works tho
            activeCosmetic = CosmeticsConfig.get().getString("kill_message." + player);
            if (activeCosmetic == null) {
                throw new NullPointerException("Player is not defined in Cosmetics config! They have been added with default.");
            }
        } catch (Exception e) {
            String configValue = CosmeticsConfig.get().getString("kill_message." + player);
            // if player is not present in file; add it to config and try again
            if (configValue == null) {
                CosmeticsConfig.get().set("kill_message." + player, "default");
                CosmeticsConfig.save();
                CosmeticsConfig.reload();
                activeCosmetic = CosmeticsConfig.get().getString("kill_effect." + player);
            }
            PluginGetter.Plugin().getLogger().log(Level.INFO, e.getMessage());
        } // If something in the config is wrong; switch statement will pick default and do nothing

        if (killMessage) {
            switch (activeCosmetic) {
                case "flex":
                    int kills = 1;
                    String killsString;
                    try {
                        kills = PlayerStatsConfig.get().getInt(player + ".kills");
                    } catch(Exception e){
                        kills = 1;
                    }
                    killsString = Integer.toString(kills + 1);
                    return (deathCauseLib.flexKillMessages.get(0)).replace("{kills}", killsString);
                case "nerd":
                    return deathCauseLib.nerdKillMessages.get(0);
                case "default":
                    return deathCauseLib.defaultKillMessages.get(0);
            }
        } else {
            // Death message
            switch (activeCosmetic) {
                case "flex":
                    return deathCauseLib.flexDeathMessages.get(environmentHit);
                case "nerd":
                    return deathCauseLib.nerdDeathMessages.get(environmentHit);
                case "default":
                    return deathCauseLib.defaultDeathMessages.get(environmentHit);
            }
        }
        PluginGetter.Plugin().getLogger().warning("Kill Message Cosmetics Error: config file contains faulty values!");
        return "???";
    }
}
