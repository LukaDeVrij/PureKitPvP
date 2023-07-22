package me.lifelessnerd.purekitpvp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtils {

    public static ItemMeta setLore(ItemMeta itemMeta, String lore, int line){
        List<Component> currentLore = null;
        if (itemMeta.hasLore()){
            currentLore = itemMeta.lore();
        } else {
            currentLore = new ArrayList<>();
        }

        TextComponent translated = stringDecoder(lore);


        if (line >= currentLore.size()){
            currentLore.add(translated);
        } else {
            currentLore.set(line, translated);
        }

        itemMeta.lore(currentLore);
        return itemMeta;
    }

    static TextComponent stringDecoder(String lore) {
        //String to TextComponent decoder
        // "&aTesting &3string!"
        TextComponent textComponent = Component.text("");
        StringBuilder toBeAdded = new StringBuilder();
        TextColor lastColor = NamedTextColor.WHITE;

        for (int index = 0; index < lore.length(); index++) {
            char character = lore.charAt(index);
            if (character == '&') {

                textComponent = textComponent.append(Component.text(
                        toBeAdded.toString()).color(lastColor));
                toBeAdded = new StringBuilder();

                switch (lore.charAt(index + 1)) {
                    case '0':
                        lastColor = NamedTextColor.BLACK;
                        break;
                    case '1':
                        lastColor = NamedTextColor.DARK_BLUE;
                        break;
                    case '2':
                        lastColor = NamedTextColor.DARK_GREEN;
                        break;
                    case '3':
                        lastColor = NamedTextColor.DARK_AQUA;
                        break;
                    case '4':
                        lastColor = NamedTextColor.DARK_RED;
                        break;
                    case '5':
                        lastColor = NamedTextColor.DARK_PURPLE;
                        break;
                    case '6':
                        lastColor = NamedTextColor.GOLD;
                        break;
                    case '7':
                        lastColor = NamedTextColor.GRAY;
                        break;
                    case '8':
                        lastColor = NamedTextColor.DARK_GRAY;
                        break;
                    case '9':
                        lastColor = NamedTextColor.BLUE;
                        break;
                    case 'a':
                        lastColor = NamedTextColor.GREEN;
                        break;
                    case 'b':
                        lastColor = NamedTextColor.AQUA;
                        break;
                    case 'c':
                        lastColor = NamedTextColor.RED;
                        break;
                    case 'd':
                        lastColor = NamedTextColor.LIGHT_PURPLE;
                        break;
                    case 'e':
                        lastColor = NamedTextColor.YELLOW;
                        break;
                    case 'f':
                        lastColor = NamedTextColor.WHITE;
                        break;


                }

                index = index + 1;
            } else {
                toBeAdded.append(character);
            }

        }
        textComponent = textComponent.append(Component.text(
                toBeAdded.toString()).color(lastColor)).decoration(TextDecoration.ITALIC, false);
        return textComponent;
    }
}
