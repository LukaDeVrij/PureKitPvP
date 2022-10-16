package me.lifelessnerd.purekitpvp.combathandlers.perkhandler;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;

public class PerkLib {

    public HashMap<String, String> perks = new HashMap<>();

    public HashMap<String, Material> perkIcons = new HashMap<>();

    public PerkLib(){
        perks.put("JUGGERNAUT", "Gain regeneration 1 (5s) after every kill");
        perks.put("BULLDOZER", "Gain strength 2 (5s) after every kill");
        perks.put("KNOWLEDGE", "Gain 1 XP level on kill");

        perkIcons.put("JUGGERNAUT" , Material.DIAMOND_CHESTPLATE);
        perkIcons.put("BULLDOZER" , Material.ANVIL);
        perkIcons.put("KNOWLEDGE" , Material.BOOK);

    }
}
