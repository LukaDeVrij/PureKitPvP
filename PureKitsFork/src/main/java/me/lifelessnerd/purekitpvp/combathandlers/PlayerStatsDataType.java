package me.lifelessnerd.purekitpvp.combathandlers;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class PlayerStatsDataType implements PersistentDataType<byte[], PlayerStats> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<PlayerStats> getComplexType() {
        return PlayerStats.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull PlayerStats complex, @NotNull PersistentDataAdapterContext context) {
        return org.apache.commons.lang3.SerializationUtils.serialize(complex);
    }

    @Override
    public @NotNull PlayerStats fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {

            InputStream is = new ByteArrayInputStream(primitive);
            ObjectInputStream o = new ObjectInputStream(is);
            return (PlayerStats) o.readObject();

        } catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
