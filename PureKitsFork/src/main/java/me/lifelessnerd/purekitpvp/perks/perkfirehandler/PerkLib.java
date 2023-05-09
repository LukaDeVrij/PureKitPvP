package me.lifelessnerd.purekitpvp.perks.perkfirehandler;

import org.bukkit.Material;

import java.util.HashMap;

public class PerkLib {

    public HashMap<String, String> perks = new HashMap<>();

    public HashMap<String, Material> perkIcons = new HashMap<>();

    public PerkLib(){
        perks.put("JUGGERNAUT", "Gain regeneration 1 (5s) after every kill");
        perks.put("BULLDOZER", "Gain strength 2 (3s) after every kill");
        perks.put("KNOWLEDGE", "Gain 1 XP level on kill");
        perks.put("NOTORIETY","15% chance of gaining a sharpness level on each kill\nSharpness will be added to the item held");
        perks.put("ENDERMAGIC", "30% chance of gaining an ender pearl on kill");
        perks.put("SPEEDSTER","Gain 10 seconds of speed 2 after every kill");
        perks.put("ROBBERY","30% chance of disarming someone if hit with your fist\nThe weapon will be swapped with a slot in the inventory");
        perks.put("SNOWMAN","Give slowness 1 (3s) when you hit someone with a snowball");
        perks.put("DISRUPTOR","Give poison 1 (5s) when you hit someone with an egg");
        perks.put("ENDERMAN", "Ender pearls behave differently, teleporting on\nclick, to the location you are looking at, max 50 blocks away\nThey do no damage");
        perks.put("VAMPIRE", "On critical hits, gain 50% of the damage\nyou dealt, as instant health");
        perks.put("MARKSMAN", "50% chance of gaining a power level on each bow kill\nPower is added to the item held, if it's a bow");
        perks.put("APOLLO", "You get each arrow you hit on a player back\nA kill nets one extra arrow of that type");

        perkIcons.put("JUGGERNAUT" , Material.DIAMOND_CHESTPLATE);
        perkIcons.put("BULLDOZER" , Material.ANVIL);
        perkIcons.put("KNOWLEDGE" , Material.BOOK);
        perkIcons.put("NOTORIETY", Material.DIAMOND_SWORD);
        perkIcons.put("ENDERMAGIC", Material.ENDER_PEARL);
        perkIcons.put("SPEEDSTER", Material.SUGAR);
        perkIcons.put("ROBBERY", Material.IRON_BARS);
        perkIcons.put("SNOWMAN", Material.SNOWBALL);
        perkIcons.put("DISRUPTOR", Material.EGG);
        perkIcons.put("ENDERMAN", Material.ENDER_EYE);
        perkIcons.put("VAMPIRE", Material.REDSTONE);
        perkIcons.put("MARKSMAN", Material.BOW);
        perkIcons.put("APOLLO", Material.ARROW);
    }
}
