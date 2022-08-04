package me.lifelessnerd.purekitpvp.createKit;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetKillItem implements TabExecutor {
    Plugin plugin;

    public SetKillItem(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){
            return false;
        }

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (!(args.length >= 1)){
            player.sendMessage(ChatColor.RED + "Please provide arguments!");
            return false;
        }

        String kitNameArg = MyStringUtils.camelCaseWord(args[0]);

        if (!(KitConfig.get().isSet("kits." + kitNameArg))){
            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        KitConfig.get().set("kits." + kitNameArg + ".killitem", itemStack);
        player.sendMessage("Set the kill item of the kit " + kitNameArg + " to " + itemStack.getType());
        KitConfig.save();
        KitConfig.reload();

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
