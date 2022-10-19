package me.lifelessnerd.purekitpvp.createKit;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.perks.perkhandler.PerkLib;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class SetPerk extends Subcommand {
    Plugin plugin;

    public SetPerk(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setperk";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set perk values for this kit";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp setperk <kit> <perk> <true/false>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 2)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }

        String kitNameArg = MyStringUtils.camelCaseWord(args[1]);
        ArrayList<String> kitNames = new ArrayList<>();
        ConfigurationSection kitsSection = KitConfig.get().getConfigurationSection("kits");

        // This nesting is painful but it works okay
        if (kitNameArg.equalsIgnoreCase("all")){

            kitNames.addAll(kitsSection.getKeys(false));

        } else {
            if (!(KitConfig.get().isSet("kits." + kitNameArg))){
                player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
                return true;
            } else {
                kitNames.add(kitNameArg);
            }
        }


        String perkNameArg = args[2].toUpperCase();
        PerkLib perkLib = new PerkLib();
        ArrayList<String> perkNames = new ArrayList<>();

        // This nesting is painful but it works okay
        if (perkNameArg.equalsIgnoreCase("all")){

            perkNames.addAll(perkLib.perks.keySet());

        } else {
            if (!(perkLib.perks.containsKey(perkNameArg))){
                player.sendMessage(ChatColor.GRAY + "That perk does not exist. Choose from the following:");
                for (String key : perkLib.perks.keySet()){
                    player.sendMessage(ChatColor.GREEN + key);
                    player.sendMessage(ChatColor.GRAY + perkLib.perks.get(key));
                }
                return true;
            } else {
                perkNames.add(perkNameArg);
            }
        }


        String booleanArg = args[3].toLowerCase();
        if (!(booleanArg.equalsIgnoreCase("true") || booleanArg.equalsIgnoreCase("false"))){
            player.sendMessage(ChatColor.GRAY + "Provide either true or false.");
            return true;
        }
        

        if (booleanArg.equalsIgnoreCase("true")) {

            for(String kitName : kitNames){
                for(String perkName : perkNames) {
                    KitConfig.get().set("kits." + kitName + ".perks." + perkName, true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7Perk &e" + perkName + " &7updated to &aTRUE&7 for kit &e" + kitName
                            ));
                }
            }

        } else { // i.e. false

            for (String kitName : kitNames) {
                for (String perkName : perkNames) {
                    KitConfig.get().set("kits." + kitName + ".perks." + perkName, null);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7Perk &e" + perkName + " &7updated to &cFALSE&7 for kit &e" + kitName
                    ));
                }
            }
        }


        KitConfig.save();
        KitConfig.reload();

        return true;
    }
}
