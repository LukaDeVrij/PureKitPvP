package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResetKit extends Subcommand {

    @Override
    public String getName() {
        return "resetkit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Reset your kit";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp resetkit";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(!(player.hasPermission("purekitpvp.admin.resetkit"))){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_NO_PERMISSION"));
            return false;
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setExp(0f);
        player.setLevel(0);
        player.getInventory().clear();
        GetKit.hasKit.remove(player.getName());
        player.sendMessage(LanguageConfig.lang.get("KITS_RESET_KIT"));


        return true;
    }
}
