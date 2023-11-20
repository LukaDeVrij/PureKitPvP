package me.lifelessnerd.purekitpvp.combathandlers.scoreboards;


import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;


public class TabManager {

    private Plugin plugin;
    private Component header = Component.text("");
    private Component footer = Component.text("");

    public TabManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void showTab(){
        if(header == null && footer == null) return;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {


            @Override
            public void run() {

                // TODO
                // get stats and such -- let owner choose what info? loop to append certain stats

                net.minecraft.network.chat.Component headerTBS = ComponentUtils.toMojangComponent(header);
                net.minecraft.network.chat.Component footerTBS = ComponentUtils.toMojangComponent(footer);

                ClientboundTabListPacket packet = new ClientboundTabListPacket(headerTBS, footerTBS);

                Set<Player> inWorld = PlayerUtils.getPlayersInWorld(plugin.getConfig().getString("world"));
                if (inWorld.size() != 0){
                    for (Player player : inWorld){
                        CraftPlayer cPlayer = (CraftPlayer) player; // frick nms
                        ServerPlayer serverPlayer = cPlayer.getHandle();
                        serverPlayer.connection.send(packet);
                    }
                }


            }
        }, 10, plugin.getConfig().getLong("tab-refresh-period"));

    }

    public void addHeader(Component header){
        this.header = this.header.append(header).appendNewline();
    }
    public void addFooter(Component footer){
        this.footer = this.footer.append(footer).appendNewline();
    }
}
