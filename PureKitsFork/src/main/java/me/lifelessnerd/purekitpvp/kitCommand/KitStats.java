package me.lifelessnerd.purekitpvp.kitCommand;

import me.lifelessnerd.purekitpvp.files.KitStatsConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class KitStats {

    public static void updateValue(String kitName){

        FileConfiguration kitStats = KitStatsConfig.get();

        if (!(kitStats.isSet(kitName))){

            kitStats.set(kitName, 1);
            return;
        }

        int value = kitStats.getInt(kitName);
        int newValue = value + 1;
        kitStats.set(kitName, newValue);

        KitStatsConfig.save();
        //TODO: think this works, havent testes
    }


}
