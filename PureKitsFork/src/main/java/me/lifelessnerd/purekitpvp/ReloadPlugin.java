package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.entity.Player;

public class ReloadPlugin extends Subcommand {
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
        KitConfig.save();
        KitConfig.reload();
        KitStatsConfig.save();
        KitStatsConfig.reload();
        LootTablesConfig.save();
        LootTablesConfig.reload();
        PlayerStatsConfig.save();
        PlayerStatsConfig.reload();
        player.sendMessage("Plugin configs were reloaded!");

        return true;
    }
}
