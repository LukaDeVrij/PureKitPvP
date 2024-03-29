package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
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
    public Component getEventDescription() {
        return LanguageConfig.lang.get("EVENTS_TELEMADNESS_DESC");
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
        pearlMeta.lore(ComponentUtils.splitComponent(LanguageConfig.lang.get("EVENTS_TELEMADNESS_ITEM_LORE")));
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
            pearlMeta.lore(ComponentUtils.splitComponent(LanguageConfig.lang.get("EVENTS_TELEMADNESS_ITEM_LORE")));
            pearlMeta.displayName(Component.text("TeleMadness Pearl").color(NamedTextColor.LIGHT_PURPLE));
            pearl.setItemMeta(pearlMeta);
            pearl.setAmount(1);
            player.getInventory().addItem(pearl);
        }
    }
}
