package me.lifelessnerd.purekitpvp.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PerkData {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup(){

        file = new File(Bukkit.getServer().getPluginManager().getPlugin("PureKitPvP").getDataFolder(), "perkdata.yml");
        if (!(file.exists())){
            try{
                file.createNewFile();
            } catch (IOException e){
                //pain
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        } catch (IOException e){
            System.out.println("Cannot save!");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void setPerk(Player player, String perkName, int slot){

        FileConfiguration perkData = PerkData.get();
        if (!(perkData.isSet(player.getName()))){
            createSection(player);
        }

        perkData.set(player.getName() + ".slot" + slot, perkName);


    }

    public static void createSection(Player player){
        FileConfiguration perkData = PerkData.get();
        perkData.set(player.getName() + ".slot1", null);
        perkData.set(player.getName() + ".slot2", null);
        perkData.set(player.getName() + ".slot3", null);
        perkData.set(player.getName() + ".slot4", null);
        perkData.set(player.getName() + ".slot5", null);
    }
}
