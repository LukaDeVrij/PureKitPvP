package me.lifelessnerd.purekitpvp.custommobs;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CustomMobCommand extends Subcommand {
    @Override
    public String getName() {
        return "custommob";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Create custom mobs that can be used in kits as spawn eggs";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp custommob <create/delete/set> <identifier>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length <= 1){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
            return true;
        }
        if (args[1].equalsIgnoreCase("create")){
            if (args.length <= 3){
                player.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
                return true;
            }
            //String customMobName = args[2];
            MobSpawnConfig.get().set(args[2], null);
            MobSpawnConfig.get().set(args[2] + ".type", EntityType.valueOf(args[3].toUpperCase()).toString());
            MobSpawnConfig.get().set(args[2] + ".child", false);
            MobSpawnConfig.get().set(args[2] + ".helmet", null);
            MobSpawnConfig.get().set(args[2] + ".chestplate", null);
            MobSpawnConfig.get().set(args[2] + ".leggings", null);
            MobSpawnConfig.get().set(args[2] + ".boots", null);
            MobSpawnConfig.get().set(args[2] + ".mainhand", null);
            MobSpawnConfig.get().set(args[2] + ".offhand", null);

            player.sendMessage("Custom mob created with type " + args[3] + ", identifier " + args[2]);
            player.sendMessage("You can now change its equipment using /purekitpvp custommob set");

            MobSpawnConfig.save();
            MobSpawnConfig.reload();
            return true;

        }
        if (args[1].equalsIgnoreCase("set")){
            if (args.length <= 3){
                player.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
                player.sendMessage("Usage: /purekitpvp custommob set <helmet/chestplate/leggings/boots/mainhand/offhand>");
                return true;
            }
            // First the identifier as arg, check if there is such a key
            if (!(MobSpawnConfig.get().isSet(args[2]))) {
                player.sendMessage("That custom mob is not defined!");
            }
            //Possible args should be: helmet, chestplate, leggings, boots, mainhand, offhand
            String[] possibleArgs = {"helmet", "chestplate", "leggings", "boots", "mainhand", "offhand"};
            if (Arrays.asList(possibleArgs).contains(args[3])) {
                setEquipment(player, args[2], args[3]);
                player.sendMessage(args[2] + "'s " + args[3] + " was changed to the item you held.");
            } else if(args[3].equalsIgnoreCase("child")){
                if (args.length == 5 && (args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false"))){
                    MobSpawnConfig.get().set(args[2] + ".child", Boolean.valueOf(args[4]));
                    MobSpawnConfig.save();
                    MobSpawnConfig.reload();
                    player.sendMessage("Custom mob " + args[2] + " property " + args[3] + " set to " + args[4]);

                } else {
                    player.sendMessage("When using argument 'child', follow with either true or false.");
                    return true;
                }
            } else {
                player.sendMessage("Possible arguments are: child (requires true/false argument), helmet, chestplate, leggings, boots, mainhand, offhand");
            }
            return true;

        }
        if (args[1].equalsIgnoreCase("delete")){

            MobSpawnConfig.get().set(args[2], null);
            player.sendMessage(Component.text("Custom mob " + args[2] + " deleted."));
            MobSpawnConfig.save();
            MobSpawnConfig.reload();
            return true;
        }
        else {
            TextReplacementConfig config = ComponentUtils.replaceConfig("%ARG%",args[1]);
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.GENERIC_WRONG_ARGS.toString()).replaceText(config));
        }

        return true;
    }

    public static void setEquipment(Player player, String mobName, String slot){
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        MobSpawnConfig.get().set(mobName + "." + slot, heldItem);
        MobSpawnConfig.save();
        MobSpawnConfig.reload();

    }
}
