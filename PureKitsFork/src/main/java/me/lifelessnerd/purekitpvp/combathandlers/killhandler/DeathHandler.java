package me.lifelessnerd.purekitpvp.combathandlers.killhandler;

import me.lifelessnerd.purekitpvp.combathandlers.ProjectileRemover;
import me.lifelessnerd.purekitpvp.combathandlers.leveling.PlayerLeveling;
import me.lifelessnerd.purekitpvp.combathandlers.libs.DamageCauseLib;
import me.lifelessnerd.purekitpvp.combathandlers.mobhandler.MobRemover;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.KillEffect;
import me.lifelessnerd.purekitpvp.cosmetics.cosmeticsListeners.KillMessage;
import me.lifelessnerd.purekitpvp.files.lang.LanguageConfig;
import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;
import me.lifelessnerd.purekitpvp.globalevents.EventDataClass;
import me.lifelessnerd.purekitpvp.globalevents.events.JuggernautListeners;
import me.lifelessnerd.purekitpvp.perks.perkfirehandler.PerkFireHandler;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import me.lifelessnerd.purekitpvp.files.PlayerStatsConfig;
import me.lifelessnerd.purekitpvp.utils.ComponentUtils;
import me.lifelessnerd.purekitpvp.utils.DoubleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class DeathHandler implements Listener {

    // To whoever sees this; I profoundly apologise: this code is garbage
    // It hangs together with duct tape and iterative bug fixes everywhere
    // I should really redesign or at least clean up this class
    // But I feel like I would just make it worse; so I probably won't ever do that
    // good luck trying to read what is going on
    // You can ping me, but I won't probably know either

    Plugin plugin;

    public DeathHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){

        Player player = e.getPlayer();
        Location deathLocation = player.getLocation();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }
        //Reset player
        e.setCancelled(true);
        double x = plugin.getConfig().getDouble("respawnX");
        double y = plugin.getConfig().getDouble("respawnY");
        double z = plugin.getConfig().getDouble("respawnZ");

        Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                player.teleport(new Location(player.getWorld(), x, y, z, 0, 0)), 1L);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {

            if (EventDataClass.dropInventoryOnDeath){
                // Global Combat Event is active -> drop items on ground
                ItemStack[] inventoryItems = player.getInventory().getContents();
                ItemStack[] armorItems = player.getInventory().getArmorContents();
                ItemStack offhandItem = player.getInventory().getItemInOffHand();
                for (ItemStack item : inventoryItems) {

                    if (item != null)
                        player.getWorld().dropItemNaturally(deathLocation, item);
                }
                for (ItemStack item : armorItems) {
                    if (item != null)
                        player.getWorld().dropItemNaturally(deathLocation, item);
                }
                player.getWorld().dropItemNaturally(deathLocation, offhandItem);

                player.getInventory().clear();
            }

            if (plugin.getConfig().getBoolean("inventory-clear")){
                player.getInventory().clear();
            }

            player.getActivePotionEffects().clear();
            player.setExp(0f);
            player.setLevel(0);
            player.setArrowsInBody(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 5, 254)); //Kinda jank, works tho
            player.setFireTicks(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10 , 1));
            if (EventDataClass.doubleHealth){
                player.setHealth(40);
            } else {
                player.setHealth(20);
            }


        }, 2L);


        long kitDelay = plugin.getConfig().getLong("auto-kit-delay");
        if (kitDelay != -1L) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                player.chat("/kit");
            }, kitDelay);
        }

        // Delete alive projectiles of that player
        ProjectileRemover.removeExistingProjectiles(player);

        DamageCauseLib damageCauseLib = new DamageCauseLib();

        // Get all damage info and print to killed player
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        PlayerDamageDistribution damageData = player.getPersistentDataContainer().get(key, new PlayerDamageDistributionDataType());
        player.sendMessage(LanguageConfig.lang.get(LanguageKey.COMBAT_YOU_DIED.toString()));
        player.sendMessage(LanguageConfig.lang.get(LanguageKey.COMBAT_DEATH_RECAP.toString()));

        // Printing process and formatting
        int totalDamageDone = 0;
        for (String hashmapKey : damageData.damageDistributionMap.keySet()){
            totalDamageDone += damageData.damageDistributionMap.get(hashmapKey);
        }
        for (String hashmapKey : damageData.damageDistributionMap.keySet()){
            int damageValue = damageData.damageDistributionMap.get(hashmapKey);
            double damagePercentage = (double) damageValue / (double) totalDamageDone * 100.0;
            int damagePercentageInt = (int) damagePercentage;
            String damageCause = damageCauseLib.damageCauseTranslations.getOrDefault(hashmapKey, hashmapKey);
            player.sendMessage(LanguageConfig.lang.get(LanguageKey.COMBAT_DEATH_CAUSE.toString()).
                    replaceText(ComponentUtils.replaceConfig("%CAUSE%", damageCause)).
                    replaceText(ComponentUtils.replaceConfig("%VALUE%", String.valueOf(damagePercentageInt))));
        }

        //Broadcasting and stuff
        String deathMessage = "";
        String lastEnvironmentHit;
        String credit = ""; // Needed for later
        boolean playerInvolved = true;


        if (damageData.lastPlayerDamager == null && damageData.lastOtherDamager != null){
            //This means the player was completely killed by environment, no player killer
            // Except when the killer is a zombie of a player -> handled down below

            lastEnvironmentHit = damageData.lastOtherDamager;
            credit = damageCauseLib.deathMessages.get(lastEnvironmentHit);
            deathMessage = KillMessage.create.byEnvironment(player.getName(), lastEnvironmentHit);
            playerInvolved = false;

        }
        if (damageData.lastPlayerDamager != null && damageData.lastOtherDamager != null && (!(damageData.lastPlayerDamager.equalsIgnoreCase(player.getName())))){
            //This means player got damage from both

            credit = damageData.lastPlayerDamager;
            if (e.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){

                deathMessage = KillMessage.create.byPlayer(player.getName(), damageData.lastPlayerDamager);
            } else {

                deathMessage = KillMessage.create.byEnvironmentAndPlayer(player.getName(), damageData.lastPlayerDamager, damageData.lastOtherDamager);
            }

        }
        if (damageData.lastPlayerDamager != null && damageData.lastOtherDamager == null && (!(damageData.lastPlayerDamager.equals(player.getName())))){
            //Means player has only been hit by other players
            credit = damageData.lastPlayerDamager;
            deathMessage = KillMessage.create.byPlayer(player.getName(), damageData.lastPlayerDamager);

        }
        if (damageData.lastPlayerDamager != null){
            if (damageData.lastPlayerDamager.equalsIgnoreCase(player.getName())){ // own kill check
                deathMessage = KillMessage.create.byPlayer(player.getName(), damageData.lastPlayerDamager);
                playerInvolved = false;
            }
        }


//        deathMessage = deathMessage + credit;
        // Death message shows who got credit, broadcast to every player in pvp world
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld() == player.getWorld()) {
                onlinePlayer.sendMessage(deathMessage);
            }
        }

        Component mainTitle = LanguageConfig.lang.get(LanguageKey.COMBAT_YOU_DIED.toString());
        Title title = Title.title(mainTitle, Component.text(""));
        player.showTitle(title);

        // get player who assisted from damageCausesMap (if there is a player in there)

        //Get all players for later (to see if DamageCause is a player or not)
        ArrayList<String> pvpPlayers = new ArrayList<>(); // List of all players in pvp world
        for (Player pvpPlayer : Bukkit.getOnlinePlayers()){
            if (pvpPlayer.getWorld() == player.getWorld()){
                pvpPlayers.add(pvpPlayer.getName());
            }
        }

        String highestAssistor = null;
        for (String key2 : damageData.damageDistributionMap.keySet()){
            if (!(key2.equalsIgnoreCase(credit)) && pvpPlayers.contains(key2) && pvpPlayers.contains(player.getName())){
                // Don't count the killer in the assist check
                if (key2.equals(player.getName())){
                    continue;// Don't count the killed player himself in the assist check
                }
                //Only check players for assist
                if (highestAssistor == null){
                    highestAssistor = key2;
                    //First element is always highest
                }
                if (damageData.damageDistributionMap.get(key2) >= damageData.damageDistributionMap.get(highestAssistor)){
                    highestAssistor = key2;
                    //Check others to see if they are higher
                }
            }
        }

        Player assistor = null;
        if (highestAssistor != null){
            assistor = Bukkit.getPlayerExact(highestAssistor);
            assistor.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aYou assisted in the kill of &7" + player.getName()));
            assistor.playSound(assistor.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT,1, 1);
        }

        // Player was killed, all damage info reset
        player.getPersistentDataContainer().remove(key);

        //Sound effects and such
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1,0);


        //Set stats of player
        //Check if player has an entry in config
        FileConfiguration playerStats = PlayerStatsConfig.get();
        if (PlayerStatsConfig.get().isSet(player.getName())){

            playerStats.set(player.getName() + ".deaths", playerStats.getInt(player.getName() + ".deaths") + 1);

            //Ratio update
            double newRatio = (double) playerStats.getInt(player.getName() + ".kills") / (double) playerStats.getInt(player.getName() + ".deaths");
            //newRatio = DoubleUtils.round(newRatio, 2);
            playerStats.set(player.getName() + ".kdratio", newRatio);

            //Update killstreak (reset)
            playerStats.set(player.getName() + ".killstreak", 0);

            PlayerStatsConfig.save();

        } else {
            //Player does not have an entry; creating one with new data
            PlayerStatsConfig.get().set(player.getName(), "");
            PlayerStatsConfig.get().set(player.getName() + ".kills", 0);
            PlayerStatsConfig.get().set(player.getName() + ".deaths", 1);
            PlayerStatsConfig.get().set(player.getName() + ".assists", 0);
            PlayerStatsConfig.get().set(player.getName() + ".kdratio", 0);
            PlayerStatsConfig.get().set(player.getName() + ".killstreak", 0);
            PlayerStatsConfig.save();

        }

        TextComponent actionBarText = Component.text("Deaths: ", NamedTextColor.GREEN)
                .append(Component.text(playerStats.getInt(player.getName() + ".deaths"), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("    K/D: ", NamedTextColor.GREEN));

        try {
            actionBarText = actionBarText.append(Component.text((DoubleUtils.round(playerStats.getDouble(player.getName() + ".kdratio"), 2)), NamedTextColor.LIGHT_PURPLE));
        } catch (Exception exception){
            actionBarText = actionBarText.append(Component.text("Infinity", NamedTextColor.LIGHT_PURPLE));
        }

        //update level
        PlayerLeveling.createLevelXPPath(player.getName());

        //remove any mobs player spawned
        MobRemover.removeMobs(player);

        player.sendActionBar(actionBarText);

        //Only give credit if there is a player involved,
        if (playerInvolved){
            Player creditPlayer = Bukkit.getPlayerExact(credit);
            creditPlayer.playSound(creditPlayer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT,1, 1);
            //Check if player has an entry in config
            FileConfiguration creditStats = PlayerStatsConfig.get();
            if (creditStats.isSet(creditPlayer.getName())) {
                creditStats.set(creditPlayer.getName() + ".kills", creditStats.getInt(creditPlayer.getName() + ".kills") + 1);

                //Ratio update
                double newRatio = (double) creditStats.getInt(creditPlayer.getName() + ".kills") / (double) creditStats.getInt(creditPlayer.getName() + ".deaths");
                //newRatio = DoubleUtils.round(newRatio, 2);
                creditStats.set(creditPlayer.getName() + ".kdratio", newRatio);

                //Killstreak update
                creditStats.set(creditPlayer.getName() + ".killstreak", creditStats.getInt(creditPlayer.getName() + ".killstreak") + 1);
                //Killstreak checker
                if (creditStats.getInt(creditPlayer.getName() + ".killstreak") % 5 == 0) {
                    creditPlayer.playSound(creditPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.3f, 1);
                    for (Player pvpPlayer : Bukkit.getOnlinePlayers()) {
                        if (pvpPlayer.getWorld() == player.getWorld()) {

                            pvpPlayer.sendMessage(ChatColor.RED + credit + ChatColor.YELLOW + " is on a KILLING STREAK of " + ChatColor.RED + creditStats.getInt(creditPlayer.getName() + ".killstreak"));


                        }
                    }
                    PlayerLeveling.addExperience(creditPlayer, creditStats.getInt(creditPlayer.getName() + ".killstreak"), "Killstreak Bonus");
                }
                if (plugin.getConfig().getBoolean("killstreak-glowing")) {

                    if (creditStats.getInt(creditPlayer.getName() + ".killstreak") >= 5) {
                        int duration = plugin.getConfig().getInt("killstreak-glowing-duration");
                        creditPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration * 20, 2, true, false));
                    }
                }

                PlayerStatsConfig.save();
            } else {
                    //Player does not have an entry; creating one with new data
                PlayerStatsConfig.get().set(creditPlayer.getName(), "");
                PlayerStatsConfig.get().set(creditPlayer.getName() + ".kills", 1);
                PlayerStatsConfig.get().set(creditPlayer.getName() + ".deaths", 0);
                PlayerStatsConfig.get().set(creditPlayer.getName() + ".assists", 0);
                PlayerStatsConfig.get().set(creditPlayer.getName() + ".kdratio", 0);
                PlayerStatsConfig.get().set(creditPlayer.getName() + ".killstreak", 1);
                PlayerStatsConfig.save();

            }

            // Actionbar thingies
            actionBarText = Component.text("Kills: ", NamedTextColor.GREEN)
                    .append(Component.text(creditStats.getInt(creditPlayer.getName() + ".kills"), NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("    K/D: ", NamedTextColor.GREEN));

            try {
                actionBarText = actionBarText.append(Component.text((DoubleUtils.round(creditStats.getDouble(creditPlayer.getName() + ".kdratio"), 2)), NamedTextColor.LIGHT_PURPLE));
            } catch (Exception exception){
                actionBarText = actionBarText.append(Component.text("Infinity", NamedTextColor.LIGHT_PURPLE));
            }

            //update level
            PlayerLeveling.createLevelXPPath(creditPlayer.getName());
            if (!(creditPlayer == player)){
                PlayerLeveling.addExperience(creditPlayer, 5, "Kill"); // 5 for KILL
            }
            PlayerLeveling.updateLevels();

            creditPlayer.sendActionBar(actionBarText);

            //Give killItem based on kit
            try {
                ItemStack killItem = new ItemStack(Material.AIR);
                String kitName = PlayerStatsConfig.get().getString(credit + ".current_kit");
                if (!(KitConfig.get().getString("kits." + kitName + ".killitem") == null)) {
                    ItemStack fromFile = (ItemStack) KitConfig.get().get("kits." + kitName + ".killitem");
                    killItem.setType(fromFile.getType());
                    killItem.setAmount(fromFile.getAmount());
                    killItem.setItemMeta(fromFile.getItemMeta());

                    creditPlayer.getInventory().addItem(killItem);
                }
            } catch (Exception exception){
                plugin.getLogger().log(Level.SEVERE, exception.toString()); // throws here. think i fixed it but not sure - i dont see anything
            }

            //PerkHandler
            PerkFireHandler.fireKillPerks(creditPlayer);

            //Juggernaut Event check
            JuggernautListeners.onDeath(player, creditPlayer);

            //The killcosmetic that the creditPlayer has fires
            KillEffect.fireKillCosmetic(player, creditPlayer);


        }

        // If there is an assistor, we need to do some stuff for that player
        if (assistor != null){
            FileConfiguration assistorStats = PlayerStatsConfig.get();
            if (assistorStats.isSet(assistor.getName())){
                assistorStats.set(assistor.getName() + ".assists", assistorStats.getInt(assistor.getName() + ".assists") + 1);

                PlayerStatsConfig.save();

            } else {
                //Player does not have an entry; creating one with new data
                PlayerStatsConfig.get().set(assistor.getName(), "");
                PlayerStatsConfig.get().set(assistor.getName() + ".kills", 0);
                PlayerStatsConfig.get().set(assistor.getName() + ".deaths", 0);
                PlayerStatsConfig.get().set(assistor.getName() + ".assists", 1);
                PlayerStatsConfig.get().set(assistor.getName() + ".kdratio", 0);
                PlayerStatsConfig.get().set(assistor.getName() + ".killstreak", 0);
                PlayerStatsConfig.save();

            }
            actionBarText = Component.text("Assists: ", NamedTextColor.GREEN)
                    .append(Component.text(assistorStats.getInt(assistor.getName() + ".assists"), NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("    K/D: ", NamedTextColor.GREEN))
                    .append(Component.text((assistorStats.getDouble(assistor.getName() + ".kdratio")), NamedTextColor.LIGHT_PURPLE));

            assistor.sendActionBar(actionBarText);

            //update level TODO: check this: I think this is in the wrong spot since it doesn't seem to work?
            // Need a 3rd player or alt to test
            PlayerLeveling.createLevelXPPath(assistor.getName());
            PlayerLeveling.addExperience(assistor, 3, "Assist"); // 3 for assists
            PlayerLeveling.updateLevels();
        }
    }

    @EventHandler
    public void onPlayerCombatHit(EntityDamageByEntityEvent event){

        Player player = null;
        Player damager = null;
        boolean rangedCombat = true; //  whether a chat message should be sent; if ranged; yes
        // HAND-TO-HAND COMBAT
        if (!(event.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            // If the entity that is damaged is in this world, the entire event should be in this world
            return;
        } // This function should make all other copies obsolete - removed all affected copies (I think)

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            //HAND COMBAT
            damager = (Player) event.getDamager();
            player = (Player) event.getEntity();

            if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
                return;
            }
            rangedCombat = false;
            PerkFireHandler.fireCombatPerks(player, damager);
            PerkFireHandler.fireVampirePerk(event);

        } else if ((event.getDamager() instanceof Arrow || event.getDamager() instanceof SpectralArrow) && event.getEntity() instanceof Player) {
            //This branch is a fuckin nightmare: too many hacked in exceptions because spectral arrows and skeletons
            //Projectile combat

            if(((AbstractArrow) event.getDamager()).getShooter() instanceof Player){
                player = (Player) event.getEntity();
                if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    damager = (Player) arrow.getShooter();
                } else {
                    SpectralArrow arrow = (SpectralArrow) event.getDamager();
                    damager = (Player) arrow.getShooter();
                }

                PerkFireHandler.fireRangedPerks(event, damager);


            } else if(((AbstractArrow) event.getDamager()).getShooter() instanceof Monster){
                // Shooter is a skeleton probably
                player = (Player) event.getEntity();
                Monster skeleton = (Monster) ((AbstractArrow) event.getDamager()).getShooter();
                String mobName = skeleton.getName();

                damager = Bukkit.getPlayerExact(mobName.split("'s")[0]);

            }
        } else if (event.getDamager() instanceof ThrownPotion && event.getEntity() instanceof Player){

            //Splash potion threw (only potions that do damage, only those are seen by this event)
            player = (Player) event.getEntity();
            ThrownPotion potion = (ThrownPotion) event.getDamager();

            damager = (Player) potion.getShooter(); //works


        } else if (event.getDamager() instanceof Trident && event.getEntity() instanceof Player){

            //Trident
            player = (Player) event.getEntity();
            Trident trident = (Trident) event.getDamager();

            damager = (Player) trident.getShooter(); //works


        } else if (event.getDamager() instanceof Monster && event.getEntity() instanceof Player) {

            String mobName = event.getDamager().getName();
            damager = Bukkit.getPlayerExact(mobName.split("'s")[0]);
            player = ((Player) event.getEntity()).getPlayer();

        } else if (event.getDamager() instanceof EnderPearl && event.getEntity() instanceof Player) {
            player = ((Player) event.getEntity()).getPlayer();
            rangedCombat = false;
            PerkFireHandler.fireEnderpearlDamagePerks((Player) event.getEntity(), event);
            return; // Prevents NPE's
        } else if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player){
            Firework fw = (Firework) event.getDamager();
            if (fw.hasMetadata("nodamage")) {
                event.setCancelled(true);
                return;
            }
            if (fw.getShooter() instanceof Player){
                player = (Player) event.getEntity();
                damager = (Player) fw.getShooter();
            }

        }
        else {
            //If we end up here idk what happened but nothing relevant (probably a zombie hitting a pig or something)
            return;
        }
        // HP Message
        if (rangedCombat){
            double damage = event.getDamage();
            double playerHP = player.getHealth() - damage;
            if (playerHP < 0){
                playerHP = 0;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String formattedDamage = decimalFormat.format(damage);
            String formattedHP = decimalFormat.format(playerHP);
            damager.sendMessage(
                    Component.text(player.getName(), NamedTextColor.GREEN).append(
                    Component.text(" is on ", NamedTextColor.RED).append(
                    Component.text(formattedHP, NamedTextColor.GREEN).append(
                    Component.text(" HP!", NamedTextColor.RED)
                    ))));
            damager.sendMessage(
                    Component.text("  You dealt ", NamedTextColor.GRAY).append(
                    Component.text(formattedDamage, NamedTextColor.GREEN).append(
                    Component.text(" damage!", NamedTextColor.GRAY))));
        }

        int damageAmount = (int) event.getDamage();

        //Make new object with that player, is mostly empty
        PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
        damageDistrib.lastPlayerDamager = damager.getName();


        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key, new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }


        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());

        if (pulledPlayerData == null){
            //Nullcheck, just trying to eliminate an NPE (see pulledPlayerData)
            plugin.getLogger().log(Level.FINE, "Aware of warning above, does not seem to matter");
            //Player does not have dataContainer, creating one with data from this hit
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
            pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());
        }

        //We get its damageData
        HashMap<String, Integer> damageMap = pulledPlayerData.damageDistributionMap;

        //If the damager has already done damage before, add damageValue to already existing value
        //If damager is new, add damageValue as value
        if (damageMap.containsKey(damager.getName())){
            int value = damageMap.get(damager.getName());
            damageMap.put(damager.getName(), value + damageAmount);
        } else {
            damageMap.put(damager.getName(), damageAmount);
        }

        pulledPlayerData.lastPlayerDamager = damager.getName();

        //Put the edited object back in the player
        player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);

    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event){
        //EVERYTHING WHERE A PLAYER IS NOT INVOLVED
        if (!(event.getEntity() instanceof Player)){
            return;
        }

        Player player = (Player) event.getEntity();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            //This is handled by onPlayerCombatHit
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
            //This is handled by onPlayerCombatHit or (with snowballs) onSnowballHit
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.MAGIC){
            return;
        }

        int damageAmount = (int) event.getDamage();
        EntityDamageEvent.DamageCause cause = event.getCause();

        // This branch prevents fall damage from happening in the spawn area; it also returns so that it does not get added to damageDistribution
        if (cause == EntityDamageEvent.DamageCause.FALL){
            Location fallLocation = player.getLocation();

            // - 0.5 because block and player location are off by 0.5 because Minecraft
            double minX = plugin.getConfig().getDouble("respawnX") - 0.5 - plugin.getConfig().getInt("spawn-radius");
            double maxX = plugin.getConfig().getDouble("respawnX") - 0.5 + plugin.getConfig().getInt("spawn-radius");

            double minZ = plugin.getConfig().getDouble("respawnZ") - 0.5 - plugin.getConfig().getInt("spawn-radius");
            double maxZ = plugin.getConfig().getDouble("respawnZ") - 0.5 + plugin.getConfig().getInt("spawn-radius");

            if(fallLocation.getBlockX() >= minX && fallLocation.getBlockX() <= maxX){
                if(fallLocation.getBlockZ() >= minZ && fallLocation.getBlockZ() <= maxZ) { // If inside spawn square
//                    event.setDamage(0);
                    event.setCancelled(true); //might have other implications - none found
                    return;
                }
            }
        }
        // This branch negates any firework damage if
        // it is of killeffect origin -> fireworks from a crossbow still count
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) event;
            if (newEvent.getDamager() instanceof Firework fw) {
                if (fw.hasMetadata("nodamage")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            //Make new object with that player, is mostly empty
            PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);

            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }

        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());

        //We get its damageData
        HashMap<String, Integer> damageMap = pulledPlayerData.damageDistributionMap;

        //If the damager has already done damage before, add damageValue to already existing value
        //If damager is new, add damageValue as value
        if (damageMap.containsKey(cause.name())){
            int value = damageMap.get(cause.name());
            damageMap.put(cause.name(), value + damageAmount);
        } else {
            damageMap.put(cause.name(), damageAmount);
        }

        pulledPlayerData.lastOtherDamager = cause.name();

        //Put the edited object back in the player
        player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);


    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event){

        if (!(event.getHitEntity() instanceof Player)){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) event.getHitEntity();
        Player shooter = null;

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        if (event.getEntity() instanceof Snowball | event.getEntity() instanceof Egg){

            shooter = (Player) event.getEntity().getShooter();

            PerkFireHandler.fireSnowballPerks(event, player, shooter);

        }
        else {
            return;
        }

        //If player has just started combat, make a dataContainer with the empty object above
        NamespacedKey key = new NamespacedKey(plugin, "damageDistributionInfo");
        if (!(player.getPersistentDataContainer().has(key,new PlayerDamageDistributionDataType()))){
            //Player does not have dataContainer, creating one with data from this hit
            //Make new object with that player, is mostly empty
            PlayerDamageDistribution damageDistrib = new PlayerDamageDistribution(player);
            damageDistrib.lastPlayerDamager = shooter.getName();
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), damageDistrib);
        }

        //We get the data that either already existed or has just been created
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerDamageDistribution pulledPlayerData = data.get(key, new PlayerDamageDistributionDataType());

        if (shooter != null){
            pulledPlayerData.lastPlayerDamager = shooter.getName();
            player.getPersistentDataContainer().set(key, new PlayerDamageDistributionDataType(), pulledPlayerData);
        }

    }

}
