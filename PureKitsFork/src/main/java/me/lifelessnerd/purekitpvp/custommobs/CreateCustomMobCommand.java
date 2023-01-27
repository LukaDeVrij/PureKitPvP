package me.lifelessnerd.purekitpvp.custommobs;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateCustomMobCommand extends Subcommand {
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
            String customMobName = args[2];
            MobSpawnConfig.get().set(args[2], null);
            MobSpawnConfig.get().set(args[2] + ".type", EntityType.valueOf(args[3]));
            MobSpawnConfig.get().set(args[2] + ".helmet", null);
            MobSpawnConfig.get().set(args[2] + ".chestplate", null);
            MobSpawnConfig.get().set(args[2] + ".leggings", null);
            MobSpawnConfig.get().set(args[2] + ".boots", null);
            MobSpawnConfig.get().set(args[2] + ".mainhand", null);
            MobSpawnConfig.get().set(args[2] + ".offhand", null);

            player.sendMessage("Custom mob created with type " + args[3] + ", identifier " + args[2]);

        }
        if (args[1].equalsIgnoreCase("set")){
            //Possible args should be: type, helmet, chestplate, leggings, boots, mainhand, offhand
            switch(args[3]){

                case "type":
                    MobSpawnConfig.get().set(args[2] + ".type", args[3]);
                    MobSpawnConfig.save();
                    MobSpawnConfig.reload();
                    break;
                case "helmet":
                    setEquipment(player, args[2], "helmet");
                    break;
                case "chestplate":

                    break;
                case "leggings":

                    break;
                case "boots":

                    break;
                case "mainhand":

                    break;
                case "offhand":

                    break;
                default:
                    System.out.println("Arguments are not valid!");

            }

        }
        if (args[1].equalsIgnoreCase("delete")){

            MobSpawnConfig.get().set(args[2], null);
        }

        return false;
    }

    public static void setEquipment(Player player, String mobName, String slot){
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        // TODO: Need to check every input here
        MobSpawnConfig.get().set(mobName + "." + slot, heldItem);

    }
}
