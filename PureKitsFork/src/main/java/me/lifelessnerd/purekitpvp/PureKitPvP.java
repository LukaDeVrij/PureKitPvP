package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.createKit.CreateKit;
import me.lifelessnerd.purekitpvp.createKit.DeleteKit;
import me.lifelessnerd.purekitpvp.deathhandlers.DeathHandler;
import me.lifelessnerd.purekitpvp.kitCommand.GUIListener;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.kitCommand.KitsGUI;
import me.lifelessnerd.purekitpvp.kitCommand.ResetKit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureKitPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabled NerdKits");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("getkit").setExecutor(new GetKit(this));
        getCommand("resetkit").setExecutor(new ResetKit());
        getCommand("kit").setExecutor(new KitsGUI(this));
        getCommand("createkit").setExecutor(new CreateKit(this));
        getCommand("deletekit").setExecutor(new DeleteKit(this));
        getServer().getPluginManager().registerEvents(new GetKit(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
