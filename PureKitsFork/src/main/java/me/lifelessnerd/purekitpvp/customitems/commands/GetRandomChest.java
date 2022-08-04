package me.lifelessnerd.purekitpvp.customitems.commands;

import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GetRandomChest extends Subcommand{
    @Override
    public String getName() {
        return "random_chest";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get the random chest with a user-input loot table";
    }

    @Override
    public String getSyntax() {
        return "/getitem random_chest <loottablename>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (!(LootTablesConfig.get().isSet(args[1]))){
            player.sendMessage("Such loot table does not exist!");
            return false;
        }
        String lore = LootTablesConfig.get().getString(args[1] + ".desiredLore");
        String displayName = LootTablesConfig.get().getString(args[1] + ".displayName");

        ItemStack chestItem = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = chestItem.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        itemMeta.setLore(loreList);
        itemMeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&',displayName)));
//        itemMeta.setCustomModelData(1);
        chestItem.setItemMeta(itemMeta);
        player.getInventory().addItem(chestItem);

        return true;
    }
}
