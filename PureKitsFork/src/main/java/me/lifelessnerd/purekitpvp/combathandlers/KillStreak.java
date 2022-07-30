package me.lifelessnerd.purekitpvp.combathandlers;

public class KillStreak {

    PlayerStats killerStats;

    public KillStreak(PlayerStats killerStats) {
        this.killerStats = killerStats;

        checkConsecutiveKills();

    }

    private void checkConsecutiveKills() {

        if (killerStats.consecutiveKills >= 3){



        }

    }
}
