package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.AbstractInventory;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class EditKitInventory extends AbstractInventory {
    Plugin plugin;
    String kitName;
    public EditKitInventory(int size, Component title, Plugin plugin, String kitName) {
        super(size, title, plugin);
        this.kitName = kitName;
        this.plugin = plugin;
        initializeItems();
    }

    @Override
    public void initializeItems() {
        FileConfiguration fileConfiguration = KitConfig.get();
        List<ItemStack> kitItems = (List<ItemStack>) fileConfiguration.get("kits." + kitName + ".contents");

        for (int index = 0; index < kitItems.size(); index++) {
            ItemStack item = kitItems.get(index);
            if (item == null) {
                item = new ItemStack(Material.AIR);
            }
            inv.setItem(index, item);
        }

        ItemStack redStainedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = redStainedGlass.getItemMeta();
        glassMeta.displayName(Component.text(""));
        redStainedGlass.setItemMeta(glassMeta);

        for (int i = 41; i < 54; i++) inv.setItem(i, redStainedGlass);

        ItemStack greenConcrete = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta saveMeta = greenConcrete.getItemMeta();
        saveMeta.displayName(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_SAVE.toString()));
        saveMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_GUI_EDIT_ITEM"), PersistentDataType.STRING, "Save");
        saveMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_GUI_EDIT_ITEM_KIT"), PersistentDataType.STRING, kitName);
        greenConcrete.setItemMeta(saveMeta);
        inv.setItem(49, greenConcrete);

    }

    @Override
    public void onInventoryClickLogic(InventoryClickEvent e) {

        if (e.getSlot() >= 41){ // red panes
            e.setCancelled(true);
        }
        if (e.getSlot() == 49){ // save icon
            e.setCancelled(true);

            ItemStack[] kitContents = inv.getContents();
            int index = 0;
            for (ItemStack item : inv.getContents()){
                if (index >= 41) {
                    kitContents[index] = null;
                }

                index++;
            }
            KitConfig.get().set("kits." + kitName + ".contents", kitContents);
            KitConfig.save();
            KitConfig.reload();

            e.getWhoClicked().sendMessage(LanguageConfig.lang.get(LanguageKey.KITS_GUI_EDIT_SAVED.toString()));
            inv.close();

        }

    }
}
