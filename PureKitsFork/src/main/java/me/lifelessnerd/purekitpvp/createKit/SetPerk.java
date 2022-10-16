package me.lifelessnerd.purekitpvp.createKit;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.combathandlers.perkhandler.PerkLib;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

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
        if (!(KitConfig.get().isSet("kits." + kitNameArg))){
            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        String perkNameArg = args[2].toUpperCase();
        PerkLib perkLib = new PerkLib();

        if (!(perkLib.perks.containsKey(perkNameArg))){
            player.sendMessage(ChatColor.GRAY + "That perk does not exist. Choose from the following:");
            for (String key : perkLib.perks.keySet()){
                player.sendMessage(ChatColor.GREEN + key);
                player.sendMessage(ChatColor.GRAY + perkLib.perks.get(key));
            }
            return true;
        }

        String booleanArg = args[3].toLowerCase();
        if (!(booleanArg.equalsIgnoreCase("true") || booleanArg.equalsIgnoreCase("false"))){
            player.sendMessage(ChatColor.GRAY + "Provide either true or false.");
            return true;
        }

        if (booleanArg.equalsIgnoreCase("true")) {
            KitConfig.get().set("kits." + kitNameArg + ".perks." + perkNameArg, true);
            player.sendMessage(ChatColor.GRAY + "Perk set to true.");
        } else {
            KitConfig.get().set("kits." + kitNameArg + ".perks." + perkNameArg, null);
            player.sendMessage(ChatColor.GRAY + "Perk set to false.");
        }

        KitConfig.save();
        KitConfig.reload();

        return true;
    }



}
