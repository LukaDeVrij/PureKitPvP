package me.lifelessnerd.purekitpvp.combathandlers.mobhandler;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MobRemover {


    public static void removeMobs(Player player){

        for (Entity entity : OnPlayerSpawnMob.mobOwners.keySet()){
            if (OnPlayerSpawnMob.mobOwners.get(entity) == player){

                entity.remove();

            }
        }

    }
}
