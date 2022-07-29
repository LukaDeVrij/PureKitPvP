package me.lifelessnerd.purekitpvp.createKit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateKit implements TabExecutor {
    Plugin plugin;

    public CreateKit(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 1)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }
        //Store arguments
        KitIcon kitIconLib = new KitIcon();
        String kitIcon = "STONE";
        String kitPermission = "kit.other";
        String kitName = args[0].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);
        String displayColor = args[1];
        if (Arrays.asList(kitIconLib.materialList).contains(args[2])){
            kitIcon = args[2].toUpperCase();

        }else {
            player.sendMessage(args[2] + " is not a valid option.");
            return true;
        }
        if (args.length > 3){
            kitPermission = args[3];
        }

        //Storing all contents
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getBoots();
        ItemStack boots = player.getInventory().getBoots();
        ItemStack[] kitContents = player.getInventory().getContents();

        player.sendMessage("Kit made with name: " + kitName + ", displayColor: " + displayColor + ", kitIcon: " + kitIcon + ", kitPermission: " + kitPermission);

        //Set permission
        plugin.getConfig().addDefault("kits." + kitName + ".permission", "");
        plugin.getConfig().set("kits." + kitName + ".permission", kitPermission);

        plugin.getConfig().addDefault("kits." + kitName + ".displayname", "");
        plugin.getConfig().set("kits." + kitName + ".displayname", displayColor);

        plugin.getConfig().addDefault("kits." + kitName + ".guiitem", "");
        plugin.getConfig().set("kits." + kitName + ".guiitem", kitIcon);

        //Add things to config
        plugin.getConfig().addDefault("kits." + kitName, "");
        plugin.getConfig().addDefault("kits." + kitName + ".displayname", "&7" + args[0]);
        plugin.getConfig().addDefault("kits." + kitName + ".guiitem", "STONE");
        plugin.getConfig().addDefault("kits." + kitName + ".guilore", "");
        plugin.getConfig().set("kits." + kitName + ".helmet", helmet);
        plugin.getConfig().set("kits." + kitName + ".chestplate", chestplate);
        plugin.getConfig().set("kits." + kitName + ".leggings", leggings);
        plugin.getConfig().set("kits." + kitName + ".boots", boots);

        plugin.getConfig().addDefault("kits." + kitName + ".contents", kitContents);


        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        player.sendMessage("Kit created.");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //Draft syntax:
        // /createkit <Name> <DisplayName> Icon permission(optional)
        // Icon will be tricky, will need to autocomplete every item in itemstack format (or minecraft format
        // and translate)
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("<kitName>");
            return arguments;
        }
        if (args.length == 2){
            List<String> arguments = new ArrayList<>();
            arguments.add("&1");
            arguments.add("&2");
            arguments.add("&3");
            arguments.add("&4");
            arguments.add("&5");
            arguments.add("&6");
            arguments.add("&7");
            arguments.add("&8");
            arguments.add("&9");
            arguments.add("&0");

            return arguments;
        }
        if (args.length == 3){
            KitIcon kitIconLib = new KitIcon();

            return Arrays.asList(kitIconLib.materialList);
        }
        if (args.length == 4){
            List<String> arguments = new ArrayList<>();
            arguments.add("kits.other");

            return arguments;
        }

        return null;

    }
}
