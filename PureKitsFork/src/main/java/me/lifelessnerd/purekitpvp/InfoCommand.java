package me.lifelessnerd.purekitpvp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
    public boolean getConsoleExecutable() {
        return true;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {

        String message =
                """       
                &bPureKitPvP - version 1.2
                &6SpigotMC: &ohttps://bit.ly/PureKitPvPSpigotMC
                &6Github: &ohttps://github.com/LifelessNerd/PureKitPvP
                &6Developer: &ohttps://twitter.com/LukaDeVrij
                &rIf you have any problems, bugs or glitches, please do reach out to me via any of the links above.
                """;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }
}
