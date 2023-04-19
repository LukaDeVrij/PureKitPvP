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
        killEffects.put("fiery_shock", Material.FIRE_CHARGE);
        killEffects.put("tornado", Material.STRING);

//        messageEffects.put("JUGGERNAUT", Material.DIAMOND_CHESTPLATE);
//        messageEffects.put("BULLDOZER", Material.ANVIL);
//        messageEffects.put("KNOWLEDGE", Material.BOOK);
//        messageEffects.put("NOTORIETY", Material.DIAMOND_SWORD);
//
//        trailEffects.put("JUGGERNAUT", Material.DIAMOND_CHESTPLATE);
//        trailEffects.put("BULLDOZER", Material.ANVIL);
//        trailEffects.put("KNOWLEDGE", Material.BOOK);
//        trailEffects.put("NOTORIETY", Material.DIAMOND_SWORD);

    }
}
