package me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand;

import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CosmeticsCommand implements CommandExecutor {
    Plugin plugin;

    public CosmeticsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            player.sendMessage(ChatColor.RED + "You can only use this menu in " + ChatColor.GRAY + plugin.getConfig().getString("world"));
            return true;
        }
        //Create inventory GUI
        TextComponent invTitle = Component.text("Cosmetics Menu").color(TextColor.color(255, 150, 20));
        Inventory cosmeticsInventory = Bukkit.createInventory(null, 54, invTitle);

        // Kill cosmetics button
        ItemStack killEffectButton = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta killEffectButtonMeta = killEffectButton.getItemMeta();
        killEffectButtonMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        killEffectButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        killEffectButtonMeta.displayName(Component.text("Kill Effect").color(TextColor.color(200, 152, 0)).decoration(TextDecoration.ITALIC, false));
        List<Component> loreList2 = new ArrayList<>();
        TextComponent txt21 = Component.text("Change your kill effect cosmetic!").color(TextColor.color(100,100,100));
        loreList2.add(txt21);
        killEffectButtonMeta.lore(loreList2);
        killEffectButton.setItemMeta(killEffectButtonMeta);
        cosmeticsInventory.setItem(20, killEffectButton);

        ItemStack trailEffectButton = new ItemStack(Material.ARROW);
        ItemMeta trailEffectButtonItemMeta = trailEffectButton.getItemMeta();
        trailEffectButtonItemMeta.displayName(Component.text("Projectile Trail").color(TextColor.color(200, 152, 0)).decoration(TextDecoration.ITALIC, false));
        List<Component> loreList3 = new ArrayList<>();
        TextComponent txt22 = Component.text("Change your projectile trail cosmetic!").color(TextColor.color(100,100,100));
        loreList3.add(txt22);
        trailEffectButtonItemMeta.lore(loreList3);
        trailEffectButton.setItemMeta(trailEffectButtonItemMeta);
        cosmeticsInventory.setItem(22, trailEffectButton);

        ItemStack killMessageEffect = new ItemStack(Material.OAK_SIGN);
        ItemMeta killMessageEffectItemMeta = killMessageEffect.getItemMeta();
        killMessageEffectItemMeta.displayName(Component.text("Kill Message").color(TextColor.color(200, 152, 0)).decoration(TextDecoration.ITALIC, false));
        List<Component> loreList = new ArrayList<>();
        TextComponent txt = Component.text("Change your kill message cosmetic!").color(TextColor.color(100,100,100));
        loreList.add(txt);
        killMessageEffectItemMeta.lore(loreList);
        killMessageEffect.setItemMeta(killMessageEffectItemMeta);
        cosmeticsInventory.setItem(24, killMessageEffect);

        player.openInventory(cosmeticsInventory);

        return true;
    }
}
