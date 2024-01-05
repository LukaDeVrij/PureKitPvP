package me.lifelessnerd.purekitpvp;


import me.lifelessnerd.purekitpvp.files.lang.LanguageKey;

import java.util.HashMap;
import java.util.Map;

public class sample {

    public static void main(String[] args) {
        Map<Integer, Integer> playerPrefs = new HashMap<>();
        playerPrefs.put(0, 8);
        playerPrefs.put(1, 7);

        System.out.println(playerPrefs);


    }


}
