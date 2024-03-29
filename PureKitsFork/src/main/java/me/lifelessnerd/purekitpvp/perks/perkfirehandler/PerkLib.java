package me.lifelessnerd.purekitpvp.perks.perkfirehandler;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.HashMap;

public class PerkLib {

    public HashMap<String, Component> perks = new HashMap<>();

    public HashMap<String, Material> perkIcons = new HashMap<>();

    public PerkLib(){

        perks.put("JUGGERNAUT", LanguageConfig.lang.get("PERKS_PERK_JUGGERNAUT"));
        perks.put("BULLDOZER", LanguageConfig.lang.get("PERKS_PERK_BULLDOZER"));
        perks.put("KNOWLEDGE", LanguageConfig.lang.get("PERKS_PERK_KNOWLEDGE"));
        perks.put("NOTORIETY", LanguageConfig.lang.get("PERKS_PERK_NOTORIETY"));
        perks.put("ENDERMAGIC", LanguageConfig.lang.get("PERKS_PERK_ENDERMAGIC"));
        perks.put("SPEEDSTER",LanguageConfig.lang.get("PERKS_PERK_SPEEDSTER"));
        perks.put("ROBBERY",LanguageConfig.lang.get("PERKS_PERK_ROBBERY"));
        perks.put("SNOWMAN",LanguageConfig.lang.get("PERKS_PERK_SNOWMAN"));
        perks.put("DISRUPTOR",LanguageConfig.lang.get("PERKS_PERK_DISRUPTOR"));
        perks.put("ENDERMAN", LanguageConfig.lang.get("PERKS_PERK_ENDERMAN"));
        perks.put("VAMPIRE", LanguageConfig.lang.get("PERKS_PERK_VAMPIRE"));
        perks.put("MARKSMAN", LanguageConfig.lang.get("PERKS_PERK_MARKSMAN"));
        perks.put("APOLLO", LanguageConfig.lang.get("PERKS_PERK_APOLLO"));
        perks.put("ADRENALINE", LanguageConfig.lang.get("PERKS_PERK_ADRENALINE"));



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
        perkIcons.put("ADRENALINE", Material.GHAST_TEAR);
    }
}
