package me.lifelessnerd.purekitpvp.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.lifelessnerd.purekitpvp.database.entities.PlayerKitPreferences;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;

public class KitDatabase {

    private final Dao<PlayerKitPreferences, String> playerKitPreferencesDao;

    public KitDatabase(String path) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path);
        TableUtils.createTableIfNotExists(connectionSource, PlayerKitPreferences.class);
        playerKitPreferencesDao = DaoManager.createDao(connectionSource, PlayerKitPreferences.class);
    }

    public void addEntry(Player player, String kitName, HashMap<Integer, Integer> prefs) throws SQLException {
        if (hasEntry(player, kitName)){
            updateEntry(player, kitName, prefs);
            return;
        }
        PlayerKitPreferences playerKitPreferences = new PlayerKitPreferences();
        playerKitPreferences.setUuid(player.getUniqueId().toString());
        playerKitPreferences.setUsername(player.getName());
        playerKitPreferences.setKitName(kitName);
        playerKitPreferences.setPreferences(prefs);
        playerKitPreferencesDao.create(playerKitPreferences);
    }


    public boolean hasEntry(Player player, String kitName) throws SQLException {
        // Query for a user with the specified uuid and kitName
        QueryBuilder<PlayerKitPreferences, ?> queryBuilder = playerKitPreferencesDao.queryBuilder();
        queryBuilder.where().eq("uuid", player.getUniqueId().toString()).and().eq("kitName", kitName);
        // Check if any records match the criteria
        return queryBuilder.queryForFirst() != null;

    }

    public void updateEntry(Player player, String kitName, HashMap<Integer, Integer> prefs) throws SQLException {
        QueryBuilder<PlayerKitPreferences, ?> queryBuilder = playerKitPreferencesDao.queryBuilder();
        queryBuilder.where().eq("uuid", player.getUniqueId().toString()).and().eq("kitName", kitName);

        PlayerKitPreferences playerKitPreferences = queryBuilder.queryForFirst();
        if (playerKitPreferences != null) {
            playerKitPreferences.setPreferences(prefs);
            playerKitPreferencesDao.update(playerKitPreferences);
        } else {
            addEntry(player, kitName, prefs);
        }

    }

    public PlayerKitPreferences getEntry(Player player, String kitName) throws SQLException {
        if (!hasEntry(player, kitName)){
            HashMap<Integer, Integer> emptyMap = new HashMap<>();
            addEntry(player, kitName, emptyMap);
        }
        // Query for a user with the specified uuid and kitName
        QueryBuilder<PlayerKitPreferences, ?> queryBuilder = playerKitPreferencesDao.queryBuilder();
        queryBuilder.where().eq("uuid", player.getUniqueId().toString()).and().eq("kitName", kitName);
        // Check if any records match the criteria
        return queryBuilder.queryForFirst();
    }
}
