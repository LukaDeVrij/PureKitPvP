package me.lifelessnerd.purekitpvp.kitCommand;

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

        Player player = e.getEntity();
        player.sendMessage("DABDABDAB");
        hasKit.remove(player.getName());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (hasKit.contains(player.getName())){
            player.sendMessage(ChatColor.GRAY + "You already have a kit!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Incorrect arguments!");
            return false;
        }

        if (!(plugin.getConfig().isSet("kits." + args[0]))){
            player.sendMessage(ChatColor.GRAY + "That kit does not exist.");
            return true;
        }

        if(!plugin.getConfig().isSet("kits." + args[0] + ".permission")){
            player.sendMessage(ChatColor.GRAY + "That kit does not have a permission associated. Please report to your administrator.");
            //TODO: Make permissions optional without duplicating code ideally
            return true;
        }

        if (!(player.hasPermission(plugin.getConfig().getString("kits." + args[0] + ".permission")))){ //IDEA lies
            player.sendMessage(ChatColor.GRAY + "You do not have permission!");
            return true;
        }

        //Okay, if all checks are passed, player may get the kit
        player.sendMessage(ChatColor.GRAY + "Kit " + ChatColor.BLUE + args[0] + ChatColor.GRAY + " given.");

        FileConfiguration fileConfiguration = plugin.getConfig();
        Object kitObject = fileConfiguration.get("kits." + args[0] + ".contents");
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
            ItemStack helmet = fileConfiguration.getItemStack("kits." + args[0] + ".helmet");
            player.getInventory().setHelmet(helmet);
            ItemStack chestplate = fileConfiguration.getItemStack("kits." + args[0] + ".helmet");
            player.getInventory().setHelmet(chestplate);
            ItemStack leggings = fileConfiguration.getItemStack("kits." + args[0] + ".helmet");
            player.getInventory().setHelmet(leggings);
            ItemStack boots = fileConfiguration.getItemStack("kits." + args[0] + ".helmet");
            player.getInventory().setHelmet(boots);
        }

        hasKit.add(player.getName());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1){
            List<String> autoComplete = new ArrayList<>();
            autoComplete.addAll(this.plugin.getConfig().getConfigurationSection("kits.").getKeys(false));

            return autoComplete;
        }
        return null;
    }
}
