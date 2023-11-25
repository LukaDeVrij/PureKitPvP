package me.lifelessnerd.purekitpvp.files;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.antlr.v4.misc.OrderedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LanguageConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static HashMap<String, Component> lang;

    public static SortedMap<String, Object> defaults;

    public static void setup(){

        defaults = new TreeMap<>();
        lang = new HashMap<>();

        file = new File(Bukkit.getServer().getPluginManager().getPlugin("PureKitPvP").getDataFolder(), "lang.yml");
        if (!(file.exists())){
            try{
                file.createNewFile();
            } catch (IOException e){
                //pain
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);

        // Language defaults
        defaults.put("GENERIC_NO_PERMISSION", "&cYou do not have permission!");
        defaults.put("GENERIC_LACK_OF_ARGS", "&cNot enough arguments!");
        defaults.put("GENERIC_WRONG_ARGS", "%ARG% &cis not a valid argument!");
        defaults.put("GENERIC_WRONG_WORLD", "&cYou can only use this menu in &7%WORLD%");
        defaults.put("PERKS_ALREADY_SELECTED", "&cYou can only change perks when you have no kit selected!");
        defaults.put("PERKS_GUI_TITLE", "&6Perks Menu");
        defaults.put("PERKS_GUI_INFO_TITLE", "&cPerks Info");
        defaults.put("PERKS_GUI_INFO_LORE", """
                &aYou can equip a total of &e5 &aperks total.
                &7Click on a perk slot to choose a perk for that slot.
                &7It will replace any perk currently in that slot.
                &7Perks are abilities that are always active.
                &7Duplicate perks do not stack.
                """);
        defaults.put("PERKS_GUI_BACK_KITS", "&aBack to kits");
        defaults.put("PERKS_GUI_SLOT_TITLE", "&cPerk Slot %SLOT%");
        defaults.put("PERKS_GUI_SLOT_LORE", "&aClick to select a perk for this slot!");
        defaults.put("PERKS_GUI_JUGGERNAUT", """
                &rGain regeneration 1 (5s) after every kill
                """);
        defaults.put("PERKS_GUI_BULLDOZER", """
                &rGain strength 2 (3s) after every kill
                """);
        defaults.put("PERKS_GUI_KNOWLEDGE", """
                &rGain 1 XP level on kill
                """);
        defaults.put("PERKS_GUI_NOTORIETY", """
                &r15% chance of gaining a sharpness level on each kill
                &rwill be added to the item held
                """);
        defaults.put("PERKS_GUI_ENDERMAGIC", """
                &r30% chance of gaining an ender pearl on kill
                """);
        defaults.put("PERKS_GUI_SPEEDSTER", """
                &rGain 10 seconds of speed 2 after every kill
                """);
        defaults.put("PERKS_GUI_ROBBERY", """
                &r30% chance of disarming someone if hit with your fist
                &rThe weapon will be swapped with a slot in the inventory
                """);
        defaults.put("PERKS_GUI_SNOWMAN", """
                &rGive slowness 1 (3s) when you hit someone with a snowball
                """);
        defaults.put("PERKS_GUI_DISRUPTOR", """
                &rGive poison 1 (5s) when you hit someone with an egg
                """);
        defaults.put("PERKS_GUI_ENDERMAN", """
                &rYou can now ride ender pearls; they do no damage
                """);
        defaults.put("PERKS_GUI_VAMPIRE", """
                &rOn critical hits, gain 50% of the damage
                &ryou dealt, as instant health
                """);
        defaults.put("PERKS_GUI_MARKSMAN", """
                &r50% chance of gaining a power level on each bow kill
                &rPower is added to the item held, if it's a bow
                """);
        defaults.put("PERKS_GUI_APOLLO", """
                &rYou get each arrow you hit on a player back
                &rA kill nets one extra arrow of that type
                """);
        defaults.put("PERKS_GUI_ADRENALINE", """
                &rWhen below 6 HP (3 hearts), you get a speed boost
                """);

        defaults.put("KITS_GUI_TITLE", "Kitties");
        defaults.put("KITS_GUI_PREV", "Previous");
        defaults.put("KITS_GUI_NEXT", "Next");


        customFile.addDefaults(defaults);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        } catch (IOException e){
            System.out.println("Cannot save!");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void loadLanguage(){
        // Take data out of file; store it in map
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        for (String key : get().getKeys(false)) {
            lang.put(key, serializer.deserialize(get().getString(key)).decoration(TextDecoration.ITALIC, false));
//            System.out.println(key + ": " + get().getString(key));
        }
    }


}
