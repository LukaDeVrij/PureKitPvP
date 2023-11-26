package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

public class PickupFrenzyEvent extends AbstractEvent {
    public PickupFrenzyEvent(Plugin plugin) {
        super(plugin);
        this.eventLength = plugin.getConfig().getInt("pickup-frenzy-length");
    }

    @Override
    public String getEventName() {
        return "Pickup Frenzy";
    }

    @Override
    public Component getEventDescription() {
        return LanguageConfig.lang.get("EVENTS_PICKUP_DESC");
    }

    @Override
    public void onStart() {
        this.running = true;
        EventDataClass.dropInventoryOnDeath = true;
        startEndListener(null);
    }

    @Override
    public void pauseResumePasser(boolean paused) {
        pauseResumeAbstractEvent(paused, null);
    }

    @Override
    public void onEnd() {
        this.running = false;
        EventDataClass.dropInventoryOnDeath = false;
    }
}
