package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.PureKitPvP;
import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.createKit.CreateKit;
import me.lifelessnerd.purekitpvp.createKit.DeleteKit;
import me.lifelessnerd.purekitpvp.createKit.SetKillItem;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KitSupercommand extends Subcommand {

    PureKitPvP plugin;
    public KitSupercommand(PureKitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "kit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "All admin commands related to kits";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp kit <create/delete/edit/setkillitem> <identifier>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
        }

        String suboption = args[1];
        args = Arrays.stream(args, 1, args.length).toArray(String[]::new);
        switch(suboption){
            case "create":
                CreateKit createKit = new CreateKit(plugin);
                createKit.perform(player, args);
                break;
            case "delete":
                DeleteKit deleteKit = new DeleteKit(plugin);
                deleteKit.perform(player, args);
                break;
            case "edit":
                // TODO
                break;
            case "setkillitem":
                SetKillItem setKillItem = new SetKillItem(plugin);
                setKillItem.perform(player, args);
                break;
            case "convertFromYAML":
                // TODO
                break;

            default:
                player.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_ARGS").replaceText(ComponentUtils.replaceConfig("%ARG%", suboption)));
        }



        return true;
    }
}
