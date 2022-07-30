package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerStats implements Serializable {

    String owner;

    HashMap<String, Integer> perPlayerKills = new HashMap<>();
    int deaths;
    int kills;
    int consecutiveKills;
    int assists;
    double kdRatio;

    public PlayerStats(Player owner) {
        this.owner = owner.getName();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void updateRatio(){
        kdRatio = (double) kills / (double) deaths;
    }

}
