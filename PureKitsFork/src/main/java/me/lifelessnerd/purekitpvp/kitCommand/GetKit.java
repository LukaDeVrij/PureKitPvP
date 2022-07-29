package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.KitConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GetKit implements TabExecutor, Listener {
    Plugin plugin;

    public static ArrayList<String> hasKit = new ArrayList<>();

    public GetKit(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player player = e.getPlayer();
        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world")))){
            return;
        }

        hasKit.remove(player.getName());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        String kitNameArg = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);

        if (hasKit.contains(player.getName())){
            player.sendMessage(ChatColor.GRAY + "You already have a kit!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Incorrect arguments!");
            return false;
        }

        if (!(KitConfig.get().isSet("kits." + kitNameArg))){
            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        if(!KitConfig.get().isSet("kits." + kitNameArg + ".permission")){
            player.sendMessage(ChatColor.GRAY + "That kit does not have a permission associated. Please report to your administrator.");
            //TODO: Make permissions optional without duplicating code ideally
            return true;
        }

        if (!(player.hasPermission(KitConfig.get().getString("kits." + kitNameArg + ".permission")))){ //IDEA lies
            player.sendMessage(ChatColor.GRAY + "You do not have permission!");
            return true;
        }

        //Okay, if all checks are passed, player may get the kit
        player.sendMessage(ChatColor.GRAY + "Kit " + ChatColor.BLUE + kitNameArg + ChatColor.GRAY + " given.");

        FileConfiguration fileConfiguration = KitConfig.get();
        Object kitObject = fileConfiguration.get("kits." + kitNameArg + ".contents");
        List<ItemStack> kitItems = (List<ItemStack>) kitObject;

        //Remove any potion effects that make PvP unfair
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        //Give items
        for (int index = 0; index < kitItems.size(); index++) {
            ItemStack item = kitItems.get(index);
            if (item == null) {
                item = new ItemStack(Material.AIR);
            }
            player.getInventory().setItem(index, item);
            ItemStack helmet = fileConfiguration.getItemStack("kits." + kitNameArg + ".helmet");
            player.getInventory().setHelmet(helmet);
            ItemStack chestplate = fileConfiguration.getItemStack("kits." + kitNameArg + ".helmet");
            player.getInventory().setHelmet(chestplate);
            ItemStack leggings = fileConfiguration.getItemStack("kits." + kitNameArg + ".helmet");
            player.getInventory().setHelmet(leggings);
            ItemStack boots = fileConfiguration.getItemStack("kits." + kitNameArg + ".helmet");
            player.getInventory().setHelmet(boots);
        }

        hasKit.add(player.getName());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1){
            List<String> autoComplete = new ArrayList<>();
            for(String key : KitConfig.get().getConfigurationSection("kits.").getKeys(false)){
                key = key.toLowerCase();
                autoComplete.add(key);
            };

            return autoComplete;
        }



        return new ArrayList<>();
    }
}
