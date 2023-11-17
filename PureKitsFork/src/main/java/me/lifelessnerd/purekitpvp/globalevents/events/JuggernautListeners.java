package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.PluginGetter;
import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class JuggernautListeners  {

    public static void onDeath(Player killed, Player killer){

        if(!EventDataClass.juggernautRunning){
            return;
        }
        if (!(killed.getName().equalsIgnoreCase(EventDataClass.juggernaut))){
            return;
        }
        // Juggernaut has been killed!


    }
}
