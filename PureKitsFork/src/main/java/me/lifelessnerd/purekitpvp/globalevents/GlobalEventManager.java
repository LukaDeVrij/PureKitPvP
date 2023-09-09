package me.lifelessnerd.purekitpvp.globalevents;

import me.lifelessnerd.purekitpvp.globalevents.events.AbstractEvent;
import me.lifelessnerd.purekitpvp.globalevents.events.PickupFrenzyEvent;
import me.lifelessnerd.purekitpvp.globalevents.events.TeleMadnessEvent;
import me.lifelessnerd.purekitpvp.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class GlobalEventManager {

    AbstractEvent currentEvent = null;
    int globalTimer = 0;
    int nextEventTime = 0;
    int period = 0;
    List<AbstractEvent> events;
    public GlobalEventManager(Plugin plugin) {

        events = new ArrayList<>();
        events.add(new PickupFrenzyEvent(plugin));
        events.add(new TeleMadnessEvent(plugin));



        if (!(plugin.getConfig().getBoolean("global-event-loop"))){
            return;
        }
        System.out.println("Global Combat Event loop enabled, starting!");
        // Global timer (1 second increments)
        new BukkitRunnable() {
            @Override
            public void run() {
                int amount = 0;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                        amount++;
                    }
                    if (amount >= 1) {
                        globalTimer++;
                    }
                }

            }
        }.runTaskTimer(plugin, 0, 20);

        period = plugin.getConfig().getInt("global-event-period");

        nextEventTime = globalTimer + period;

        // Runnable that starts new events, if nextEventTime is passed
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(globalTimer >= nextEventTime)){
                    // Still in cooldown period (or event is running)
                    return;
                }
                // No event running - cooldown period over!
                AbstractEvent randomEvent = events.get(ThreadLocalRandom.current().nextInt(0, events.size()));
                randomEvent.onStart();
                currentEvent = randomEvent;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                        onlinePlayer.sendMessage(
                            Component.text("RANDOM EVENT! ").color(NamedTextColor.AQUA).append(
                            Component.text(currentEvent.getEventName()).color(NamedTextColor.LIGHT_PURPLE).append(
                            Component.text(" has been selected!").color(NamedTextColor.GRAY).appendNewline().append(
                            Component.text(currentEvent.getEventLength() + "s" + " - " + currentEvent.getEventDescription())
                            )))
                        );
                    }
                }

                nextEventTime = globalTimer + currentEvent.getEventLength() + period;
                // This means the nextEventTime is eventTime + cooldown period after current time
            }

        }.runTaskTimer(plugin, 0, 20);

    }

    public void startEvent(String name) {
    // TODO rework starting event mechanism so that starting events with command is possible -> also correct timer and such
        for (AbstractEvent event : this.events) {
            if (MyStringUtils.itemCamelCase(event.getEventName()).equalsIgnoreCase(name){
                System.out.println("yes!");
            }
        }
        
    }

}
