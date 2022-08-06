package me.lifelessnerd.purekitpvp.createKit;

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
        String killItem = "AIR";
        String kitPermission = "kit.other";
        String kitName = args[0].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);
        String displayColor = args[1];
        if (!(args.length >= 5)){
            player.sendMessage("Provide arguments!");
            return false;

        }
        if (Arrays.asList(kitIconLib.materialList).contains(args[2])){
            kitIcon = args[2].toUpperCase();

        }else {
            player.sendMessage(args[2] + " is not a valid option.");
            return true;
        }
        if (Arrays.asList(kitIconLib.materialList).contains(args[4])){
            killItem = args[4].toUpperCase();

        }else {
            player.sendMessage(args[4] + " is not a valid option.");
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

        //Set permission
        KitConfig.get().addDefault("kits." + kitName + ".permission", "");
        KitConfig.get().set("kits." + kitName + ".permission", kitPermission);
        KitConfig.get().addDefault("kits." + kitName + ".displayname", "");
        KitConfig.get().set("kits." + kitName + ".displayname", displayColor);
        KitConfig.get().addDefault("kits." + kitName + ".guiitem", "");
        KitConfig.get().set("kits." + kitName + ".guiitem", kitIcon);
        KitConfig.get().addDefault("kits." + kitName, "");
        KitConfig.get().addDefault("kits." + kitName + ".displayname", "&7" + args[0]);

        KitConfig.get().addDefault("kits." + kitName + ".guiitem", "STONE");
        KitConfig.get().addDefault("kits." + kitName + ".guilore", "");
        KitConfig.get().set("kits." + kitName + ".helmet", helmet);
        KitConfig.get().set("kits." + kitName + ".chestplate", chestplate);
        KitConfig.get().set("kits." + kitName + ".leggings", leggings);
        KitConfig.get().set("kits." + kitName + ".boots", boots);
        KitConfig.get().addDefault("kits." + kitName + ".contents", kitContents);

        //Killitem
        Material killItemMat = Material.getMaterial(killItem);
        ItemStack killItemStack = new ItemStack(killItemMat);
        KitConfig.get().addDefault("kits." + kitName + ".killitem", "GOLDEN_APPLE");
        KitConfig.get().set("kits." + kitName + ".killitem", killItemStack);

        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();
        KitConfig.reload();

        player.sendMessage("Kit made with name: " + kitName + ",\n displayColor: " + displayColor + ",\n kitIcon: "
                + kitIcon + ",\n kitPermission: " + kitPermission + ",\nkillItem: " + killItem);

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
            arguments.add("kit.other");

            return arguments;
        }
        if (args.length == 5){
            KitIcon kitIconLib = new KitIcon();
            return Arrays.asList(kitIconLib.materialList);
        }

        return null;

    }
}
