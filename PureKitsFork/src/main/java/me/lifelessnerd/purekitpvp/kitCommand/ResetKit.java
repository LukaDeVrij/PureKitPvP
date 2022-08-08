package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.Subcommand;
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
    public boolean perform(Player player, String[] args) {

        if(!(player.hasPermission("purekitpvp.admin.resetkit"))){
            player.sendMessage(ChatColor.RED + "You do not have permission!");
            return false;
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setExp(0f);
        player.setLevel(0);
        player.getInventory().clear();
        GetKit.hasKit.remove(player.getName());
        player.sendMessage(ChatColor.GRAY + "You have reset your kit.");


        return true;
    }
}
