package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class SetKillItem extends Subcommand {
    Plugin plugin;

    public SetKillItem(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setkillitem";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set the kill item to the item in your hand";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp setkillitem <kit>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.GENERIC_NO_PERMISSION.toString()));
            return true;
        }

        if (!(args.length >= 2)){
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.GENERIC_LACK_OF_ARGS.toString()));
            return false;
        }

        String kitNameArg = MyStringUtils.camelCaseWord(args[1]);

        if (!(KitConfig.get().isSet("kits." + kitNameArg))){
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.KITS_DOES_NOT_EXIST.toString()));
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        KitConfig.get().set("kits." + kitNameArg + ".killitem", itemStack);
        player.sendMessage(Component.text("Set the kill item of the kit " + kitNameArg + " to " + itemStack.getType()));
        KitConfig.save();
        KitConfig.reload();

        return true;
    }
}
