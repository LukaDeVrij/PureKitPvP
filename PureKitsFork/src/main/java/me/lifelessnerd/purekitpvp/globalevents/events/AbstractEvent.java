package me.lifelessnerd.purekitpvp.globalevents.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractEvent {
    public Plugin plugin;
    public int timer;
    public int taskId;
    public boolean running;
    public int eventLength;
    public BukkitTask runnable;

    public AbstractEvent(Plugin plugin) {
        this.plugin = plugin;
        this.timer = 0;
        this.taskId = 0;
        this.running = false;
        this.eventLength = 30; // default -> can be overwritten in class
    }

    public abstract String getEventName();

    public abstract String getEventDescription();

    public int getEventLength() {
        return this.eventLength;
    }

    public abstract void onStart();

    /**
     * Calling this method with a BukkitTask ensures the Task is cancelled after the event ends
     */
    public void startEndListener(BukkitTask eventLoop) {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer >= getEventLength()) {
                    this.cancel();
                    if (eventLoop != null) {
                        eventLoop.cancel();
                    }
                    timer = 0;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                            onlinePlayer.sendMessage(Component.text("The event has ended.").color(NamedTextColor.GRAY));
                        }
                    }
                    onEnd();
                }
                timer++;
            }

        }.runTaskTimer(plugin, 0, 20);
    }

    public abstract void pauseResumePasser(boolean paused);

    public void pauseResumeAbstractEvent(boolean paused, BukkitTask eventLoop) {
        if (paused) {
            this.runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (timer >= getEventLength()) {
                        this.cancel();
                        if (eventLoop != null) {
                            eventLoop.cancel();
                        }
                        timer = 0;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                                onlinePlayer.sendMessage(Component.text("The event has ended.").color(NamedTextColor.GRAY));
                            }
                        }
                        onEnd();
                    }
                    timer++;
                }

            }.runTaskTimer(plugin, 0, 20);
        } else {
            runnable.cancel();
        }
    }

    public abstract void onEnd();
}
