package me.lifelessnerd.purekitpvp.custommobs;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
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
        return "/purekits custommob";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (args[1].equalsIgnoreCase("create")){
            //String customMobName = args[2];
            MobSpawnConfig.get().set(args[2], null);
            MobSpawnConfig.get().set(args[2] + ".type", EntityType.valueOf(args[3]).toString());
            MobSpawnConfig.get().set(args[2] + ".child", false);
            MobSpawnConfig.get().set(args[2] + ".helmet", null);
            MobSpawnConfig.get().set(args[2] + ".chestplate", null);
            MobSpawnConfig.get().set(args[2] + ".leggings", null);
            MobSpawnConfig.get().set(args[2] + ".boots", null);
            MobSpawnConfig.get().set(args[2] + ".mainhand", null);
            MobSpawnConfig.get().set(args[2] + ".offhand", null);

            player.sendMessage("Custom mob created with type " + args[3] + ", identifier " + args[2]);
            MobSpawnConfig.save();
            MobSpawnConfig.reload();
            return true;

        }
        if (args[1].equalsIgnoreCase("set")){
            // First the identifier as arg, check if there is such a key
            if (!(MobSpawnConfig.get().isSet(args[2]))) {
                player.sendMessage("That custom mob is not defined!");
            }
            //Possible args should be: helmet, chestplate, leggings, boots, mainhand, offhand
            String[] possibleArgs = {"helmet", "chestplate", "leggings", "boots", "mainhand", "offhand"};
            if (Arrays.asList(possibleArgs).contains(args[3])) {
                setEquipment(player, args[2], args[3]);
                player.sendMessage(args[2] + "'s " + args[3] + " was changed to the item you held.");
            } else {
                player.sendMessage("Possible arguments are: type, helmet, chestplate, leggings, boots, mainhand, offhand");
            }
            return true;

        }
        if (args[1].equalsIgnoreCase("delete")){

            MobSpawnConfig.get().set(args[2], null);
            player.sendMessage("Custom mob " + args[2] + " deleted.");
            MobSpawnConfig.save();
            MobSpawnConfig.reload();
            return true;
        }
        else {
            player.sendMessage("Invalid argument!");
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
