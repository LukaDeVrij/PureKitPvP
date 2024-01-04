package me.lifelessnerd.purekitpvp.globalevents.events;

import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import org.bukkit.entity.Player;

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
