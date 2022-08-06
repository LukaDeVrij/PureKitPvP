package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminCommandManager implements TabExecutor {
    ArrayList<Subcommand> subcommands = new ArrayList<>();
    Plugin plugin;
    public AdminCommandManager(Plugin plugin){
        //subcommands.add();
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
            player.sendMessage("Please specify what function to use."); //TODO: Add all options in a message
            return false;
        }

        //Check for names of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                boolean result = getSubcommands().get(i).perform(player, args);
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }


        player.sendMessage(args[0] + " is not a valid sub-command."); //TODO: Add all options in a message same as above
        return false;

    }

    public ArrayList<Subcommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("golden_head");
            arguments.add("random_chest");
            return arguments;
        }

        if (args[0].equalsIgnoreCase("random_chest")){
            List<String> arguments = new ArrayList<>(LootTablesConfig.get().getKeys(false));
            return arguments;
        }
        return null;
    }
}
