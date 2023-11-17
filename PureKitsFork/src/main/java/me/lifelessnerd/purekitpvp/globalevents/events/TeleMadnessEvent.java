package me.lifelessnerd.purekitpvp.globalevents.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class TeleMadnessEvent extends AbstractEvent {
    BukkitTask eventLoop;

    public TeleMadnessEvent(Plugin plugin) {
        super(plugin);
        this.eventLength = plugin.getConfig().getInt("telemadness-length");
    }

    @Override
    public String getEventName() {
        return "TeleMadness";
    }

    @Override
    public String getEventDescription() {
        return "Everyone gets Ender Pearls, all the time!";
    }

    @Override
    public void onStart() {
        this.running = true;
        eventLoop = new BukkitRunnable() {
            @Override
            public void run() {
                taskId = this.getTaskId();

                giveEnderpearls();

            }
        }.runTaskTimer(plugin, 0, 20);

        startEndListener(eventLoop);
    }

    @Override
    public void pauseResumePasser(boolean paused) {
        pauseResumeAbstractEvent(paused, eventLoop);
    }

    @Override
    public void onEnd() {
        this.running = false;
        World pvpWorld = Bukkit.getWorld(plugin.getConfig().getString("world"));
        List<Player> players = pvpWorld.getPlayers();
        if (players.isEmpty()) {
            return;
        }

        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta pearlMeta = pearl.getItemMeta();
        pearlMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "telemadness"), PersistentDataType.BOOLEAN, true);
        ArrayList<Component> lore = new ArrayList<>() {
            {
                add(Component.text("This is an event specific item, which").color(NamedTextColor.GRAY));
                add(Component.text("will be removed as soon as the event ends.").color(NamedTextColor.GRAY));
            }
        };
        pearlMeta.lore(lore);
        pearlMeta.displayName(Component.text("TeleMadness Pearl").color(NamedTextColor.LIGHT_PURPLE));
        pearl.setItemMeta(pearlMeta);
        pearl.setAmount(99999999); // will do
        for (Player player : players) {
            player.getInventory().removeItemAnySlot(pearl);
        }
    }

    public void giveEnderpearls() {
        World pvpWorld = Bukkit.getWorld(plugin.getConfig().getString("world"));
        List<Player> players = pvpWorld.getPlayers();
        if (players.isEmpty()) {
            return;
        }
        for (Player player : players) {
            ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
            ItemMeta pearlMeta = pearl.getItemMeta();
            pearlMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "telemadness"), PersistentDataType.BOOLEAN, true);
            ArrayList<Component> lore = new ArrayList<>() {
                {
                    add(Component.text("This is an event specific item, which").color(NamedTextColor.GRAY));
                    add(Component.text("will be removed as soon as the event ends.").color(NamedTextColor.GRAY));
                }
            };
            pearlMeta.lore(lore);
            pearlMeta.displayName(Component.text("TeleMadness Pearl").color(NamedTextColor.LIGHT_PURPLE));
            pearl.setItemMeta(pearlMeta);
            pearl.setAmount(1);
            player.getInventory().addItem(pearl);
        }
    }
}
