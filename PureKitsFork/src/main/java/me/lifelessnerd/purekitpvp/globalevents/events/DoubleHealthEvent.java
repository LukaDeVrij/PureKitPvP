package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;

public class DoubleHealthEvent extends AbstractEvent{

    BukkitTask eventLoop;

    public DoubleHealthEvent(Plugin plugin) {
        super(plugin);
        this.eventLength = plugin.getConfig().getInt("double-health-length");
    }

    @Override
    public String getEventName() {
        return "Double Health";
    }

    @Override
    public Component getEventDescription() {
        return Component.text(""); // TODO add lang defs
    }

    @Override
    public void onStart() {
        this.running = true;
        EventDataClass.doubleHealth = true;
        Set<Player> players = PlayerUtils.getPlayersInWorld(plugin.getConfig().getString("world"));
        for (Player player : players){
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(40.0D);
        }
        startEndListener(null);
    }

    @Override
    public void pauseResumePasser(boolean paused) {
        pauseResumeAbstractEvent(paused, eventLoop);
    }

    @Override
    public void onEnd() {
        EventDataClass.doubleHealth = false;
        Set<Player> players = PlayerUtils.getPlayersInWorld(plugin.getConfig().getString("world"));
        for (Player player : players){
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(40.0D);
        }
        this.running = false;
    }
}
