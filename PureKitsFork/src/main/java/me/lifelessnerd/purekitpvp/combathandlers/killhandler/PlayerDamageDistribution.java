package me.lifelessnerd.purekitpvp.combathandlers.killhandler;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerDamageDistribution implements Serializable {

    String owner;
    String lastPlayerDamager; // Either null if never hit by player, or player.getName
    String lastOtherDamager; // Either null if never hit by environment, or damageCause.toString
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
