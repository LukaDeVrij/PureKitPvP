package me.lifelessnerd.purekitpvp;
import me.lifelessnerd.purekitpvp.combathandlers.*;
import me.lifelessnerd.purekitpvp.combathandlers.leveling.PlayerLevelChat;
import me.lifelessnerd.purekitpvp.combathandlers.killhandler.Suicide;
import me.lifelessnerd.purekitpvp.combathandlers.mobhandler.OnPlayerSpawnMob;
import me.lifelessnerd.purekitpvp.combathandlers.killhandler.DeathHandler;
import me.lifelessnerd.purekitpvp.database.KitDatabase;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.globalevents.events.DoubleHealthListeners;
import me.lifelessnerd.purekitpvp.scoreboards.SidebarScoreboard;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsCommand;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsGUIListener;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.KillEffectInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.KillMessageInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.ProjectileTrailInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.KillEffect;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.ProjectileTrail;
import me.lifelessnerd.purekitpvp.perks.perkCommand.PerkCommand;
import me.lifelessnerd.purekitpvp.perks.perkCommand.PerkGUIListener;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkFireHandler;
import me.lifelessnerd.purekitpvp.customItems.GoldenHeadEat;
import me.lifelessnerd.purekitpvp.customItems.OpenRandomChest;
import me.lifelessnerd.purekitpvp.files.*;
import me.lifelessnerd.purekitpvp.kitCommand.GUIListener;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.kitCommand.KitsGUI;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetStats;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ArrowsShotStat;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ProjectilesThrownStat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class PureKitPvP extends JavaPlugin {

    private KitDatabase kitDatabase;
    @Override
    public void onEnable() {
        PerkFireHandler.plugin = this; //idk? fixes npe?
        PluginGetter.plugin = this; //this is dumb but used in >= 2 instances
        // Has to be at the start because other class constructors use it

        getConfig().options().copyDefaults();
        saveConfig();

        //Database
        try {
            // Ensure the plugin's data folder exists
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            kitDatabase = new KitDatabase(getDataFolder().getAbsolutePath() + "/kitdatabase.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Kits config
        KitConfig.setup();
        KitConfig.get().addDefault("kits", "");

        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();

        PerkData.setup();
        PerkData.get().options().copyDefaults(true);
        PerkData.save();

        KitStatsConfig.setup();
        KitStatsConfig.get().options().copyDefaults(true);
        KitStatsConfig.save();

        LootTablesConfig.setup();
        LootTablesConfig.get().options().copyDefaults(true);
        LootTablesConfig.save();

        MobSpawnConfig.setup();
        MobSpawnConfig.get().options().copyDefaults(true);
        MobSpawnConfig.save();

        PlayerStatsConfig.setup();
        PlayerStatsConfig.get().options().copyDefaults(true);
        PlayerStatsConfig.save();

        CosmeticsConfig.setup();
        CosmeticsConfig.get().options().copyDefaults(true);
        CosmeticsConfig.save();

        LanguageConfig.setup();
        LanguageConfig.get().options().copyDefaults(true);
        LanguageConfig.save();
        LanguageConfig.loadLanguage();


        SidebarScoreboard scoreboard = new SidebarScoreboard(this);

        getCommand("getkit").setExecutor(new GetKit(this));
        getCommand("kit").setExecutor(new KitsGUI(this));
        getCommand("perk").setExecutor(new PerkCommand(this));
        getCommand("suicide").setExecutor(new Suicide(this));
        getCommand("getstats").setExecutor(new GetStats(this));
        getCommand("purekitpvphelp").setExecutor(new HelpCommand());
        getCommand("purekitpvp").setExecutor(new AdminCommandManager(this));
        getCommand("cosmetics").setExecutor(new CosmeticsCommand(this));
        getServer().getPluginManager().registerEvents(new GetKit(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new PerkGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(this), this);
        getServer().getPluginManager().registerEvents(new VoidKiller(this), this);
        getServer().getPluginManager().registerEvents(new ProjectilesThrownStat(this), this);
        getServer().getPluginManager().registerEvents(new ArrowsShotStat(this), this);
        getServer().getPluginManager().registerEvents(new GoldenHeadEat(this), this);
        getServer().getPluginManager().registerEvents(new OpenRandomChest(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerSpawnMob(this), this);
        getServer().getPluginManager().registerEvents(new PearlListener(this), this);
        getServer().getPluginManager().registerEvents(new HealthEvents(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLevelChat(this), this);
        getServer().getPluginManager().registerEvents(new ChickenPrevention(this), this);
        getServer().getPluginManager().registerEvents(new PickupListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockRemover(this), this);
//        getServer().getPluginManager().registerEvents(new KitPreviewListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileRemover(this), this);
        getServer().getPluginManager().registerEvents(new DoubleHealthListeners(this), this);

        getServer().getPluginManager().registerEvents(new CosmeticsGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new KillEffect(this), this);
        getServer().getPluginManager().registerEvents(new KillEffectInventory(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileTrail(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileTrailInventory(this), this);
        getServer().getPluginManager().registerEvents(new KillMessageInventory(this), this);


        getLogger().info("Enabled PureKitPvP");
        getLogger().warning("Plugin active in world " + getConfig().getString("world")  + ". To change this, see the config.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Goodbye!");
    }

    public KitDatabase getKitDatabase() {
        return kitDatabase;
    }

}
