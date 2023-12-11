package me.lifelessnerd.purekitpvp.utils;

import me.lifelessnerd.purekitpvp.PluginGetter;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Text;

import javax.annotation.Nullable;
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

    public static TextReplacementConfig replaceConfig(String find, String replace){
        return replaceConfig(find, replace, 1);
    }
    public static TextReplacementConfig replaceConfig(String find, String replace, int times){
        return TextReplacementConfig.builder().matchLiteral(find).replacement(replace).times(times).build();
    }
    public static ArrayList<Component> splitComponent(Component massiveComponent){
        ArrayList<Component> output = new ArrayList<>();
//        System.out.println(massiveComponent);
        Component prevChild = Component.text("");
        int children = 0;
        for (Component child : massiveComponent.iterable(ComponentIteratorType.DEPTH_FIRST, ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME)){
            try {
                TextComponent textChild = (TextComponent) child;
                if (child.equals(massiveComponent)) { // DFS through Component will also return itself; now its just its children
                    continue;
                }
                if (textChild.content().endsWith("\n")) {

                    TextComponent replaced = (TextComponent) textChild.replaceText(ComponentUtils.replaceConfig("\n", ""));
                    prevChild = prevChild.append(replaced).decoration(TextDecoration.ITALIC, false);
                    output.add(prevChild);
                    prevChild = Component.text("");
                } else {
                    prevChild = prevChild.append(child);
                }
            } catch(Exception e){
                PluginGetter.Plugin().getLogger().severe(e.toString());
            }
            children++;

        }

        if (children == 0){ // Edge case i hate myself
            ArrayList<Component> edgeOutput = new ArrayList<>();
            edgeOutput.add(massiveComponent.replaceText(ComponentUtils.replaceConfig("\n", "")));
            return edgeOutput;
        }


        return output;
    }


}
