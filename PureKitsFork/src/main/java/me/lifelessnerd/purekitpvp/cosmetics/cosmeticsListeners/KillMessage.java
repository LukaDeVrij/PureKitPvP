package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners;

import me.lifelessnerd.purekitpvp.combathandlers.libs.DeathCauseLib;

import java.util.ArrayList;
import java.util.List;

public class KillMessage {

    public static class create {
        public static String byEnvironment(String player, String lastEnvironmentHit){

            return "";
        }

        public static String byEnvironmentAndPlayer(String player, String killer, String environmentHit){
            DeathCauseLib deathCauseLib = new DeathCauseLib();
            String[] interestingCauses = deathCauseLib.deathMessages.keySet().toArray(new String[0]);

            return "";
        }

        public static String byPlayer(String player, String killer){

            return "";
        }
    }



}
