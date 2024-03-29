package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.custommobs.CustomMobCommand;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import me.lifelessnerd.purekitpvp.kitadmin.*;
import me.lifelessnerd.purekitpvp.customItems.GetCustomItem;
import me.lifelessnerd.purekitpvp.customItems.loottable.CreateLootTable;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.globalevents.EventCommand;
import me.lifelessnerd.purekitpvp.globalevents.GlobalEventManager;
import me.lifelessnerd.purekitpvp.globalevents.events.AbstractEvent;
import me.lifelessnerd.purekitpvp.kitadmin.ResetKit;
import me.lifelessnerd.purekitpvp.kitadmin.KitSupercommand;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetKitStats;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
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

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class AdminCommandManager implements TabExecutor {
    ArrayList<Subcommand> subcommands = new ArrayList<>();
    Plugin plugin;

    GlobalEventManager globalEventManager;

    public AdminCommandManager(Plugin plugin) {
        subcommands.add(new KitSupercommand((PureKitPvP) plugin));
        subcommands.add(new ResetKit());
        subcommands.add(new GetKitStats(plugin));
        this.globalEventManager = new GlobalEventManager(plugin);
        subcommands.add(new EventCommand(plugin, this.globalEventManager));
        subcommands.add(new CreateLootTable(plugin));
        subcommands.add(new GetCustomItem());
        subcommands.add(new CustomMobCommand());
        subcommands.add(new AdminHelpCommand(subcommands, plugin));
        subcommands.add(new InfoCommand());
        subcommands.add(new ReloadPlugin(plugin));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (!(sender.hasPermission("purekitpvp.admin.*"))) {
            sender.sendMessage(LanguageConfig.lang.get("GENERIC_NO_PERMISSION"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
            return false;
        }

        //Check for names of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++) {
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                if (sender instanceof Player) {
                    getSubcommands().get(i).perform(sender, args);
                } else {
                    if (!getSubcommands().get(i).getConsoleExecutable()){
                        sender.sendMessage(Component.text("The console cannot perform this command!"));
                    } else {
                        getSubcommands().get(i).perform(sender, args);
                    }
                }
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }

        TextReplacementConfig config = ComponentUtils.replaceConfig("%ARG%", args[0]);
        sender.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_ARGS").replaceText(config));
        return false;

    }

    public ArrayList<Subcommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            for (Subcommand subcommand : subcommands) {
                arguments.add(subcommand.getName());
            }
            return arguments;
        }

        if (args[0].equalsIgnoreCase("getcustomitem")) {

            if (args[1].equalsIgnoreCase("random_chest")) {
                List<String> arguments = new ArrayList<>(LootTablesConfig.get().getKeys(false));
                return arguments;
            } else if (args[1].equalsIgnoreCase("golden_head")) {
                return new ArrayList<>();
            } else if (args[1].equalsIgnoreCase("custom_mob_egg")) {
                return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
            }

            if (args.length > 3) {
                return new ArrayList<>();
            }

            List<String> arguments = new ArrayList<>();
            arguments.add("random_chest");
            arguments.add("golden_head");
            arguments.add("custom_mob_egg");
            return arguments;

        }
        if (args[0].equalsIgnoreCase("createloottable")) {

            if (args.length == 2) {
                List<String> arguments = new ArrayList<>();
                arguments.add("<name>");
                return arguments;
            }
            if (args.length == 3) {
                List<String> arguments = new ArrayList<>();
                arguments.add("<lore>");
                return arguments;
            }
            if (args.length == 4) {
                List<String> arguments = new ArrayList<>();
                arguments.add("<displayName>");
                return arguments;
            }
        }
        if (args[0].equalsIgnoreCase("kit")){

            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("setkillitem");
                arguments.add("delete");
                arguments.add("create");
                arguments.add("edit");
                return arguments;
            }
            if (args[1].equalsIgnoreCase("setkillitem")) {

                if (args.length == 3) {
                    List<String> autoComplete = new ArrayList<>();
                    for (String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)) {
                        key = key.toLowerCase();
                        autoComplete.add(key);
                    }
                    ;
                    return autoComplete;
                }
            }
            if (args[1].equalsIgnoreCase("delete")) {

                if (args.length == 3) {
                    List<String> autoComplete = new ArrayList<>();
                    for (String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)) {
                        key = key.toLowerCase();
                        autoComplete.add(key);
                    }

                    return autoComplete;
                }
            }
            if (args[1].equalsIgnoreCase("create")) {

                if (args.length == 3) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("<kitName>");
                    return arguments;
                }
                if (args.length == 4) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("black");
                    arguments.add("dark_blue");
                    arguments.add("dark_green");
                    arguments.add("dark_aqua");
                    arguments.add("dark_red");
                    arguments.add("dark_purple");
                    arguments.add("gold");
                    arguments.add("gray");
                    arguments.add("dark_gray");
                    arguments.add("blue");
                    arguments.add("green");
                    arguments.add("aqua");
                    arguments.add("red");
                    arguments.add("light_purple");
                    arguments.add("yellow");
                    arguments.add("white");

                    return arguments;
                }
                if (args.length == 5) {
                    KitIcon kitIconLib = new KitIcon();

                    return Arrays.asList(kitIconLib.materialList);
                }
                if (args.length == 6) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("kit.other");

                    return arguments;
                }
                if (args.length == 7) {
                    KitIcon kitIconLib = new KitIcon();
                    return Arrays.asList(kitIconLib.materialList);
                }
            }
            if (args[1].equalsIgnoreCase("edit")){
                if (args.length == 3) {
                    List<String> autoComplete = new ArrayList<>();
                    for (String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)) {
                        key = key.toLowerCase();
                        autoComplete.add(key);
                    }

                    return autoComplete;
                }
            }


        }
        if (args[0].equalsIgnoreCase("custommob")) {
            if (args.length == 2) {
                List<String> arguments = new ArrayList<>();
                arguments.add("create");
                arguments.add("set");
                arguments.add("delete");
                return arguments;
            }
            if (args[1].equalsIgnoreCase("create")) {
                if (args.length == 3) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("<identifier>");
                    return arguments;
                }
                if (args.length == 4) {
                    List<EntityType> entities = Arrays.asList(EntityType.values());
                    ArrayList<String> arguments = new ArrayList<>();
                    // AAAH LAMBDA IN JAVA? sure
                    Consumer<EntityType> method = (entityType) -> {
                        Entity entity;
                        try {
                            // This is stupid, only reason i am keeping this here is because it won't run that often
                            entity = getServer().getWorlds().get(0).spawnEntity(getServer().getWorlds().get(0).getSpawnLocation(), entityType);
                            if (entity instanceof Monster) {
                                arguments.add(entityType.toString().toLowerCase());
                            }
                            entity.remove();
                        } catch(Exception e){
//                            System.out.println(entityType);
                        }
                    };
                    entities.forEach(method);
                    return arguments;
                }
            }
            if (args[1].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
                }
                if (args.length == 4) {
                    String[] possibleArgs = {"helmet", "chestplate", "leggings", "boots", "mainhand", "offhand", "child"};
                    return Arrays.asList(possibleArgs);
                }
                if (args.length == 5){
                    if (args[3].equalsIgnoreCase("child")){
                        String[] possibleArgs = {"true", "false"};
                        return Arrays.asList(possibleArgs);
                    }
                }

            }
            if (args.length == 3) {
                return new ArrayList<>(MobSpawnConfig.get().getKeys(false));
            }
        }
        if (args[0].equalsIgnoreCase("event")){
            if (args.length == 2) {
                List<String> autoComplete = new ArrayList<>();
                autoComplete.add("start");
                autoComplete.add("stop");
                autoComplete.add("pause");
                return autoComplete;
            }
            if (args[1].equalsIgnoreCase("start") || args[1].equalsIgnoreCase("stop") ){
                if (args.length == 3) {
                    List<String> autoComplete = new ArrayList<>();
                    for (AbstractEvent event : globalEventManager.enabledByConfig) {
                        autoComplete.add(event.getEventName().replace(' ', '_').toLowerCase());
                    }
                    return autoComplete;
                }
            }
        }
        return new ArrayList<>();
    }
}
