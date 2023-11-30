package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KitPreview {

    Plugin plugin;
    String kitName;
    Player player;

    public KitPreview(Plugin plugin, String kitName, Player player) {
        this.plugin = plugin;
        this.kitName = kitName;
        this.player = player;
    }

    public void openPreviewInventory(){

        Inventory previewInv = Bukkit.createInventory(null, 54, LanguageConfig.lang.get("KITS_GUI_PREVIEW_TITLE").replaceText(ComponentUtils.replaceConfig("%KIT%", kitName)));

        FileConfiguration fileConfiguration = KitConfig.get();
        List<ItemStack> kitItems = (List<ItemStack>) fileConfiguration.get("kits." + kitName + ".contents");

        for (int index = 0; index < kitItems.size(); index++) {
            ItemStack item = kitItems.get(index);
            if (item == null) {
                item = new ItemStack(Material.AIR);
            }
            previewInv.setItem(index, item);
        }

        ItemStack redStainedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = redStainedGlass.getItemMeta();
        glassMeta.displayName(Component.text(""));
        redStainedGlass.setItemMeta(glassMeta);

        previewInv.setItem(45, redStainedGlass);
        previewInv.setItem(46, redStainedGlass);
        previewInv.setItem(52, redStainedGlass);
        previewInv.setItem(53, redStainedGlass);

        ItemStack selectItem = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta selectMeta = selectItem.getItemMeta();
        selectMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_SELECT"));
        selectMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Select");
        selectMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING, kitName);
        selectItem.setItemMeta(selectMeta);
        previewInv.setItem(50, selectItem);

        ItemStack anvil =  new ItemStack(Material.ANVIL, 1);
        ItemMeta anvilMeta = anvil.getItemMeta();
        anvilMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_STATS"));
        anvilMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Stats");
        ArrayList<Component> lore2 = new ArrayList<>();
        int kitChosen = KitStatsConfig.get().getInt(kitName);
        lore2.add(Component.text("Times Chosen: ", NamedTextColor.AQUA).append(Component.text(kitChosen, NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false));
        anvilMeta.lore(lore2);
        anvil.setItemMeta(anvilMeta);
        previewInv.setItem(49, anvil);

        ItemStack backItem = new ItemStack(Material.ARROW, 1);
        ItemMeta backMeta = selectItem.getItemMeta();
        backMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_BACK"));
        backMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Back");
        backItem.setItemMeta(backMeta);
        previewInv.setItem(48, backItem);

        player.openInventory(previewInv);

    }
}
