package me.lifelessnerd.purekitpvp.noncombatstats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GetKitStats implements CommandExecutor {
    Plugin plugin;
    public GetKitStats(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //TODO: check sender
        // Get kitstatsconfig
        // loop trough getKeys()
        // send them to sender

        return true;
    }
}
