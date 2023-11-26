package me.lifelessnerd.purekitpvp.customitems;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (!(heldItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "loottable_id")))){
            return;
        }

        String currentLootTable = heldItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "loottable_id"), PersistentDataType.STRING);
        if (currentLootTable == null){
            player.sendMessage(Component.text("This loottable is not defined."));
            plugin.getLogger().warning("Player " + player.getName() + " attempted to open a loottable with name " + currentLootTable + ", but no loottable was found!");
            plugin.getLogger().warning("Player " + player.getName() + " current kit is " + PlayerStatsConfig.get().getString(player.getName() + ".current_kit"));
            return;
        }

        // Remove 1 golden head
        heldItem.setAmount(heldItem.getAmount() - 1);
        int slot = player.getInventory().getHeldItemSlot();
        player.getInventory().remove(heldItem);
        player.getInventory().setItem(slot, heldItem);

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
                    if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") <= (double) plugin.getConfig().getInt("loottable-message-percentage") / 100){
                        player.sendMessage(LanguageConfig.lang.get("LOOT_LUCKY").replaceText(
                                ComponentUtils.replaceConfig("%ITEM%",MyStringUtils.itemCamelCase(itemToGive.getType().toString()))).replaceText(
                                ComponentUtils.replaceConfig("%CHANCE%", String.valueOf(LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") * 100))));
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
                    if (LootTablesConfig.get().getDouble(currentLootTable + ".content." + key + ".chance") <= (double) plugin.getConfig().getInt("loottable-message-percentage") / 100){
                        player.sendMessage(LanguageConfig.lang.get("LOOT_LUCKY").replaceText(
                                ComponentUtils.replaceConfig("%ITEM%",MyStringUtils.itemCamelCase(itemToGive.getType().toString()))).replaceText(
                                ComponentUtils.replaceConfig("%CHANCE%", String.valueOf(LootTablesConfig.get().getDouble(currentLootTable + ".content." + contentKeys.get(0) + ".chance") * 100))));
                    }
                }
            }
        }

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);


        LootTablesConfig.save();
        LootTablesConfig.reload();
    }
}
