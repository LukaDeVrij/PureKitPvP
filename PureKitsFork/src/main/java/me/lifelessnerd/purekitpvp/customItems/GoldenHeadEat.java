package me.lifelessnerd.purekitpvp.customItems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (!(heldItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "golden_head"), PersistentDataType.BOOLEAN))){
            return;
        }
        if (!(heldItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "golden_head"), PersistentDataType.BOOLEAN))){
            return;
        }

        e.setCancelled(true);

        heldItem.setAmount(heldItem.getAmount() - 1);
        int slot = player.getInventory().getHeldItemSlot();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
        player.getInventory().remove(heldItem);
        player.getInventory().setItem(slot, heldItem);
        // FIXED
        // This is suboptimal, but apparently removing itemstack with amount 1 does not work
        // Side effect is change in inventory slot possibly
        // FIXED using SLOT

    }
}
