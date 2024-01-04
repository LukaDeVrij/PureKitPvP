package me.lifelessnerd.purekitpvp.perks.perkCommand;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.PerkData;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkLib;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class PerkGUIListener implements Listener {
    Plugin plugin;

    public PerkGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (e.getCurrentItem() == null || e.getRawSlot() >= 53) {
            return;
        }
        // For later (for the else if)
        TextComponent desiredInvTitle = (TextComponent) LanguageConfig.lang.get("PERKS_GUI_SLOT_TITLE").
                replaceText(ComponentUtils.replaceConfig("%SLOT%", ""));
        PlainTextComponentSerializer ptcs = PlainTextComponentSerializer.plainText();
        //
        ItemStack clickedItem = e.getCurrentItem();
        InventoryView inv = e.getView();
        if (inv.title().equals(LanguageConfig.lang.get("PERKS_GUI_TITLE"))) {
            e.setCancelled(true);

            Component itemDisplayName = clickedItem.displayName();

            // This is when you click on a perk slot to modify it
            if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "PERKS_GUI_SLOT_TITLE"), PersistentDataType.INTEGER)) {
                int slot = clickedItem.getAmount();
                //Create inventory GUI
                Component invTitle = LanguageConfig.lang.get("PERKS_GUI_SLOT_TITLE").
                        replaceText(ComponentUtils.replaceConfig("%SLOT%", String.valueOf(slot)));
                Inventory perkSlotInventory = Bukkit.createInventory(null, 54, invTitle);

                //Fill inventory with interactive perk items
                PerkLib perkLib = new PerkLib();
                for (String perk : perkLib.perkIcons.keySet()) {

                    ItemStack icon = new ItemStack(perkLib.perkIcons.get(perk));
                    ItemMeta itemMeta = icon.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemMeta.displayName(Component.text(perk).color(TextColor.color(200, 0, 0)).decoration(TextDecoration.ITALIC, false));

                    // Check if it is selected: if so add glint
                    for(String selectedPerkKey : PerkData.get().getConfigurationSection(player.getName()).getKeys(false)){
                        String selectedPerk = PerkData.get().getString(player.getName() + "." + selectedPerkKey);
                        if (perk.equals(selectedPerk)){
                            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            itemMeta.displayName(Component.text(perk).color(TextColor.color(50, 200, 0)).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
                        }
                    }

                    icon.setItemMeta(itemMeta);

                    ArrayList<Component> loreTBA = new ArrayList<>();

                    Component perkLore = perkLib.perks.get(perk);
                    loreTBA.addAll(ComponentUtils.splitComponent(perkLore));

                    icon.lore(loreTBA);
                    perkSlotInventory.addItem(icon);
                }

                ItemStack backButton = new ItemStack(Material.ARROW);
                ItemMeta backButtonMeta = backButton.getItemMeta();
                backButtonMeta.displayName(LanguageConfig.lang.get("PERKS_GUI_BACK"));
                backButtonMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "PERKS_GUI_BACK"), PersistentDataType.BOOLEAN, true);
                backButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                backButton.setItemMeta(backButtonMeta);
                perkSlotInventory.setItem(49,backButton);


                player.openInventory(perkSlotInventory);
            }
            else if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "PERKS_GUI_BACK_KITS"), PersistentDataType.BOOLEAN)){
                player.chat("/kit");
            }
        }
        else if (ptcs.serialize(inv.title()).contains(desiredInvTitle.content())){
            e.setCancelled(true);
            Component title = inv.title();
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            String itemName = serializer.serialize(title);
//            String clickedItemName = serializer.serialize(e.getCurrentItem().displayName());
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "PERKS_GUI_BACK"))){
                player.chat("/perks");
            } else {

                int slot = Integer.parseInt(String.valueOf(itemName.charAt(itemName.length() - 1)));
                ItemStack clickedPerk = e.getCurrentItem();
                String displayName = serializer.serialize(clickedPerk.displayName());
                displayName = displayName.substring(1, displayName.length() - 1);
                PerkData.setPerk(player, displayName, slot);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,0);
                PerkData.save();
                PerkData.reload();

                player.chat("/perks");
            }

        }





    }

}
