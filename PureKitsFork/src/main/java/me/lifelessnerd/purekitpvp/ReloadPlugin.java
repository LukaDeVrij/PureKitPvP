package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


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

        KitConfig.reload();
        KitConfig.save();

        KitStatsConfig.reload();
        KitStatsConfig.save();

        LootTablesConfig.reload();
        LootTablesConfig.save();

        PlayerStatsConfig.reload();
        PlayerStatsConfig.save();

        PerkData.reload();
        PerkData.save();

        MobSpawnConfig.reload();
        MobSpawnConfig.save();

        CosmeticsConfig.reload();
        CosmeticsConfig.save();

        LanguageConfig.reload();
        LanguageConfig.loadLanguage();
        LanguageConfig.save();

        plugin.reloadConfig();
        plugin.getConfig();
        plugin.saveConfig();
        plugin.saveDefaultConfig();

        player.sendMessage(Component.text("Plugin configs were reloaded!"));
        player.sendMessage(Component.text("Please note that settings relating to events are not reloaded." +
                "\nIf you have made such changes, a server reload/restart is required.").color(NamedTextColor.GRAY));


        return true;
    }
}
