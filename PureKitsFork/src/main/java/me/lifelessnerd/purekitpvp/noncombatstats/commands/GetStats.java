package me.lifelessnerd.purekitpvp.noncombatstats.commands;

import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GetStats implements TabExecutor {
    Plugin plugin;

    public GetStats(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        FileConfiguration playerStatsConfig = PlayerStatsConfig.get();
        String argumentPlayer = player.getName();
        if (args.length == 1){
            argumentPlayer = args[0];
        }

        Set<String> allKeys = playerStatsConfig.getKeys(true);
        if (!(allKeys.contains(argumentPlayer))){
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.STATS_NO_STATS.toString()));
            return true;
        }

        player.sendMessage(LanguageConfig.lang.get(LanguageKey.STATS_TITLE.toString()).
                replaceText(ComponentUtils.replaceConfig("%PLAYER%", argumentPlayer)));

        ConfigurationSection playerStatsSection = PlayerStatsConfig.get().getConfigurationSection(argumentPlayer);

        for (String key : playerStatsSection.getKeys(false)){
            player.sendMessage(
                Component.text("    " + MyStringUtils.itemCamelCase(key)).color(NamedTextColor.GREEN).append(
                Component.text("- ").color(NamedTextColor.RED).append(
                Component.text(playerStatsSection.get(key).toString()).color(NamedTextColor.WHITE)
            )));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return null;
    }
}
