package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand;

import org.bukkit.Material;

import java.util.HashMap;

public class CosmeticsLib {

    public HashMap<String, Material> killEffects = new HashMap<>();
    public HashMap<String, Material> messageEffects = new HashMap<>();
    public HashMap<String, Material> trailEffects = new HashMap<>();

    public CosmeticsLib() {

        killEffects.put("firework", Material.FIREWORK_ROCKET);
        killEffects.put("blood_explosion", Material.REDSTONE);
        killEffects.put("ritual", Material.FIRE_CHARGE);
        killEffects.put("tornado", Material.STRING);

//        messageEffects.put("JUGGERNAUT", Material.DIAMOND_CHESTPLATE);
//        messageEffects.put("BULLDOZER", Material.ANVIL);
//        messageEffects.put("KNOWLEDGE", Material.BOOK);
//        messageEffects.put("NOTORIETY", Material.DIAMOND_SWORD);

        trailEffects.put("none", Material.BARRIER);
        trailEffects.put("flame", Material.FIRE_CHARGE);
        trailEffects.put("sparkle", Material.END_ROD);
        trailEffects.put("water_drip", Material.LILY_PAD);
        trailEffects.put("heart", Material.GOLD_INGOT);
        trailEffects.put("firework", Material.FIREWORK_ROCKET);
        trailEffects.put("totem", Material.TOTEM_OF_UNDYING);
        trailEffects.put("smoke", Material.CAMPFIRE);

    }
}
