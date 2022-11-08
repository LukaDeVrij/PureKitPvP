package me.lifelessnerd.purekitpvp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InfoCommand extends Subcommand{
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Info/version about the PureKitPvP plugin.";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp info";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        String message =
                """       
                &bPureKitPvP - version 1.1
                &6SpigotMC: &ohttps://bit.ly/PureKitPvPSpigotMC
                &6Github: &ohttps://github.com/LifelessNerd/PureKitPvP
                &6Developer: &ohttps://twitter.com/NerdLifeless
                &rIf you have any problems, bugs or glitches, reach out to me via any of the links above.
                """;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }
}
