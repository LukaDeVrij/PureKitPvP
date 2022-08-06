package me.lifelessnerd.purekitpvp;

import me.lifelessnerd.purekitpvp.combathandlers.commands.Suicide;
import me.lifelessnerd.purekitpvp.combathandlers.VoidKiller;
import me.lifelessnerd.purekitpvp.combathandlers.mobhandler.OnPlayerSpawnMob;
import me.lifelessnerd.purekitpvp.createKit.CreateKit;
import me.lifelessnerd.purekitpvp.createKit.DeleteKit;
import me.lifelessnerd.purekitpvp.combathandlers.killhandler.DeathHandler;
import me.lifelessnerd.purekitpvp.createKit.SetKillItem;
import me.lifelessnerd.purekitpvp.customitems.GoldenHeadEat;
import me.lifelessnerd.purekitpvp.customitems.OpenRandomChest;
import me.lifelessnerd.purekitpvp.customitems.commands.CommandManager;
import me.lifelessnerd.purekitpvp.customitems.loottablelogic.CreateLootTable;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import me.lifelessnerd.purekitpvp.files.LootTablesConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.kitCommand.GUIListener;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.kitCommand.KitsGUI;
import me.lifelessnerd.purekitpvp.kitCommand.ResetKit;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetKitStats;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetStats;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ArrowsShotStat;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ProjectilesThrownStat;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureKitPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabled PureKitPvP");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Kits config
        KitConfig.setup();
        KitConfig.get().addDefault("kits", "");
        //Might create problems, I tried to just keep value null but then the entire value didn't show up until I created a kit
        //Which is not too bad either so whatever, if there are any problems ever change this and hope for the best
        //Otherwise find a way to make a path without a value, like: kits: , instead of kits: ''

        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();

        KitStatsConfig.setup();
        KitStatsConfig.get().options().copyDefaults(true);
        KitStatsConfig.save();

        LootTablesConfig.setup();
        LootTablesConfig.get().options().copyDefaults(true);
        LootTablesConfig.save();

        PlayerStatsConfig.setup();
        PlayerStatsConfig.get().options().copyDefaults(true);
        PlayerStatsConfig.save();

        getCommand("getkit").setExecutor(new GetKit(this));
        getCommand("resetkit").setExecutor(new ResetKit());
        getCommand("kit").setExecutor(new KitsGUI(this));
        getCommand("createkit").setExecutor(new CreateKit(this));
        getCommand("deletekit").setExecutor(new DeleteKit(this));
        getCommand("suicide").setExecutor(new Suicide(this));
        getCommand("getstats").setExecutor(new GetStats(this));
        getCommand("kitstats").setExecutor(new GetKitStats(this));
        getCommand("setkillitem").setExecutor(new SetKillItem(this));
        getCommand("getitem").setExecutor(new CommandManager(this));
        getCommand("createloottable").setExecutor(new CreateLootTable(this));
        getServer().getPluginManager().registerEvents(new GetKit(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(this), this);
        getServer().getPluginManager().registerEvents(new VoidKiller(this), this);
        getServer().getPluginManager().registerEvents(new ProjectilesThrownStat(this), this);
        getServer().getPluginManager().registerEvents(new ArrowsShotStat(this), this);
        getServer().getPluginManager().registerEvents(new GoldenHeadEat(this), this);
        getServer().getPluginManager().registerEvents(new OpenRandomChest(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerSpawnMob(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
