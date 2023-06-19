package me.lifelessnerd.purekitpvp.combathandlers.libs;

import java.util.HashMap;

public class DeathCauseLib {

    public HashMap<String, String> deathMessages = new HashMap<>();

    public DeathCauseLib() {

        deathMessages.put("VOID", " was thrown into the void by {killer}");
        deathMessages.put("LAVA", " jumped into lava trying to escape {killer}");
        deathMessages.put("FIRE", " stood in fire trying to escape {killer}");
        deathMessages.put("FIRE_TICK", " burnt to a crisp trying to escape {killer}");
        deathMessages.put("ENTITY_EXPLOSION", " burnt to a crisp trying to escape {killer}");

    }
}
