package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import org.bukkit.plugin.Plugin;

public class PickupFrenzyEvent extends AbstractEvent{
    public PickupFrenzyEvent(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String getEventName() {
        return "Pickup Frenzy";
    }

    @Override
    public String getEventDescription() {
        return "Killed players drop their stuff!";
    }

    @Override
    public int getEventLength() {
        return 10;
    }

    @Override
    public void onStart() {
        EventDataClass.dropInventoryOnDeath = true;
    }

    @Override
    public void onEnd() {
        EventDataClass.dropInventoryOnDeath = false;
    }
}
