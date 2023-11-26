package me.lifelessnerd.purekitpvp.scoreboards;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SidebarScoreboard {
    Plugin plugin;
    Scoreboard scoreboard;
    int currentPage;
    List<String> enabled;
    public SidebarScoreboard(Plugin plugin) {
        this.plugin = plugin;
        this.currentPage = 1;
        this.enabled = plugin.getConfig().getStringList("enabled-slides");


    }

    public void enableSidebar(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        this.scoreboard = scoreboard;




        List<AbstractObjective> objectives = new ArrayList<>();
        objectives.add(new PersonalObjective(plugin));
        objectives.add(new GlobalObjective(plugin));


        // Start task with this
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                swapScoreboard(scoreboard, objectives);
            }
        }.runTaskTimer(plugin, 0, plugin.getConfig().getLong("scoreboard-period") * 20);



    }

    private void swapScoreboard(Scoreboard scoreboard, List<AbstractObjective> objectives){

        Set<Player> players = PlayerUtils.getPlayersInWorld(plugin.getConfig().getString("world"));

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        Component displayName = serializer.deserialize(plugin.getConfig().getString("scoreboard-title"));

        try {
            scoreboard.getObjective("PersonalStats").unregister();
            scoreboard.getObjective("GlobalStats").unregister();
        } catch (Exception e){
        }


        Objective personalStats = scoreboard.registerNewObjective("PersonalStats", Criteria.DUMMY, displayName);
        Objective globalStats = scoreboard.registerNewObjective("GlobalStats", Criteria.DUMMY, displayName);

        switch (currentPage % enabled.size()){
            case 0:
                for (Player player: players) {
                    objectives.get(0).show(scoreboard, player);
                    player.setScoreboard(scoreboard);
                }
                break;
            case 1:
                objectives.get(1).show(scoreboard, null);
                break;
        }



        currentPage++;
    }


}
