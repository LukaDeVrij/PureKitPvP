package me.lifelessnerd.purekitpvp.versioning;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftVersion {

    public static String getVersion(){
        return extractVersion(Bukkit.getVersion());
    }

    public static String extractVersion(String input) {
        // Define a regular expression pattern to match the version number
        Pattern pattern = Pattern.compile("\\b(\\d+\\.\\d+\\.\\d+)\\b");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("No version number found in the input string");
        }
    }
}
