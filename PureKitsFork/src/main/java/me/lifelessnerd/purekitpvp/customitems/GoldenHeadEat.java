package me.lifelessnerd.purekitpvp.customitems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.Objects;

public class GoldenHeadEat implements Listener {
    Plugin plugin;

    public GoldenHeadEat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeadUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }



        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            return;

        }

        if (!(player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD)) {
            return;
        }


        ItemStack heldItem = player.getInventory().getItemInMainHand();
        String desiredLore = "Healing Item";


        if (!(heldItem.getItemMeta().hasLore())) {
            return;
        }

        if (!(heldItem.getItemMeta().getLore().contains(desiredLore))) { //NPE lies? // Also fuck component with their serializers
            return;
        }

        heldItem.setAmount(heldItem.getAmount() - 1); //TODO does not work? see comment below

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
        player.getInventory().remove(heldItem);
        player.getInventory().addItem(heldItem); //TODO: I HATE THIS
        // This is suboptimal, but apparently removing itemstack with amount 1 does not work
        // Side effect is change in inventory slot possible

    }
}
