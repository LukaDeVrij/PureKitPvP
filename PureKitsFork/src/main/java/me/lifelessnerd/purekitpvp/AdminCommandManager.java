package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.custommobs.CustomMobCommand;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import me.lifelessnerd.purekitpvp.createKit.*;
import me.lifelessnerd.purekitpvp.customitems.GetCustomItem;
import me.lifelessnerd.purekitpvp.customitems.loottablelogic.CreateLootTable;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.kitCommand.ResetKit;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetKitStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AdminCommandManager implements TabExecutor {
    ArrayList<Subcommand> subcommands = new ArrayList<>();
    Plugin plugin;
    public AdminCommandManager(Plugin plugin){
        subcommands.add(new CreateKit(plugin));
        subcommands.add(new DeleteKit(plugin));
        subcommands.add(new ResetKit());
        subcommands.add(new SetKillItem(plugin));
        //subcommands.add(new SetPerk(plugin));
        subcommands.add(new CreateLootTable(plugin));
        subcommands.add(new GetCustomItem());
        subcommands.add(new CustomMobCommand());
        subcommands.add(new GetKitStats(plugin));
        subcommands.add(new AdminHelpCommand(subcommands, plugin));
        subcommands.add(new InfoCommand());
        subcommands.add(new ReloadPlugin(plugin));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("The console cannot perform these commands.");
            return false;
        }
        Player player = (Player) sender;

        if(!(player.hasPermission("purekitpvp.admin.*"))){
            player.sendMessage(ChatColor.RED + "You do not have permission!");
            return true;
        }

        if (args.length < 1){
            player.sendMessage("Please specify what function to use.");
            return false;
        }

        //Check for names of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                boolean result = getSubcommands().get(i).perform(player, args);
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }


        player.sendMessage(args[0] + " is not a valid sub-command.");
        return false;

    }

    public ArrayList<Subcommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1) {
            List<String> arguments = new ArrayList<>();
            for (Subcommand subcommand : subcommands){
                arguments.add(subcommand.getName());
            }
            return arguments;
        }

        if (args[0].equalsIgnoreCase("getcustomitem")){

            if (args[1].equalsIgnoreCase("random_chest")){
                List<String> arguments = new ArrayList<>(LootTablesConfig.get().getKeys(false));
                return arguments;
            }
            else if (args[1].equalsIgnoreCase("golden_head")){
                return new ArrayList<>();
            }
            else if (args[1].equalsIgnoreCase("custom_mob_egg")){
                return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
            }

            if (args.length > 3){
                return new ArrayList<>();
            }

            List<String> arguments = new ArrayList<>();
            arguments.add("random_chest");
            arguments.add("golden_head");
            arguments.add("custom_mob_egg");
            return arguments; //TODO: make this only appear when args.length == 1 // What do you mean?

        }
        if (args[0].equalsIgnoreCase("createloottable")){

            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("<name>");
                return arguments;
            }
            if (args.length == 3){
                List<String> arguments = new ArrayList<>();
                arguments.add("<lore>");
                return arguments;
            }
            if (args.length == 4){
                List<String> arguments = new ArrayList<>();
                arguments.add("<displayName>");
                return arguments;
            }
        }
        if (args[0].equalsIgnoreCase("setkillitem")){

            if (args.length == 2){
                List<String> autoComplete = new ArrayList<>();
                for(String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)){
                    key = key.toLowerCase();
                    autoComplete.add(key);
                };
                return autoComplete;
            }
        }
        if (args[0].equalsIgnoreCase("deletekit")){

            if (args.length == 2){
                List<String> autoComplete = new ArrayList<>();
                for(String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)){
                    key = key.toLowerCase();
                    autoComplete.add(key);
                };
                return autoComplete;
            }
        }
        if (args[0].equalsIgnoreCase("createkit")){

            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("<kitName>");
                return arguments;
            }
            if (args.length == 3){
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
            if (args.length == 4){
                KitIcon kitIconLib = new KitIcon();

                return Arrays.asList(kitIconLib.materialList);
            }
            if (args.length == 5){
                List<String> arguments = new ArrayList<>();
                arguments.add("kit.other");

                return arguments;
            }
            if (args.length == 6){
                KitIcon kitIconLib = new KitIcon();
                return Arrays.asList(kitIconLib.materialList);
            }
        }
        if (args[0].equalsIgnoreCase("custommob")){
            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("create");
                arguments.add("set");
                arguments.add("delete");
                return arguments;
            }
            if (args[1].equalsIgnoreCase("create")){
                if (args.length == 3){
                    List<String> arguments = new ArrayList<>();
                    arguments.add("<identifier>");
                    return arguments;
                }
                if (args.length == 4){
                    List<EntityType> entities = Arrays.asList(EntityType.values());
                    ArrayList<String> arguments = new ArrayList<>();
                    // AAAH LAMBDA IN JAVA? sure
                    Consumer<EntityType> method = (entity) -> {arguments.add(entity.toString().toLowerCase());};
                    entities.forEach(method);
                    return arguments;
                }
            }
            if (args[1].equalsIgnoreCase("set")){
                if (args.length == 3){
                    return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
                }
                if (args.length == 4){
                    String[] possibleArgs = {"helmet", "chestplate", "leggings", "boots", "mainhand", "offhand"};
                    return Arrays.asList(possibleArgs);
                }
            }
            if (args[1].equalsIgnoreCase("delete")){
                if (args.length == 3){
                    return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
                }
            }

        }


        return new ArrayList<>();
    }
}
