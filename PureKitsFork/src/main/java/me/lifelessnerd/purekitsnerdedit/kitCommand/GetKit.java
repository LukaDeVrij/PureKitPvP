package me.lifelessnerd.purekitsnerdedit.kitCommand;

import me.lifelessnerd.purekitsnerdedit.PureKitsNerdEdit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GetKit implements CommandExecutor {
    Plugin plugin;

    public static ArrayList<String> hasKit = new ArrayList<>();

    public GetKit(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}
