package me.lifelessnerd.purekitpvp.perks.perkCommand;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.PerkData;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkLib;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PerkCommand implements CommandExecutor {
    Plugin plugin;

    public PerkCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_WORLD").
                    replaceText(ComponentUtils.replaceConfig("%WORLD%",plugin.getConfig().getString("world"))));
            return true;
        }
        if (GetKit.hasKit.contains(player.getName())){
            player.sendMessage(LanguageConfig.lang.get("PERKS_ALREADY_SELECTED"));
            return true;
        }
        //Create inventory GUI
        Component invTitle = LanguageConfig.lang.get("PERKS_GUI_TITLE").decoration(TextDecoration.ITALIC, false);
        Inventory perksInventory = Bukkit.createInventory(null, 54, invTitle);

        //Info item
        ItemStack infoItem = new ItemStack(Material.BOOK);
        Component infoItemName = LanguageConfig.lang.get("PERKS_GUI_INFO_TITLE");
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.displayName(infoItemName);

        ArrayList<Component> infoLore = ComponentUtils.splitComponent(LanguageConfig.lang.get("PERKS_GUI_INFO_LORE"));

        infoMeta.lore(infoLore);
        infoItem.setItemMeta(infoMeta);
        perksInventory.setItem(13, infoItem);

        //Back to kits item
        ItemStack kitItem = new ItemStack(Material.ARROW);
        Component kitItemName = LanguageConfig.lang.get("PERKS_GUI_BACK_KITS");
        ItemMeta kitItemMeta = kitItem.getItemMeta();
        kitItemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "PERKS_GUI_BACK_KITS"), PersistentDataType.BOOLEAN, true);
        kitItemMeta.displayName(kitItemName);
        kitItem.setItemMeta(kitItemMeta);
        perksInventory.setItem(49, kitItem);


        //Fill perk slots with either empty or actual perk icon
        for (int index = 20; index <= 24; index++){
            int slot = index - 19;
            String fileContent = PerkData.get().getString(player.getName() + ".slot" + slot);
            //System.out.println(fileContent);
            if (fileContent == null){
                // No perk selected in this slot; show red stained-glass
                ItemStack perkSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                Component perkSlotName = LanguageConfig.lang.get("PERKS_GUI_SLOT_TITLE").
                        replaceText(ComponentUtils.replaceConfig("%SLOT%", String.valueOf(slot)));
                ItemMeta perkSlotMeta = perkSlot.getItemMeta();
                perkSlotMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "PERKS_GUI_SLOT_TITLE"), PersistentDataType.INTEGER, slot);
                perkSlotMeta.displayName(perkSlotName);
                perkSlotMeta.lore(ComponentUtils.splitComponent(LanguageConfig.lang.get("PERKS_GUI_SLOT_LORE")));
                perkSlot.setItemMeta(perkSlotMeta);
                perkSlot.setAmount(slot);
                perksInventory.setItem(index, perkSlot);

            } else {
                PerkLib perkLib = new PerkLib();
                ItemStack perkSlot = new ItemStack(perkLib.perkIcons.get(fileContent));
                Component perkSlotName = LanguageConfig.lang.get("PERKS_GUI_SLOT_TITLE").
                        replaceText(ComponentUtils.replaceConfig("%SLOT%", String.valueOf(slot)));
                ItemMeta perkSlotMeta = perkSlot.getItemMeta();
                perkSlotMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "PERKS_GUI_SLOT_TITLE"), PersistentDataType.INTEGER, slot);
                perkSlotMeta.displayName(perkSlotName);

                ArrayList<Component> loreTBA = new ArrayList<>();
                Component loreTitle = Component.text(fileContent).color(TextColor.color(0, 230, 0)).decoration(TextDecoration.ITALIC, false);
                loreTBA.add(loreTitle);

                ArrayList<Component> perkLore = ComponentUtils.splitComponent(perkLib.perks.get(fileContent));
                loreTBA.addAll(perkLore);

                perkSlotMeta.lore(loreTBA);
                perkSlotMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                perkSlot.setItemMeta(perkSlotMeta);
                perkSlot.setAmount(slot);
                perksInventory.setItem(index, perkSlot);


            }
        }

        player.openInventory(perksInventory);
//        System.out.println(PerkData.get().getString(player.getName()));
        // Create perks config values if not present
        if (PerkData.get().getString(player.getName()) == null){
            Set<String> empty = new HashSet<>();
            PerkData.get().set(player.getName(), empty);

            PerkData.save();
            PerkData.reload();
        }
        return true;
    }
}
