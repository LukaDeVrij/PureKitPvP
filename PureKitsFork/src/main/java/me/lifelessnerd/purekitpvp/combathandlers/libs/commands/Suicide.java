package me.lifelessnerd.purekitpvp.combathandlers.libs.commands;

import me.lifelessnerd.purekitpvp.PureKitPvP;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class Suicide implements CommandExecutor {
    Plugin plugin;

    public Suicide(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){

            return true;
        }

        if (!(plugin.getConfig().getBoolean("suicide-command"))){
            player.sendMessage("This is disabled.");
            return true;
        }

        player.setHealth(1);
        for(PotionEffect effect:player.getActivePotionEffects()){
        //Now use the method Player#removePotionEffect to remove the potion effect. This method accepts a PotionEffectType, so we need to get the type of the effect variable, and then remove it:
            player.removePotionEffect(effect.getType());
        }
        player.teleport(new Location(player.getWorld(),
                player.getLocation().getX(),
                plugin.getConfig().getDouble("voidY"),
                player.getLocation().getY()
        ));

        //Kind of jank way to do it, but without this damageData is null, this way the void gives final blow
        // Other way may be to set damageData.lastDamager to SUICIDE, but then we would have to get the
        // PersistentDataContainer and all of its checks once more, so this is actually more efficient

        return true;
    }
}
