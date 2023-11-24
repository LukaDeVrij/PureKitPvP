package me.lifelessnerd.purekitpvp.files;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static HashMap<String, Component> lang;

    public static Map<String, Object> defaults;

    public static void setup(){

        defaults = new HashMap<>();
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
            lang.put(key, serializer.deserialize(get().getString(key)));
//            System.out.println(key + ": " + get().getString(key));
        }
    }


}
