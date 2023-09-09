package me.lifelessnerd.purekitpvp.globalevents.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class TeleMadnessEvent extends AbstractEvent{
    public TeleMadnessEvent(Plugin plugin) {
        super(plugin);
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
    public int getEventLength() {
        return 10;
    }

    @Override
    public void onStart() {
        BukkitTask eventLoop = new BukkitRunnable() {
            @Override
            public void run() {
                taskId = this.getTaskId();

                giveEnderpearls();

            }
        }.runTaskTimer(plugin, 0, 20);

        startEndListener(eventLoop);
    }

    @Override
    public void onEnd() {
    }

    public void giveEnderpearls(){
        World pvpWorld = Bukkit.getWorld(plugin.getConfig().getString("world"));
        List<Player> players = pvpWorld.getPlayers();
        if (players.isEmpty()){
            return;
        }
        for (Player player : players) {
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        }
    }
}
