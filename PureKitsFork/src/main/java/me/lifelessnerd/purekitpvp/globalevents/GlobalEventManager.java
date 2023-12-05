package me.lifelessnerd.purekitpvp.globalevents;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.globalevents.events.AbstractEvent;
import me.lifelessnerd.purekitpvp.globalevents.events.DoubleHealthEvent;
import me.lifelessnerd.purekitpvp.globalevents.events.PickupFrenzyEvent;
import me.lifelessnerd.purekitpvp.globalevents.events.TeleMadnessEvent;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

public class GlobalEventManager {

    AbstractEvent currentEvent = null;
    int globalTimer = 0;
    public BukkitTask timerRunnable;
    int nextEventTime = 0;
    int period = 0;

    public List<AbstractEvent> enabledByConfig;
    public List<AbstractEvent> allEvents;

    public boolean paused;

    public Plugin plugin;

    public GlobalEventManager(Plugin plugin) {
        this.plugin = plugin;
        this.allEvents = new ArrayList<>();
        allEvents.add(new PickupFrenzyEvent(plugin));
        allEvents.add(new TeleMadnessEvent(plugin));
        allEvents.add(new DoubleHealthEvent(plugin));

        enabledByConfig = new ArrayList<>();
        for (AbstractEvent event : this.allEvents) {
            for (String inConfig : plugin.getConfig().getStringList("global-events-enabled")) {
                if (event.getEventName().equalsIgnoreCase(inConfig)) {
                    enabledByConfig.add(event);
                }
            }
        }

        if (!(plugin.getConfig().getBoolean("global-event-loop"))) {
            return;
        }
//        System.out.println("Global Combat Event loop enabled, starting!");
        paused = false;
        // Global timer (1 second increments)
        timerRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                int amount = 0;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                        amount++;
                    }
                    if (amount >= 1) {
                        globalTimer++;
//                        System.out.println(1 + " - " + globalTimer);
                    }
                }

            }
        }.runTaskTimer(plugin, 0, 20);

        period = plugin.getConfig().getInt("global-event-period");
        // TODO somewhere in here is a bug: first event after reload is 1 sec too long
        nextEventTime = globalTimer + period;

        // Runnable that starts new events, if nextEventTime is passed
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(globalTimer >= nextEventTime)) {
                    // Still in cooldown period (or event is running)
                    return;
                }
                // No event running - cooldown period over!
                AbstractEvent randomEvent = enabledByConfig.get(ThreadLocalRandom.current().nextInt(0, enabledByConfig.size()));
//                System.out.println(randomEvent.getEventName());
                startEvent(randomEvent.getEventName().replace(' ', '_'));

            }

        }.runTaskTimer(plugin, 0, 20);

    }

    public Component startEvent(String name) {
        if (this.currentEvent != null) {
            if (this.currentEvent.running) { // Event already running?
                return Component.text("An event is already/still running!").color(NamedTextColor.RED);
            }
        }
        for (AbstractEvent event : this.allEvents) {
            String eventId = event.getEventName().replace(' ', '_');
            if (eventId.equalsIgnoreCase(name)) {
                event.onStart();
                this.currentEvent = event;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {

                        onlinePlayer.sendMessage(LanguageConfig.lang.get("EVENTS_START").
                                replaceText(ComponentUtils.replaceConfig("%EVENT%", this.currentEvent.getEventName())).
                                appendNewline().append(Component.text(this.currentEvent.getEventLength() + "s" + " - ").
                                append(this.currentEvent.getEventDescription())
                                ));
                    }
                }
                this.nextEventTime = this.globalTimer + this.currentEvent.getEventLength() + this.period;
//                System.out.println(this.nextEventTime + " Now: " + this.globalTimer);
                // This means the nextEventTime is eventTime + cooldown period after current time
            }
        }
        return Component.text("Started event.").color(NamedTextColor.GREEN);
    }

    public Component stopEvent() {
        if (this.currentEvent == null) {
            return Component.text("No event is running.").color(NamedTextColor.RED);
        }
        if (!this.currentEvent.running) {
            return Component.text("No event is running.").color(NamedTextColor.RED);
        }
        AbstractEvent currentEvent = this.currentEvent;
        // Fast forward globalTimer to time event would have stopped
        this.globalTimer = this.globalTimer + (currentEvent.getEventLength() - currentEvent.timer);
        // Force end event
        currentEvent.timer = currentEvent.getEventLength();
        this.currentEvent = null;

        return Component.text("Stopping current event and continuing the global event loop...").color(NamedTextColor.GREEN);
    }

    public Component pauseResumeTimer() {
        if (paused) {
            currentEvent.pauseResumePasser(true);
            paused = false;
            // Global timer (1 second increments)
            timerRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    int amount = 0;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                            amount++;
                        }
                        if (amount >= 1) {
                            globalTimer++;
//                            System.out.println(2 + " - " + globalTimer);
                        }
                    }

                }
            }.runTaskTimer(plugin, 0, 20);


            return Component.text("Global event loop resumed.").color(NamedTextColor.GREEN);
        } else {
            currentEvent.pauseResumePasser(false);
            paused = true;
            timerRunnable.cancel();
            return Component.text("Global event loop paused.").color(NamedTextColor.GREEN);
        }
    }

}
