package me.lifelessnerd.purekitpvp.kitAdmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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

//        if(!(player.hasPermission("purekitpvp.admin.resetkit"))){
//            player.sendMessage(LanguageConfig.lang.get("GENERIC_NO_PERMISSION"));
//            return false;
//        } Redundant, admin.* check is done in superclass

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
