package me.lifelessnerd.purekitpvp.combatHandler.killHandler;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class PlayerDamageDistributionDataType implements PersistentDataType<byte[], PlayerDamageDistribution> {


    @Override
    public @NotNull Class<PlayerDamageDistribution> getComplexType() {
        return PlayerDamageDistribution.class;
    }

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }



    @Override
    public byte @NotNull [] toPrimitive(@NotNull PlayerDamageDistribution complex, @NotNull PersistentDataAdapterContext context) {
        return org.apache.commons.lang3.SerializationUtils.serialize(complex);
    }

    @Override
    public @NotNull PlayerDamageDistribution fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {

            InputStream is = new ByteArrayInputStream(primitive);
            ObjectInputStream o = new ObjectInputStream(is);
            return (PlayerDamageDistribution) o.readObject();

        } catch (IOException| ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
