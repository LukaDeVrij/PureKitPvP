package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.AbstractInventory;
import me.lifelessnerd.purekitpvp.PureKitPvP;
import me.lifelessnerd.purekitpvp.database.KitDatabase;
import me.lifelessnerd.purekitpvp.database.entities.PlayerKitPreferences;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitPreferencesInventory extends AbstractInventory {
    String kitName;
    Player player;
    Component title;
    KitDatabase db;
    public KitPreferencesInventory(int size, Component title, Plugin plugin, String kitName, Player toBeShownPlayer) {
        super(size, title, plugin);
        this.kitName = kitName;
        this.title = title;
        this.player = toBeShownPlayer;
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
            ItemMeta meta = item.getItemMeta();
            // Sets defaults indices as PDC - only after this we shift stuff around
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "KITS_GUI_PREFS_INDEX"), PersistentDataType.INTEGER, index);
            item.setItemMeta(meta);

            inv.setItem(playerPrefs.getOrDefault(index, index), item);
        }

        ItemStack redStainedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = redStainedGlass.getItemMeta();
        glassMeta.displayName(Component.text(""));
        redStainedGlass.setItemMeta(glassMeta);

        for (int i = 41; i < 54; i++) inv.setItem(i, redStainedGlass);

        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
        ItemMeta resetMeta = barrier.getItemMeta();
        resetMeta.displayName(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_RESET.toString()));
        barrier.setItemMeta(resetMeta);
        inv.setItem(50, barrier);

        ItemStack greenConcrete = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta saveMeta = greenConcrete.getItemMeta();
        saveMeta.displayName(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_SAVE.toString()));
        greenConcrete.setItemMeta(saveMeta);
        inv.setItem(49, greenConcrete);

        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemMeta backMeta = arrow.getItemMeta();
        backMeta.displayName(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_BACK.toString()));
        arrow.setItemMeta(backMeta);
        inv.setItem(48, arrow);

    }

    @Override
    public void onInventoryClickLogic(InventoryClickEvent e) {
        if (e.getSlot() < 41){
            e.setCancelled(false);
            return;
        }
        if (e.getSlot() >= 41) { // red panes
            e.setCancelled(true);
        }
        if (e.getSlot() == 50){ // reset layout
            try {
                if(db.hasEntry(player, kitName)){
                    db.updateEntry(player, kitName, new HashMap<>());
                    inv.close();
                    KitPreferencesInventory kitPreferencesInventory = new KitPreferencesInventory(54, title, plugin, kitName, player);
                    kitPreferencesInventory.openInventory(player);

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSlot() == 48){ // back button
            KitPreviewInventory kitPreviewInventory = new KitPreviewInventory(54,
                    LanguageConfig.lang.get("KITS_GUI_PREVIEW_TITLE").replaceText(ComponentUtils.replaceConfig("%KIT%", kitName)),
                    plugin,
                    kitName, player);
            kitPreviewInventory.openInventory(player);
            return;
        }
        if (e.getSlot() == 49) { // save icon

            HashMap<Integer, Integer> newPrefs = new HashMap<>();
            // See changes in inventory
            int index = 0;
            for (ItemStack item : inv.getContents()){
                if (index >= 41) break;
                if (item == null) {
                    index++;
                    continue;
                }
                ItemMeta itemMeta = item.getItemMeta();
                int oldIndex = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "KITS_GUI_PREFS_INDEX"), PersistentDataType.INTEGER);
                if (oldIndex != index){ // Change is different from standard!
                    System.out.println("change! item that was on " + oldIndex + " now on " + index);
                    newPrefs.put(oldIndex, index);
                }
                index++;
            }

            // Update db pref value
            try {
                db.updateEntry(player, kitName, newPrefs);
            } catch(Exception ex){
                throw new RuntimeException(ex);
            }
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.KITS_GUI_PREFS_SAVED.toString()));
            return;
        }
        e.setCancelled(true); // ensures no listeners are fighting
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent e){

        if (!e.getView().getTopInventory().equals(inv)) return;
        if (!e.getView().getBottomInventory().getType().equals(InventoryType.PLAYER)) return;
        if (e.getRawSlot() >= 54)
            e.setCancelled(true);
        // I hate this - BUG PRONE (am i missing an InventoryAction - if so > report immediately)
        if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
            e.setCancelled(true);
        if(e.getAction() == InventoryAction.HOTBAR_SWAP)
            e.setCancelled(true);
        if(e.getClickedInventory() != e.getView().getTopInventory())
            e.setCancelled(true);

    }
    @EventHandler
    // Should prevent issue https://www.spigotmc.org/threads/cancelling-inventoryclickevent-being-inconsistent.631817/
    public void onInventoryDrag(InventoryDragEvent e){
        if (!e.getView().getTopInventory().equals(inv)) return;
        if (!e.getView().getBottomInventory().getType().equals(InventoryType.PLAYER)) return;
        for (int slot : e.getRawSlots()){
            if (slot >= 54){
                e.setCancelled(true);
            }
        }
    }

}
