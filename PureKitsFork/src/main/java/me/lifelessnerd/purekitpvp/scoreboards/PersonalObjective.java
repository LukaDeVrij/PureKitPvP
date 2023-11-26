package me.lifelessnerd.purekitpvp.scoreboards;

import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.DoubleUtils;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.List;
import java.util.Set;

public class PersonalObjective extends AbstractObjective {

    public PersonalObjective(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void show(Scoreboard scoreboard, Player player) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacy('§');

        scoreboard.clearSlot(DisplaySlot.SIDEBAR);

        Objective personalStats = scoreboard.getObjective("PersonalStats");
        personalStats.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> enabled = plugin.getConfig().getStringList("personal-stats-components");

        Score score = personalStats.getScore(serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_TITLE")));
        score.setScore(15);
        int max = 14;
        for (String component : enabled) {
            int value;
            String scoreName;
            Score tempScore;
            switch(component){
                case "Killstreak":
                    value = PlayerStatsConfig.get().getInt(player.getName() + ".killstreak");
                    scoreName = serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KILLSTREAK").
                            replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(value))));
                    tempScore = personalStats.getScore(scoreName);
                    tempScore.setScore(max);
                    break;
                case "Kills":
                    value = PlayerStatsConfig.get().getInt(player.getName() + ".kills");
                    scoreName = serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KILLS").
                            replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(value))));
                    tempScore = personalStats.getScore(scoreName);
                    tempScore.setScore(max);
                    break;
                case "Deaths":
                    value = PlayerStatsConfig.get().getInt(player.getName() + ".deaths");
                    scoreName = serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_DEATHS").
                            replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(value))));
                    tempScore = personalStats.getScore(scoreName);
                    tempScore.setScore(max);
                    break;
                case "KD":
                    double doubleValue = DoubleUtils.round(PlayerStatsConfig.get().getDouble(player.getName() + ".kdratio"), 2);
                    scoreName = serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KD").
                            replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(doubleValue))));
                    tempScore = personalStats.getScore(scoreName);
                    tempScore.setScore(max);
                    break;
                case "Level":
                    value = PlayerStatsConfig.get().getInt(player.getName() + ".level");
                    scoreName = serializer.serialize(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_LEVEL").
                            replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(value))));
                    tempScore = personalStats.getScore(scoreName);
                    tempScore.setScore(max);
                    break;
            }
            max--;

        }
    }

}
