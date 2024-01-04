package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){
            return false;
        }

        Component message = LanguageConfig.lang.get("GENERIC_PLAYER_HELP");
        player.sendMessage(message);
        return true;
    }
}
