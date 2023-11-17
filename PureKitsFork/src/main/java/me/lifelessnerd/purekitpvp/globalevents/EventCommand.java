package me.lifelessnerd.purekitpvp.globalevents;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.globalevents.events.AbstractEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
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
    public boolean perform(Player player, String[] args) {

        if (!(args.length >= 2)) {
            player.sendMessage(Component.text("Please provide arguments!").color(NamedTextColor.RED));
            return false;
        }

        if (args[1].equalsIgnoreCase("start")) {
            if (!(args.length >= 3)) {
                player.sendMessage(Component.text("Please provide an event to start!").color(NamedTextColor.RED));
                return false;
            }
            String eventName = args[2];
            Component success = gem.startEvent(eventName);
            player.sendMessage(success);
        } else if (args[1].equalsIgnoreCase("stop")) {
            // Do stuff
            Component success = gem.stopEvent();
            player.sendMessage(success);
        } else if (args[1].equalsIgnoreCase("pause")) {
            player.sendMessage(gem.pauseResumeTimer());

        } else {
            player.sendMessage(Component.text(args[0] + " is not a valid subcommand."));
        }


        return false;
    }
}
