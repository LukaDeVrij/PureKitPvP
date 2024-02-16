package me.lifelessnerd.purekitpvp.files.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class LanguageConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static HashMap<String, Component> lang;

    public static SortedMap<LanguageKey, Object> defaults;

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
        defaults.put(LanguageKey.GENERIC_NO_PERMISSION, "&cYou do not have permission!");
        defaults.put(LanguageKey.GENERIC_LACK_OF_ARGS, "&cNot enough arguments!");
        defaults.put(LanguageKey.GENERIC_WRONG_ARGS, "%ARG% &cis not a valid argument!");
        defaults.put(LanguageKey.GENERIC_WRONG_WORLD, "&cYou can only use this menu in &7%WORLD%");
        defaults.put(LanguageKey.GENERIC_PLAYER_HELP, """
                &bPureKitPvP - Help Menu
                &a/kit &r- &eOpen up the kit menu
                &a/getkit <kit> &r- &eGet a kit directly
                &a/suicide &r- &eCommit suicide (if enabled)
                &a/stats <player> &r- &eGet PVP stats of a player
                &a/perks &r- &eSelect perks
                &bFor admin commands, see &a/pkpvp help 2&b!
                """);
        defaults.put(LanguageKey.GENERIC_FEATURE_DISABLED, "&cThis feature is disabled.");
        defaults.put(LanguageKey.PERKS_ALREADY_SELECTED, "&cYou can only change perks when you have no kit selected!");
        defaults.put(LanguageKey.PERKS_GUI_TITLE, "&6Perks Menu");
        defaults.put(LanguageKey.PERKS_GUI_INFO_TITLE, "&cPerks Info");
        defaults.put(LanguageKey.PERKS_GUI_INFO_LORE, """
                &aYou can equip a total of &e5 &aperks total.
                &7Click on a perk slot to choose a perk for that slot.
                &7It will replace any perk currently in that slot.
                &7Perks are abilities that are always active.
                &7Duplicate perks do not stack.
                """);
        defaults.put(LanguageKey.PERKS_GUI_BACK_KITS, "&aBack to Kits");
        defaults.put(LanguageKey.PERKS_GUI_SLOT_TITLE, "&cPerk Slot %SLOT%");
        defaults.put(LanguageKey.PERKS_GUI_SLOT_LORE, "&aClick to select a perk for this slot!");
        defaults.put(LanguageKey.PERKS_GUI_BACK, "&aGo Back");
        defaults.put(LanguageKey.PERKS_PERK_JUGGERNAUT, """
                &rGain regeneration 1 (5s) after every kill
                """);
        defaults.put(LanguageKey.PERKS_PERK_BULLDOZER, """
                &rGain strength 2 (3s) after every kill
                """);
        defaults.put(LanguageKey.PERKS_PERK_KNOWLEDGE, """
                &rGain 1 XP level on kill
                """);
        defaults.put(LanguageKey.PERKS_PERK_NOTORIETY, """
                &rChance of gaining a sharpness level on each kill
                &rChance is 100 - (25 * Sharpness Level) percent
                """);
        defaults.put(LanguageKey.PERKS_PERK_NOTORIETY_MAX, "&eYou have reached the maximum Notoriety!");
        defaults.put(LanguageKey.PERKS_PERK_NOTORIETY_UPGRADE, "&eYour sharpness level has increased (chance of %CHANCE%%)!");
        defaults.put(LanguageKey.PERKS_PERK_ENDERMAGIC, """
                &r30% chance of gaining an ender pearl on kill
                """);
        defaults.put(LanguageKey.PERKS_PERK_ENDERMAGIC_OCCUR, "&eYou gained an ender pearl!");
        defaults.put(LanguageKey.PERKS_PERK_SPEEDSTER, """
                &rGain 10 seconds of speed 2 after every kill
                """);
        defaults.put(LanguageKey.PERKS_PERK_ROBBERY, """
                &r30% chance of disarming someone if hit with your fist
                &rThe weapon will be swapped with a slot in the inventory
                """);
        defaults.put(LanguageKey.PERKS_PERK_ROBBERY_DISARMED,"&eYou were disarmed by %PLAYER%'s Robbery perk!");
        defaults.put(LanguageKey.PERKS_PERK_ROBBERY_DISARMER,"&eYou disarmed %PLAYER% with your Robbery perk!");
        defaults.put(LanguageKey.PERKS_PERK_SNOWMAN, """
                &rGive slowness 1 (3s) when you hit someone with a snowball
                """);
        defaults.put(LanguageKey.PERKS_PERK_DISRUPTOR, """
                &rGive poison 1 (5s) when you hit someone with an egg
                """);
        defaults.put(LanguageKey.PERKS_PERK_ENDERMAN, """
                &rYou can now ride ender pearls; they do no damage
                """);
        defaults.put(LanguageKey.PERKS_PERK_VAMPIRE, """
                &rOn critical hits, gain 50% of the damage
                &ryou dealt, as instant health
                """);
        defaults.put(LanguageKey.PERKS_PERK_MARKSMAN, """
                &rChance of 100 - (25 * Power level) percent to
                &radd power to the item held, if it's a bow
                """);
        defaults.put(LanguageKey.PERKS_PERK_MARKSMAN_UPGRADE,"&eYour power level has increased (chance of %CHANCE%%)!");
        defaults.put(LanguageKey.PERKS_PERK_MARKSMAN_MAX,"&eYou have reached the maximum power level!");
        defaults.put(LanguageKey.PERKS_PERK_APOLLO, """
                &rYou get each arrow you hit on a player back
                &rA kill nets one extra arrow of that type
                """);
        defaults.put(LanguageKey.PERKS_PERK_ADRENALINE, """
                &rWhen below 6 HP (3 hearts), you get a speed boost
                """);
        defaults.put(LanguageKey.KITS_GUI_TITLE, "Kits");
        defaults.put(LanguageKey.KITS_GUI_PREV, "Previous Page");
        defaults.put(LanguageKey.KITS_GUI_NEXT, "Next Page");
        defaults.put(LanguageKey.KITS_ALREADY_SELECTED, "&cYou already have a kit!");
        defaults.put(LanguageKey.KITS_DOES_NOT_EXIST, "&cThat kit does not exist!");
        defaults.put(LanguageKey.KITS_PERMISSION_NOT_DEFINED, "&cThat kit does not have a permission associated. Please report this to your administrator.");
        defaults.put(LanguageKey.KITS_KIT_GIVEN, "&aKit &6%KIT% &agiven.");
        defaults.put(LanguageKey.KITS_NO_KITS, "&cThere are no kits... yet. Create some with /pkpvp createkit");
        defaults.put(LanguageKey.KITS_GUI_NO_PERMISSION, "&cYou don't have permission!");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW, "&bRIGHT CLICK to preview!");
        defaults.put(LanguageKey.KITS_GUI_WEAPONS, "&3Weapons:");
        defaults.put(LanguageKey.KITS_GUI_ITEMS, "&3Items:");
        defaults.put(LanguageKey.KITS_GUI_ARMOR, "&3Armor:");
        defaults.put(LanguageKey.KITS_GUI_NO_ARMOR, "&3No Armor");
        defaults.put(LanguageKey.KITS_GUI_OFFHAND, "&3Offhand:");
        defaults.put(LanguageKey.KITS_GUI_KILL_ITEM, "&fItem on Kill:");
        defaults.put(LanguageKey.KITS_GUI_NO_KILL_ITEM, "&fNo Item on Kill");
        defaults.put(LanguageKey.KITS_GUI_RESET, "&fReset Kit");
        defaults.put(LanguageKey.KITS_GUI_RESET_LORE, """
                &9If you do not have permission to reset your kit,
                &9this will run /suicide on your behalf.
                """);
        defaults.put(LanguageKey.KITS_GUI_PERKS, "&rPerks menu");
        defaults.put(LanguageKey.KITS_GUI_PERKS_LORE, """
                &9Click here to change your perks!
                """);
        defaults.put(LanguageKey.KITS_RESET_KIT, "&aKit has been reset.");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_TITLE, "&aKit Preview > &b%KIT%");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_TEXT, "&aRight click to preview.");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_STATS, "&6Global Stats");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_PREFS, "&6Customize layout");
        defaults.put(LanguageKey.KITS_GUI_PREFS_TITLE, "&6Customizing > &b%KIT%");
        defaults.put(LanguageKey.KITS_GUI_PREFS_SAVE, "&6Save custom layout");
        defaults.put(LanguageKey.KITS_GUI_PREFS_INFO, "&7The slot above resembles the off-hand.");
        defaults.put(LanguageKey.KITS_GUI_PREFS_SAVED, "&6Custom layout saved!");
        defaults.put(LanguageKey.KITS_GUI_PREFS_BACK, "&6Back to kit preview");
        defaults.put(LanguageKey.KITS_GUI_PREFS_RESET, "&cReset your layout");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_SELECT, "&aSelect Kit");
        defaults.put(LanguageKey.KITS_GUI_PREVIEW_BACK, "&aBack to Kits");
        defaults.put(LanguageKey.KITS_GUI_EDIT_SAVE, "&aSave Kit");
        defaults.put(LanguageKey.KITS_GUI_EDIT_SAVED, "&aKit updated.");
        defaults.put(LanguageKey.LOOT_LUCKY, "&bLUCKY! &a%ITEM%had a chance of &a%CHANCE%%!");

        defaults.put(LanguageKey.EVENTS_END, "&7The event has ended.");
        defaults.put(LanguageKey.EVENTS_START, "&bRANDOM EVENT! &d%EVENT% &bhas been selected!");
        defaults.put(LanguageKey.EVENTS_TELEMADNESS_DESC, "Everyone gets Ender Pearls, all the time!");
        defaults.put(LanguageKey.EVENTS_TELEMADNESS_ITEM_LORE, """
                &7This is an event specific item, which
                &7will be removed as soon as the event ends.
                """);
        defaults.put(LanguageKey.EVENTS_PICKUP_DESC, "Killed players drop their stuff!");
        defaults.put(LanguageKey.EVENTS_JUGGERNAUT_DESC, "Team up to kill the Juggernaut!");
        defaults.put(LanguageKey.EVENTS_DOUBLE_DESC, "All player health is doubled!");

        defaults.put(LanguageKey.SCOREBOARD_MAIN_TITLE, "&6PureKitPvP");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_TITLE, "&6Personal Stats");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_KILLSTREAK, "  &7Killstreak: &b%VALUE%");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_KILLS, "  &7Kills: &b%VALUE%");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_DEATHS, "  &7Deaths: &b%VALUE%");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_KD, "  &7K/D Ratio: &b%VALUE%");
        defaults.put(LanguageKey.SCOREBOARD_PERSONAL_LEVEL, "  &7Level: &b%VALUE%");
        defaults.put(LanguageKey.SCOREBOARD_GLOBAL_TITLE, "&6Global Stats");
        defaults.put(LanguageKey.SCOREBOARD_GLOBAL_KILLSTREAK, "  &dKillstreak");
        defaults.put(LanguageKey.SCOREBOARD_GLOBAL_KILLS, "  &dKills");
        defaults.put(LanguageKey.SCOREBOARD_GLOBAL_KD, "  &dK/D Ratio");
        defaults.put(LanguageKey.SCOREBOARD_GLOBAL_LEVEL, "&dLevel");
        defaults.put(LanguageKey.STATS_NO_STATS, "That player has no stats attached to them!");
        defaults.put(LanguageKey.STATS_TITLE, "%PLAYER% &bStatistics");
        defaults.put(LanguageKey.STATS_LEVEL_UP, "&6You leveled up to LEVEL %VALUE%!");
        defaults.put(LanguageKey.COMBAT_YOU_DIED, "&c&lYou died!");
        defaults.put(LanguageKey.COMBAT_DEATH_RECAP, "&7Death Recap");
        defaults.put(LanguageKey.COMBAT_DEATH_CAUSE, "&7- &e%CAUSE% &7did &a%VALUE%%");

        SortedMap<String, Object> defaultsString = new TreeMap<>();
        for (LanguageKey key : defaults.keySet()){
            defaultsString.put(key.toString(), defaults.get(key));
        }

        customFile.addDefaults(defaultsString);
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
