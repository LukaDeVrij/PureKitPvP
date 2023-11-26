package me.lifelessnerd.purekitpvp.scoreboards;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class GlobalObjective extends AbstractObjective {
    public GlobalObjective(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void show(Scoreboard scoreboard, Player player) { // Player is unused in this class - not in the brother class

        scoreboard.clearSlot(DisplaySlot.SIDEBAR);

        Objective globalStats = scoreboard.getObjective("GlobalStats");
        globalStats.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> enabled = plugin.getConfig().getStringList("global-stats-components");

        Score score = globalStats.getScore("Global Stats");
        score.setScore(15);

        for (String component : enabled) {

            switch(component){
                case "Killstreak":
                    Score tempScore = globalStats.getScore("  Killstreak Leaders");
                    tempScore.setScore(14);
                    tempScore = globalStats.getScore("    LifelessNerd - 12");
                    tempScore.setScore(13);
            }

        }
    }

    private ArrayList<Double> createLeaderboard(String key){

        return null;
    }

}
