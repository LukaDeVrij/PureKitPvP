package me.lifelessnerd.purekitpvp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Subcommand {

    public abstract String getName();

    public abstract String[] getAliases();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract boolean perform(Player player, String[] args);

}
