package me.lifelessnerd.purekitpvp.kitCommand;


import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.util.NumberConversions.floor;

public class KitsGUI implements TabExecutor {

    Plugin plugin;

    public KitsGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){
            return false;
        }

        if (!KitConfig.get().isSet("kits.")) {
            player.sendMessage(LanguageConfig.lang.get("KITS_NO_KITS"));
            return true;
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(LanguageConfig.lang.get("GENERIC_WRONG_WORLD").
                    replaceText(ComponentUtils.replaceConfig("%WORLD%",plugin.getConfig().getString("world"))));
            return true;
        }

        // Only argument is used for page, the plugin will run /kits 2 at some point for the second page of kits
        int currentPage = 1;
        if (args.length >= 1) {
            currentPage = Integer.parseInt(args[0]);
        }
        //Create inventory GUI
        Component inventoryTitle = LanguageConfig.lang.get("KITS_GUI_TITLE").appendSpace().append(Component.text(currentPage));
        Inventory kits = Bukkit.createInventory(null, 54, inventoryTitle);
        int amountOfKits = KitConfig.get().getConfigurationSection("kits").getKeys(false).size();
        int lastPage = floor(((double) amountOfKits / 46)) + 1;
        // Will a second page be necessary?
        if (amountOfKits >= 45 && (currentPage != lastPage)){ // On last page? Don't show next item
            // Create next page item
            ItemStack nextPageButton = new ItemStack(Material.ARROW);
            ItemMeta nextPageButtonMeta = nextPageButton.getItemMeta();
            nextPageButtonMeta.displayName(LanguageConfig.lang.get("KITS_GUI_NEXT"));
            nextPageButton.setItemMeta(nextPageButtonMeta);
            kits.setItem(50, nextPageButton);

        }
        if (currentPage > 1){
            // If this, we need a prev button too
            ItemStack prevPageButton = new ItemStack(Material.ARROW);
            ItemMeta prevPageButtonMeta = prevPageButton.getItemMeta();
            prevPageButtonMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PREV"));
            prevPageButton.setItemMeta(prevPageButtonMeta);
            kits.setItem(48, prevPageButton);
        }

        int kitNumber = 1;
        for (String key : KitConfig.get().getConfigurationSection("kits").getKeys(false)) {

            // What page should this kit be put on?
            int intendedPage = floor(((double) kitNumber / 46)) + 1;
            kitNumber++;
            if (intendedPage != currentPage){ // Not this page? Don't do anything
                continue;
            }

            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            //Set gui item
            itemStack.setType(Material.getMaterial(KitConfig.get().getString("kits." + key + ".guiitem")));

            LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

            //Set lore
            ArrayList<Component> lore = new ArrayList<>();

            // Quip lore at the top
            Component loreText = serializer.deserialize(KitConfig.get().getString("kits." + key + ".guilore"));
            lore.add(loreText);

            // If no permission: set gui item to red stained glass pane and a bit of lore is added to explain the situation
            if (!(player.hasPermission(KitConfig.get().getString("kits." + key + ".permission")))){
                itemStack.setType(Material.RED_STAINED_GLASS_PANE);
                lore.add(LanguageConfig.lang.get("KITS_GUI_NO_PERMISSION"));
            }

            // Item lore that consists of contents of kit
            FileConfiguration fileConfiguration = KitConfig.get();
            List<ItemStack> kitContent = (List<ItemStack>) fileConfiguration.get("kits." + key + ".contents");
            lore.add(LanguageConfig.lang.get("KITS_GUI_WEAPONS"));
            for (int index = 0; index < kitContent.size(); index++) {

                ItemStack item = kitContent.get(index);

                switch (index) {
                    case 3 -> lore.add(LanguageConfig.lang.get("KITS_GUI_ITEMS")); //TODO change 3 value to user-config?
                    case 36 -> {
                        if(kitContent.get(36) != null | kitContent.get(37) != null | kitContent.get(38) != null | kitContent.get(39) != null) {
                            lore.add(LanguageConfig.lang.get("KITS_GUI_ARMOR"));
                        } else {
                            lore.add(LanguageConfig.lang.get("KITS_GUI_NO_ARMOR"));
                        }
                    }
                    case 40 -> {
                        if (item != null){
                            lore.add(LanguageConfig.lang.get("KITS_GUI_OFFHAND"));
                        }
                    }
                }



                if (item == null) {
                    item = new ItemStack(Material.AIR);
                } else if (item.getType().toString().equalsIgnoreCase("DIAMOND")){
                    // Some old kits have a diamond for identification of that kit, that is legacy
                    //Diamond check; do nothing

                } else if (item.getType().toString().equalsIgnoreCase("SPLASH_POTION")){
                    //Do stuff with potions
                    String amount = String.valueOf(item.getAmount());
                    lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(MyStringUtils.itemCamelCase(item.getType().toString()), NamedTextColor.YELLOW)));
                    lore.add(Component.text("    " + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()), NamedTextColor.GRAY));

                } else if (item.getType().toString().equalsIgnoreCase("POTION")){
                    //Do stuff with potions
                    String amount = String.valueOf(item.getAmount());
                    lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(MyStringUtils.itemCamelCase(item.getType().toString()), NamedTextColor.YELLOW)));
                    lore.add(Component.text("    " + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()), NamedTextColor.GRAY));

                } else if (item.getType().toString().equalsIgnoreCase("PLAYER_HEAD")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                            Component.text("Golden Head", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
                    lore.add(Component.text("    ", NamedTextColor.GRAY).append(item.getItemMeta().lore().get(0).color(NamedTextColor.GRAY)));

                } else if (item.getType().toString().equalsIgnoreCase("CHEST")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                            Component.text("Random Loot Chest", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
                    lore.add(Component.text("    ", NamedTextColor.GRAY).append(item.getItemMeta().lore().get(0).color(NamedTextColor.GRAY)));

                } else {
                    String amount = String.valueOf(item.getAmount());
                    lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(MyStringUtils.itemCamelCase(item.getType().toString()), NamedTextColor.YELLOW)));
                    //If it has enchants, view them
                    if (!(item.getEnchantments().isEmpty())){
                        lore.add(Component.text("    " + MyStringUtils.mapStringToEnchantment(item.getEnchantments().toString()), NamedTextColor.GRAY));
                    }

                }
            }
            //Set KillItem as lore
            ItemStack killItem = (ItemStack) KitConfig.get().get("kits." + key + ".killitem");

            if (killItem.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) {
                lore.add(LanguageConfig.lang.get("KITS_GUI_KILL_ITEM"));
                lore.add(Component.text("1x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("Golden Head", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
                lore.add(Component.text("    ", NamedTextColor.GRAY).append(Component.text("Healing Item")).color(NamedTextColor.GRAY));

            } else if (killItem.getType().toString().equalsIgnoreCase("CHEST")){
                lore.add(LanguageConfig.lang.get("KITS_GUI_KILL_ITEM"));
                int amount = killItem.getAmount();
                lore.add(Component.text("1x ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).
                        append(Component.text("Random Loot Chest", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
                lore.add(Component.text("    " + killItem.getItemMeta().lore().get(0), NamedTextColor.GRAY)); //might not be needed
                //System.out.println(killItem.getItemMeta().getLore().get(0));

            } else if (killItem.getType().toString().equalsIgnoreCase("AIR")){
                lore.add(LanguageConfig.lang.get("KITS_GUI_NO_KILL_ITEM"));
            } else {
                lore.add(LanguageConfig.lang.get("KITS_GUI_KILL_ITEM"));
                int amount = killItem.getAmount();
                lore.add(Component.text(amount + "x ", NamedTextColor.GRAY).
                        append(Component.text(MyStringUtils.itemCamelCase(killItem.getType().toString()), NamedTextColor.YELLOW)));

            }

            itemMeta.lore(lore);

            //Set meta
            String kitDisplayColor = KitConfig.get().getString("kits." + key + ".displayname");
            if (kitDisplayColor.startsWith("&")){
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', kitDisplayColor + key)); //For legacy kits
            } else {
                itemMeta.displayName(Component.text(key, NamedTextColor.NAMES.value(kitDisplayColor)).decoration(TextDecoration.ITALIC, false));
            }
            itemStack.setItemMeta(itemMeta);

            //Add items to inventory
            kits.addItem(itemStack);

            //Reset button
            ItemStack resetButton = new ItemStack(Material.BARRIER);
            ItemMeta resetButtonMeta = resetButton.getItemMeta();
            resetButtonMeta.displayName(LanguageConfig.lang.get("KITS_GUI_RESET"));
            List<Component> loreList = new ArrayList<>();
            loreList.addAll(ComponentUtils.splitComponent(LanguageConfig.lang.get("KITS_GUI_RESET_LORE")));
            resetButtonMeta.lore(loreList);
            resetButton.setItemMeta(resetButtonMeta);
            kits.setItem(53, resetButton);

//            Perk Redirect Button
            ItemStack perkHelpButton = new ItemStack(Material.BOOK);
            ItemMeta perkHelpButtonMeta = perkHelpButton.getItemMeta();
            perkHelpButtonMeta.displayName(LanguageConfig.lang.get("KITS_GUI_PERKS"));
            perkHelpButtonMeta.lore(ComponentUtils.splitComponent(LanguageConfig.lang.get("KITS_GUI_PERKS_LORE")));
            perkHelpButton.setItemMeta(perkHelpButtonMeta);
            kits.setItem(49, perkHelpButton);


            player.openInventory(kits);

        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
