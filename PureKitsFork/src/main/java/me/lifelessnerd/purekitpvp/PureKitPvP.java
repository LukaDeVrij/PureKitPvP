package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.combathandlers.KillHandler;
import me.lifelessnerd.purekitpvp.createKit.CreateKit;
import me.lifelessnerd.purekitpvp.createKit.DeleteKit;
import me.lifelessnerd.purekitpvp.combathandlers.DeathHandler;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.kitCommand.GUIListener;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.kitCommand.KitsGUI;
import me.lifelessnerd.purekitpvp.kitCommand.ResetKit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureKitPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabled PureKitPvP");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Kits config
        KitConfig.setup();
        KitConfig.get().addDefault("kits", null);
        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();

        getCommand("getkit").setExecutor(new GetKit(this));
        getCommand("resetkit").setExecutor(new ResetKit());
        getCommand("kit").setExecutor(new KitsGUI(this));
        getCommand("createkit").setExecutor(new CreateKit(this));
        getCommand("deletekit").setExecutor(new DeleteKit(this));
        getServer().getPluginManager().registerEvents(new GetKit(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(this), this);
        getServer().getPluginManager().registerEvents(new KillHandler(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
