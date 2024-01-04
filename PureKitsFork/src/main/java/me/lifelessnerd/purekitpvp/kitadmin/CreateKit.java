package me.lifelessnerd.purekitpvp.kitadmin;

import me.lifelessnerd.purekitpvp.Subcommand;
import me.lifelessnerd.purekitpvp.files.KitConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class CreateKit extends Subcommand {
    Plugin plugin;

    public CreateKit(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "createkit";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Create a kit from the contents of your inventory";
    }

    @Override
    public String getSyntax() {
        return "/purekitpvp kit create <kitName> <displayColor> <kitIcon> <kitPermission> <killItem>";
    }

    @Override
    public boolean getConsoleExecutable() {
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("purekitpvp.admin.createkit")){
            player.sendMessage(Component.text("No permission!", NamedTextColor.RED));
            return true;
        }

        if (!(args.length > 3)){
            player.sendMessage(Component.text("Please provide arguments!", NamedTextColor.RED));
            return false;
        }
        //Store default arguments
        KitIcon kitIconLib = new KitIcon();
        String killItem;
        String kitPermission = "kit.other";

        String kitName = args[1].toLowerCase();
        kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);

        NamedTextColor displayColor;
        try {
            displayColor = NamedTextColor.NAMES.value(args[2]);
//            System.out.println(displayColor);
            if (displayColor == null){
                throw new NullPointerException();
            }
        } catch(Exception e) {
            player.sendMessage(Component.text("Color is not valid!", NamedTextColor.RED));
            return false;
        }

        String kitIcon;
        if (Arrays.asList(kitIconLib.materialList).contains(args[3])){
            kitIcon = args[3].toUpperCase();

        } else {
            player.sendMessage(Component.text(args[3] + " is not a valid material.", NamedTextColor.RED));
            return true;
        }

        if (args.length >= 5){
            kitPermission = args[4];
        }

        // Kill item
        if (args.length >= 6){
            if (Arrays.asList(kitIconLib.materialList).contains(args[5])){
                killItem = args[5].toUpperCase();

            }else {
                player.sendMessage(Component.text(args[3] + " is not a valid material.", NamedTextColor.RED));
                return true;
            }
        } else {
            killItem = "AIR";
        }

        ItemStack[] kitContents = player.getInventory().getContents();

        //Create kit in config
        KitConfig.get().createSection("kits." + kitName);
        KitConfig.get().set("kits." + kitName + ".permission", kitPermission);
        KitConfig.get().set("kits." + kitName + ".displayname", displayColor.toString());
        KitConfig.get().set("kits." + kitName + ".guiitem", kitIcon);
        KitConfig.get().set("kits." + kitName + ".guilore", "");
        KitConfig.get().set("kits." + kitName + ".contents", kitContents);

        //Killitem
        Material killItemMat = Material.getMaterial(killItem);
        ItemStack killItemStack = new ItemStack(killItemMat); // Forced by contains in MatList above
//        KitConfig.get().addDefault("kits." + kitName + ".killitem", "GOLDEN_APPLE"); //what does addDefault even do? not this at least
        KitConfig.get().set("kits." + kitName + ".killitem", killItemStack);

        KitConfig.get().options().copyDefaults(true);
        KitConfig.save();
        KitConfig.reload();

        player.sendMessage(
                Component.text("Kit created with the following properties:", NamedTextColor.GREEN).appendNewline().append(
                Component.text("Kit Name: ", NamedTextColor.GOLD).append(Component.text(kitName).color(NamedTextColor.WHITE).appendNewline().append(
                Component.text("Kit Color: ", NamedTextColor.GOLD).append(Component.text(displayColor.toString()).color(displayColor)).appendNewline().append(
                Component.text("Kit Icon: ", NamedTextColor.GOLD).append(Component.text(kitIcon).color(NamedTextColor.WHITE).appendNewline().append(
                Component.text("Kit Permission: ", NamedTextColor.GOLD).append(Component.text(kitPermission).color(NamedTextColor.WHITE).appendNewline().append(
                Component.text("Kill Item: ", NamedTextColor.GOLD).append(Component.text(killItem).color(NamedTextColor.WHITE)
                )))))))))); // Component LUL
        player.sendMessage("You can change these in the kits.yml file, but remember to back-up the file when manually editing.");

        return true;

    }
}
