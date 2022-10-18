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
        perks.put("NOTORIETY","10% chance of gaining a sharpness level on each kill\nSharpness will be added to the item held");
        perks.put("ENDERMAGIC", "30% chance of gaining an ender pearl on kill");
        perks.put("SPEEDSTER","Gain 10 seconds of speed 2 after every kill");

        perkIcons.put("JUGGERNAUT" , Material.DIAMOND_CHESTPLATE);
        perkIcons.put("BULLDOZER" , Material.ANVIL);
        perkIcons.put("KNOWLEDGE" , Material.BOOK);
        perkIcons.put("NOTORIETY", Material.DIAMOND_SWORD);
        perkIcons.put("ENDERMAGIC", Material.ENDER_PEARL);
        perkIcons.put("SPEEDSTER", Material.SUGAR);
    }
}
