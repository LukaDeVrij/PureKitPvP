package me.lifelessnerd.purekitpvp.createKit;

import me.lifelessnerd.purekitpvp.files.KitConfig;
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
import java.util.Locale;

public class DeleteKit implements TabExecutor {
    Plugin plugin;

    public DeleteKit(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("purekitpvp.admin.deletekit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 1)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }

        String kitName = args[0].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);

        if(KitConfig.get().get("kits." + kitName) == null){

            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        KitConfig.get().getConfigurationSection("kits").set(kitName, null);
        KitConfig.save();
        KitConfig.reload();
        player.sendMessage("You removed kit " + ChatColor.BLUE + kitName);


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            List<String> autoComplete = new ArrayList<>();
            for(String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)){
                key = key.toLowerCase();
                autoComplete.add(key);
            };

            return autoComplete;
        }
        return null;
    }
}
