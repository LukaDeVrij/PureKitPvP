package me.lifelessnerd.purekitpvp.createKit;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class CreateKit extends Subcommand {
    Plugin plugin;

    public CreateKit(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "createkit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Create a kit from the contents of your inventory";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp createkit <kitName> <displayColor> <kitIcon> <kitPermission> <killItem>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 4)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }
        //Store arguments
        KitIcon kitIconLib = new KitIcon();
        String kitIcon = "STONE";
        String killItem = "AIR";
        String kitPermission = "kit.other";
        String kitName = args[1].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);
        String displayColor = args[2];
        if (!(args.length >= 4)){
            player.sendMessage("Provide arguments!");
            return false;

        }
        if (Arrays.asList(kitIconLib.materialList).contains(args[3])){
            kitIcon = args[3].toUpperCase();

        }else {
            player.sendMessage(args[3] + " is not a valid option.");
            return true;
        }
        if (!(args[5] == null)){
            if (Arrays.asList(kitIconLib.materialList).contains(args[5])){
                killItem = args[5].toUpperCase();

            }else {
                player.sendMessage(args[5] + " is not a valid option.");
                return true;
            }
        } else {
            killItem = "AIR";
            player.sendMessage("Defaulted to kill item AIR (none). You may change this with /pkpvp setkillitem");
        }

        if (!(args[4] == null)){
            kitPermission = args[4];
        } else {
            kitPermission = "kit.other";
        }



        //Storing all contents
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        ItemStack[] kitContents = player.getInventory().getContents();

        //Create kit in config
        KitConfig.get().createSection("kits." + kitName);
        KitConfig.get().set("kits." + kitName + ".permission", kitPermission);
        KitConfig.get().set("kits." + kitName + ".displayname", displayColor);
        KitConfig.get().set("kits." + kitName + ".guiitem", kitIcon);
        KitConfig.get().set("kits." + kitName + ".guilore", "");
        KitConfig.get().set("kits." + kitName + ".helmet", helmet);
        KitConfig.get().set("kits." + kitName + ".chestplate", chestplate);
        KitConfig.get().set("kits." + kitName + ".leggings", leggings);
        KitConfig.get().set("kits." + kitName + ".boots", boots);
        KitConfig.get().set("kits." + kitName + ".contents", kitContents);

        //Killitem
        Material killItemMat = Material.getMaterial(killItem);
        ItemStack killItemStack = new ItemStack(killItemMat);
        KitConfig.get().addDefault("kits." + kitName + ".killitem", "GOLDEN_APPLE");
        KitConfig.get().set("kits." + kitName + ".killitem", killItemStack);

        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();
        KitConfig.reload();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "Kit created with following properties: \nKitName: &a" + kitName +
                        "&r\n displayColor: &a" + displayColor + "■" + "&r\n kitIcon: &a"
                + kitIcon + "&r\n kitPermission: &a" + kitPermission + "&r\nkillItem: &a" + killItem));
        player.sendMessage("You can change these in the kits.yml file, but remember to backup the file when manually editing.");

        return true;

    }
}
