package me.lifelessnerd.purekitpvp.customItems;

import me.lifelessnerd.purekitpvp.PluginGetter;
import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.files.MobSpawnConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCustomItem extends Subcommand {
    @Override
    public String getName() {
        return "getcustomitem";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get a custom item to use in a kit";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp getcustomitem <golden_head/random_chest/custom_mob_egg>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Plugin plugin = PluginGetter.Plugin();
        if (args[1].equalsIgnoreCase("golden_head")){


            ItemStack goldenHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) goldenHead.getItemMeta();
            meta.displayName(Component.text("Golden Head").decoration(TextDecoration.ITALIC, false));
            PersistentDataContainer itemContainer = meta.getPersistentDataContainer();
            itemContainer.set(new NamespacedKey(plugin, "golden_head"), PersistentDataType.BOOLEAN, true);
            // Deprecated lore system
                String[] loreList = {"Healing Item"};
                meta.setLore(Arrays.asList(loreList));
            //
            meta.setOwningPlayer(Bukkit.getOfflinePlayer("PhantomTupac"));
            goldenHead.setItemMeta(meta);

            player.getInventory().addItem(goldenHead);

            return true;


        } else if (args[1].equalsIgnoreCase("random_chest")){

            if (!(LootTablesConfig.get().isSet(args[2]))){
                player.sendMessage(Component.text("Such loot table does not exist!"));
                return false;
            }
            String lore = LootTablesConfig.get().getString(args[2] + ".desiredLore");
            String displayName = LootTablesConfig.get().getString(args[2] + ".displayName");

            ItemStack chestItem = new ItemStack(Material.CHEST);

            ItemMeta itemMeta = chestItem.getItemMeta();
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "loottable_id"), PersistentDataType.STRING, args[2]);
            List<String> loreList = new ArrayList<>();
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
            itemMeta.setLore(loreList);
            itemMeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&',displayName)));
//        itemMeta.setCustomModelData(1);
            chestItem.setItemMeta(itemMeta);
            player.getInventory().addItem(chestItem);

            return true;

        } else if(args[1].equalsIgnoreCase("custom_mob_egg")){

            if (!(MobSpawnConfig.get().isSet(args[2]))){
                player.sendMessage("Such custom mob does not exist!");
                return false;
            }
            // Try catch dit miss?
            String type = MobSpawnConfig.get().getString(args[2] + ".type");
            ItemStack egg = new ItemStack(Material.valueOf(type + "_SPAWN_EGG"));
            ItemMeta eggMeta = egg.getItemMeta();
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Custom Mob Spawner Egg"));
            eggMeta.lore(lore);
            PersistentDataContainer data = eggMeta.getPersistentDataContainer();

            data.set(new NamespacedKey(plugin, "custom_mob_id"), PersistentDataType.STRING, args[2]);

            egg.setItemMeta(eggMeta);
            player.getInventory().addItem(egg);

        }
        return false;
    }
}
