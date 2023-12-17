package me.lifelessnerd.purekitpvp.globalevents;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EventCommand extends Subcommand {
    Plugin plugin;
    GlobalEventManager gem;

    public EventCommand(Plugin plugin, GlobalEventManager gem) {
        this.plugin = plugin;
        this.gem = gem;
    }

    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Start and stop events";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp event <start/stop/pause> <eventName>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return true;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {

        if (!(args.length >= 2)) {
            sender.sendMessage(Component.text("Please provide arguments!").color(NamedTextColor.RED));
            return false;
        }
        if (!(plugin.getConfig().getBoolean("global-event-loop"))){
            sender.sendMessage(Component.text("Global event loop is disabled in config.").color(NamedTextColor.RED));
            return false;
        }

        if (args[1].equalsIgnoreCase("start")) {
            if (!(args.length >= 3)) {
                sender.sendMessage(Component.text("Please provide an event to start!").color(NamedTextColor.RED));
                return false;
            }
            String eventName = args[2];
            Component success = gem.startEvent(eventName);
            sender.sendMessage(success);
        } else if (args[1].equalsIgnoreCase("stop")) {
            // Do stuff
            Component success = gem.stopEvent();
            sender.sendMessage(success);
        } else if (args[1].equalsIgnoreCase("pause")) {
            sender.sendMessage(gem.pauseResumeTimer());

        } else {
            sender.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_ARGS").replaceText(ComponentUtils.replaceConfig("%ARG%", args[0])));
        }


        return false;
    }
}
