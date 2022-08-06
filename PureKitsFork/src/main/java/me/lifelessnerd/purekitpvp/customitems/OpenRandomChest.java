package me.lifelessnerd.purekitpvp.customitems;

import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Logger;

public class OpenRandomChest implements Listener {
    Plugin plugin;
    Logger log;


    public OpenRandomChest(Plugin plugin) {
        this.plugin = plugin;
        this.log = Bukkit.getLogger();
    }

    @EventHandler
    public void onChestItemClick(PlayerInteractEvent e){
        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }



        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            return;

        }

        if (!(player.getInventory().getItemInMainHand().getType() == Material.CHEST)) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (!(heldItem.getItemMeta().hasLore())){
            return;
        }
        String itemLore = heldItem.getLore().get(0);
        List<String> lootTables = new ArrayList<>(LootTablesConfig.get().getKeys(false));
        String currentLootTable = null;
        for(String lootName : lootTables){
            String lootLore = LootTablesConfig.get().getString(lootName + ".desiredLore");
            lootLore = ChatColor.translateAlternateColorCodes('&', lootLore);
            if (lootLore.equalsIgnoreCase(itemLore)){
                currentLootTable = lootName;
            }
        }
        if (currentLootTable == null){
            return;
        }

        // Giving algorithm
        if (LootTablesConfig.get().getBoolean(currentLootTable + ".guaranteed.enabled")){
            //Guaranteed items algo
            ConfigurationSection content = LootTablesConfig.get().getConfigurationSection(currentLootTable + ".content");
            List<String> contentKeys = new ArrayList<>(content.getKeys(false));
            int itemsFilled = 0;
            int itemsDesired = LootTablesConfig.get().getInt(currentLootTable + ".guaranteed.items");
            while (itemsFilled < itemsDesired){

                Collections.shuffle(contentKeys);
                double rand = Math.random();
                if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") > rand){
                    //Succes, get the item
                    ItemStack itemToGive = (ItemStack) LootTablesConfig.get().get(currentLootTable + ".content." + contentKeys.get(0) + ".item");
                    player.getInventory().addItem(itemToGive);
                    itemsFilled++;
                    if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") <= 0.01){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&bLUCKY! &a" + MyStringUtils.itemCamelCase(itemToGive.getType().toString()) + "&bhad a chance of &a" +
                                        LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") * 100
                                        + "%!"));
                    }
                }
            }
        }
        else {
            ConfigurationSection content = LootTablesConfig.get().getConfigurationSection(currentLootTable + ".content");
            List<String> contentKeys = new ArrayList<>(content.getKeys(false));

            for (String key : contentKeys){
                double rand = Math.random();
                if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + key + ".chance") > rand){
                    //Succes, get the item
                    ItemStack itemToGive = (ItemStack) LootTablesConfig.get().get(currentLootTable + ".content." + key + ".item");
                    player.getInventory().addItem(itemToGive);
                    if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + key + ".chance") <= 0.01){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&bLUCKY! &a" + MyStringUtils.itemCamelCase(itemToGive.getType().toString()) + "&bhad a chance of&a " +
                                LootTablesConfig.get().getDouble(currentLootTable + ".content." + key + ".chance") * 100 + "%!"));
                    }
                }
            }
        }

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        player.getInventory().remove(heldItem);

        if (heldItem.getAmount() > 1){
            int itemsAdded = 0;
            int desiredAmount = heldItem.getAmount() - 1;
            while(itemsAdded < desiredAmount){
                ItemStack heldItemSingle = heldItem;
                heldItemSingle.setAmount(1);
                player.getInventory().addItem(heldItemSingle);
                itemsAdded++;
            }

        }


        LootTablesConfig.save();
        LootTablesConfig.reload();
    }
}
