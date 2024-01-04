package me.lifelessnerd.purekitpvp.customItems.loottable;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CreateLootTable extends Subcommand {
    Plugin plugin;

    public CreateLootTable(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "createloottable";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Create a loot table by looking at a chest";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp createloottable <name> <lore> <displayname>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (!(player.hasPermission("purekitpvp.admin.createloottable"))) {
            player.sendMessage(LanguageConfig.lang.get("GENERIC_NO_PERMISSION"));
            return true;
        }

        if (!(args.length >= 4)){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_LACK_OF_ARGS"));
            return false;
        }
        String name = args[1];
        String desiredLore = args[2];
        String displayName = args[3];

        Block targetBlock = player.getTargetBlock(null, 10);

        if(!(targetBlock.getType() == Material.CHEST)){
            player.sendMessage(Component.text("Please look at a chest!"));
            return true;
        }

        if (!(targetBlock.getState() instanceof Chest targetChest)){
            player.sendMessage(Component.text("Please look at a chest!"));
            return true;
        }

        FileConfiguration lootTables = LootTablesConfig.get();
        ConfigurationSection section = lootTables.createSection(name);
        lootTables.set(name + ".desiredLore", desiredLore);
        lootTables.set(name + ".displayName", displayName);
        ConfigurationSection fillModeSection = lootTables.createSection(name + ".guaranteed");
        lootTables.set(name + ".guaranteed.enabled", false);
        lootTables.set(name + ".guaranteed.items", 3);
        ConfigurationSection itemContent = lootTables.createSection(name + ".content");

        Inventory chestInventory = targetChest.getBlockInventory();

        int index = 0;
        for (ItemStack item : chestInventory.getContents()){
            if (item == null){
                continue;
            } else {
                lootTables.set(name + ".content." + index + ".item", item);
                lootTables.set(name + ".content." + index + ".chance", 0.1);
                index++;
            }
        }

        player.sendMessage(
                "Loot table created. \nTo specify chances per item, and add multi-word lore" +
                        " please use the loottables.yml config!");
        player.sendMessage("When manually editing loottables.yml, remember to back it up if it contains a lot of information.");

        LootTablesConfig.save();
        LootTablesConfig.reload();
        return true;

    }
}
