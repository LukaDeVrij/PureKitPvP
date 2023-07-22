package me.lifelessnerd.purekitpvp;
import me.lifelessnerd.purekitpvp.combathandlers.*;
import me.lifelessnerd.purekitpvp.combathandlers.leveling.PlayerLevelChat;
import me.lifelessnerd.purekitpvp.combathandlers.libs.commands.Suicide;
import me.lifelessnerd.purekitpvp.combathandlers.mobhandler.OnPlayerSpawnMob;
import me.lifelessnerd.purekitpvp.combathandlers.killhandler.DeathHandler;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsCommand;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.CosmeticsGUIListener;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.KillEffectInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.KillMessageInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsCommand.inventories.ProjectileTrailInventory;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.KillEffect;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.KillMessage;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.ProjectileTrail;
import me.lifelessnerd.purekitpvp.perks.perkCommand.PerkCommand;
import me.lifelessnerd.purekitpvp.perks.perkCommand.PerkGUIListener;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkFireHandler;
import me.lifelessnerd.purekitpvp.customitems.GoldenHeadEat;
import me.lifelessnerd.purekitpvp.customitems.OpenRandomChest;
import me.lifelessnerd.purekitpvp.files.*;
import me.lifelessnerd.purekitpvp.kitCommand.GUIListener;
import me.lifelessnerd.purekitpvp.kitCommand.GetKit;
import me.lifelessnerd.purekitpvp.kitCommand.KitsGUI;
import me.lifelessnerd.purekitpvp.noncombatstats.commands.GetStats;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ArrowsShotStat;
import me.lifelessnerd.purekitpvp.noncombatstats.listeners.ProjectilesThrownStat;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureKitPvP extends JavaPlugin {

    @Override
    public void onEnable() {


        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Kits config
        KitConfig.setup();
        KitConfig.get().addDefault("kits", "");
        //Might create problems, I tried to just keep value null but then the entire value didn't show up until I created a kit
        //Which is not too bad either so whatever, if there are any problems ever change this and hope for the best
        //Otherwise find a way to make a path without a value, like: kits: , instead of kits: ''
        //: fix this with createConfiguration if I feel like it, tbf it works fine (IT DOES)

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

        getServer().getPluginManager().registerEvents(new CosmeticsGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new KillEffect(this), this);
        getServer().getPluginManager().registerEvents(new KillEffectInventory(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileTrail(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileTrailInventory(this), this);
        getServer().getPluginManager().registerEvents(new KillMessageInventory(this), this);
        PerkFireHandler.plugin = this; //idk? fixes npe?
        PluginGetter.plugin = this; //this is dumb but used in 2 instances

        getLogger().info("Enabled PureKitPvP");
        getLogger().warning("Plugin active in world " + getConfig().getString("world")  + ". To change this, see the config.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Goodbye!");
    }


}
