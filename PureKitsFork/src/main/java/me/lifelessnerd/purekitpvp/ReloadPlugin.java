package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ReloadPlugin extends Subcommand {
    Plugin plugin;
    public ReloadPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Reloads plugin configs";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp reload";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        //TODO Reload should read the files, even if they are manually edited, and load them into the plugin
        // NO WORKIE
        KitConfig.reload();
        KitConfig.save();
        KitStatsConfig.reload();
        KitStatsConfig.save();
        LootTablesConfig.reload();
        LootTablesConfig.save();
        PlayerStatsConfig.reload();
        PlayerStatsConfig.save();

        plugin.reloadConfig();
        plugin.getConfig();
        plugin.saveConfig();
        plugin.saveDefaultConfig();

        player.sendMessage("Plugin configs were reloaded!");

        return true;
    }
}
