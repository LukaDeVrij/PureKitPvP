package me.lifelessnerd.purekitpvp.combatHandler.mobHandler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MobRemover {

    public static void removeMobs(Player player){

        for (Entity entity : OnPlayerSpawnMob.mobOwners.keySet()){
            if (OnPlayerSpawnMob.mobOwners.get(entity) == player){

                entity.remove();

            }
        }

    }
}
