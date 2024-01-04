package me.lifelessnerd.purekitpvp.combatHandler;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class BlockRemover implements Listener {
    Plugin plugin;
    public BlockRemover(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block placedBlock = event.getBlockPlaced();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))) {
            return;
        }
        if (!(plugin.getConfig().getBoolean("remove-blocks"))){
            return;
        }
        // If player is in survival // adventure -> remove block
        if (player.getGameMode() == GameMode.CREATIVE){
            return;
        }
        long secDelay = plugin.getConfig().getInt("remove-blocks-delay");
        Bukkit.getScheduler().runTaskLater(this.plugin, () ->
        {
            placedBlock.getLocation().getBlock().setType(Material.AIR);

        }, secDelay * 20);


    }
}
