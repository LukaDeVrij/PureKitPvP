package me.lifelessnerd.purekitpvp.combatHandler.leveling;

import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class PlayerLevelChat implements Listener {

    public Plugin plugin;
    public PlayerLevelChat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        // Some shenanigans regarding async, but should be fine for this use case
        if (!(plugin.getConfig().getBoolean("level-chat"))){
            return;
        }
        Player player = event.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }

        event.setFormat("[" + PlayerStatsConfig.get().getString(player.getName() + ".level") + "] " + event.getFormat());

    }
}
