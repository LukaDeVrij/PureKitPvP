package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.PluginGetter;
import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.Random;
import java.util.Set;

public class JuggernautEvent extends AbstractEvent {
    public JuggernautEvent(Plugin plugin) {
        super(plugin);
        this.eventLength = plugin.getConfig().getInt("juggernaut-length");
    }

    @Override
    public String getEventName() {
        return "Juggernaut";
    }

    @Override
    public String getEventDescription() {
        return "Team up to kill the Juggernaut!";
    }

    @Override
    public void onStart() {
        this.running = true;
        startEndListener(null);
        EventDataClass.juggernautRunning = true;
        // Pick a juggernaut
        Player nextJuggernaut = null;
        Set<Player> players = PlayerUtils.getPlayersInWorld(PluginGetter.Plugin().getConfig().getString("world"));
        if(players.size() < 2) return;
        //https://stackoverflow.com/questions/124671/picking-a-random-element-from-a-set
        int size = players.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(Player player : players)
        {
            if (i == item)
                nextJuggernaut = player;
            i++;
        }
        EventDataClass.juggernaut = nextJuggernaut.getName();
        // Give him strength according to nr. of opponents
        // TODO For every opponent + 30% strength, +30% resistance
        int opponentsAmount = players.size() - 1;


    }

    @Override
    public void pauseResumePasser(boolean paused) {
        pauseResumeAbstractEvent(paused, null);
    }

    @Override
    public void onEnd() {
        // TODO reset everything
        throw new NotImplementedException();
    }
}
