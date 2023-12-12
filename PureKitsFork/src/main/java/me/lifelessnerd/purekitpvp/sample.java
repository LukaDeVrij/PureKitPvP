package me.lifelessnerd.purekitpvp;


import me.lifelessnerd.purekitpvp.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class sample {

    public static void main(String[] args) {

        System.out.println(mapStringToEnchantment("{CraftEnchantment[minecraft:knockback]=11, CraftEnchantment[minecraft:fire_aspect]=2}"));



    }

    public static String mapStringToEnchantment(String string){

//I {CraftEnchantment[minecraft:knockback]=2, CraftEnchantment[minecraft:fire_aspect]=2}
        StringBuilder result = new StringBuilder();

        StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(0);
        builder.deleteCharAt(string.length() - 2);
        string = builder.toString();

        String[] split = string.split(",");
        for(String substring : split){
            substring = substring.trim();
            substring = substring.substring(16);
            String level = substring.split("=")[1];
            String splitEnchantment = substring.split("]")[0];
            String enchantment = splitEnchantment.split(":")[1];

            result.append(MyStringUtils.itemCamelCase(enchantment)).append(level);
            result.append(", ");
        }
        return result.substring(0, result.length() - 2);
        //Output: Fire Protection 4, Infinity 1
    }

}
