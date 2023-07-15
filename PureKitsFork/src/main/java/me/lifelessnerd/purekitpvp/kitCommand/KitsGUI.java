package me.lifelessnerd.purekitpvp.kitCommand;


import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
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

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (!KitConfig.get().isSet("kits.")) {
            player.sendMessage(ChatColor.GRAY + "There aren't any kits yet!");
            return true;
        }

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(ChatColor.RED + "You can only use this menu in " + ChatColor.GRAY + plugin.getConfig().getString("world"));
            return true;
        }

        // Only argument is used for page, the plugin will run /kits 2 at some point for the second page of kits
        int currentPage = 1;
        if (args.length >= 1) {
            currentPage = Integer.parseInt(args[0]);
        }
        //Create inventory GUI
        TextComponent inventoryTitle = Component.text("Kits - ").
                color(TextColor.color(255, 150, 20)).
                append(Component.text(currentPage));
        Inventory kits = Bukkit.createInventory(null, 54, inventoryTitle);
        int amountOfKits = KitConfig.get().getConfigurationSection("kits").getKeys(false).size();

        // Will a second page be necessary?
        if (amountOfKits >= 45){
            // Create next page item
            ItemStack nextPageButton = new ItemStack(Material.ARROW);
            ItemMeta nextPageButtonMeta = nextPageButton.getItemMeta();
            nextPageButtonMeta.displayName(Component.text("Next"));
            List<Component> loreList = new ArrayList<>();
            TextComponent txt = Component.text("Go to the next kit selection page.").color(TextColor.color(100,100,100));
            loreList.add(txt);
            nextPageButtonMeta.lore(loreList);
            nextPageButton.setItemMeta(nextPageButtonMeta);
            kits.setItem(50, nextPageButton);
            loreList.clear();

        }
        if (currentPage > 1){
            // If this, we need a prev button too
            ItemStack prevPageButton = new ItemStack(Material.ARROW);
            ItemMeta prevPageButtonMeta = prevPageButton.getItemMeta();
            prevPageButtonMeta.displayName(Component.text("Previous"));
            List<Component> loreList = new ArrayList<>();
            TextComponent txt = Component.text("Go to the previous kit selection page.").color(TextColor.color(100,100,100));
            loreList.add(txt);
            prevPageButtonMeta.lore(loreList);
            prevPageButton.setItemMeta(prevPageButtonMeta);
            kits.setItem(48, prevPageButton);
            loreList.clear();
        }

        int kitNumber = 1;
        for (String key : KitConfig.get().getConfigurationSection("kits").getKeys(false)) {

            // What page should this kit be put on?
            int intendedPage = floor(((double) kitNumber / 45)) + 1;
            kitNumber++;
            if (intendedPage != currentPage){ // Not this page? Don't do anything
                continue;
            }

            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            //Set block
            itemStack.setType(Material.getMaterial(KitConfig.get().getString("kits." + key + ".guiitem")));

            //Set lore
            String loreText = KitConfig.get().getString("kits." + key + ".guilore");
            loreText = ChatColor.translateAlternateColorCodes('&', loreText);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(loreText);

            // Item lore that consists of contents of kit
            FileConfiguration fileConfiguration = KitConfig.get();
            List<ItemStack> kitContent = (List<ItemStack>) fileConfiguration.get("kits." + key + ".contents");
            lore.add(ChatColor.BLUE + "Weapons:");
            for (int index = 0; index < kitContent.size(); index++) {

                ItemStack item = kitContent.get(index);

                switch (index) {
                    case 3 -> lore.add(ChatColor.BLUE + "Items:");
                    case 36 -> {
                        if(kitContent.get(36) != null | kitContent.get(37) != null | kitContent.get(38) != null | kitContent.get(39) != null) {
                            lore.add(ChatColor.BLUE + "Armor:");
                        } else {
                            lore.add(ChatColor.BLUE + "No Armor");
                        }
                    }
                    case 40 -> {
                        if (item != null){
                            lore.add(ChatColor.BLUE + "Offhand:");
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
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    lore.add("    " + ChatColor.GRAY + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()));

                } else if (item.getType().toString().equalsIgnoreCase("POTION")){
                    //Do stuff with potions
                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    lore.add("    " + ChatColor.GRAY + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()));

                } else if (item.getType().toString().equalsIgnoreCase("PLAYER_HEAD")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase("golden_head"));
                    lore.add(ChatColor.GRAY + "    " + item.getItemMeta().getLore().get(0));
                } else if (item.getType().toString().equalsIgnoreCase("CHEST")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase("random_loot_chest"));
                    lore.add(ChatColor.GRAY + "    " + item.getItemMeta().getLore().get(0));

                } else {
                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    //If it has enchants, view them
                    if (!(item.getEnchantments().isEmpty())){
                        lore.add("    " + ChatColor.GRAY + MyStringUtils.mapStringToEnchantment(item.getEnchantments().toString()));
                    }

                }
            }
            //Set KillItem as lore
            ItemStack killItem = (ItemStack) KitConfig.get().get("kits." + key + ".killitem");

            if (killItem.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) {
                lore.add(ChatColor.WHITE + "Item on Kill:");
                lore.add(ChatColor.GRAY + "1x " + ChatColor.YELLOW + "Golden Head"); //fixedI would just get the displayname to make this more dynamic but I cant because of fecking component
                lore.add(ChatColor.GRAY + "    " + killItem.getItemMeta().getLore().get(0));

            } else if (killItem.getType().toString().equalsIgnoreCase("CHEST")){
                lore.add(ChatColor.WHITE + "Item on Kill:");
                int amount = killItem.getAmount();
                lore.add(ChatColor.GRAY + "" +  amount + "x " + ChatColor.YELLOW + "Random Loot Chest");
                lore.add(ChatColor.GRAY + "    " + killItem.getItemMeta().getLore().get(0));
                //System.out.println(killItem.getItemMeta().getLore().get(0));

            } else if (killItem.getType().toString().equalsIgnoreCase("AIR")){
                lore.add(ChatColor.WHITE + "No Item on Kill");
            } else {
                lore.add(ChatColor.WHITE + "Item on Kill:");
                int amount = killItem.getAmount();
                lore.add(ChatColor.GRAY + "" +  amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(killItem.getType().toString()));

            }

            itemMeta.setLore(lore); //Heck you Component

            //Set meta
            String kitDisplayColor = KitConfig.get().getString("kits." + key + ".displayname");
            kitDisplayColor = ChatColor.translateAlternateColorCodes('&', kitDisplayColor);
            itemMeta.setDisplayName(kitDisplayColor + key); //Heck you Component
            itemStack.setItemMeta(itemMeta);

            //Add items to inventory
            kits.addItem(itemStack);

            //Reset button
            ItemStack resetButton = new ItemStack(Material.BARRIER);
            ItemMeta resetButtonMeta = resetButton.getItemMeta();
            resetButtonMeta.displayName(Component.text("Reset kit"));
            List<Component> loreList = new ArrayList<>();
            TextComponent txt = Component.text("If you do not have permission to reset your kit,").color(TextColor.color(100,100,100));
            TextComponent txt2 = Component.text("this will run /suicide on your behalf.").color(TextColor.color(100,100,100));
            loreList.add(txt);
            loreList.add(txt2);
            resetButtonMeta.lore(loreList);
            resetButton.setItemMeta(resetButtonMeta);
            kits.setItem(53, resetButton);

//            Perk Redirect Button
            ItemStack perkHelpButton = new ItemStack(Material.BOOK);
            ItemMeta perkHelpButtonMeta = perkHelpButton.getItemMeta();
            perkHelpButtonMeta.displayName(Component.text("Perk Info"));
            List<Component> loreList2 = new ArrayList<>();
            TextComponent txt21 = Component.text("Click here to change your perks!").color(TextColor.color(100,100,100));
            loreList2.add(txt21);
            perkHelpButtonMeta.lore(loreList2);
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
