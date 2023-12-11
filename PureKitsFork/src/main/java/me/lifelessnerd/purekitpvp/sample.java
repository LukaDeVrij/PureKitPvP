package me.lifelessnerd.purekitpvp;


import me.lifelessnerd.purekitpvp.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class sample {

    public static void main(String[] args) {

        System.out.println(mapStringToEnchantment("{CraftEnchantment[minecraft:knockback]=2, CraftEnchantment[minecraft:fire_aspect]=2}"));



    }

    public static String mapStringToEnchantment(String string){

//I {Enchantment:[minecraft:fire_protection, PROTECTION_FIRE]=4, Enchantment:[minecraft:infinity, INFINITY]=1}
        StringBuilder result = new StringBuilder();

        StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(0);
        builder.deleteCharAt(string.length() - 2);
        string = builder.toString();
        String[] split = string.split("minecraft:");
        for (int index = 1; index < split.length; index++){
            String word = split[index];

            String[] splitAgain = word.split(",");
            String levelString = splitAgain[1];
            String[] isolateLevel = levelString.split("=");
            int level = Integer.parseInt(isolateLevel[isolateLevel.length - 1]);
            word = splitAgain[0];
            word = MyStringUtils.itemCamelCase(word);
            result.append(word).append(level).append(" ");
        }


        return result.toString();
        //Output: Fire Protection 4, Infinity 1
    }

}
