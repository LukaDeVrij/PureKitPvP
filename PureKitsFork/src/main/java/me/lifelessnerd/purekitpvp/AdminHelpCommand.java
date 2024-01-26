package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class AdminHelpCommand extends Subcommand {
    Plugin plugin;
    ArrayList<Subcommand> subcommands;
    public AdminHelpCommand(ArrayList<Subcommand> subcommands, Plugin plugin) {
        this.plugin = plugin;
        this.subcommands = subcommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get a list of all commands";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp help";
    }

    @Override
    public boolean getConsoleExecutable() {
        return true;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        if (args.length == 1){
            String message = """
                    &bPureKitPvP - Help Menu
                    &a/kit &r- &eOpen up the kit menu
                    &a/getkit <kit> &r- &eGet a kit directly
                    &a/suicide &r- &eCommit suicide (if enabled)
                    &a/stats <player> &r- &eGet PVP stats of a player
                    &a/perks &r- &eSelect perks
                    &a/purekitpvphelp &r- &eShow this menu for non-admins
                    &bFor admin commands, see &a/pkpvp help 2&b!
                    """;

            sender.sendMessage(serializer.deserialize(message));


        }
        if (args.length >= 2){

            if (args[1].equalsIgnoreCase("2")){

                sender.sendMessage(Component.text("PureKitPvP - Admin Commands").color(NamedTextColor.BLUE));
                for (Subcommand subcommand : subcommands){
                    String message = "&a" + subcommand.getSyntax() + " &r- &e" + subcommand.getDescription();
                    sender.sendMessage(serializer.deserialize(message));
                }

            } else {
                TextReplacementConfig config = ComponentUtils.replaceConfig("%ARG%", args[1]);
                sender.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_ARGS").replaceText(config));
            }

        }

        return true;
    }
}
