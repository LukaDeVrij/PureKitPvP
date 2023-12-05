package me.lifelessnerd.purekitpvp.scoreboards;
import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import me.catcoder.sidebar.pager.SidebarPager;
import me.lifelessnerd.purekitpvp.files.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.DoubleUtils;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class SidebarScoreboard implements Listener {
    Plugin plugin;

    public SidebarScoreboard(Plugin plugin) {
        this.plugin = plugin;

        refreshLeaderboard();

    }

    public void refreshLeaderboard() {
        Set<Player> players = PlayerUtils.getPlayersInWorld(plugin.getConfig().getString("world"));

        List<String> enabled = plugin.getConfig().getStringList("enabled-slides");

        ArrayList<Sidebar<Component>> enabledSidebars = new ArrayList<>();

        Component titleComponent = LanguageConfig.lang.get("SCOREBOARD_MAIN_TITLE");

        for (String panel : enabled) {

            switch(panel){
                case "GlobalStats":
                    Sidebar<Component> globalStatsSidebar = ProtocolSidebar.newAdventureSidebar(titleComponent, plugin);
                    fillGlobalSidebar(globalStatsSidebar);
                    enabledSidebars.add(globalStatsSidebar);
                    break;
                case "PersonalStats":
                    Sidebar<Component> personalStatsSidebar = ProtocolSidebar.newAdventureSidebar(titleComponent, plugin);
                    fillPersonalSidebar(personalStatsSidebar);
                    enabledSidebars.add(personalStatsSidebar);
                    break;
            }
        }

        SidebarPager<Component> pager = new SidebarPager<>(
                enabledSidebars, plugin.getConfig().getInt("scoreboard-period") * 20L, plugin);

        // add page status line to all sidebars in pager
        pager.addPageLine((page, maxPage, sidebar) ->
                sidebar.addLine(Component.text("Page " + page + "/" + maxPage).color(NamedTextColor.GREEN)));


        for (Player player : players){
            pager.show(player);
        }
    }


    private void fillGlobalSidebar(Sidebar<Component> sidebar) {

        int places = plugin.getConfig().getInt("global-stats-place-amount");
        List<String> enabled = plugin.getConfig().getStringList("global-stats-components");

        sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_GLOBAL_TITLE"));

        for (String component : enabled){
            ArrayList<Component> createdLeaderboard = null;
            switch(component) {
                case "Killstreak":
                    sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_GLOBAL_KILLSTREAK"));
                    createdLeaderboard = createLeaderboard(".killstreak", places);

                    break;
                case "Level":
                    sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_GLOBAL_LEVEL"));
                    createdLeaderboard = createLeaderboard(".level", places);

                    break;
                case "Kills":
                    sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_GLOBAL_KILLS"));
                    createdLeaderboard = createLeaderboard(".kills", places);

                    break;
                case "KD":
                    sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_GLOBAL_KD"));
                    createdLeaderboard = createLeaderboard(".kdratio", places);

                    break;
                default:
                    plugin.getLogger().warning("Global leaderboard components are wrongly defined! " +
                            "If you do not want a global scoreboard, change this in the enabled-slides value.");
                    createdLeaderboard = new ArrayList<>(); // This is empty; only happens when there are no/faulty values defined
                    break;
            }
            System.out.println("LIST: " + createdLeaderboard);
            for (Component line : createdLeaderboard){
                sidebar.addUpdatableLine(
                        (player) -> {
                            System.out.println("testing");
                            refreshLeaderboard(); // I need to make this function anyway to make it work with /pkpvp reload
                            return line;
                        }
                        // TODO test this
                );
            }
        }

        sidebar.updateLinesPeriodically(0, 20);
    }


    private ArrayList<Component> createLeaderboard(String statKey, int places) {
        HashMap<String, Integer> leaderboard = new HashMap<>();

        Set<String> players = PlayerStatsConfig.get().getKeys(false);
        System.out.println(players.size() + "," +  places);
        for (String player : players){
            int statValue = PlayerStatsConfig.get().getInt(player + statKey);
            leaderboard.put(player, statValue);
        }

        ArrayList<Component> toBeReturned = new ArrayList<>();

        for (int i = 0; i < places; i++) {
            System.out.println(i);
            System.out.println(leaderboard);
            int highestScore = 0;
            String highestPlayer = null;
            for (String player : leaderboard.keySet()) {
                if (leaderboard.get(player) > highestScore) {
                    highestScore = leaderboard.get(player);
                    highestPlayer = player;
                }
            }
            if (highestPlayer != null) {
                toBeReturned.add(Component.text(highestPlayer + " - " + highestScore, NamedTextColor.GRAY));
                leaderboard.remove(highestPlayer);
            }
        }

        return toBeReturned;
    }

    private void fillPersonalSidebar(Sidebar<Component> sidebar){

        sidebar.addLine(LanguageConfig.lang.get("SCOREBOARD_PERSONAL_TITLE"));

        List<String> enabled = plugin.getConfig().getStringList("personal-stats-components");

        for (String component : enabled){
            switch(component){
                case "Killstreak":
                    sidebar.addUpdatableLine(
                            (player) -> LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KILLSTREAK").
                                    replaceText(ComponentUtils.replaceConfig("%VALUE%",
                                            PlayerStatsConfig.get().getString(player.getName() + ".killstreak")))
                    );
                    break;
                case "Kills":
                    sidebar.addUpdatableLine(
                            (player) -> LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KILLS").
                                    replaceText(ComponentUtils.replaceConfig("%VALUE%",
                                            PlayerStatsConfig.get().getString(player.getName() + ".kills")))
                    );
                    break;
                case "Deaths":
                    sidebar.addUpdatableLine(
                            (player) -> LanguageConfig.lang.get("SCOREBOARD_PERSONAL_DEATHS").
                                    replaceText(ComponentUtils.replaceConfig("%VALUE%",
                                            PlayerStatsConfig.get().getString(player.getName() + ".deaths")))
                    );
                    break;
                case "KD":
                    sidebar.addUpdatableLine(
                            (player) -> LanguageConfig.lang.get("SCOREBOARD_PERSONAL_KD").
                                    replaceText(ComponentUtils.replaceConfig("%VALUE%",
                                            String.valueOf(DoubleUtils.round(
                                                    PlayerStatsConfig.get().getDouble(player.getName() + ".kdratio"), 2)
                                            )))
                    );
                    break;
                case "Level":
                    sidebar.addUpdatableLine(
                            (player) -> LanguageConfig.lang.get("SCOREBOARD_PERSONAL_LEVEL").
                                    replaceText(ComponentUtils.replaceConfig("%VALUE%",
                                            PlayerStatsConfig.get().getString(player.getName() + ".level")))
                    );
                    break;
            }
        }

        sidebar.updateLinesPeriodically(0, 20);
    }

    private HashMap<String, Integer> orderLeaderboard(HashMap<String, Integer> unordered){
        HashMap<String, Integer> sortedMap = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : unordered.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<Integer>() {
            public int compare(Integer int1, Integer int2) {
                return (int1).compareTo(int2);
            }
        });
        for (Integer integer : list) {
            for (Map.Entry<String, Integer> entry : unordered.entrySet()) {
                if (entry.getValue().equals(integer)) {
                    sortedMap.put(entry.getKey(), integer);
                }
            }
        }
        return sortedMap;
    }
}
