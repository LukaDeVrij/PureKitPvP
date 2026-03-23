package me.lifelessnerd.purekitpvp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyStringUtils {

    public static String itemCamelCase(String string) {
        //Input: IRON_SWORD
        String[] split = string.split("_");
        StringBuilder processedSentence = new StringBuilder();
        for (String word : split) {
            String processedWord = camelCaseWord(word);
            processedSentence.append(processedWord).append(" ");
        }

        return processedSentence.toString();
        //Output: Iron Sword
    }

    public static String camelCaseWord(String string) {
        //Input: SAMPLE
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
        //Output: Sample
    }

    @Deprecated
    public static String mapStringToEnchantment(String string) {
        System.out.println(string);
        //{CraftEnchantment[minecraft:knockback]=11, CraftEnchantment[minecraft:fire_aspect]=2}
        StringBuilder result = new StringBuilder();

        StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(0);
        builder.deleteCharAt(string.length() - 2);
        string = builder.toString();

        String[] split = string.split(",");
        for (String substring : split) {
            substring = substring.trim();
            substring = substring.substring(16);
            String level = substring.split("=")[1];
            String splitEnchantment = substring.split("]")[0];
            String enchantment = splitEnchantment.split(":")[1];

            result.append(MyStringUtils.itemCamelCase(enchantment)).append(level);
            result.append(", ");
        }
        return result.substring(0, result.length() - 2);
        //Output: Knockback 11, Fire Aspect 2
    }

    public static String mapEnchantmentsToString(Map<Enchantment, Integer> map) {
        if (map == null || map.isEmpty()) return "0";

        return map.entrySet().stream()
                .map(e -> formatEnchantmentName(e.getKey()) + " " + romanize(e.getValue()))
                .collect(Collectors.joining(", "));
    }

    private static String formatEnchantmentName(Enchantment enchantment) {
        String raw = enchantment.getKey().getKey(); // e.g. "sharpness", "blast_protection"
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String potionMetaToEffects(ItemMeta potionItemMeta) {
        StringBuilder sb = new StringBuilder();

        PotionMeta potionMeta = (PotionMeta) potionItemMeta;

        // Add information about base effect
        if (potionMeta.getBasePotionType() == null) return "";
        if (!potionMeta.getBasePotionType().name().equals("WATER")) {
            for (PotionEffect potionEffect : potionMeta.getBasePotionType().getPotionEffects()) {
                String translationKey = potionEffect.getType().translationKey();
                String[] split = translationKey.split("\\.");
                sb.append(itemCamelCase(split[split.length - 1]));
                sb.append(romanize(potionEffect.getAmplifier() + 1));
                if (potionEffect.getDuration() > 1) sb.append(" (").append(potionEffect.getDuration()).append("s)");
            }
        }

        List<PotionEffect> customEffects = potionMeta.getCustomEffects();
        if (!customEffects.isEmpty()) {
            // Add information about each custom effect
            if (!potionMeta.getBasePotionType().name().equals("WATER")) sb.append(", ");
            for (PotionEffect customEffect : customEffects) {
                String customEffectName = customEffect.getType().translationKey();
                String[] split = customEffectName.split("\\.");
                int amplifier = customEffect.getAmplifier() + 1;
                int duration = customEffect.getDuration();

                sb.append(itemCamelCase(split[split.length - 1]));
                sb.append(romanize(amplifier));
                if (duration > 1) sb.append(" (").append(duration).append("s)");
            }
        }
        return sb.toString();
    }

    @Deprecated
    public static String itemMetaToEffects(String string) {

//I: POTION_META:meta-type=POTION, potion-type=minecraft:strong_healing)
//OR
//I: POTION_META:meta-type=POTION, display-name="Golem Debuff", potion-type=minecraft:water, custom-effects=[LEVITATION:40t-x7]}

        StringBuilder result = new StringBuilder("");

        if (string.contains("custom-effects")) {

            String[] split = string.split("custom-effects=");
            StringBuilder builder = new StringBuilder(split[1]);
            builder.deleteCharAt(0);
            builder.deleteCharAt(split[1].length() - 2);
            builder.deleteCharAt(split[1].length() - 3);
            String[] splitAgain = builder.toString().split(":");

            result.append(itemCamelCase(splitAgain[0]));
            String meta = splitAgain[1];
            String[] splitOnceMore = meta.split("-");

            StringBuilder time = new StringBuilder(splitOnceMore[0]);
            time.deleteCharAt(splitOnceMore[0].length() - 1);
            int timeSec = Integer.parseInt(time.toString()) / 20;


            StringBuilder potency = new StringBuilder(splitOnceMore[1]);
            potency.deleteCharAt(0);
            int potencyInt = Integer.parseInt(potency.toString()) + 1;

            StringBuilder lvlAndTime = new StringBuilder("");
            if (timeSec > 100000000) {
                lvlAndTime.append(potencyInt + " (Infinite)");
            } else {
                lvlAndTime.append(potencyInt + " (" + timeSec + "s)");
            }

            result.append(lvlAndTime);

            return result.toString();
        }

        String[] split = string.split("potion-type=minecraft:");
        StringBuilder effectB = new StringBuilder(split[1]);
        String effect = effectB.deleteCharAt(split[1].length() - 1).toString();

        if (effect.contains("strong")) {
            String[] splitAgain = effect.split("strong_");
            result.append(itemCamelCase(splitAgain[1]));
            result.append("II ");
        } else if (effect.contains("long")) {
            String[] splitAgain = effect.split("long_");
            result.append(itemCamelCase(splitAgain[1]));
            result.append("(Long) ");
        } else {

            result.append(itemCamelCase(effect));
        }

        return result.toString();
    }

    public static String[] perkLoreDecoder(String string) {
        String[] output = string.split("\n");
        return output;
    }

    public static String itemNameToCosmeticId(String input) {
        String processed = input.toLowerCase();
        processed = processed.replace(' ', '_');

        return processed;
    }

    // Opposite of function above
    public static String cosmeticIdToItemName(String input) {
        // Input: blood_explosion -> Blood Explosion
        return itemCamelCase(input).trim();
    }

    public static String componentToString(Component singleLineComponent) {
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        String processed = serializer.serialize(singleLineComponent);
        processed = processed.substring(1, processed.length() - 1);
        return processed;
    }

    public static String romanize(int input) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (input >= values[i]) {
                result.append(symbols[i]);
                input -= values[i];
            }
        }
        return result.toString();
    }

}
