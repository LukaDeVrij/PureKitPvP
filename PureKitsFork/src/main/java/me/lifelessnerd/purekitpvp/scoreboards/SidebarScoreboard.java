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

        sidebar.updateLinesPeriodically(0, 20);
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
}
