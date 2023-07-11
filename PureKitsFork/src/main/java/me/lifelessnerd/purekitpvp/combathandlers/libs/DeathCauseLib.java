package me.lifelessnerd.purekitpvp.combathandlers.libs;

import java.util.ArrayList;
import java.util.HashMap;

public class DeathCauseLib {

    public HashMap<String, String> defaultDeathMessages = new HashMap<>();
    public ArrayList<String> defaultKillMessages = new ArrayList<>();

    public HashMap<String, String> flexDeathMessages = new HashMap<>();
    public ArrayList<String> flexKillMessages = new ArrayList<>();

    public HashMap<String, String> nerdDeathMessages = new HashMap<>();
    public ArrayList<String> nerdKillMessages = new ArrayList<>();
    public DeathCauseLib() {

        defaultDeathMessages.put("VOID", " was thrown into the void by {killer}");
        defaultDeathMessages.put("LAVA", " jumped into lava trying to escape {killer}");
        defaultDeathMessages.put("FIRE", " stood in fire trying to escape {killer}");
        defaultDeathMessages.put("FIRE_TICK", " burnt to a crisp trying to escape {killer}");
        defaultDeathMessages.put("ENTITY_EXPLOSION", " exploded trying to escape {killer}");
        defaultDeathMessages.put("FALL", " fell off a cliff trying to escape {killer}");

        defaultKillMessages.add(" was killed by {killer}");

        flexDeathMessages.put("VOID", " was yeeted into the void by {killer}");
        flexDeathMessages.put("LAVA", " jumped in lava, he couldn't kill {killer}");
        flexDeathMessages.put("FIRE", " stood in fire trying to escape {killer}");
        flexDeathMessages.put("FIRE_TICK", " burnt to a crisp trying to escape {killer}");
        flexDeathMessages.put("ENTITY_EXPLOSION", " exploded trying to escape {killer}");
        flexDeathMessages.put("FALL", " was thrown off a cliff by {killer}");

        flexKillMessages.add(" became kill #{kills} of {killer}");

        nerdDeathMessages.put("VOID", " was thrown into the void by {killer}");
        nerdDeathMessages.put("LAVA", " jumped into lava trying to escape {killer}");
        nerdDeathMessages.put("FIRE", " stood in fire trying to escape {killer}");
        nerdDeathMessages.put("FIRE_TICK", " burnt to a crisp trying to escape {killer}");
        nerdDeathMessages.put("ENTITY_EXPLOSION", " burnt to a crisp trying to escape {killer}");
        nerdDeathMessages.put("FALL", " thought this was Borderlands, being chased by {killer}");

        nerdKillMessages.add(" was Ctrl+Alt+Deleted by {killer}");



    }
}
