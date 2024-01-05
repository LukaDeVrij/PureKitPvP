package me.lifelessnerd.purekitpvp.database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;

@DatabaseTable(tableName = "player_kit_preferences")
public class PlayerKitPreferences {

    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    @DatabaseField(canBeNull = false)
    private String uuid;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String kitName;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, Integer> preferences;

    public PlayerKitPreferences() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public HashMap<Integer, Integer> getPreferences() {
        return preferences;
    }

    public void setPreferences(HashMap<Integer, Integer> preferences) {
        this.preferences = preferences;
    }
}
