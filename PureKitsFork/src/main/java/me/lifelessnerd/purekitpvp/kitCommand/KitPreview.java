package me.lifelessnerd.purekitpvp.kitCommand;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class KitPreview {

    Plugin plugin;
    String kitName;
    Player player;

    public KitPreview(Plugin plugin, String kitName, Player player) {
        this.plugin = plugin;
        this.kitName = kitName;
        this.player = player;
    }

    public void openInventory(){

        Inventory previewInv = Bukkit.createInventory(null, 54, Component.text("Kit Preview > " + kitName));
    }
}
