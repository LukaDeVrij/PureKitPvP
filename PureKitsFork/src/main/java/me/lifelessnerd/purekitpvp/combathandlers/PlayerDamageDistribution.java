package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerDamageDistribution implements Serializable {

    String owner;
    String lastDamager = "Environment"; // Could be name of player or "lava", "fire", "environment"
    HashMap<String, Integer> damageDistributionMap = new HashMap<>();

    public PlayerDamageDistribution(Player owner) {
        this.owner = owner.getName();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
