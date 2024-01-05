package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.AbstractInventory;
import me.lifelessnerd.purekitpvp.PureKitPvP;
import me.lifelessnerd.purekitpvp.database.KitDatabase;
import me.lifelessnerd.purekitpvp.database.entities.PlayerKitPreferences;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitPreviewInventory extends AbstractInventory {
    public String kitName;
    KitDatabase db;
    Player player;

    public KitPreviewInventory(int size, Component title, Plugin plugin, String kitName, Player playerToBeShown) {
        super(size, title, plugin);
        this.kitName = kitName;
        this.player = playerToBeShown;
        PureKitPvP pureKitPvP = (PureKitPvP) this.plugin;
        db = pureKitPvP.getKitDatabase();
        initializeItems();
    }

    @Override
    public void initializeItems() {
        FileConfiguration fileConfiguration = KitConfig.get();
        List<ItemStack> kitItems = (List<ItemStack>) fileConfiguration.get("kits." + kitName + ".contents");

        Map<Integer, Integer> playerPrefs = new HashMap<>();
        try {
            if(db.hasEntry(player, kitName)){
                PlayerKitPreferences playerKitPreferences = db.getEntry(player, kitName);
                playerPrefs = playerKitPreferences.getPreferences();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int index = 0; index < kitItems.size(); index++) {
            ItemStack item = kitItems.get(index);
            if (item == null) continue;
            inv.setItem(playerPrefs.getOrDefault(index, index), item);
        }

        ItemStack redStainedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = redStainedGlass.getItemMeta();
        glassMeta.displayName(Component.text(""));
        redStainedGlass.setItemMeta(glassMeta);

        inv.setItem(45, redStainedGlass);
        inv.setItem(46, redStainedGlass);
        inv.setItem(52, redStainedGlass);
        inv.setItem(53, redStainedGlass);

        ItemStack selectItem = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta selectMeta = selectItem.getItemMeta();
        selectMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_SELECT"));
        selectMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Select");
        selectMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING, kitName);
        selectItem.setItemMeta(selectMeta);
        inv.setItem(50, selectItem);

        ItemStack paper =  new ItemStack(Material.PAPER, 1);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_STATS"));
        paperMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Stats");
        ArrayList<Component> lore2 = new ArrayList<>();
        int kitChosen = KitStatsConfig.get().getInt(kitName);
        lore2.add(Component.text("Times Chosen: ", NamedTextColor.AQUA).append(Component.text(kitChosen, NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false));
        paperMeta.lore(lore2);
        paper.setItemMeta(paperMeta);
        inv.setItem(51, paper);

        ItemStack anvil =  new ItemStack(Material.ANVIL, 1);
        ItemMeta anvilMeta = anvil.getItemMeta();
        anvilMeta.displayName(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREVIEW_PREFS.toString()));
        anvilMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Edit");
        anvilMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING, kitName);
        anvil.setItemMeta(anvilMeta);
        inv.setItem(49, anvil);

        ItemStack backItem = new ItemStack(Material.ARROW, 1);
        ItemMeta backMeta = selectItem.getItemMeta();
        backMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREVIEW_BACK"));
        backMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING, "Back");
        backItem.setItemMeta(backMeta);
        inv.setItem(48, backItem);
    }

    @Override
    public void onInventoryClickLogic(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        String kitName;

        if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"))){
            String pdcValue = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM"), PersistentDataType.STRING);
            switch(pdcValue){
                case "Select":
                    kitName = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING);
                    player.chat("/getkit " + kitName);
                    player.closeInventory();
                    break;
                case "Stats":
                    //Can be used later?
                    break;
                case "Back":
                    player.chat("/kit");
                    break;
                case "Edit":
                    kitName = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_PREVIEW_GUI_ITEM_KIT"), PersistentDataType.STRING);
                    KitPreferencesInventory kitPreferencesInventory = new KitPreferencesInventory(54,
                            LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_TITLE.toString()).
                                    replaceText(ComponentUtils.replaceConfig("%KIT%", kitName)).
                                    decoration(TextDecoration.ITALIC, false),
                            plugin, kitName, player);
                    kitPreferencesInventory.openInventory(player);
                    break;
                default:
                    break;
            }
        }
    }
}
